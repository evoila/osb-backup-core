package de.evoila.cf.backup.listener;

import de.evoila.cf.backup.interfaces.BackupCleanupEventInterface;
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
public class BackupCleanupEventListner {
        private static final Logger log = LoggerFactory.getLogger(BackupCleanupEventListner.class);
        private KafkaProperties kafkaProperties;
        private BackupCleanupEventInterface backupCleanupEventInterface;

        public BackupCleanupEventListner(KafkaProperties kafkaProperties, BackupCleanupEventInterface backupCleanupEventInterface) {
                this.kafkaProperties = kafkaProperties;
                this.backupCleanupEventInterface = backupCleanupEventInterface;
        }

        @Bean
        public DefaultKafkaConsumerFactory<String, BackupCleanupResultEvent> backupCleanupEventConsumerFactory() {
                Map<String, Object> config = kafkaProperties.buildConsumerProperties();
                config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
                config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
                config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
                config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, org.apache.kafka.common.serialization.StringDeserializer.class);
                config.put(JsonDeserializer.VALUE_DEFAULT_TYPE,AbstractJob.class);
                config.put(JsonDeserializer.KEY_DEFAULT_TYPE,String.class);
                config.put(JsonDeserializer.TRUSTED_PACKAGES,"de.evoila.cf.backup.model.messages");

                return new DefaultKafkaConsumerFactory<>(config);
        }

        @Bean
        public ConcurrentKafkaListenerContainerFactory<String, BackupCleanupResultEvent> backupCleanupEventKafkaListenerContainerFactory() {

                ConcurrentKafkaListenerContainerFactory<String, BackupCleanupResultEvent> factory =
                        new ConcurrentKafkaListenerContainerFactory<>();
                factory.setConsumerFactory(backupCleanupEventConsumerFactory());
                factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
                return factory;
        }

        @KafkaListener(
                topicPattern = "Backup-CleanupResult",
                containerFactory = "backupCleanupEventKafkaListenerContainerFactory",
                groupId = "${kafka.manager.group-id}"
        )
        public void listen(@Payload BackupCleanupResultEvent backupCleanupEvent, Acknowledgment ack) {
                backupCleanupEventInterface.receiveBackupCleanupEvent(backupCleanupEvent, ack);
        }
}
