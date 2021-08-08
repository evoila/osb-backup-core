package de.evoila.cf.backup.interfaces;

import de.evoila.cf.backup.model.messages.ServiceInstanceEvent;
import org.springframework.kafka.support.Acknowledgment;

public interface ServiceInstanceEventInterface {
    void receiveServiceInstanceEvent(ServiceInstanceEvent serviceInstnanceEvent, Acknowledgment ack);
}