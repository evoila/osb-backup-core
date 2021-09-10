package de.evoila.cf.backup.model.agent.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import de.evoila.cf.backup.model.enums.JobStatus;

import java.util.Date;

/**
 * @author Johannes Hiemer.
 */

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AgentExecutionResponse {

    protected JobStatus status;

    protected String message = "";

    protected String errorMessage = "";

    protected Date startTime;

    protected Date endTime;

    protected long executionTime;

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(long executionTime) {
        this.executionTime = executionTime;
    }

    AgentExecutionResponse(){

    }

    AgentExecutionResponse(AgentExecutionResponse agentExecutionResponse){
        this.endTime = agentExecutionResponse.endTime;
        this.executionTime = agentExecutionResponse.executionTime;
        this.errorMessage = agentExecutionResponse.errorMessage;
        this.message = agentExecutionResponse.message;
        this.startTime = agentExecutionResponse.startTime;
        this.status = agentExecutionResponse.status;
    }
}
