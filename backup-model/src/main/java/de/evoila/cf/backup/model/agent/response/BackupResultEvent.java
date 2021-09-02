package de.evoila.cf.backup.model.agent.response;

import de.evoila.cf.backup.model.api.BackupJob;

public class BackupResultEvent extends  AgentBackupResponse {

    String item;
    BackupJob backupJob;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public BackupJob getBackupJob() {
        return backupJob;
    }

    public void setBackupJob(BackupJob backupJob) {
        this.backupJob = backupJob;
    }

}
