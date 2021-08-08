package de.evoila.cf.backup.clients;

import de.evoila.cf.backup.model.api.file.SwiftFileDestination;
import org.openstack4j.api.OSClient;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.common.DLPayload;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.common.Payloads;
import org.openstack4j.model.identity.v3.Endpoint;
import org.openstack4j.model.identity.v3.Service;
import org.openstack4j.model.storage.object.options.ObjectPutOptions;
import org.openstack4j.openstack.OSFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Yannic Remmet, Johannes Hiemer.
 *
 * OpenStack Swift implementation of the FileClient interface.
 */
public class SwiftClient implements FileClient {

    Logger log = LoggerFactory.getLogger(getClass());

    private OSClient.OSClientV3 os;

    /**
     * Constructor.
     *
     * @param endpoint URL to the storage
     * @param username username
     * @param password password
     * @param domain the domain for identifying the user
     * @param project the project withing the domain
     */
    public SwiftClient(String endpoint, String username, String password, String domain, String project) {
        OSFactory.enableHttpLoggingFilter(true);

        os = OSFactory
                .builderV3()
                .endpoint(endpoint)
                .credentials(username, password, Identifier.byName(domain))
                .scopeToProject(Identifier.byName(project), Identifier.byName(domain))
                .authenticate();
    }

    public SwiftClient(SwiftFileDestination swiftFileDestination) {
        this(swiftFileDestination.getDomain(),
                swiftFileDestination.getUsername(),
                swiftFileDestination.getPassword(),
                swiftFileDestination.getDomain(),
                swiftFileDestination.getProjectName());
    }

    @Override
    public String upload(File file, String containerName, String identifier, String extension) throws MalformedURLException {
        Map<String, String> metadata = new HashMap<>();
        metadata.put("identifier", identifier);
        metadata.put("extension", extension);

        String filename = FileClient.concatIdentifier(identifier, extension);

        String etag = os.objectStorage().objects().put(containerName, filename,
                Payloads.create(file),
                ObjectPutOptions.create().metadata(metadata)
        );

        return generateUrl(containerName, identifier, extension).toString();
    }

    @Override
    public URL generateUrl(String containerName, String identifier, String extension) throws MalformedURLException {
        String filename = FileClient.concatIdentifier(identifier, extension);

        return new URL(endpointUrl() +  "/" + containerName + "/" + filename);
    }

    @Override
    public File download(String containerName, String identifier, String extension, String path) throws IOException, IllegalArgumentException {
        String filename = FileClient.concatIdentifier(identifier, extension);
        DLPayload dlPayload = os.objectStorage().objects().download(containerName, filename);

        InputStream inputStream = new BufferedInputStream(dlPayload.getInputStream());

        File downloadedFile = new File(path + File.separator + filename);

        log.info("Opening file save to: " + downloadedFile.getAbsolutePath());
        OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(downloadedFile));

        int read;
        while ((read = inputStream.read()) != -1)
            outputStream.write(read);

        outputStream.flush();
        outputStream.close();
        inputStream.close();

        log.info("Saved file to: " + downloadedFile.getAbsolutePath());

        return downloadedFile;
    }

    @Override
    public void delete(String containerName, String identifier, String extension) {
        String filename = FileClient.concatIdentifier(identifier, extension);
        delete(containerName,filename);
    }
    public void delete(String containerName, String filename) {
        os.objectStorage().objects().delete(containerName, filename);
    }

    public void delete(SwiftFileDestination swiftFileDestination) {
        delete(swiftFileDestination.getContainerName(),
                swiftFileDestination.getFilenamePrefix() + swiftFileDestination.getFilename());
    }

    /**
     * Generate an URL pointing to the Swift catalog endpoint, with the region specified in Dallas.
     *
     * @return a generated URL with
     */
    private String endpointUrl() {
        String endpointUrl = null;
        for (Service s : os.getToken().getCatalog()) {
            if (s.getName().equals("swift")) {
                for (Endpoint e : s.getEndpoints()) {
                    if (e.getRegion().equals("dallas") && e.getIface().equals(Facing.PUBLIC)) {
                        endpointUrl = e.getUrl().toString();
                    }
                }
            }
        }
        return endpointUrl;
    }
}
