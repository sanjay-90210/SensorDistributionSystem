package yourpackage;

import java.io.*;
import java.util.*;

public class ConfigReader {
    private static final Properties props = new Properties();

    static {
        try (InputStream input = new FileInputStream("config.properties")) {
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load config.properties", ex);
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static List<String> getNodeList(String key) {
        String[] parts = get(key).split(",");
        List<String> nodeList = new ArrayList<>();
        for (String part : parts) {
            nodeList.add(part.contains(":") ? part : part + ":8080");
        }
        return nodeList;
    }
}
