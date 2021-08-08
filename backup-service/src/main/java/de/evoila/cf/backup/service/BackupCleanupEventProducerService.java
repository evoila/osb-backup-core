package de.evoila.cf.backup.service;

import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.enums.JobStatus;
import de.evoila.cf.backup.model.messages.BackupCleanupResultEvent;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AutoConfigureAfter(BackupJob.class)
public class BackupCleanupEventProducerService {
    KafkaTemplate<String, BackupCleanupResultEvent> backupCleanupEventKafkaTemplate;

    BackupCleanupEventProducerService(KafkaTemplate<String, BackupCleanupResultEvent> backupCleanupEventKafkaTemplate) {
        this.backupCleanupEventKafkaTemplate = backupCleanupEventKafkaTemplate;
    }

    public void send(BackupJob backupJob, String message, JobStatus jobStatus) {
        backupCleanupEventKafkaTemplate.send("Backup-CleanupResult",
                backupJob.getId().toString(),
                new BackupCleanupResultEvent(backupJob, message, jobStatus));
    }

}
