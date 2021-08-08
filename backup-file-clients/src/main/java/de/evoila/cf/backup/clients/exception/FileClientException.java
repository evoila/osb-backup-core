/**
 *
 */
package de.evoila.cf.backup.clients.exception;

/**
 * @author Johannes Hiemer.
 */
public class FileClientException extends Exception {

    public FileClientException(Exception e) {
        super(e);
    }

    public FileClientException(String s) {
        super(s);
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

}
