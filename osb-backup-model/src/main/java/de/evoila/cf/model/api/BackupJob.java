package de.evoila.cf.model.api;

import de.evoila.cf.broker.model.ServiceInstance;
import de.evoila.cf.model.enums.JobStatus;
import de.evoila.cf.model.enums.JobType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yannic Remmet, Johannes Hiemer.
 */
@Document(collection = "backopOrRestoreJob")
public class BackupJob extends AbstractJob {

    private Map<String, String> files = new HashMap<>();

    public BackupJob() {}

    public BackupJob(JobType jobType, ServiceInstance serviceInstance, JobStatus status) {
        this.jobType = jobType;
        this.serviceInstance = serviceInstance;
        this.status = status;
        this.startDate = new Date();
    }

    public Map<String, String> getFiles() {
        return files;
    }

    public void setFiles(Map<String, String> files) {
        this.files = files;
    }
}
