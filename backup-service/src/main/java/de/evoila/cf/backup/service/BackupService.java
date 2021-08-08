package de.evoila.cf.backup.service;

import de.evoila.cf.backup.model.agent.BackupRequestEvent;
import de.evoila.cf.backup.model.agent.RestoreRequestEvent;
import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.messages.BackupPlanEvent;
import de.evoila.cf.backup.model.messages.FileDestinationEvent;
import de.evoila.cf.broker.model.ServiceInstance;
import de.evoila.cf.broker.model.catalog.plan.Plan;

/**
 * @author Patrick Weber.
 *
 */
public interface BackupService {

    /**
     *
     */

    public boolean backup(BackupRequestEvent backupRequestEvent, ServiceInstance serviceInstance, Plan plan);

    public boolean restore(RestoreRequestEvent restoreRequestEvent, ServiceInstance serviceInstance, Plan plan);

    public boolean changeFileDestination(FileDestinationEvent fileDestationEvent, ServiceInstance serviceInstance, Plan plan);

    public boolean changeBackupPlan(BackupPlanEvent backupPlanEvent, ServiceInstance serviceInstance, Plan plan);

    public boolean cleanupBackup(BackupJob backupJob, ServiceInstance serviceInstance, Plan plan);

    public String getPlatform();



}
