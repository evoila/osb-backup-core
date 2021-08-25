package de.evoila.cf.backup.service;

import de.evoila.cf.backup.config.BackupKafkaBean;
import de.evoila.cf.backup.exception.BackupException;
import de.evoila.cf.backup.model.agent.AbstractRequest;
import de.evoila.cf.backup.model.agent.BackupRequestEvent;
import de.evoila.cf.backup.model.agent.RestoreRequestEvent;
import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.api.BackupPlan;
import de.evoila.cf.backup.model.api.RestoreJob;
import de.evoila.cf.backup.model.api.request.RequestDetails;
import de.evoila.cf.backup.model.api.request.RestoreRequest;
import de.evoila.cf.broker.model.ServiceInstance;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public void backup(BackupJob backupJob) throws BackupException {
        BackupPlan backupPlan = backupJob.getBackupPlan();
        ServiceInstance serviceInstance = backupPlan.getServiceInstance();
        BackupRequestEvent backupRequestEvent = new BackupRequestEvent(backupJob.getId(),
                backupPlan.isCompression(),
                backupPlan.getPrivateKey(),
                backupPlan.getFileDestination(),
                credentialService.getCredentials(backupPlan.getServiceInstance()),
                backupJob.getIdAsString(),
                convertItemListToMap(backupPlan.getItems())
                );
        abstractRequestKafkaTemplate.send(createTopic(serviceInstance),
                backupPlan.getId().toString(),
                backupRequestEvent);
    }

    public void restore(RestoreJob restoreJob, RestoreRequest restoreRequest) throws BackupException {
        BackupPlan backupPlan = restoreJob.getBackupPlan();
        ServiceInstance serviceInstance = backupPlan.getServiceInstance();
        RestoreRequestEvent restoreRequestEvent = new RestoreRequestEvent(restoreJob.getId(),
                backupPlan.isCompression(),
                backupPlan.getPrivateKey(),
                backupPlan.getFileDestination(),
                credentialService.getCredentials(backupPlan.getServiceInstance()),
                restoreJob.getIdAsString(),
                convertRestoreJobToItemMap(restoreRequest)
                );
        abstractRequestKafkaTemplate.send(createTopic(serviceInstance),
                backupPlan.getId().toString(),
                restoreRequestEvent);
    }


    private Map<String, String> convertItemListToMap(List<String> items){
        return items.stream().collect(Collectors.toMap(item -> item, null));
    }

    private Map<String, String> convertRestoreJobToItemMap(RestoreRequest restoreRequest){
       return restoreRequest.getItems().stream().collect(Collectors.toMap(RequestDetails::getItem, RequestDetails::getFilename));
    }
}
