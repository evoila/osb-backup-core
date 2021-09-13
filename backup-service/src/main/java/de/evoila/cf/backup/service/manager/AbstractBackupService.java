package de.evoila.cf.backup.service.manager;

import de.evoila.cf.backup.model.enums.BackupType;
import de.evoila.cf.backup.model.enums.DestinationType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Yannic Remmet, Johannes Hiemer
 */
public abstract class AbstractBackupService {

    protected static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd_HH-mm");

    protected List<DestinationType> destinationTypes = new ArrayList<>();

    protected BackupType backupType = BackupType.AGENT;

}