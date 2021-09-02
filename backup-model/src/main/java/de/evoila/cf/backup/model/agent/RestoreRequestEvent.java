package de.evoila.cf.backup.model.agent;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import de.evoila.cf.backup.model.api.RestoreJob;
import de.evoila.cf.backup.model.api.endpoint.EndpointCredential;
import de.evoila.cf.backup.model.api.file.FileDestination;
import org.bson.types.ObjectId;
import java.util.Map;

/**
 * @author Johannes Hiemer.
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class RestoreRequestEvent extends AbstractRequest {

    private FileDestination destination;

    private EndpointCredential restore;

    private RestoreJob restoreJob;

    public RestoreRequestEvent() {}

    public RestoreRequestEvent(RestoreJob restoreJob, boolean compression, String privateKey,
                               FileDestination destination, EndpointCredential restore, String filePath, Map<String, String> items) {
        this.restoreJob = restoreJob;
        this.destination = destination;
        this.restore = restore;
        this.compression = compression;
        this.encryptionKey = privateKey;
        this.filePath = filePath;
        this.items = items;
    }

    public FileDestination getDestination() {
        return destination;
    }

    public void setDestination(FileDestination destination) {
        this.destination = destination;
    }

    public EndpointCredential getRestore() {
        return restore;
    }

    public void setRestore(EndpointCredential restore) {
        this.restore = restore;
    }

    public RestoreJob getRestoreJob() {
        return restoreJob;
    }

    public void setRestoreJob(RestoreJob restoreJob) {
        this.restoreJob = restoreJob;
    }
}
