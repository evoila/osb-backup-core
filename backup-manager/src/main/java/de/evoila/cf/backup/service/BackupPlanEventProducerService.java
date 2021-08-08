package de.evoila.cf.backup.service;

import de.evoila.cf.backup.config.BackupKafkaBean;
import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.api.BackupPlan;
import de.evoila.cf.backup.model.enums.Operation;
import de.evoila.cf.backup.model.messages.BackupPlanEvent;
import de.evoila.cf.broker.model.ServiceInstance;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AutoConfigureAfter(BackupJob.class)
public class BackupPlanEventProducerService {
    KafkaTemplate<String, BackupPlanEvent> backupPlanEventKafkaTemplateKafka;
    BackupKafkaBean backupKafkaBean;
    KafkaAdmin kafkaAdmin;

    BackupPlanEventProducerService(KafkaTemplate<String, BackupPlanEvent> backupPlanEventKafkaTemplate,
                                   BackupKafkaBean backupKafkaBean,
                                   KafkaAdmin kafkaAdmin) {
        this.backupPlanEventKafkaTemplateKafka = backupPlanEventKafkaTemplate;
        this.backupKafkaBean = backupKafkaBean;
        this.kafkaAdmin = kafkaAdmin;
    }

    private String createTopic(ServiceInstance serviceInstance){
        String topic ="Backup-Plan-" + serviceInstance.getServiceDefinitionId().replace("-", "");
        kafkaAdmin.createOrModifyTopics(TopicBuilder.name(topic)
                .config("retention.ms", Integer.toString(backupKafkaBean.getRetention()))
                .partitions(backupKafkaBean.getPartition())
                .replicas(backupKafkaBean.getReplication())
                .build());
        return  topic;
    }

    public void create(BackupPlan backupPlan)  {
        ServiceInstance serviceInstance = backupPlan.getServiceInstance();
        BackupPlanEvent backupPlanEvent = new BackupPlanEvent(Operation.CREATE, null, backupPlan);
        backupPlanEventKafkaTemplateKafka.send(createTopic(serviceInstance),
                backupPlan.getId().toString(),
                backupPlanEvent);
    }

    public void update(BackupPlan oldBackupPlan, BackupPlan newBackupPlan)  {
        ServiceInstance serviceInstance = oldBackupPlan.getServiceInstance();
        BackupPlanEvent backupPlanEvent = new BackupPlanEvent(Operation.UPDATE, oldBackupPlan, newBackupPlan);
        backupPlanEventKafkaTemplateKafka.send(createTopic(serviceInstance),
                oldBackupPlan.getId().toString(),
                backupPlanEvent);
    }

    public void delete(BackupPlan backupPlan)  {
        ServiceInstance serviceInstance = backupPlan.getServiceInstance();
        BackupPlanEvent backupPlanEvent = new BackupPlanEvent(Operation.DELETE, null, backupPlan);
        backupPlanEventKafkaTemplateKafka.send(createTopic(serviceInstance),
                backupPlan.getId().toString(),
                backupPlanEvent);
    }
}
