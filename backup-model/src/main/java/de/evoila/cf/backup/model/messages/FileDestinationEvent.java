package de.evoila.cf.backup.model.messages;

import de.evoila.cf.backup.model.api.file.FileDestination;
import de.evoila.cf.backup.model.enums.Operation;
import de.evoila.cf.broker.model.ServiceInstance;

public class FileDestinationEvent {

    protected Operation operation;

    protected ServiceInstance serviceInstance;

    protected String serviceType;

    protected FileDestination oldDestination;

    protected FileDestination newDestination;

    public FileDestinationEvent(Operation operation, FileDestination oldDestination, FileDestination newDestination){
        this.operation = operation;
        this.oldDestination = oldDestination;
        this.newDestination = newDestination;
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
    public FileDestination getOldDestination() {
        return oldDestination;
    }

    public void setOldDestination(FileDestination oldDestination) {
        this.oldDestination = oldDestination;
    }

    public FileDestination getNewDestination() {
        return newDestination;
    }

    public void setNewDestination(FileDestination newDestination) {
        this.newDestination = newDestination;
    }
}
