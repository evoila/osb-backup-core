package de.evoila.cf.backup.model.api.request;

import de.evoila.cf.backup.model.api.BackupPlan;

/**
 * @author Yannic Remmet, Johannes Hiemer
 */
public class BackupRequest {

    private BackupPlan backupPlan;

    public BackupRequest(){

    }

    BackupRequest(BackupPlan backupPlan){
        this.backupPlan = backupPlan;
    }

    public BackupPlan getBackupPlan() {
        return backupPlan;
    }

    public void setBackupPlan(BackupPlan backupPlan) {
        this.backupPlan = backupPlan;
    }
}
