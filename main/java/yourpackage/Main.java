package yourpackage;

import java.io.File;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        File configFile = new File("C:/Users/pulit/OneDrive/Desktop/Documents/Kalkitech Project 1/src/config.properties");
        Properties props = FileUtil.loadProperties(configFile);

         RunTimeUtil.measure("", () -> {
            ConsistentHashingFactory.getConsistentHashing(props);
        });
    }
}
