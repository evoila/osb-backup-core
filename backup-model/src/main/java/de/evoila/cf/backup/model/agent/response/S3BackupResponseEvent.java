package de.evoila.cf.backup.model.agent.response;

/**
 * @author Johannes Hiemer.
 */
public class S3BackupResponseEvent extends AgentBackupResponse {

    private String region;

    private String bucket;

    public S3BackupResponseEvent(){
        super();
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
