package de.evoila.cf.backup.model.api.endpoint;

import de.evoila.cf.broker.model.ServiceInstance;
import de.evoila.cf.backup.model.enums.BackupType;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Map;

/**
 * @author Johannes Hiemer.
 */
public class EndpointCredential {

    @DBRef
    private ServiceInstance serviceInstance;

    private String host;

    private int port;

    private String username;

    private String password;

    private String backupUsername;

    private String backupPassword;

    private String database;

    private BackupType type;

    private Map<String, Object> parameters;

    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
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

    public String getBackupUsername() {
        return backupUsername;
    }

    public void setBackupUsername(String backupUsername) {
        this.backupUsername = backupUsername;
    }

    public String getBackupPassword() {
        return backupPassword;
    }

    public void setBackupPassword(String backupPassword) {
        this.backupPassword = backupPassword;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public BackupType getType() {
        return type;
    }

    public void setType(BackupType type) {
        this.type = type;
    }

    public void setTypeFromString(String type) {
        this.type = BackupType.valueOf(type);
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        this.parameters = parameters;
    }
}
