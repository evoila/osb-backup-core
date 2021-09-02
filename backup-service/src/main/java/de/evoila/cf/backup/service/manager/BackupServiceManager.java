package de.evoila.cf.backup.service.manager;

import de.evoila.cf.backup.model.agent.response.AgentBackupResponse;
import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.api.BackupPlan;
import de.evoila.cf.backup.model.api.endpoint.EndpointCredential;
import de.evoila.cf.backup.model.api.file.FileDestination;
import de.evoila.cf.backup.model.enums.JobStatus;
import de.evoila.cf.backup.model.exception.BackupException;
import de.evoila.cf.backup.service.AgentExecutionResponseProducerService;
import de.evoila.cf.backup.service.manager.executer.BackupExecutorService;
import org.bson.types.ObjectId;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Yannic Remmet, Johannes Hiemer.
 *
 * The BackupServiceManager provides methods for executing backups with an implemented ExecutorService. BackupRequests
 * are used to trigger the backup process and a BackupJob will be added to the repository. During the process, the
 * status of the BackupJob will be updated by communicating with the ExecuterService.
 */
@Component
public class BackupServiceManager extends AbstractServiceManager {

    AgentExecutionResponseProducerService agentExecutionResponseProducerService;

    BackupServiceManager(AgentExecutionResponseProducerService agentExecutionResponseProducerService){
        this.agentExecutionResponseProducerService = agentExecutionResponseProducerService;
    }

    /**
     * Execute a BackupJob. During the process, the JobStatus of the BackupJob will be continuously updated. If a
     * backup has been successfully executed, then old backup files which are exceeding the retention period will be
     * automatically removed too.
     *
     * @param backupExecutorService Service with a connection to a component which can create backup files
     * @param endpointCredential Credentials for the FileDestination
     * @param backupJob A BackupJob
     * @param destination Location to store the backup files
     * @param items the storage systems (e.g. database) from the service instance to be backed up
     */
    public void executeBackup(BackupExecutorService backupExecutorService, EndpointCredential endpointCredential, BackupJob backupJob,
                               FileDestination destination, List<String> items) {
        try {
            log.info("Starting execution of Backup Job");


            List<CompletableFuture<AgentBackupResponse>> completionFutures = new ArrayList<>();

            int i = 0;
            for (String item : items) {
                String id = backupJob.getIdAsString() + i;

                BackupPlan backupPlan = backupJob.getBackupPlan();
                backupExecutorService.backup(endpointCredential, destination, id, item,
                        backupPlan.isCompression(), backupPlan.getPublicKey(), backupPlan.getIdAsString());
                backupJob.setDestination(destination);

                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                CompletableFuture<AgentBackupResponse> completionFuture = new CompletableFuture<>();
                completionFutures.add(completionFuture);
                ScheduledFuture checkFuture = executor.scheduleAtFixedRate(() -> {
                    try {
                        AgentBackupResponse agentBackupResponse = backupExecutorService.pollExecutionState(endpointCredential,
                                "backup", id, new ParameterizedTypeReference<AgentBackupResponse>() {});

                        agentExecutionResponseProducerService.backupLog(backupJob, item, agentBackupResponse);
                        if (!agentBackupResponse.getStatus().equals(JobStatus.RUNNING)) {
                            completionFuture.complete(agentBackupResponse);
                        }
                    } catch (BackupException ex) {
                        completionFuture.complete(null);
                    }

                }, 0, 5, TimeUnit.SECONDS);
                completionFuture.whenComplete((result, thrown) -> {
                    if (result != null) {
                        if (result.getStatus().equals(JobStatus.SUCCEEDED)) {
                            backupJob.getFiles().put(item, result.getFilename());
                        }

                        agentExecutionResponseProducerService.backupLog(backupJob, item, result);
                    }

                    checkFuture.cancel(true);
                    log.info("Finished execution of Backup Job");
                });
                i++;
            }

            for(CompletableFuture completionFuture : completionFutures) {
                completionFuture.get();
            }


        } catch (Exception e) {
            log.error("Exception during backup execution", e);
            //updateStateAndLog(backupJob, JobStatus.FAILED, String.format("An error occurred (%s) : %s", backupJob.getId(), e.getMessage()));
        }
    }
}
