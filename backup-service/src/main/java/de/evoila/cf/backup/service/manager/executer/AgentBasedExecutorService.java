package de.evoila.cf.backup.service.manager.executer;


import de.evoila.cf.backup.model.agent.response.AgentExecutionResponse;
import de.evoila.cf.backup.model.api.endpoint.EndpointCredential;
import de.evoila.cf.backup.model.enums.BackupType;
import de.evoila.cf.backup.model.enums.DestinationType;
import de.evoila.cf.backup.model.enums.JobStatus;
import de.evoila.cf.backup.service.manager.AbstractBackupService;
import de.evoila.cf.security.utils.AcceptSelfSignedClientHttpRequestFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import javax.annotation.PostConstruct;

/**
 * @authorYannic Remmet, Johannes Hiemer.
 */
public class AgentBasedExecutorService extends AbstractBackupService {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected RestTemplate restTemplate;

    protected HttpHeaders headers;

    public List<DestinationType> getDestinationTypes() {
        return this.destinationTypes;
    }

    public BackupType getSourceType() { return BackupType.AGENT; }

    public AgentBasedExecutorService() {
        this.restTemplate = new RestTemplate();
    }

    @PostConstruct
    private void postConstruct () {
        destinationTypes.add(DestinationType.SWIFT);
        destinationTypes.add(DestinationType.S3);
    }

    @ConditionalOnBean(AcceptSelfSignedClientHttpRequestFactory.class)
    @Autowired(required = false)
    private void selfSignedRestTemplate(AcceptSelfSignedClientHttpRequestFactory requestFactory) {
        restTemplate.setRequestFactory(requestFactory);
    }

    /**
     * Create a HTTPHeaders object needed to access the Agent.
     *
     * @param username the username
     * @param password the password
     * @return configured headers
     */
    public HttpHeaders setAuthenticationHeader(String username, String password) {
        this.headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("Accept", "application/json");
        String token = new String(Base64Utils.encode((username + ":" + password).getBytes()));
        headers.add("Authorization", "Basic " + token);
        return headers;
    }

    /**
     * Send out a new http request to check for the JobStatus.
     *
     * @param endpointCredential Credentials for the ServiceInstance
     * @param suffix TODO
     * @param id TODO
     * @param type TODO
     * @param <T> TODO
     * @return A AgentExecutionResponse with information on the job
     */
    public <T extends AgentExecutionResponse> T pollExecutionState(EndpointCredential endpointCredential, String suffix, String id,
                                                                   ParameterizedTypeReference<T> type) {
        T agentExecutionResponse = null;
        try {
            log.info("Polling state of Backup Process for " + id);

            this.setAuthenticationHeader(endpointCredential.getBackupUsername(),
                    endpointCredential.getBackupPassword());
            HttpEntity entity = new HttpEntity(headers);

            ResponseEntity<T> agentExecutionResponseEntity = restTemplate
                    .exchange("http://" + endpointCredential.getHost() + ":8000/" + suffix + "/" + id,
                            HttpMethod.GET, entity, type);
            agentExecutionResponse = agentExecutionResponseEntity.getBody();
        } catch (Exception ex) {
            log.error("Failed to poll task", ex);
            agentExecutionResponse.setStatus(JobStatus.FAILED);
            // we don't need to here anything.
        }
        return agentExecutionResponse;
    }

}
