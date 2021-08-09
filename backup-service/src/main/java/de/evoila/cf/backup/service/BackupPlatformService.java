package de.evoila.cf.backup.service;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

@Component
public class BackupPlatformService {
    HashMap<String,BackupService> backupServiceMap;

    BackupPlatformService(List<BackupService> backupServiceList){
        backupServiceMap = new  HashMap<String, BackupService>();
        backupServiceList.forEach( backupService -> {
            backupServiceMap.put(backupService.getPlatform().toLowerCase(),backupService);
        });
    }

    public BackupService getBackupPlatform(String platform){
        return backupServiceMap.get(platform.toLowerCase());
    }
}
