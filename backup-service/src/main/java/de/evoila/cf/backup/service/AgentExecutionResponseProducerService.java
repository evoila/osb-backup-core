package de.evoila.cf.backup.service;

import de.evoila.cf.backup.model.agent.response.*;
import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.api.RestoreJob;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AutoConfigureAfter(BackupJob.class)
public class AgentExecutionResponseProducerService {
    KafkaTemplate<String, AgentExecutionResponse> agentExecutionResponseKafkaTemplate;

    private String createTopic(){
        String topic ="Backup-JobResultLogs";
        return  topic;
    }

    AgentExecutionResponseProducerService(KafkaTemplate<String, AgentExecutionResponse> agentExecutionResponseKafkaTemplate) {
        this.agentExecutionResponseKafkaTemplate = agentExecutionResponseKafkaTemplate;
    }

    public void backupLog(BackupJob backupJob, String item, AgentBackupResponse backupResponse){
        BackupResultEvent backupResultEvent = new BackupResultEvent(backupResponse);
        backupResultEvent.setItem(item);
        backupResultEvent.setBackupJob(backupJob);
        agentExecutionResponseKafkaTemplate.send(createTopic(),backupJob.getIdAsString(),backupResultEvent);
    }

    public void restoreLog(RestoreJob restoreJob, String item, AgentRestoreResponse agentRestoreResponse){
        RestoreResultEvent restoreResultEvent = new RestoreResultEvent(agentRestoreResponse);
        restoreResultEvent.setItem(item);
        restoreResultEvent.setRestoreJob(restoreJob);
        agentExecutionResponseKafkaTemplate.send(createTopic(),restoreJob.getIdAsString(),restoreResultEvent);
    }
}
