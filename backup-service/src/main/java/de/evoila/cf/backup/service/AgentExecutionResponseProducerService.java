package de.evoila.cf.backup.service;

import de.evoila.cf.backup.model.agent.response.*;
import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.api.RestoreJob;
import de.evoila.cf.backup.model.enums.Operation;
import de.evoila.cf.backup.model.messages.ServiceInstanceEvent;
import de.evoila.cf.broker.model.ServiceInstance;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AutoConfigureAfter(BackupJob.class)
public class AgentExecutionResponseProducerService {
    KafkaTemplate<String, AgentExecutionResponse> agentExecutionResponseKafkaTemplate;

    private String createTopic(){
        String topic ="Backup-JobResult-Logs";
        return  topic;
    }

    AgentExecutionResponseProducerService(KafkaTemplate<String, AgentExecutionResponse> agentExecutionResponseKafkaTemplate) {
        this.agentExecutionResponseKafkaTemplate = agentExecutionResponseKafkaTemplate;
    }

    public void backupLog(BackupJob backupJob, String item, AgentBackupResponse backupResponse){
        BackupResultEvent backupResultEvent = (BackupResultEvent)backupResponse;
        backupResultEvent.setItem(item);
        backupResultEvent.setBackupJob(backupJob);
        agentExecutionResponseKafkaTemplate.send(createTopic(),backupJob.getIdAsString(),backupResultEvent);
    }

    public void restoreLog(RestoreJob restoreJob, String item, AgentRestoreResponse backupResponse){
        RestoreResultEvent restoreResultEvent = (RestoreResultEvent)backupResponse;
        restoreResultEvent.setItem(item);
        restoreResultEvent.setRestoreJob(restoreJob);
        agentExecutionResponseKafkaTemplate.send(createTopic(),restoreJob.getIdAsString(),restoreResultEvent);
    }
}
