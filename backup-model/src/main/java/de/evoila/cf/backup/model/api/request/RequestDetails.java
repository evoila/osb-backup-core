package de.evoila.cf.backup.model.api.request;

public class RequestDetails {

    private String item;

    private String filename;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}