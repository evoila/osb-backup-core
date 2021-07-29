package de.evoila.cf.backup.service;

import de.evoila.cf.backup.exception.BackupException;
import de.evoila.cf.backup.model.agent.AbstractRequest;
import de.evoila.cf.backup.model.agent.AgentBackupRequest;
import de.evoila.cf.backup.model.agent.AgentRestoreRequest;
import de.evoila.cf.backup.model.api.AbstractJob;
import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.api.BackupPlan;
import de.evoila.cf.backup.model.api.RestoreJob;
import de.evoila.cf.backup.model.api.request.BackupRequest;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AutoConfigureAfter(BackupJob.class)
public class AbstractRequestProducerService {
    KafkaTemplate<String, AbstractRequest> abstractRequestKafkaTemplate;
    CredentialService credentialService;

    AbstractRequestProducerService(KafkaTemplate<String, AbstractRequest> abstractRequestKafkaTemplate, CredentialService credentialService){
        this.abstractRequestKafkaTemplate = abstractRequestKafkaTemplate;
        this.credentialService = credentialService;
    }

    public void backup(String topic, BackupPlan backupPlan) throws BackupException {
        AgentBackupRequest agentBackupRequest = new AgentBackupRequest(backupPlan.getId().toString(),
                backupPlan.isCompression(),
                backupPlan.getPrivateKey(),
                backupPlan.getFileDestination(),
                credentialService.getCredentials(backupPlan.getServiceInstance()));
        abstractRequestKafkaTemplate.send("JobRequest-" + topic,
                backupPlan.getId().toString(),
                agentBackupRequest);
    }

    public void restore(String topic, BackupPlan backupPlan) throws BackupException {
        AgentRestoreRequest agentRestoreRequest = new AgentRestoreRequest(backupPlan.getId().toString(),
                backupPlan.isCompression(),
                backupPlan.getPrivateKey(),
                backupPlan.getFileDestination(),
                credentialService.getCredentials(backupPlan.getServiceInstance()));
        abstractRequestKafkaTemplate.send("JobRequest-" + topic,
                backupPlan.getId().toString(),
                agentRestoreRequest);
    }




}
