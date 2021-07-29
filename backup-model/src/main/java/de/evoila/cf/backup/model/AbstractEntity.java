package de.evoila.cf.backup.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class AbstractEntity implements BaseEntity<ObjectId> {

    @Id
    @JsonSerialize(using=ToStringSerializer.class)
    private ObjectId id;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getIdAsString() {
        if (this.id != null)
            return this.id.toString();
        return null;
    }

}
