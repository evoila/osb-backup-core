package de.evoila.cf.model.api.file;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Yannic Remmet, Johannes Hiemer
 */
@Document(collection = "fileDestination")
public class S3FileDestination extends FileDestination {

    private String authKey;

    private String authSecret;

    private String region;

    private String bucket;

    public S3FileDestination() {
        super();
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public String getAuthSecret() {
        return authSecret;
    }

    public void setAuthSecret(String authSecret) {
        this.authSecret = authSecret;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

}
