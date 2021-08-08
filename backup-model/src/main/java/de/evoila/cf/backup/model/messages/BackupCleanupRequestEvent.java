package de.evoila.cf.backup.model.messages;

import de.evoila.cf.backup.model.api.BackupJob;

public class BackupCleanupRequestEvent {

    BackupJob backupJob;

    public BackupCleanupRequestEvent() {

    }

    public BackupCleanupRequestEvent(BackupJob backupJob) {
        this.backupJob = backupJob;
    }

    public BackupJob getBackupJob() {
        return backupJob;
    }

    public void setBackupJob(BackupJob backupJob) {
        this.backupJob = backupJob;
    }

}