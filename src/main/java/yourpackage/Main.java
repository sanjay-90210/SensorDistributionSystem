package yourpackage;

import java.io.File;
import java.util.Properties;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        File configFile = new File(args[0]);
        Properties props = FileUtil.loadProperties(configFile);
        List<String> nodes = FileUtil.parseNodesFromFile(props.getProperty("nodeFile"));
        List<Long> sensors = FileUtil.readSensorsFromFile(props.getProperty("sensorFile"));

        String type = props.getProperty("consistentHashingType", "simple").toLowerCase();
        ConsistentHashing h = ConsistentHashingFactory.getConsistentHashing(type, props);
        h.hashNodes(nodes);
        h.hashSensors(sensors);
    }
}
