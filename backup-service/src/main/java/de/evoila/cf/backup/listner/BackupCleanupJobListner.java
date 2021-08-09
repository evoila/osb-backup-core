package de.evoila.cf.backup.listner;

import de.evoila.cf.backup.config.BackupKafkaBean;
import de.evoila.cf.backup.model.api.AbstractJob;
import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.messages.BackupCleanupRequestEvent;
import de.evoila.cf.backup.model.messages.BackupPlanEvent;
import de.evoila.cf.backup.repository.BackupPlatformRepository;
import de.evoila.cf.broker.exception.ServiceDefinitionDoesNotExistException;
import de.evoila.cf.broker.exception.ServiceDefinitionPlanDoesNotExistException;
import de.evoila.cf.broker.model.ServiceInstance;
import de.evoila.cf.broker.model.catalog.plan.Plan;
import de.evoila.cf.broker.repository.ServiceDefinitionRepository;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnBean(BackupKafkaBean.class)
@EnableKafka
public class BackupCleanupJobListner {
        private static final Logger log = LoggerFactory.getLogger(BackupCleanupJobListner.class);
        private KafkaProperties kafkaProperties;
        private BackupPlatformRepository backupPlatformRepository;
        private ServiceDefinitionRepository serviceDefinitionRepository;

        public BackupCleanupJobListner(KafkaProperties kafkaProperties) {
                this.kafkaProperties = kafkaProperties;
        }

        @Bean
        public DefaultKafkaConsumerFactory<String, BackupCleanupRequestEvent> backupCleanupRequestConsumerFactory() {
                Map<String, Object> config = kafkaProperties.buildConsumerProperties();
                config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
                config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
                config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
                config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
                config.put(JsonDeserializer.VALUE_DEFAULT_TYPE,AbstractJob.class);
                config.put(JsonDeserializer.KEY_DEFAULT_TYPE,String.class);
                config.put(JsonDeserializer.TRUSTED_PACKAGES,"de.evoila.cf.backup.model.messages");

                return new DefaultKafkaConsumerFactory<>(config);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, BackupCleanupRequestEvent> backupCleanupRequestKafkaListenerContainerFactory() {

                ConcurrentKafkaListenerContainerFactory<String, BackupCleanupRequestEvent> factory =
                        new ConcurrentKafkaListenerContainerFactory<>();
                factory.setConsumerFactory(backupCleanupRequestConsumerFactory());
                factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
                return factory;
        }

        @KafkaListener(
                topicPattern = "Backup-CleanupJob-(#{T(org.thymeleaf.util.StringUtils).join(catalogServiceImpl.catalogServiceIds(),\"|\")})-.*",
                containerFactory = "backupCleanupRequestKafkaListenerContainerFactory",
                groupId = "${kafka.backup.group-id}"
        )
        public void listen(@Payload BackupCleanupRequestEvent backupCleanupRequestEvent, Acknowledgment ack) {
                ServiceInstance serviceInstance;
                Plan plan;
                serviceInstance = backupCleanupRequestEvent.getBackupJob().getServiceInstance();
                try {
                        plan = serviceDefinitionRepository.getPlan(serviceInstance.getServiceDefinitionId(),serviceInstance.getPlanId());
                        backupPlatformRepository.getBackupPlatformService(plan.getMetadata().getBackup().getPlatform()).cleanupBackup(backupCleanupRequestEvent.getBackupJob(), serviceInstance, plan);
                } catch (ServiceDefinitionDoesNotExistException e) {
                        e.printStackTrace();
                } catch (ServiceDefinitionPlanDoesNotExistException e) {
                        e.printStackTrace();
                }
                ack.acknowledge();
        }
}
