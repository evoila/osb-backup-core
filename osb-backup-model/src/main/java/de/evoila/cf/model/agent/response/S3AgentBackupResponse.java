package de.evoila.cf.model.agent.response;

/**
 * @author Johannes Hiemer.
 */
public class S3AgentBackupResponse extends AgentBackupResponse {

    private String region;

    private String bucket;

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
