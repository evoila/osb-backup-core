package de.evoila.cf.backup.service;

import de.evoila.cf.backup.config.BackupKafkaBean;
import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.messages.BackupCleanupRequestEvent;
import de.evoila.cf.broker.model.ServiceInstance;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AutoConfigureAfter(BackupJob.class)
public class BackupCleanupRequestProducerService {
    KafkaTemplate<String, BackupCleanupRequestEvent> backupCleanupRequestEventKafkaTemplate;
    CredentialService credentialService;
    BackupKafkaBean backupKafkaBean;
    KafkaAdmin kafkaAdmin;

    BackupCleanupRequestProducerService(KafkaTemplate<String, BackupCleanupRequestEvent> backupCleanupRequestEventKafkaTemplate,
                                        CredentialService credentialService,
                                        BackupKafkaBean backupKafkaBean,
                                        KafkaAdmin kafkaAdmin) {
        this.backupCleanupRequestEventKafkaTemplate = backupCleanupRequestEventKafkaTemplate;
        this.credentialService = credentialService;
        this.backupKafkaBean = backupKafkaBean;
        this.kafkaAdmin = kafkaAdmin;
    }

    private String createTopic(ServiceInstance serviceInstance){
        String topic ="Backup-CleanupRequest-" + serviceInstance.getServiceDefinitionId().replace("-", "");
        kafkaAdmin.createOrModifyTopics(TopicBuilder.name(topic)
                .config("retention.ms", Integer.toString(backupKafkaBean.getRetention()))
                .partitions(backupKafkaBean.getPartition())
                .replicas(backupKafkaBean.getReplication())
                .build());
        return  topic;
    }

    public void request(BackupJob backupJob) {
        ServiceInstance serviceInstance = backupJob.getServiceInstance();
        backupCleanupRequestEventKafkaTemplate.send(createTopic(serviceInstance),
                backupJob.getId().toString(),
                new BackupCleanupRequestEvent(backupJob));
    }

}
