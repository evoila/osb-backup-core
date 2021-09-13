package de.evoila.cf.backup.producer;


import de.evoila.cf.backup.model.agent.response.AgentExecutionResponse;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@EnableKafka
public class AgentExecutionResponseProducer {
        private static final Logger log = LoggerFactory.getLogger(AgentExecutionResponseProducer.class);
        private KafkaProperties kafkaProperties;

        public AgentExecutionResponseProducer(KafkaProperties kafkaProperties) {
                this.kafkaProperties = kafkaProperties;
        }

        @Bean
        public Map<String, Object> agentExecutionResponseProducerConfigs() {
                Map<String, Object> props = kafkaProperties.buildProducerProperties();
                props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                        StringSerializer.class);
                props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                        JsonSerializer.class);
                return props;
        }

        @Bean
        public DefaultKafkaProducerFactory<String, AgentExecutionResponse> agentExecututionResponseProducerFactory() {
                return new DefaultKafkaProducerFactory<>(agentExecutionResponseProducerConfigs());
        }

        @Bean
        public KafkaTemplate<String, AgentExecutionResponse> agentExecutionResponseKafkaTemplate() {
                return new KafkaTemplate<>(agentExecututionResponseProducerFactory());
        }
}