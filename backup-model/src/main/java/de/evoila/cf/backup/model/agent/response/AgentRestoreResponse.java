package de.evoila.cf.backup.model.agent.response;

/**
 * @author Johannes Hiemer.
 */
public class AgentRestoreResponse extends AgentExecutionResponse {

    private String preRestoreLockLog;

    private String preRestoreLockErrorLog;

    private String restoreLog;

    private String restoreErrorLog;

    private String restoreCleanupLog;

    private String restoreCleanupErrorLog;

    private String postRestoreUnlockLog;

    private String postRestoreUnlockErrorLog;

    AgentRestoreResponse(){

    }

    AgentRestoreResponse(AgentRestoreResponse agentRestoreResponse){
        super(agentRestoreResponse);
        this.restoreCleanupErrorLog = agentRestoreResponse.restoreCleanupErrorLog;
        this.restoreLog = agentRestoreResponse.restoreLog;
        this.restoreErrorLog = agentRestoreResponse.restoreErrorLog;
        this.postRestoreUnlockErrorLog = agentRestoreResponse.postRestoreUnlockErrorLog;
        this.postRestoreUnlockLog = agentRestoreResponse.postRestoreUnlockLog;
    }

    public String getPreRestoreLockLog() {
        return preRestoreLockLog;
    }

    public void setPreRestoreLockLog(String preRestoreLockLog) {
        this.preRestoreLockLog = preRestoreLockLog;
    }

    public String getPreRestoreLockErrorLog() {
        return preRestoreLockErrorLog;
    }

    public void setPreRestoreLockErrorLog(String preRestoreLockErrorLog) {
        this.preRestoreLockErrorLog = preRestoreLockErrorLog;
    }

    public String getRestoreLog() {
        return restoreLog;
    }

    public void setRestoreLog(String restoreLog) {
        this.restoreLog = restoreLog;
    }

    public String getRestoreErrorLog() {
        return restoreErrorLog;
    }

    public void setRestoreErrorLog(String restoreErrorLog) {
        this.restoreErrorLog = restoreErrorLog;
    }

    public String getRestoreCleanupLog() {
        return restoreCleanupLog;
    }

    public void setRestoreCleanupLog(String restoreCleanupLog) {
        this.restoreCleanupLog = restoreCleanupLog;
    }

    public String getRestoreCleanupErrorLog() {
        return restoreCleanupErrorLog;
    }

    public void setRestoreCleanupErrorLog(String restoreCleanupErrorLog) {
        this.restoreCleanupErrorLog = restoreCleanupErrorLog;
    }

    public String getPostRestoreUnlockLog() {
        return postRestoreUnlockLog;
    }

    public void setPostRestoreUnlockLog(String postRestoreUnlockLog) {
        this.postRestoreUnlockLog = postRestoreUnlockLog;
    }

    public String getPostRestoreUnlockErrorLog() {
        return postRestoreUnlockErrorLog;
    }

    public void setPostRestoreUnlockErrorLog(String postRestoreUnlockErrorLog) {
        this.postRestoreUnlockErrorLog = postRestoreUnlockErrorLog;
    }
}
