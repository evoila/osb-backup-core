package de.evoila.cf.model.api.file;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Yannic Remmet, Johannes Hiemer
 */
@Document(collection = "fileDestination")
public class SwiftFileDestination extends FileDestination {

    public SwiftFileDestination() {
        super();
    }

    private String authUrl;

    private String domain;

    @JsonProperty("container_name")
    private String containerName;

    @JsonProperty("project_name")
    private String projectName;

    public String getAuthUrl() {
        return authUrl;
    }

    public void setAuthUrl(String authUrl) {
        this.authUrl = authUrl;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

}

