package de.evoila.cf.backup.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@ConditionalOnBean(BackupKafkaBean.class)
@AutoConfigureAfter(BackupKafkaBean.class)
public class KafkaTopics {

    KafkaProperties kafkaProperties;
    BackupKafkaBean backupKafkaBean;

    KafkaTopics(KafkaProperties kafkaProperties,
                BackupKafkaBean backupKafkaBean) {
        this.kafkaProperties = kafkaProperties;
        this.backupKafkaBean = backupKafkaBean;
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = kafkaProperties.buildAdminProperties();
        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic serviceInstance() {
        return TopicBuilder.name("ServiceInstance")
                .partitions(backupKafkaBean.getPartition())
                .replicas(backupKafkaBean.getReplication())
                .config("retention.ms", Integer.toString(backupKafkaBean.getRetention()))
                .build();
    }

    @Bean
    public NewTopic backupCleanupResponse() {
        return TopicBuilder.name("BackupCleanupResponse")
                .partitions(backupKafkaBean.getPartition())
                .replicas(backupKafkaBean.getReplication())
                .config("retention.ms", Integer.toString(backupKafkaBean.getRetention()))
                .build();
    }

    @Bean
    public NewTopic jobResponse() {
        return TopicBuilder.name("JobResponse")
                .partitions(backupKafkaBean.getPartition())
                .replicas(backupKafkaBean.getReplication())
                .config("retention.ms", Integer.toString(backupKafkaBean.getRetention()))
                .build();
    }

    @Bean
    public NewTopic jobResponseDetail() {
        return TopicBuilder.name("JobResponse-Detail")
                .partitions(backupKafkaBean.getPartition())
                .replicas(backupKafkaBean.getReplication())
                .config("retention.ms", Integer.toString(backupKafkaBean.getRetention()))
                .build();

    }
}