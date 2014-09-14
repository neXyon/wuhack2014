package wuhack2014.digipaper;

import android.util.Log;

import java.io.File;

/**
 * Created by xenon on 13.09.14.
 */
public class Document {
    private File file;
    final String[] imageFormats = {"bmp", "gif", "png", "jpg"};
    final String[] webFormats = {"html", "htm", "xhtml"};

    public Document(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return file.getName();
    }

    public boolean isImage() {
        String ext = getExtension();
        for(int i=0; i<imageFormats.length; i++) {
            if (ext.equalsIgnoreCase(imageFormats[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean isWeb() {
        String ext = getExtension();
        for(int i=0; i<webFormats.length; i++) {
            if (ext.equalsIgnoreCase(webFormats[i])) {
                return true;
            }
        }
        return false;
    }

    public boolean isLink() {
        if (file.getName().startsWith("http")) {
            return true;
        }
        return false;
    }

    public String getExtension() {
        String split[] = toString().split("[.]");
        return split.length > 1 ? split[split.length-1].toLowerCase() : "";
    }

    public File getFile() {
        return file;
    }
}
