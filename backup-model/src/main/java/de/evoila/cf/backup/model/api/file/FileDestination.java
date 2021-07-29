package de.evoila.cf.backup.model.api.file;

import com.fasterxml.jackson.annotation.*;
import de.evoila.cf.broker.model.ServiceInstance;
import de.evoila.cf.backup.model.AbstractEntity;
import de.evoila.cf.backup.model.enums.DestinationType;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Yannic Remmet, Johannes Hiemer
 */
@Document(collection = "fileDestination")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = S3FileDestination.class, name = "S3"),
        @JsonSubTypes.Type(value = SwiftFileDestination.class, name = "SWIFT")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class FileDestination extends AbstractEntity {

    @DBRef
    protected ServiceInstance serviceInstance;

    protected String name;

    protected String username;

    protected String password;

    protected DestinationType type;

    protected String filenamePrefix;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected String filename;

    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public DestinationType getType() {
        return type;
    }

    public void setType(DestinationType type) {
        this.type = type;
    }

    public String getFilenamePrefix() {
        return filenamePrefix;
    }

    public void setFilenamePrefix(String filenamePrefix) {
        this.filenamePrefix = filenamePrefix;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

}