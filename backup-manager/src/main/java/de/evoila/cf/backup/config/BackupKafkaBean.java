package de.evoila.cf.backup.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(prefix = "kafka.manager", name = {"group-id"})
public class BackupKafkaBean {
    String groupId;
}