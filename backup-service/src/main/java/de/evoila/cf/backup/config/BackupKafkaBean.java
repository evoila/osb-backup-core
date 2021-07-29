package de.evoila.cf.backup.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "kafka.backup")
@ConditionalOnProperty(prefix = "kafka.backup", name = {"group-id", "topic-prefix"})
public class BackupKafkaBean {
    String groupId;
    String topicPrefix;
}