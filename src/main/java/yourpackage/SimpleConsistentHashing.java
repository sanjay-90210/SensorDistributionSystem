package yourpackage;

import java.util.*;

public class SimpleConsistentHashing implements ConsistentHashing {
    private final HashFunction hashFunction;
    private Map<String, Integer> nodeToSensorCount = new HashMap<>();
    private List<String> nodes;
    private String outputFile;

    public SimpleConsistentHashing(Properties props) {
        String hashType = props.getProperty("type", "murmur");
        this.hashFunction = HashFunctionFactory.getHashFunction(hashType);
        this.outputFile = props.getProperty("outputFile");
    }

    public void hashNodes(List<String> nodes) {
        this.nodes = nodes;
        for (String node : nodes) {
            nodeToSensorCount.put(node, 0);
        }
    }

    public void hashSensors(List<Long> sensors) {
        for (Long sensorId : sensors) {
            int hash = hashFunction.hash(sensorId);
            int assignedIndex = Math.abs(hash) % nodes.size();
            String assignedNode = nodes.get(assignedIndex);
            nodeToSensorCount.put(assignedNode, nodeToSensorCount.get(assignedNode) + 1);
        }
    }

    public void printStats() {
        if (outputFile != null) {
            FileUtil.writeSimpleStats(nodeToSensorCount, outputFile);
        } else {
            FileUtil.printSimpleStats(nodeToSensorCount);
        }
    }
}
