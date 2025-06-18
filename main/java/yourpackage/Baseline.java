package yourpackage;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;

public class Baseline {
    private final Properties props;
    private final List<String> nodes;
    private final List<Integer> sensorIds;

    public Baseline(Properties props) {
        this.props = props;
        this.nodes = FileUtil.parseNodes(props.getProperty("nodes", ""));
        this.sensorIds = FileUtil.readSensorIdsFromFile(props.getProperty("sensor_file"));
    }

    public void run() {
        System.out.println("Baseline running...");
        System.out.println("Nodes: " + nodes);
        System.out.println("Number of sensors loaded: " + sensorIds.size());

        Map<String, Integer> nodeToSensorCount = new HashMap<>();
        for (String node : nodes) {
            nodeToSensorCount.put(node, 0);
        }

        for (Integer sensorId : sensorIds) {
            int assignedIndex = Math.abs(sensorId.hashCode()) % nodes.size();
            String assignedNode = nodes.get(assignedIndex);
            nodeToSensorCount.put(assignedNode, nodeToSensorCount.get(assignedNode) + 1);
        }

        FileUtil.writeSimpleOutput(nodeToSensorCount, props.getProperty("output_file", "baseline_output.txt"));
    }
}
