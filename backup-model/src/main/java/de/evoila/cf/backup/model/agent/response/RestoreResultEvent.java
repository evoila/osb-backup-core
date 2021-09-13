package de.evoila.cf.backup.model.agent.response;

import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.api.RestoreJob;

public class RestoreResultEvent extends  AgentRestoreResponse {

    private String item;
    private RestoreJob restoreJob;

    RestoreResultEvent(){

    }

    public RestoreResultEvent(AgentRestoreResponse agentRestoreResponse){
        super(agentRestoreResponse);
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public RestoreJob getRestoreJob() {
        return restoreJob;
    }

    public void setRestoreJob(RestoreJob restoreJob) {
        this.restoreJob = restoreJob;
    }

}
