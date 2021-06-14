package de.evoila.cf.model.api;

import de.evoila.cf.broker.model.ServiceInstance;
import de.evoila.cf.model.AbstractEntity;
import de.evoila.cf.model.api.file.FileDestination;
import de.evoila.cf.model.enums.RetentionStyle;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.List;

/**
 * @author Yanic Remmet, Johannes Hiemer.
 */
public class BackupPlan extends AbstractEntity {

    private String name;

    @DBRef
    private ServiceInstance serviceInstance;

    @DBRef
    private FileDestination fileDestination;

    private boolean paused;

    private boolean compression;

    private String frequency;

    private RetentionStyle retentionStyle;

    private int retentionPeriod;

    List<String> items;

    private String privateKey;

    private String publicKey;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ServiceInstance getServiceInstance() {
        return serviceInstance;
    }

    public void setServiceInstance(ServiceInstance serviceInstance) {
        this.serviceInstance = serviceInstance;
    }

    public FileDestination getFileDestination() {
        return fileDestination;
    }

    public void setFileDestination(FileDestination fileDestination) {
        this.fileDestination = fileDestination;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isCompression() {
        return compression;
    }

    public void setCompression(boolean compression) {
        this.compression = compression;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public RetentionStyle getRetentionStyle() {
        return retentionStyle;
    }

    public void setRetentionStyle(RetentionStyle retentionStyle) {
        this.retentionStyle = retentionStyle;
    }

    public int getRetentionPeriod() {
        return retentionPeriod;
    }

    public void setRetentionPeriod(int retentionPeriod) {
        this.retentionPeriod = retentionPeriod;
    }

    public void update(BackupPlan plan) {
        if (plan.getFrequency() != null)
            this.frequency = plan.getFrequency();

        if (plan.getRetentionStyle() != null)
            this.retentionStyle = plan.getRetentionStyle();

        if (plan.getRetentionPeriod() > 0)
            this.retentionPeriod = plan.getRetentionPeriod();

        if (plan.getItems() != null && plan.getItems().size() > 0)
            this.items = plan.getItems();

        if (plan.getName() != null)
            this.name = plan.getName();

        if (plan.getFileDestination() != null)
            this.fileDestination = plan.getFileDestination();

        this.paused = plan.isPaused();
        this.compression = plan.isCompression();
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    public String getPrivateKey() {
        if (privateKey == null)
            return "none";

        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPublicKey() {
        if (publicKey == null)
            return "none";
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
}
