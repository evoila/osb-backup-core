package de.evoila.cf.backup.service;

import de.evoila.cf.backup.exception.BackupException;
import de.evoila.cf.backup.model.api.endpoint.EndpointCredential;
import de.evoila.cf.backup.model.enums.BackupType;
import de.evoila.cf.backup.repository.ServiceInstanceRepository;
import de.evoila.cf.broker.model.ServiceInstance;
import de.evoila.cf.broker.model.catalog.ServerAddress;
import de.evoila.cf.broker.model.credential.UsernamePasswordCredential;
import de.evoila.cf.security.credentials.CredentialStore;
import de.evoila.cf.security.credentials.DefaultCredentialConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author Johannes Hiemer.
 */
@Component
public class CredentialService {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private ServiceInstanceRepository serviceInstanceRepository;

    private CredentialStore credentialStore;

    public CredentialService(ServiceInstanceRepository serviceInstanceRepository, CredentialStore credentialStore) {
        this.serviceInstanceRepository = serviceInstanceRepository;
        this.credentialStore = credentialStore;
    }

    /**
     * Gets the credentials for given service instance, if they exist in the repository.
     *
     * @param serviceInstance object describing a service instance
     * @return credentials for the service instance
     * @throws BackupException
     */
    public EndpointCredential getCredentials(ServiceInstance serviceInstance) throws BackupException {
        ServiceInstance fullServiceInstance = serviceInstanceRepository
                .findById(serviceInstance.getId()).orElse(null);
        if(fullServiceInstance == null || fullServiceInstance.getHosts().size() <= 0) {
            throw new BackupException("Could not find Service Instance: " + serviceInstance.getId());
        }

        ServerAddress backupEndpoint = fullServiceInstance.getHosts().stream().filter(serverAddress -> {
            if (serverAddress.isBackup())
                return true;
            return false;
        }).findFirst().orElse(null);

        EndpointCredential endpointCredential = new EndpointCredential();
        if (backupEndpoint != null) {
            endpointCredential.setServiceInstance(fullServiceInstance);
            UsernamePasswordCredential usernamePasswordCredential = credentialStore.getUser(serviceInstance,
                    DefaultCredentialConstants.BACKUP_CREDENTIALS);

            if (usernamePasswordCredential == null)
                throw new BackupException("Could not load endpoint credentials for Backup User (see DefaultCredentialsConstants)");

            endpointCredential.setUsername(usernamePasswordCredential.getUsername());
            endpointCredential.setPassword(usernamePasswordCredential.getPassword());

            UsernamePasswordCredential backupUsernamePasswordCredential = credentialStore.getUser(serviceInstance,
                    DefaultCredentialConstants.BACKUP_AGENT_CREDENTIALS);

            if (backupUsernamePasswordCredential == null)
                throw new BackupException("Could not load backup agent credentials for Backup User (see DefaultCredentialsConstants)");

            endpointCredential.setBackupUsername(backupUsernamePasswordCredential.getUsername());
            endpointCredential.setBackupPassword(backupUsernamePasswordCredential.getPassword());
            log.debug(String.format("Agent Credentials for Service [%s] are %s:%s:", serviceInstance.getId(), backupUsernamePasswordCredential.getUsername(),backupUsernamePasswordCredential.getPassword()));

            endpointCredential.setHost(backupEndpoint.getIp());
            endpointCredential.setPort(backupEndpoint.getPort());
            endpointCredential.setType(BackupType.AGENT);
        } else
            throw new BackupException("Could not find valid Backup Endpoint in Hosts of Service Instances");

        return endpointCredential;
    }

}
