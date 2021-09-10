package de.evoila.cf.backup.interfaces;

import de.evoila.cf.backup.model.agent.response.BackupResultEvent;
import de.evoila.cf.backup.model.messages.BackupCleanupResultEvent;
import org.springframework.kafka.support.Acknowledgment;

public interface BackupJobResultInterface {
    void receiveBackupJobResult(BackupResultEvent backupResultEvent, Acknowledgment ack);
}