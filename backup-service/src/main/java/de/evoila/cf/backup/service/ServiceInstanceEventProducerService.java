package de.evoila.cf.backup.service;

import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.enums.Operation;
import de.evoila.cf.backup.model.messages.ServiceInstanceEvent;
import de.evoila.cf.broker.model.ServiceInstance;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AutoConfigureAfter(BackupJob.class)
public class ServiceInstanceEventProducerService {
    KafkaTemplate<String, ServiceInstanceEvent> serviceInstanceEventKafkaTemplate;

    private String createTopic(ServiceInstance serviceInstance){
        String topic ="ServiceInstance-" + serviceInstance.getServiceDefinitionId().replace("-", "");
        /*
        TopicBuilder.name(topic)
                .config("retention.ms","300000")
                .partitions(3)
                .replicas(2)
                .build()
         */
        return  topic;
    }

    ServiceInstanceEventProducerService(KafkaTemplate<String, ServiceInstanceEvent> serviceInstanceEventKafkaTemplate) {
        this.serviceInstanceEventKafkaTemplate = serviceInstanceEventKafkaTemplate;
    }

    public void create(ServiceInstance serviceInstance) {
        serviceInstanceEventKafkaTemplate.send(createTopic(serviceInstance),
                serviceInstance.getId().toString(),
                new ServiceInstanceEvent(Operation.CREATE,null,serviceInstance));
    }

    public void update(ServiceInstance oldServiceInstance, ServiceInstance newServiceInstance) {
        serviceInstanceEventKafkaTemplate.send(createTopic(oldServiceInstance),
                oldServiceInstance.getId().toString(),
                new ServiceInstanceEvent(Operation.UPDATE,oldServiceInstance,newServiceInstance));
    }

    public void delete(ServiceInstance serviceInstance) {
        serviceInstanceEventKafkaTemplate.send(createTopic(serviceInstance),
                serviceInstance.getId().toString(),
                new ServiceInstanceEvent(Operation.UPDATE,serviceInstance,null));
    }

}
