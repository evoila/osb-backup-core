package de.evoila.cf.backup.listner;

import de.evoila.cf.backup.config.BackupKafkaBean;
import de.evoila.cf.backup.model.agent.AbstractRequest;
import de.evoila.cf.backup.model.api.AbstractJob;
import de.evoila.cf.backup.model.enums.JobType;
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
public class AbstractRequestListner {
        private static final Logger log = LoggerFactory.getLogger(AbstractRequestListner.class);
        private KafkaProperties kafkaProperties;

        public AbstractRequestListner(KafkaProperties kafkaProperties) {
                this.kafkaProperties = kafkaProperties;
        }

        @Bean
        public DefaultKafkaConsumerFactory<String, AbstractRequest> abstractRequestConsumerFactory() {
                Map<String, Object> config = kafkaProperties.buildConsumerProperties();
                config.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
                config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
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
                topicPattern = "JobRequest-${kafka.backup.topic-prefix}-.*",
                containerFactory = "abstractRequestKafkaListenerContainerFactory",
                groupId = "${kafka.backup.group-id}"
        )
        public void listen(@Payload AbstractRequest abstractRequest, Acknowledgment ack) {
                if (abstractRequest.getOperation().equals(AbstractRequest.Operation.backup)) {
                        log.info("backup");
                }else if (abstractRequest.getOperation().equals(AbstractRequest.Operation.restore)) {
                        log.info("restore");
                }
                log.info("finish");
        }
}
