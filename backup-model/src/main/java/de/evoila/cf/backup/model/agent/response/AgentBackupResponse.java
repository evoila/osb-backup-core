package de.evoila.cf.backup.model.agent.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

/**
 * @author Johannes Hiemer.
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AgentBackupResponse extends AgentExecutionResponse {

    private String filenamePrefix = "";

    private String filename = "";

    private Filesize filesize;

    private String preBackupLockLog = "";

    private String preBackupLockErrorLog = "";

    private String preBackCheckLog = "";

    private String preBackCheckErrorLog = "";

    private String backupLog = "";

    private String backupErrorLog = "";

    private String backupCleanupLog = "";

    private String backupCleanupErrorLog = "";

    private String postBackupUnlockLog = "";

    private String postBackupUnlockErrorLog = "";

    public class Filesize {

        private int size;

        private String unit;

        public Filesize() {}

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
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

    public Filesize getFilesize() {
        return filesize;
    }

    public void setFilesize(Filesize filesize) {
        this.filesize = filesize;
    }

    public String getPreBackupLockLog() {
        return preBackupLockLog;
    }

    public void setPreBackupLockLog(String preBackupLockLog) {
        this.preBackupLockLog = preBackupLockLog;
    }

    public String getPreBackupLockErrorLog() {
        return preBackupLockErrorLog;
    }

    public void setPreBackupLockErrorLog(String preBackupLockErrorLog) {
        this.preBackupLockErrorLog = preBackupLockErrorLog;
    }

    public String getPreBackCheckLog() {
        return preBackCheckLog;
    }

    public void setPreBackCheckLog(String preBackCheckLog) {
        this.preBackCheckLog = preBackCheckLog;
    }

    public String getPreBackCheckErrorLog() {
        return preBackCheckErrorLog;
    }

    public void setPreBackCheckErrorLog(String preBackCheckErrorLog) {
        this.preBackCheckErrorLog = preBackCheckErrorLog;
    }

    public String getBackupLog() {
        return backupLog;
    }

    public void setBackupLog(String backupLog) {
        this.backupLog = backupLog;
    }

    public String getBackupErrorLog() {
        return backupErrorLog;
    }

    public void setBackupErrorLog(String backupErrorLog) {
        this.backupErrorLog = backupErrorLog;
    }

    public String getBackupCleanupLog() {
        return backupCleanupLog;
    }

    public void setBackupCleanupLog(String backupCleanupLog) {
        this.backupCleanupLog = backupCleanupLog;
    }

    public String getBackupCleanupErrorLog() {
        return backupCleanupErrorLog;
    }

    public void setBackupCleanupErrorLog(String backupCleanupErrorLog) {
        this.backupCleanupErrorLog = backupCleanupErrorLog;
    }

    public String getPostBackupUnlockLog() {
        return postBackupUnlockLog;
    }

    public void setPostBackupUnlockLog(String postBackupUnlockLog) {
        this.postBackupUnlockLog = postBackupUnlockLog;
    }

    public String getPostBackupUnlockErrorLog() {
        return postBackupUnlockErrorLog;
    }

    public void setPostBackupUnlockErrorLog(String postBackupUnlockErrorLog) {
        this.postBackupUnlockErrorLog = postBackupUnlockErrorLog;
    }

    AgentBackupResponse(){

    }

    AgentBackupResponse(AgentBackupResponse agentBackupResponse){
        super(agentBackupResponse);
        this.filename = agentBackupResponse.filename;
        this.backupCleanupLog = agentBackupResponse.backupCleanupLog;
        this.backupCleanupErrorLog = agentBackupResponse.backupCleanupErrorLog;
        this.backupLog = agentBackupResponse.backupLog;
        this.backupErrorLog = agentBackupResponse.backupErrorLog;
        this.filenamePrefix = agentBackupResponse.getFilenamePrefix();
        this.postBackupUnlockErrorLog = agentBackupResponse.postBackupUnlockErrorLog;
        this.postBackupUnlockLog = agentBackupResponse.postBackupUnlockLog;
        this.preBackCheckLog = agentBackupResponse.preBackCheckLog;
        this.preBackCheckErrorLog = agentBackupResponse.preBackCheckErrorLog;
        this.preBackupLockLog = agentBackupResponse.preBackupLockLog;
        this.preBackupLockErrorLog = agentBackupResponse.preBackupLockErrorLog;
    }
}