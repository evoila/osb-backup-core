package de.evoila.cf.backup.interfaces;

import de.evoila.cf.backup.model.agent.response.RestoreResultEvent;
import org.springframework.kafka.support.Acknowledgment;

public interface RestoreJobResultInterface {
    void receiveRestoreJobResult(RestoreResultEvent restoreResultEvent, Acknowledgment ack);
}