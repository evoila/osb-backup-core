package de.evoila.cf.model.api;

import de.evoila.cf.broker.model.ServiceInstance;
import de.evoila.cf.model.enums.JobStatus;
import de.evoila.cf.model.enums.JobType;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author Yannic Remmet, Johannes Hiemer.
 */
@Document(collection = "backopOrRestoreJob")
public class RestoreJob extends AbstractJob {

    public RestoreJob() {}

    public RestoreJob(JobType jobType, ServiceInstance serviceInstance, JobStatus status) {
        this.jobType = jobType;
        this.serviceInstance = serviceInstance;
        this.status = status;
        this.startDate = new Date();
    }

}
