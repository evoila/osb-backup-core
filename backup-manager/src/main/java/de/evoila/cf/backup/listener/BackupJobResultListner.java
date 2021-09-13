package de.evoila.cf.backup.listener;

import de.evoila.cf.backup.interfaces.BackupCleanupEventInterface;
import de.evoila.cf.backup.interfaces.BackupJobResultInterface;
import de.evoila.cf.backup.interfaces.RestoreJobResultInterface;
import de.evoila.cf.backup.model.agent.response.AgentExecutionResponse;
import de.evoila.cf.backup.model.agent.response.BackupResultEvent;
import de.evoila.cf.backup.model.agent.response.RestoreResultEvent;
import de.evoila.cf.backup.model.api.AbstractJob;
import de.evoila.cf.backup.model.messages.BackupCleanupResultEvent;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@EnableKafka
public class BackupJobResultListner {
        private static final Logger log = LoggerFactory.getLogger(BackupJobResultListner.class);
        private KafkaProperties kafkaProperties;
        private BackupJobResultInterface backupJobResultInterface;
        private RestoreJobResultInterface restoreJobResultInterface;

        public BackupJobResultListner(KafkaProperties kafkaProperties,
                                      BackupJobResultInterface backupJobResultInterface,
                                      RestoreJobResultInterface restoreJobResultInterface) {
                this.kafkaProperties = kafkaProperties;
                this.backupJobResultInterface = backupJobResultInterface;
                this.restoreJobResultInterface = restoreJobResultInterface;
        }

        @Bean
        public DefaultKafkaConsumerFactory<String, AgentExecutionResponse> backupJobResultLogConsumerFactory() {
                Map<String, Object> config = kafkaProperties.buildConsumerProperties();
                config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
                config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
                config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
                config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
                config.put(JsonDeserializer.VALUE_DEFAULT_TYPE,AbstractJob.class);
                config.put(JsonDeserializer.KEY_DEFAULT_TYPE,String.class);
                config.put(JsonDeserializer.TRUSTED_PACKAGES,"de.evoila.cf.backup.model.agent.response");

                return new DefaultKafkaConsumerFactory<>(config);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, AgentExecutionResponse> backupJobResultLogKafkaListenerContainerFactory() {

                ConcurrentKafkaListenerContainerFactory<String, AgentExecutionResponse> factory =
                        new ConcurrentKafkaListenerContainerFactory<>();
                factory.setConsumerFactory(backupJobResultLogConsumerFactory());
                factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
                return factory;
        }

        @KafkaListener(
                topicPattern = "Backup-JobResultLogs",
                containerFactory = "backupJobResultLogKafkaListenerContainerFactory",
                groupId = "${kafka.manager.group-id}"
        )

        public void listen(@Payload AgentExecutionResponse agentExecutionResponse, Acknowledgment ack) {
                if (agentExecutionResponse.getClass().equals(BackupResultEvent.class)){
                        backupJobResultInterface.receiveBackupJobResult((BackupResultEvent) agentExecutionResponse, ack);
                } else if (agentExecutionResponse.getClass().equals(RestoreResultEvent.class)){
                        restoreJobResultInterface.receiveRestoreJobResult((RestoreResultEvent) agentExecutionResponse, ack);
                }
        }
}
