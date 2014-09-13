package wuhack2014.digipaper;

import java.util.List;

/**
 * Created by xenon on 13.09.14.
 */
public class Folder {
    private List<Document> files;
    private String path;

    public Folder(String path) {
        this.path = path;
    }
}
