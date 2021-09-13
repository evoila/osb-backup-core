package de.evoila.cf.backup.service.manager.executer;

import de.evoila.cf.backup.model.agent.response.AgentRestoreResponse;
import de.evoila.cf.backup.model.api.endpoint.EndpointCredential;
import de.evoila.cf.backup.model.api.file.FileDestination;
import de.evoila.cf.backup.model.api.request.RequestDetails;
import de.evoila.cf.backup.model.exception.BackupException;
import de.evoila.cf.backup.service.manager.executer.request.AgentRestoreRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * @author Yannic Remmet, Johannes Hiemer.
 *
 * The AgentBasedRestoreExecutorService communicates with the Backup Agent to execute restorations of backups.
 */
@Service
public class AgentBasedRestoreExecutorService extends AgentBasedExecutorService implements RestoreExecutorService {

    /**
     * Start a restore process with the help of the Backup Agent.
     *
     * @param endpointCredential the credentials to access the ServiceInstance
     * @param destination the storage from where the backup files can be retrieved
     * @param requestDetails object containing the database and filename of the backup
     * @param id set the ID of the AgentRestoreRequest
     * @param compression if the files are compressed or not
     * @param privateKey a key to access the files on the destination
     * @param planId the ID of the BackupPlan
     * @throws BackupException
     */
    @Override
    public void restore(EndpointCredential endpointCredential, FileDestination destination,
                        RequestDetails requestDetails, String id, boolean compression, String privateKey,
                        String planId) throws BackupException {
        endpointCredential.setDatabase(requestDetails.getItem());
        destination.setFilename(requestDetails.getFilename());
        destination.setFilenamePrefix(planId + "/");

        log.info(String.format("Starting restore process to %s:%d/%s",
                endpointCredential.getHost(),
                endpointCredential.getPort(),
                requestDetails.getItem()
        ));

        log.info("Calling Agent to run Restore Process");
        AgentRestoreRequest agentRestoreRequest = new AgentRestoreRequest(id, compression, privateKey,
                destination, endpointCredential);

        this.setAuthenticationHeader(endpointCredential.getBackupUsername(),
                endpointCredential.getBackupPassword());
        HttpHeaders httpHeaders = this.setAuthenticationHeader(endpointCredential.getBackupUsername(),
                endpointCredential.getBackupPassword());
        HttpEntity entity = new HttpEntity(agentRestoreRequest, httpHeaders);

        try {
            ResponseEntity<AgentRestoreResponse> agentRestoreResponse = restTemplate
                .exchange("http://" + endpointCredential.getHost() + ":8000/restore",
                        HttpMethod.PUT, entity, AgentRestoreResponse.class);
        } catch (Exception ex) {
            throw new BackupException("Error during Restore Process Run Call", ex);
            // we don't need to here anything.
        }
    }

}
