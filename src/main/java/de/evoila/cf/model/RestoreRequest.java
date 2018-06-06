package de.evoila.cf.model;

/**
 * Created by yremmet on 28.06.17.
 */
public class RestoreRequest {

    private BackupPlan plan;

    private FileDestination source;

    public BackupPlan getPlan() {
        return plan;
    }

    public void setPlan(BackupPlan plan) {
        this.plan = plan;
    }

    public FileDestination getSource() {
        return source;
    }

    public void setSource(FileDestination source) {
        this.source = source;
    }

}
