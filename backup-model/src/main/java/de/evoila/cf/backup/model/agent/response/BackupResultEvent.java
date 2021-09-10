package de.evoila.cf.backup.model.agent.response;

import de.evoila.cf.backup.model.api.BackupJob;

public class BackupResultEvent extends  AgentBackupResponse {

    private String item;
    private BackupJob backupJob;

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

    BackupResultEvent(){

    }

    public BackupResultEvent(AgentBackupResponse agentBackupResponse){
        super(agentBackupResponse);
    }

}
