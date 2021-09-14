/**
 *
 */
package de.evoila.cf.backup.clients;

import de.evoila.cf.backup.model.agent.response.AgentBackupResponse;
import de.evoila.cf.backup.model.agent.response.AgentExecutionResponse;
import de.evoila.cf.backup.model.api.BackupJob;
import de.evoila.cf.backup.model.api.file.S3FileDestination;
import io.minio.*;
import io.minio.errors.*;
import io.minio.http.Method;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Johannes Hiemer.
 *
 * Amazon S3 implementation of the FileClient interface. Implements an additional validation method to check if client
 * is able to read and write data into the storage. Communication with the S3 is done through a MinioClient.
 *
 * <p>
 *
 * Create a public bucket:
 * {
 * "Version": "2008-10-17",
 * "Id": "Policy1410846366931",
 * "Statement": [
 * {
 * "Sid": "Stmt1410846362554",
 * "Effect": "Allow",
 * "Principal": {
 * "AWS": "*"
 * },
 * "Action": [
 * "s3:DeleteObject",
 * "s3:GetObject",
 * "s3:PutObject"
 * ],
 * "Resource": "arn:aws:s3:::whibs/*"
 * }
 * ]
 * }
 */
public class S3Client implements FileClient {

    private static final Logger log = LoggerFactory.getLogger(S3Client.class);

    private static final String s3ValidationFileName = "s3_validation_testfile";
    private static final String s3ValidationFileExtension = "txt";

    private MinioClient client;


    /**
     * Constructor.
     *
     * @param endpoint the domain
     * @param region region of the server
     * @param authKey key to access the S3
     * @param authSecret secret to access the S3
     */
    public S3Client(String endpoint, String region, String authKey, String authSecret) {

        if(region.isEmpty()) {
            client = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(authKey, authSecret)
                    .build();
        } else {
            client = MinioClient.builder()
                    .endpoint(endpoint)
                    .region(region)
                    .credentials(authKey, authSecret)
                    .build();
        }
    }

    /**
     * Constructor use Filedestination
     * @param s3FileDestination contain all endpoint specific data
     */
    public S3Client(S3FileDestination s3FileDestination) {
        this(s3FileDestination.getEndpoint(), s3FileDestination.getRegion(), s3FileDestination.getAuthKey(), s3FileDestination.getAuthSecret());
    }

    /**
     * Checks whether the created Client is able to write data to the specified endpoint & bucket.
     *
     * @param destination destination of the storage
     * @throws URISyntaxException
     */
    public void validate(S3FileDestination destination) throws URISyntaxException, IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, ErrorResponseException, XmlParserException, InternalException {

        String bucket = destination.getBucket();
        String url = generateUrl(bucket, s3ValidationFileName, s3ValidationFileExtension).toString();

        log.info("Starting validation for " + url.substring(0, url.lastIndexOf("/")));

        URL resource = getClass().getClassLoader().getResource(FileClient.concatIdentifier(s3ValidationFileName, s3ValidationFileExtension));
        File file = new File(resource.toURI());

        upload(file, bucket, s3ValidationFileName, s3ValidationFileExtension);

        delete(bucket, file.getName());
    }

    @Override
    public String upload(File file, String bucket, String identifier, String extension) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        Assert.notNull(bucket, "Bucket may not be undefined");

        client.uploadObject(
                UploadObjectArgs.builder()
                        .bucket(bucket)
                        .object(file.getName())
                        .filename(file.getAbsolutePath())
                        .build());

        URL url = generateUrl(bucket, identifier, extension);

        log.info("Uploaded file to: " + url.toString());

        return url.toString();
    }

    /**
     * Generate an url to the file location.
     *
     * @param bucket bucket in destination
     * @param identifier filename
     * @param extension filename extension
     * @return a generated URL
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    public URL generateUrl(String bucket, String identifier, String extension) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {
        String filename = FileClient.concatIdentifier(identifier, extension);

        //This is sadly the only way to get any url from the minio client
        String url = client.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                .method(Method.GET)
                .bucket(bucket)
                .object(filename)
                .build());

        return new URL(url.split("\\?")[0]);
    }

    @Override
    public File download(String bucket, String identifier, String extension, String path) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException {
        Assert.notNull(client, "S3 Connection may not be null");
        Assert.notNull(bucket, "Bucket may not be undefined");

        if (!client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build()))
            throw new IllegalArgumentException("Bucket does not exists -> " + bucket);

        String filename = FileClient.concatIdentifier(identifier, extension);

        InputStream inputStream = client.getObject(GetObjectArgs.builder()
                .bucket(bucket)
                .object(filename)
                .build());

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
    public void delete(String bucket, String identifier, String extension) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {

        String filename = FileClient.concatIdentifier(identifier, extension);

        delete(bucket, filename);
    }

    /**
     * Delete an file from the S3 cloud storage.
     *
     * @param bucket name of the bucket
     * @param filename filename with extension
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    public void delete(String bucket, String filename) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException {

        client.removeObject(RemoveObjectArgs.builder()
                .bucket(bucket)
                .object(filename)
                .build());

        log.info("File deleted: " + bucket + "/" + filename);
    }

    /**
     * Delete an file from the S3 cloud storage.
     *
     * @param  s3FileDestination with information to bucket and filename
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    public void delete(S3FileDestination s3FileDestination) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        delete(s3FileDestination.getBucket(),
                s3FileDestination.getFilenamePrefix() + s3FileDestination.getFilename());
    }
    
    /**
     * Delete an file from the S3 cloud storage.
     *
     * @param  s3FileDestination with information to bucket and filename
     * @throws IOException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws InsufficientDataException
     * @throws NoSuchAlgorithmException
     * @throws ServerException
     * @throws InternalException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    public void delete(BackupJob backupJob) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        S3FileDestination s3FileDestination = (S3FileDestination) backupJob.getDestination();
        for (var entry:backupJob.getAgentExecutionReponses().entrySet()){
                String filenamePrefix = ((AgentBackupResponse) entry.getValue()).getFilenamePrefix();
                String filename = ((AgentBackupResponse) entry.getValue()).getFilename();
                delete(s3FileDestination.getBucket(),
                        filenamePrefix + filename);
        }
    }
}