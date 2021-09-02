package de.evoila.cf.backup.model.exception;

public class BackupException extends Exception{

    public BackupException(String s) {
        super(s);
    }

    public  BackupException(String s, Throwable t) {
        super(s, t);
    }

}
