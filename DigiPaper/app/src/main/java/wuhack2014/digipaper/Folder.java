package wuhack2014.digipaper;

import java.io.File;
import java.util.List;

/**
 * Created by xenon on 13.09.14.
 */
public class Folder {
    private List<Document> documents;
    private File path;

    public Folder(File path) {
        this.path = path;

        File files[] = path.listFiles();

        for(File file : files) {
            if(file.isDirectory()) {
                Document document = new Document(file);
                documents.add(document);
            }
        }
    }

    @Override
    public String toString() {
        return path.getName();
    }

    public File getPath() {
        return path;
    }
}
