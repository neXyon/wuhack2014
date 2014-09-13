package wuhack2014.digipaper;

import java.io.File;

/**
 * Created by xenon on 13.09.14.
 */
public class Document {
    private File file;

    public Document(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return file.getName();
    }

    public File getFile() {
        return file;
    }
}