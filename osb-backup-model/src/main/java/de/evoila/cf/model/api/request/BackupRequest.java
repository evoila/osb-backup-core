package de.evoila.cf.model.api.request;

import de.evoila.cf.model.api.BackupPlan;

/**
 * @author Yannic Remmet, Johannes Hiemer
 */
public class BackupRequest {

    private BackupPlan backupPlan;

    public BackupPlan getBackupPlan() {
        return backupPlan;
    }

    public void setBackupPlan(BackupPlan backupPlan) {
        this.backupPlan = backupPlan;
    }
}
