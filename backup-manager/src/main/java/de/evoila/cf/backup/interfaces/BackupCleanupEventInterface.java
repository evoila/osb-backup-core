package de.evoila.cf.backup.interfaces;

import de.evoila.cf.backup.model.messages.BackupCleanupResultEvent;
import org.springframework.kafka.support.Acknowledgment;

public interface BackupCleanupEventInterface {
    void receiveBackupCleanupEvent(BackupCleanupResultEvent backupJob, Acknowledgment ack);
}