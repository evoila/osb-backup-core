package de.evoila.cf.backup.model.messages;

import de.evoila.cf.backup.model.api.file.FileDestination;
import de.evoila.cf.backup.model.enums.Operation;
import de.evoila.cf.broker.model.ServiceInstance;

public class ServiceInstanceEvent {

    protected Operation operation;

    protected ServiceInstance oldServiceInstance;

    protected ServiceInstance newServiceInstance;

    public ServiceInstanceEvent(Operation operation, ServiceInstance oldServiceInstance, ServiceInstance newServiceInstance){
        this.operation = operation;
        this.oldServiceInstance = oldServiceInstance;
        this.newServiceInstance = newServiceInstance;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public ServiceInstance getOldServiceInstance() {
        return oldServiceInstance;
    }

    public void setOldServiceInstance(ServiceInstance oldServiceInstance) {
        this.oldServiceInstance = oldServiceInstance;
    }

    public ServiceInstance getNewServiceInstance() {
        return newServiceInstance;
    }

    public void setNewServiceInstance(ServiceInstance newServiceInstance) {
        this.newServiceInstance = newServiceInstance;
    }
}
