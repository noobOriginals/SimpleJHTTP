package app.core.server;

import java.util.HashMap;

import app.core.util.File;

public class LinkToPath {
    private HashMap<String, String> paths;
    private String home;

    public LinkToPath(String home) {
        paths = new HashMap<>();
        this.home = home;
    }
    public LinkToPath(String home, HashMap<String, String> newPaths) {
        paths = new HashMap<>();
        this.home = home;
        setLinks(newPaths);
    }
    public LinkToPath(String home, String filepath) {
        paths = new HashMap<>();
        this.home = home;
        System.err.println("LinkToPath: Loading links from file \"" + filepath + "\".");
        File file = new File(filepath, File.READ);
        String line = file.readln();
        while (line != null) {
            parseLinkLine(line);
            System.out.println("LinkToPath: Loading link " + line);
            line = file.readln();
        }
    }

    public void setLinks(HashMap<String, String> newPaths) {
        paths = newPaths;
    }
    public void addLink(String link, String path) {
        paths.put(link, home + path);
    }

    public File getSourceFile(String link) {
        if (!paths.containsKey(link)) {
            System.err.println("LinkToPath: ERROR - Link \"" + link + "\" does not exist.");
            return null;
        }
        File file = new File(paths.get(link), File.READ);
        return file;
    }
    public String getFileExt(String link) {
        if (!paths.containsKey(link)) {
            System.err.println("LinkToPath: ERROR - Link \"" + link + "\" does not exist.");
            return null;
        }
        String path = paths.get(link);
        return path.substring(path.lastIndexOf('.'));
    }

    private void parseLinkLine(String line) {
        if (line == null || line.isEmpty()) {
            return;
        }
        int separator = line.indexOf(' ');
        paths.put(line.substring(0, separator), home + line.substring(separator + 1));
    }
}
