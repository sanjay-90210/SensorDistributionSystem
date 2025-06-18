package yourpackage;

import java.util.*;

public class BucketBasedConsistentHashing {
    private final Properties props;
    private final List<String> nodes;
    private final List<Integer> sensorIds;
    private final int numBuckets;
    private final Hashfunction hashFunction;

    public BucketBasedConsistentHashing(Properties props) {
        this.props = props;
        this.nodes = FileUtil.parseNodes(props.getProperty("nodes", ""));
        this.sensorIds = FileUtil.readSensorIdsFromFile(props.getProperty("sensorFile"));
        this.numBuckets = Integer.parseInt(props.getProperty("numBuckets", "1000"));
        String hashType = props.getProperty("type", "murmur");
        this.hashFunction = HashFunctionFactory.getHashFunction(hashType);
    }

    public void run() {
        System.out.println("BucketBasedConsistentHashing running...");
        System.out.println("Nodes: " + nodes);
        System.out.println("Number of Buckets: " + numBuckets);
        System.out.println("Number of Sensors: " + sensorIds.size());
        System.out.println("Hash function used: " + hashFunction.getClass().getSimpleName());

        Map<Integer, String> bucketToNode = new HashMap<>();
        for (int i = 0; i < numBuckets; i++) {
            bucketToNode.put(i, nodes.get(i % nodes.size()));
        }

        Map<String, Map<Integer, Integer>> nodeBucketSensorCount = new HashMap<>();
        for (String node : nodes) {
            nodeBucketSensorCount.put(node, new HashMap<>());
        }

        for (Integer sensorId : sensorIds) {
            int bucketId = Math.abs(hashFunction.hash(sensorId)) % numBuckets;
            String assignedNode = bucketToNode.get(bucketId);
            Map<Integer, Integer> bucketCountMap = nodeBucketSensorCount.get(assignedNode);
            bucketCountMap.put(bucketId, bucketCountMap.getOrDefault(bucketId, 0) + 1);
        }

        boolean writeToFile = props.getProperty("WriteInFile", "no").equalsIgnoreCase("yes");
        if (writeToFile) {
            String outputPath = props.getProperty("outputFile", "bucket_output.txt");
            FileUtil.writeBucketStats(outputPath, nodeBucketSensorCount);
            System.out.println("✔ Output written to: " + outputPath);
        } else {
            FileUtil.printBucketStats(nodeBucketSensorCount);
        }
    }
}
