package de.evoila.cf.model;

/**
 * Created by yremmet on 27.06.17.
 */
public class BackupRequest {

    private BackupPlan plan;

    private String destinationId;

    public BackupPlan getPlan() {
        return plan;
    }

    public void setPlan(BackupPlan plan) {
        this.plan = plan;
    }

    public void setDestinationId (String destinationId) {
        this.destinationId = destinationId;
    }

    public String getDestinationId () {
        return destinationId;
    }
}
