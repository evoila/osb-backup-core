package de.evoila.cf.backup.model.agent;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import de.evoila.cf.backup.model.api.endpoint.EndpointCredential;
import de.evoila.cf.backup.model.api.file.FileDestination;
import org.bson.types.ObjectId;
import java.util.Map;
/**
 * @author Johannes Hiemer.
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class BackupRequestEvent extends AbstractRequest {

    private FileDestination destination;

    private EndpointCredential backup;

    private Map<String, String> files;

    public BackupRequestEvent() {}

    public BackupRequestEvent(ObjectId id, boolean compression, String privateKey,
                              FileDestination destination, EndpointCredential backup, String filePath, Map<String,String> items) {
        this.id = id;
        this.destination = destination;
        this.backup = backup;
        this.compression = compression;
        this.encryptionKey = privateKey;
        this.items = items;
        this.filePath = filePath;
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
