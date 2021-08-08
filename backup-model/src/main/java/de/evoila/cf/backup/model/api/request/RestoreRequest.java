package de.evoila.cf.backup.model.api.request;

import de.evoila.cf.backup.model.api.BackupJob;

import java.util.List;

/**
 * @author Yannic Remmet, Johannes Hiemer.
 */
public class RestoreRequest {

    private BackupJob backupJobEvent;

    private List<RequestDetails> items;

    public BackupJob getBackupJob() {
        return backupJobEvent;
    }

    public void setBackupJob(BackupJob backupJobEvent) {
        this.backupJobEvent = backupJobEvent;
    }

    public List<RequestDetails> getItems() {
        return items;
    }

    public void setItems(List<RequestDetails> items) {
        this.items = items;
    }

}