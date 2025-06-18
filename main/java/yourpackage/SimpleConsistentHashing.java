package yourpackage;

import java.util.*;

public class SimpleConsistentHashing {
    private final Properties props;
    private final List<String> nodes;
    private final List<Integer> sensorIds;
    private final Hashfunction hashFunction;

    public SimpleConsistentHashing(Properties props) {
        this.props = props;
        this.nodes = FileUtil.parseNodes(props.getProperty("nodes", ""));
        this.sensorIds = FileUtil.readSensorIdsFromFile(props.getProperty("sensor_file"));
        String hashType = props.getProperty("type", "murmur");
        this.hashFunction = HashFunctionFactory.getHashFunction(hashType);
    }

    public void run() {
        System.out.println("SimpleConsistentHashing running...");
        System.out.println("Nodes: " + nodes);
        System.out.println("Number of sensors loaded: " + sensorIds.size());
        System.out.println("Hash function used: " + hashFunction.getClass().getSimpleName());

        Map<String, Integer> nodeToSensorCount = new HashMap<>();
        for (String node : nodes) {
            nodeToSensorCount.put(node, 0);
        }

        for (Integer sensorId : sensorIds) {
            int hash = hashFunction.hash(sensorId);
            int assignedIndex = Math.abs(hash) % nodes.size();
            String assignedNode = nodes.get(assignedIndex);
            nodeToSensorCount.put(assignedNode, nodeToSensorCount.get(assignedNode) + 1);
        }

        boolean writeToFile = props.getProperty("WriteInFile", "no").equalsIgnoreCase("yes");
        if (writeToFile) {
            String outputFile = props.getProperty("outputFile", "simple_output.txt");
            FileUtil.writeSimpleOutput(nodeToSensorCount, outputFile);
            System.out.println("✔ Output written to: " + outputFile);
        } else {
            FileUtil.printSimpleStats(nodeToSensorCount);
        }
    }
}
