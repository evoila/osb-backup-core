package de.evoila.cf.backup.service.manager.executer.request;

import de.evoila.cf.backup.model.api.endpoint.EndpointCredential;
import de.evoila.cf.backup.model.api.file.FileDestination;

/**
 * @author Johannes Hiemer.
 */
public class AgentBackupRequest extends AbstractRequest {

    private FileDestination destination;

    private EndpointCredential backup;

    public AgentBackupRequest() {}

    public AgentBackupRequest(String id, boolean compression, String privateKey,
                              FileDestination destination, EndpointCredential backup) {
        this.id = id;
        this.destination = destination;
        this.backup = backup;
        this.compression = compression;
        this.encryptionKey = privateKey;
    }

    public FileDestination getDestination() {
        return destination;
    }

    public void setDestination(FileDestination destination) {
        this.destination = destination;
    }

    public EndpointCredential getBackup() {
        return backup;
    }

    public void setBackup(EndpointCredential backup) {
        this.backup = backup;
    }
}
