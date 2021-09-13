package de.evoila.cf.backup.service.manager.executer;

import de.evoila.cf.backup.model.agent.response.AgentExecutionResponse;
import de.evoila.cf.backup.model.api.endpoint.EndpointCredential;
import de.evoila.cf.backup.model.enums.BackupType;
import de.evoila.cf.backup.model.enums.DestinationType;
import de.evoila.cf.backup.model.exception.BackupException;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;

/**
 * @author Johannes Hiemer, Yannic Remmet.
 *
 * A generic specification for a executor services.
 */
public interface BaseExecutorService {

    BackupType getSourceType();

    List<DestinationType> getDestinationTypes();

    <T extends AgentExecutionResponse> T pollExecutionState(EndpointCredential endpointCredential, String suffix, String id,
                                                            ParameterizedTypeReference<T> type) throws BackupException;

}
