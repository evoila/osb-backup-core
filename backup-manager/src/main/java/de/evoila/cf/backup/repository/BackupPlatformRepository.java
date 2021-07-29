package de.evoila.cf.backup.repository;

import de.evoila.cf.backup.service.BackupPlatformService;

/**
 * @author Patrick Weber.
 */
public interface BackupPlatformRepository {

	void addBackupPlatform(String platform, BackupPlatformService platformService);

	BackupPlatformService getBackupPlatformService(String platform);

}