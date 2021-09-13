package de.evoila.cf.backup.config;

import de.evoila.cf.broker.service.CatalogService;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@ConditionalOnBean(BackupKafkaBean.class)
@AutoConfigureAfter(BackupKafkaBean.class)
public class KafkaTopics {

    KafkaProperties kafkaProperties;
    BackupKafkaBean backupKafkaBean;
    CatalogService catalogService;

    KafkaTopics(KafkaProperties kafkaProperties,
                BackupKafkaBean backupKafkaBean,
                CatalogService catalogService) {
        this.kafkaProperties = kafkaProperties;
        this.backupKafkaBean = backupKafkaBean;
        this.catalogService = catalogService;
    }

    @Bean
    public KafkaAdmin admin() {
        Map<String, Object> configs = kafkaProperties.buildAdminProperties();
        return new KafkaAdmin(configs);
    }

    @Bean
    public List<NewTopic> serviceInstance() {
        List<NewTopic> newTopics = new ArrayList<NewTopic>();
        for (String serviceId: catalogService.getServiceIdsWithoutHyphen()) {
            newTopics.add(
                    TopicBuilder.name("Backup-FileDestination-" + serviceId)
                            .partitions(backupKafkaBean.getPartition())
                            .replicas(backupKafkaBean.getReplication())
                            .config("retention.ms", Integer.toString(backupKafkaBean.getRetention()))
                            .build()
            );
        }
        admin().createOrModifyTopics(newTopics.toArray(new NewTopic[]{}));
        return newTopics;
    }

    @Bean
    public List<NewTopic> backupCleanupResponse() {
        List<NewTopic> newTopics = new ArrayList<NewTopic>();
        for (String serviceId: catalogService.getServiceIdsWithoutHyphen()) {
            newTopics.add(
                    TopicBuilder.name("Backup-CleanupJob-" + serviceId)
                            .partitions(backupKafkaBean.getPartition())
                            .replicas(backupKafkaBean.getReplication())
                            .config("retention.ms", Integer.toString(backupKafkaBean.getRetention()))
                            .build()
            );
        }
        admin().createOrModifyTopics(newTopics.toArray(new NewTopic[]{}));
        return newTopics;
    }

}