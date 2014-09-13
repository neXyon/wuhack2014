package wuhack2014.digipaper;

/**
 * Created by xenon on 13.09.14.
 */
public class Document {
    private String filename;

    public Document(String filename) {
        this.setFilename(filename);
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
