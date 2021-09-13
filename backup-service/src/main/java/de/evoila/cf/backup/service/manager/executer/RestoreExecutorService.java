package de.evoila.cf.backup.service.manager.executer;


import de.evoila.cf.backup.model.api.endpoint.EndpointCredential;
import de.evoila.cf.backup.model.api.file.FileDestination;
import de.evoila.cf.backup.model.api.request.RequestDetails;
import de.evoila.cf.backup.model.exception.BackupException;

/**
 * @author Johannes Hiemer, Yannic Remmet.
 *
 * A RestoreExecutorService provides methods to restore backup files.
 */
public interface RestoreExecutorService extends BaseExecutorService {

    void restore(EndpointCredential endpointCredential, FileDestination destination,
                 RequestDetails requestDetails, String id, boolean compression, String encryptionKey,
                 String planId) throws BackupException;

}
