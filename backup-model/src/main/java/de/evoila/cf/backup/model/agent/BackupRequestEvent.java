package de.evoila.cf.backup.model.agent;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import de.evoila.cf.backup.model.api.endpoint.EndpointCredential;
import de.evoila.cf.backup.model.api.file.FileDestination;

/**
 * @author Johannes Hiemer.
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BackupRequestEvent extends AbstractRequest {

    private FileDestination destination;

    private EndpointCredential backup;

    public BackupRequestEvent() {}

    public BackupRequestEvent(String id, boolean compression, String privateKey,
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
