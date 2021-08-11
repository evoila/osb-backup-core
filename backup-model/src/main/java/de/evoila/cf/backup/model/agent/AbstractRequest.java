package de.evoila.cf.backup.model.agent;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import de.evoila.cf.backup.model.enums.OperationType;
import org.bson.types.ObjectId;

import java.util.Map;

/**
 * @author Johannes Hiemer.
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "operation", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BackupRequestEvent.class, name = "BACKUP"),
        @JsonSubTypes.Type(value = RestoreRequestEvent.class, name = "RESTORE")
})
public class AbstractRequest {


    protected ObjectId id;

    protected  boolean compression;

    protected String encryptionKey;

    protected Map<String, String> files;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    protected OperationType operation;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public boolean isCompression() {
        return compression;
    }

    public void setCompression(boolean compression) {
        this.compression = compression;
    }

    public String getEncryptionKey() {
        return encryptionKey;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public OperationType getOperation() {
        return operation;
    }

    public void setOperation(OperationType operation) {
        this.operation = operation;
    }


    public void setFiles(Map<String,String> files){
        this.files = files;
    }

    public Map<String,String> getFiles(){
        return files;
    }

}
