package de.evoila.cf.backup.listner;

import de.evoila.cf.backup.config.BackupKafkaBean;
import de.evoila.cf.backup.model.agent.AbstractRequest;
import de.evoila.cf.backup.model.agent.BackupRequestEvent;
import de.evoila.cf.backup.model.agent.RestoreRequestEvent;
import de.evoila.cf.backup.model.api.AbstractJob;
import de.evoila.cf.backup.model.enums.OperationType;
import de.evoila.cf.backup.repository.BackupPlatformRepository;
import de.evoila.cf.backup.service.BackupPlatformService;
import de.evoila.cf.broker.exception.ServiceDefinitionDoesNotExistException;
import de.evoila.cf.broker.exception.ServiceDefinitionPlanDoesNotExistException;
import de.evoila.cf.broker.model.ServiceInstance;
import de.evoila.cf.broker.model.catalog.plan.Plan;
import de.evoila.cf.broker.repository.ServiceDefinitionRepository;
import de.evoila.cf.broker.service.CatalogService;
import de.evoila.cf.broker.service.impl.CatalogServiceImpl;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
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
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnBean(BackupKafkaBean.class)
@AutoConfigureAfter(CatalogServiceImpl.class)
@EnableKafka
public class AbstractRequestListner {
        private static final Logger log = LoggerFactory.getLogger(AbstractRequestListner.class);
        private KafkaProperties kafkaProperties;
        private BackupPlatformService backupPlatformService;
        private ServiceDefinitionRepository serviceDefinitionRepository;
        public CatalogService catalogService;

        public AbstractRequestListner(KafkaProperties kafkaProperties,
                                      BackupPlatformService backupPlatformService,
                                      ServiceDefinitionRepository serviceDefinitionRepository,
                                      CatalogService catalogService) {
                this.kafkaProperties = kafkaProperties;
                this.serviceDefinitionRepository = serviceDefinitionRepository;
                this.catalogService = catalogService;
                this.backupPlatformService = backupPlatformService;
        }

        @Bean
        public DefaultKafkaConsumerFactory<String, AbstractRequest> abstractRequestConsumerFactory() {
                Map<String, Object> config = kafkaProperties.buildConsumerProperties();
                config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
                config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
                config.put(CommonClientConfigs.METADATA_MAX_AGE_CONFIG,"30000");
                config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
                config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
                config.put(JsonDeserializer.VALUE_DEFAULT_TYPE,AbstractJob.class);
                config.put(JsonDeserializer.KEY_DEFAULT_TYPE,String.class);
                config.put(JsonDeserializer.TRUSTED_PACKAGES,"de.evoila.cf.backup.model.agent");

                return new DefaultKafkaConsumerFactory<>(config);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, AbstractRequest> abstractRequestKafkaListenerContainerFactory() {

                ConcurrentKafkaListenerContainerFactory<String, AbstractRequest> factory =
                        new ConcurrentKafkaListenerContainerFactory<>();
                factory.setConsumerFactory(abstractRequestConsumerFactory());
                factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
                return factory;
        }


        @KafkaListener(
                topicPattern = "Backup-JobRequest-(#{T(org.thymeleaf.util.StringUtils).join(catalogServiceImpl.getServiceIdsWithoutHyphen(),\"|\")})-.*",
                containerFactory = "abstractRequestKafkaListenerContainerFactory",
                groupId = "${kafka.backup.group-id}"
        )
        public void listen(@Payload AbstractRequest abstractRequest, Acknowledgment ack) {
                new Thread() {
                        @Override
                        public void run() {

                                ServiceInstance serviceInstance;
                                Plan plan;
                                if (abstractRequest.getOperation().

                                        equals(OperationType.BACKUP)) {
                                        BackupRequestEvent backupRequestEvent = (BackupRequestEvent) abstractRequest;
                                        serviceInstance = backupRequestEvent.getBackup().getServiceInstance();
                                        try {
                                                plan = serviceDefinitionRepository.getPlan(serviceInstance.getServiceDefinitionId(), serviceInstance.getPlanId());
                                                backupPlatformService.getBackupPlatform(plan.getMetadata().getBackup().getPlatform()).backup(backupRequestEvent, serviceInstance, plan);
                                        } catch (ServiceDefinitionDoesNotExistException e) {
                                                e.printStackTrace();
                                        } catch (ServiceDefinitionPlanDoesNotExistException e) {
                                                e.printStackTrace();
                                        } finally {
                                                ack.acknowledge();
                                        }
                                } else if (abstractRequest.getOperation().

                                        equals(OperationType.RESTORE)) {
                                        RestoreRequestEvent restoreRequestEvent = (RestoreRequestEvent) abstractRequest;
                                        serviceInstance = restoreRequestEvent.getRestore().getServiceInstance();
                                        try {
                                                plan = serviceDefinitionRepository.getPlan(serviceInstance.getServiceDefinitionId(), serviceInstance.getPlanId());
                                                backupPlatformService.getBackupPlatform(plan.getMetadata().getBackup().getPlatform()).restore(restoreRequestEvent, serviceInstance, plan);
                                        } catch (ServiceDefinitionDoesNotExistException e) {
                                                e.printStackTrace();
                                        } catch (ServiceDefinitionPlanDoesNotExistException e) {
                                                e.printStackTrace();
                                        } finally {
                                                ack.acknowledge();
                                        }
                                }
                        }
                }.start();
                ack.acknowledge();
        }
}
