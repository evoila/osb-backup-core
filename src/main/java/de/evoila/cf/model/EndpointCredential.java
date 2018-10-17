package de.evoila.cf.model;

import de.evoila.cf.model.enums.BackupType;

public class EndpointCredential {

    String serviceInstanceId;

    String hostname;

    int port;

    String username;

    String password;

    BackupType type;

    public String getServiceInstanceId() {
        return serviceInstanceId;
    }

    public void setServiceInstanceId(String serviceInstanceId) {
        this.serviceInstanceId = serviceInstanceId;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public BackupType getType() {
        return type;
    }

    public void setType(BackupType type) {
        this.type = type;
    }

    public void setType(String type) {
        this.type = BackupType.valueOf(type);
    }

}