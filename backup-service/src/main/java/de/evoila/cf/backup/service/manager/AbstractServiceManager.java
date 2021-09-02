package de.evoila.cf.backup.service.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;

/**
 * @author Yannic Remmet, Johannes Hiemer.
 */
public class AbstractServiceManager {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    protected ThreadPoolTaskExecutor taskExecutor;

    @PostConstruct
    private void postConstruct() {
        taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(2);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.initialize();
    }


}
