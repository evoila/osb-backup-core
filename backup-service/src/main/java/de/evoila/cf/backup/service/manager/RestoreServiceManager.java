package de.evoila.cf.backup.service.manager;


import de.evoila.cf.backup.model.agent.response.AgentRestoreResponse;
import de.evoila.cf.backup.model.api.BackupPlan;
import de.evoila.cf.backup.model.api.RestoreJob;
import de.evoila.cf.backup.model.api.endpoint.EndpointCredential;
import de.evoila.cf.backup.model.api.file.FileDestination;
import de.evoila.cf.backup.model.api.request.RequestDetails;
import de.evoila.cf.backup.model.enums.JobStatus;
import de.evoila.cf.backup.model.exception.BackupException;
import de.evoila.cf.backup.service.AgentExecutionResponseProducerService;
import de.evoila.cf.backup.service.manager.executer.RestoreExecutorService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.*;

/**
 * @author Yannic Remmet, Johannes Hiemer.
 *
 * The BackupServiceManager provides methods for restoring backups with an implemented ExecutorService. RestoreRequests
 * are used to trigger the restoration process and a RestoreJob will be added to the repository. During the process, the
 * status of the RestoreJob will be updated by communicating with the ExecuterService.
 */
@Component
public class RestoreServiceManager extends AbstractServiceManager {

    AgentExecutionResponseProducerService agentExecutionResponseProducerService;

    public RestoreServiceManager(AgentExecutionResponseProducerService agentExecutionResponseProducerService) {
        this.agentExecutionResponseProducerService = agentExecutionResponseProducerService;
    }

    /**
     * Execute a RestoreJob. During the process, the JobStatus of the RestoreJob will be continuously updated. Backup
     * files stored in the specified destination and database instances will be restored.
     *
     * @param restoreExecutorService Service with a connection to the component, which can restore backup files
     * @param restoreJob A RestoreJob
     * @param endpointCredential Credentials for the ServiceInstance
     * @param destination Location to restore the backup files from
     * @param items Database instances on the specified destination
     */
    private void executeRestore(RestoreExecutorService restoreExecutorService, RestoreJob restoreJob, EndpointCredential endpointCredential,
                                FileDestination destination, List<RequestDetails> items) {
        try {
            log.info("Starting execution of Restore Job");

            int i = 0;
            for (RequestDetails requestDetails : items) {
                String id = restoreJob.getIdAsString() + i;

                BackupPlan backupPlan = restoreJob.getBackupPlan();
                restoreExecutorService.restore(endpointCredential, destination, requestDetails, id,
                        backupPlan.isCompression(), backupPlan.getPrivateKey(), backupPlan.getIdAsString());

                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                CompletableFuture<AgentRestoreResponse> completionFuture = new CompletableFuture<>();
                ScheduledFuture checkFuture = executor.scheduleAtFixedRate(() -> {
                    try {
                        AgentRestoreResponse agentRestoreResponse = restoreExecutorService.pollExecutionState(endpointCredential,
                                "restore", id, new ParameterizedTypeReference<AgentRestoreResponse>() {});

                        agentExecutionResponseProducerService.restoreLog(restoreJob, requestDetails.getItem(), agentRestoreResponse);
                        if (!agentRestoreResponse.getStatus().equals(JobStatus.RUNNING)) {
                            completionFuture.complete((AgentRestoreResponse) agentRestoreResponse);
                        }
                    } catch (BackupException ex) {
                        completionFuture.complete(null);
                    }

                }, 0, 5, TimeUnit.SECONDS);
                completionFuture.whenComplete((result, thrown) -> {
                    if (result != null) {
                        agentExecutionResponseProducerService.restoreLog(restoreJob, requestDetails.getItem(), result);
                    }

                    checkFuture.cancel(true);
                    log.info("Finished execution of Restore Job");
                });
                i++;
            }
        } catch (BackupException e) {
            log.error("Exception during restore execution", e);
            //updateStateAndLog(restoreJob, JobStatus.FAILED, String.format("An error occurred (%s) : %s", restoreJob.getId(), e.getMessage()));
        }
    }
}
