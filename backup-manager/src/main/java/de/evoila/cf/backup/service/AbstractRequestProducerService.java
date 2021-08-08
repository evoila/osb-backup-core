package de.evoila.cf.backup.service;

import de.evoila.cf.backup.config.BackupKafkaBean;
import de.evoila.cf.backup.exception.BackupException;
import de.evoila.cf.backup.model.agent.AbstractRequest;
import de.evoila.cf.backup.model.agent.BackupRequestEvent;
import de.evoila.cf.backup.model.agent.RestoreRequestEvent;
import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.api.BackupPlan;
import de.evoila.cf.broker.model.ServiceInstance;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AutoConfigureAfter(BackupJob.class)
public class AbstractRequestProducerService {
    KafkaTemplate<String, AbstractRequest> abstractRequestKafkaTemplate;
    CredentialService credentialService;
    BackupKafkaBean backupKafkaBean;
    KafkaAdmin kafkaAdmin;

    AbstractRequestProducerService(KafkaTemplate<String, AbstractRequest> abstractRequestKafkaTemplate,
                                   CredentialService credentialService,
                                   BackupKafkaBean backupKafkaBean,
                                   KafkaAdmin kafkaAdmin) {
        this.abstractRequestKafkaTemplate = abstractRequestKafkaTemplate;
        this.credentialService = credentialService;
        this.backupKafkaBean = backupKafkaBean;
        this.kafkaAdmin = kafkaAdmin;
    }

    private String createTopic(ServiceInstance serviceInstance){
        String topic ="Backup-JobRequest-" + serviceInstance.getServiceDefinitionId().replace("-", "") + "-" + serviceInstance.getId().replace("-","");
        kafkaAdmin.createOrModifyTopics(TopicBuilder.name(topic)
                .config("retention.ms", Integer.toString(backupKafkaBean.getRetention()))
                .partitions(backupKafkaBean.getPartition())
                .replicas(backupKafkaBean.getReplication())
                .build());
        return  topic;
    }

    public void backup(BackupPlan backupPlan) throws BackupException {
        ServiceInstance serviceInstance = backupPlan.getServiceInstance();
        BackupRequestEvent backupRequestEvent = new BackupRequestEvent(backupPlan.getId().toString(),
                backupPlan.isCompression(),
                backupPlan.getPrivateKey(),
                backupPlan.getFileDestination(),
                credentialService.getCredentials(backupPlan.getServiceInstance()));
        abstractRequestKafkaTemplate.send(createTopic(serviceInstance),
                backupPlan.getId().toString(),
                backupRequestEvent);
    }

    public void restore(BackupPlan backupPlan) throws BackupException {
        ServiceInstance serviceInstance = backupPlan.getServiceInstance();
        RestoreRequestEvent restoreRequestEvent = new RestoreRequestEvent(backupPlan.getId().toString(),
                backupPlan.isCompression(),
                backupPlan.getPrivateKey(),
                backupPlan.getFileDestination(),
                credentialService.getCredentials(backupPlan.getServiceInstance()));
        abstractRequestKafkaTemplate.send(createTopic(serviceInstance),
                backupPlan.getId().toString(),
                restoreRequestEvent);
    }


}
