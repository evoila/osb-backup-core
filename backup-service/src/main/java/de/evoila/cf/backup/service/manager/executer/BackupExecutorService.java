package de.evoila.cf.backup.service.manager.executer;

import de.evoila.cf.backup.model.api.endpoint.EndpointCredential;
import de.evoila.cf.backup.model.api.file.FileDestination;
import de.evoila.cf.backup.model.exception.BackupException;

/**
 * @author Johannes Hiemer, Yannic Remmet.
 *
 * A BackupExecutorService provides methods for creating and storing backup files.
 */
public interface BackupExecutorService extends BaseExecutorService {

    void backup(EndpointCredential endpointCredential, FileDestination destination, String id,
                String item, boolean compression, String encryptionKey, String planId) throws BackupException;

}
