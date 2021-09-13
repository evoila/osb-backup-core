package de.evoila.cf.backup.service.manager.executer;

import de.evoila.cf.backup.model.agent.response.AgentBackupResponse;
import de.evoila.cf.backup.model.agent.response.AgentExecutionResponse;
import de.evoila.cf.backup.model.api.endpoint.EndpointCredential;
import de.evoila.cf.backup.model.api.file.FileDestination;
import de.evoila.cf.backup.model.enums.JobStatus;
import de.evoila.cf.backup.model.exception.BackupException;
import de.evoila.cf.backup.service.manager.executer.request.AgentBackupRequest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author Yannic Remmet, Johannes Hiemer.
 *
 * The AgentBasedBackupExecutorService communicates with the Backup Agent to execute backups.
 */
@Service
public class AgentBasedBackupExecutorService extends AgentBasedExecutorService implements BackupExecutorService {

    public AgentBasedBackupExecutorService() {}

    /**
     * Start a backup process with the help of the Backup Agent.
     *
     * @param endpointCredential Credentials to access the ServiceInstance
     * @param destination Destination to store the backup files
     * @param id set the ID of the AgentBackupRequest
     * @param item a collection of data (e.g. database) on the service instance, for which to create backup files from
     * @param compression define if the backupfiles should be compressed
     * @param publicKey a key to access the files on the destination
     * @param planId the ID of the BackupPlan
     * @throws BackupException
     */
    @Override
    public void backup(EndpointCredential endpointCredential, FileDestination destination, String id,
                       String item, boolean compression, String publicKey, String planId) throws BackupException {
        endpointCredential.setDatabase(item);
        destination.setFilenamePrefix(planId + "/");

        log.info(String.format("Starting backup process to %s:%d/%s",
                endpointCredential.getHost(),
                endpointCredential.getPort(),
                item
        ));

        log.info("Calling Agent to run Backup Process");
        AgentBackupRequest agentBackupRequest = new AgentBackupRequest(id, compression, publicKey,
                destination, endpointCredential);

        log.info(String.format("Credentials are %s:%s",
                endpointCredential.getBackupUsername(),
                endpointCredential.getBackupPassword()
        ));
        this.setAuthenticationHeader(endpointCredential.getBackupUsername(),
                endpointCredential.getBackupPassword());
        HttpHeaders httpHeaders = this.setAuthenticationHeader(endpointCredential.getBackupUsername(),
                endpointCredential.getBackupPassword());
        HttpEntity entity = new HttpEntity(agentBackupRequest, httpHeaders);


        try {
            restTemplate
                    .exchange("http://" + endpointCredential.getHost() + ":8000/backup",
                            HttpMethod.POST, entity, AgentBackupResponse.class);
        } catch (Exception ex) {
            throw new BackupException("Error during Backup Process Run Call", ex);
        }

    }

    /**
     * Send out a new http request to check for the JobStatus.
     *
     * @param endpointCredential Credentials for the ServiceInstance
     * @param suffix TODO
     * @param id TODO
     * @param type TODO
     * @param <T> TODO
     * @return A AgentExecutionResponse with information on the job
     */
    public <T extends AgentExecutionResponse> T pollExecutionState(EndpointCredential endpointCredential, String suffix, String id,
                                                                   ParameterizedTypeReference<T> type) {
        T agentExecutionResponse = null;
        try {
            log.info("Polling state of Backup Process for " + id);

            this.setAuthenticationHeader(endpointCredential.getBackupUsername(),
                    endpointCredential.getBackupPassword());
            HttpEntity entity = new HttpEntity(headers);

            ResponseEntity<T> agentExecutionResponseEntity = restTemplate
                    .exchange("http://" + endpointCredential.getHost() + ":8000/" + suffix + "/" + id,
                            HttpMethod.GET, entity, type);
            agentExecutionResponse = agentExecutionResponseEntity.getBody();
        } catch (Exception ex) {
            log.error("Failed to poll task", ex);
            agentExecutionResponse.setStatus(JobStatus.FAILED);
            // we don't need to here anything.
        }
        return agentExecutionResponse;
    }
}
