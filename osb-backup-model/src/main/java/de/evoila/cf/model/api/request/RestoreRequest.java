package de.evoila.cf.model.api.request;

import de.evoila.cf.model.api.BackupJob;
import de.evoila.cf.model.api.RestoreJob;

import java.util.List;

/**
 * @author Yannic Remmet, Johannes Hiemer.
 */
public class RestoreRequest {

    private BackupJob backupJob;

    private List<RequestDetails> items;

    public BackupJob getBackupJob() {
        return backupJob;
    }

    public void setBackupJob(BackupJob backupJob) {
        this.backupJob = backupJob;
    }

    public List<RequestDetails> getItems() {
        return items;
    }

    public void setItems(List<RequestDetails> items) {
        this.items = items;
    }

}