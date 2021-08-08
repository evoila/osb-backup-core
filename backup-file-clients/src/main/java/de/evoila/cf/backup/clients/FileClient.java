package de.evoila.cf.backup.clients;

import io.minio.errors.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;


/**
 * A FileClient for uploading, downloading and deleting backup files from cloud storage destinations.
 */
public interface FileClient {

    /**
     * Concatenate the identifier with the extension to a new string with a "." in-between.
     * @param identifier filename
     * @param extension filename extension
     * @return concatenated string with a "." in-between
     */
    static String concatIdentifier(String identifier, String extension) {
        if(identifier == null){
            new IllegalArgumentException("Identifier may not be null");
        }
        if(extension == null) {
            new IllegalArgumentException("Extension may not be undefined");
        }

        return identifier + "." + extension;
    }

    /**
     * Upload a file to the destination.
     *
     * @param file file to upload
     * @param bucket bucket in destination
     * @param identifier filename
     * @param extension filename extension
     * @return the URL to the file
     * @throws IOException
     * @throws ServerException
     * @throws InsufficientDataException
     * @throws InternalException
     * @throws InvalidResponseException
     * @throws InvalidKeyException
     * @throws NoSuchAlgorithmException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    String upload(File file, String bucket, String identifier, String extension) throws IOException, ServerException, InsufficientDataException, InternalException, InvalidResponseException, InvalidKeyException, NoSuchAlgorithmException, XmlParserException, ErrorResponseException;

    /**
     * Generate an URL for the implemented destination.
     *
     * @param bucket bucket in destination
     * @param identifier filename
     * @param extension filename extension
     * @return the generated URL
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
    URL generateUrl(String bucket, String identifier, String extension) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;

    /**
     * Download a file from the implemented destination.
     *
     * @param bucket bucket in destination
     * @param identifier filename
     * @param extension filename extension
     * @param path URL to the file
     * @return the downloaded file
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws ServerException
     * @throws InsufficientDataException
     * @throws InvalidKeyException
     * @throws InvalidResponseException
     * @throws NoSuchAlgorithmException
     * @throws InternalException
     * @throws XmlParserException
     * @throws ErrorResponseException
     */
    File download(String bucket, String identifier, String extension, String path)
            throws IOException, IllegalArgumentException, ServerException, InsufficientDataException, InvalidKeyException, InvalidResponseException, NoSuchAlgorithmException, InternalException, XmlParserException, ErrorResponseException;

    /**
     * Delete a file from the implemented destination.
     *
     * @param bucket bucket in destination
     * @param identifier filename
     * @param extension filename extension
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
    void delete(String bucket, String identifier, String extension) throws IOException, InvalidKeyException, InvalidResponseException, InsufficientDataException, NoSuchAlgorithmException, ServerException, InternalException, XmlParserException, ErrorResponseException;
}
