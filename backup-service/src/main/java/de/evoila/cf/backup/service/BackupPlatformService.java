package de.evoila.cf.backup.service;

import de.evoila.cf.backup.model.messages.FileDestinationEvent;

import java.util.Map;

/**
 * @author Patrick Weber.
 *
 */
public interface BackupPlatformService {

    /**
     *
     */
    void registerCustomBackupPlatformService();

    void backup();

    void restore();

    void changeFileDestination(FileDestinationEvent fileDestationEvent);

}
