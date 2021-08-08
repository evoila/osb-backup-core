package de.evoila.cf.backup.model.messages;

import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.enums.JobStatus;

public class BackupCleanupResultEvent {
    JobStatus status;
    String message;
    BackupJob backupJob;

    public BackupCleanupResultEvent(){}

    public BackupCleanupResultEvent(BackupJob backupJob, String message, JobStatus jobStatus) {
        this.backupJob = backupJob;
        this.message = message;
        this.backupJob = backupJob;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public BackupJob getBackupJob() {
        return backupJob;
    }

    public void setBackupJob(BackupJob backupJob) {
        this.backupJob = backupJob;
    }
}