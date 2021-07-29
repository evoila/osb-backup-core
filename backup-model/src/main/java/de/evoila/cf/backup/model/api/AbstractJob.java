package de.evoila.cf.backup.model.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import de.evoila.cf.backup.model.AbstractEntity;
import de.evoila.cf.backup.model.agent.response.AgentExecutionResponse;
import de.evoila.cf.backup.model.api.file.FileDestination;
import de.evoila.cf.broker.model.ServiceInstance;
import de.evoila.cf.backup.model.enums.JobStatus;
import de.evoila.cf.backup.model.enums.JobType;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

/**
 * @author Yannic Remmet, Johannes Hiemer.
 */
@Document(collection = "backopOrRestoreJob")
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "jobType", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = BackupJob.class, name = "BACKUP"),
        @JsonSubTypes.Type(value = RestoreJob.class, name = "RESTORE")
})
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class AbstractJob extends AbstractEntity {

    protected Date startDate;

    protected Date endDate;

    @DBRef
    protected ServiceInstance serviceInstance;

    protected JobStatus status;

    protected JobType jobType;

    @DBRef
    protected FileDestination destination;

    protected List<String> jobLogs = new ArrayList<>();
    protected Map<String, AgentExecutionResponse> agentExecutionReponses = new HashMap<>();

    @DBRef
    protected BackupPlan backupPlan;

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public FileDestination getDestination() {
        return destination;
    }

    public void setDestination(FileDestination destination) {
        this.destination = destination;
    }

    public List<String> getJobLogs() {
        return jobLogs;
    }

    public void setJobLogs(List<String> jobLogs) {
        this.jobLogs = jobLogs;
    }

    public Map<String, AgentExecutionResponse> getAgentExecutionReponses() {
        return agentExecutionReponses;
    }

    public void setAgentExecutionReponses(Map<String, AgentExecutionResponse> agentExecutionReponses) {
        this.agentExecutionReponses = agentExecutionReponses;
    }

    public void appendLog(String msg) {
        this.jobLogs.add(msg);
    }

    public BackupPlan getBackupPlan() {
        return backupPlan;
    }

    public void setBackupPlan(BackupPlan backupPlan) {
        this.backupPlan = backupPlan;
    }
}
