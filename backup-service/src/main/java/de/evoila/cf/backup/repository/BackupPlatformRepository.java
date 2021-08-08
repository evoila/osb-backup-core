package de.evoila.cf.backup.repository;

import de.evoila.cf.backup.service.BackupService;

/**
 * @author Patrick Weber.
 */
public interface BackupPlatformRepository {

	void addBackupPlatform(String platform, BackupService platformService);

	BackupService getBackupPlatformService(String platform);

}