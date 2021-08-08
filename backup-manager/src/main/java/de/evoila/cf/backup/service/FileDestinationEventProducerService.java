package de.evoila.cf.backup.service;

import de.evoila.cf.backup.config.BackupKafkaBean;
import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.api.file.FileDestination;
import de.evoila.cf.backup.model.enums.Operation;
import de.evoila.cf.backup.model.messages.FileDestinationEvent;
import de.evoila.cf.broker.model.ServiceInstance;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AutoConfigureAfter(BackupJob.class)
public class FileDestinationEventProducerService {
    KafkaTemplate<String, FileDestinationEvent> fileDestinationEventKafkaTemplateKafka;
    BackupKafkaBean backupKafkaBean;
    KafkaAdmin kafkaAdmin;

    FileDestinationEventProducerService(KafkaTemplate<String, FileDestinationEvent> fileDestinationEventKafkaTemplate,
                                        BackupKafkaBean backupKafkaBean,
                                        KafkaAdmin kafkaAdmin) {
        this.fileDestinationEventKafkaTemplateKafka = fileDestinationEventKafkaTemplate;
        this.backupKafkaBean = backupKafkaBean;
        this.kafkaAdmin = kafkaAdmin;
    }

    private String createTopic(ServiceInstance serviceInstance){
        String topic ="Backup-FileDestination-" + serviceInstance.getServiceDefinitionId().replace("-", "");
        kafkaAdmin.createOrModifyTopics(TopicBuilder.name(topic)
                .config("retention.ms", Integer.toString(backupKafkaBean.getRetention()))
                .partitions(backupKafkaBean.getPartition())
                .replicas(backupKafkaBean.getReplication())
                .build());
        return  topic;
    }

    public void create(FileDestination fileDestination)  {
        ServiceInstance serviceInstance = fileDestination.getServiceInstance();
        FileDestinationEvent fileDestinationEvent = new FileDestinationEvent(Operation.CREATE, null, fileDestination);
        fileDestinationEventKafkaTemplateKafka.send(createTopic(serviceInstance),
                fileDestination.getId().toString(),
                fileDestinationEvent);
    }

    public void update(FileDestination oldFileDestination, FileDestination newFileDestination)  {
        ServiceInstance serviceInstance = oldFileDestination.getServiceInstance();
        FileDestinationEvent fileDestinationEvent = new FileDestinationEvent(Operation.UPDATE, oldFileDestination, newFileDestination);
        fileDestinationEventKafkaTemplateKafka.send(createTopic(serviceInstance),
                oldFileDestination.getId().toString(),
                fileDestinationEvent);
    }

    public void delete(FileDestination fileDestination)  {
        ServiceInstance serviceInstance = fileDestination.getServiceInstance();
        FileDestinationEvent fileDestinationEvent = new FileDestinationEvent(Operation.DELETE, fileDestination, null);
        fileDestinationEventKafkaTemplateKafka.send(createTopic(serviceInstance),
                fileDestination.getId().toString(),
                fileDestinationEvent);
    }

}
