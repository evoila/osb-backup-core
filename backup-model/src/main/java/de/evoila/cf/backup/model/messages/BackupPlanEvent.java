package de.evoila.cf.backup.model.messages;

import de.evoila.cf.backup.model.api.BackupPlan;
import de.evoila.cf.broker.model.ServiceInstance;

public class BackupPlanEvent {

    protected Operation operation;

    protected ServiceInstance serviceInstance;

    protected String serviceType;

    protected BackupPlan oldBackupPlan;

    protected BackupPlan newBackupPlan;

    public BackupPlanEvent(Operation operation, BackupPlan oldBackupPlan, BackupPlan newBackupPlan){
        this.operation = operation;
        this.oldBackupPlan = oldBackupPlan;
        this.newBackupPlan = newBackupPlan;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public BackupPlan getOldBackupPlan() {
        return oldBackupPlan;
    }

    public void setOldBackupPlan(BackupPlan backupPlan) {
        this.oldBackupPlan = oldBackupPlan;
    }

    public BackupPlan getNewBackupPlan() {
        return newBackupPlan;
    }

    public void setNewBackupPlan(BackupPlan newBackupPlan) {
        this.oldBackupPlan = newBackupPlan;
    }

    enum Operation{
        create,
        update,
        delete
    }
}
