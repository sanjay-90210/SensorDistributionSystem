package yourpackage;

import java.util.*;

public class BucketBasedConsistentHashing implements ConsistentHashing {
    private final int numBuckets;
    private final String outputFile;
    private final HashFunction hashFunction;
    private Map<Integer, String> bucketToNode = new HashMap<>();
    private Map<String, Map<Integer, Integer>> nodeBucketSensorCount = new HashMap<>();

    public BucketBasedConsistentHashing(Properties props) {
        this.numBuckets = Integer.parseInt(props.getProperty("numBuckets", "1000"));
        String hashType = props.getProperty("hashType", "murmur");
        this.hashFunction = HashFunctionFactory.getHashFunction(hashType);
        this.outputFile = props.getProperty("outputFile");
    }

    public void hashNodes(List<String> nodes) {
        for (int i = 0; i < numBuckets; i++) {
            bucketToNode.put(i, nodes.get(i % nodes.size()));
        }

        for (String node : nodes) {
            nodeBucketSensorCount.put(node, new HashMap<>());
        }
    }

    public void hashSensors(List<Long> sensors) {
        for (Long sensorId : sensors) {
            int bucketId = Math.abs(hashFunction.hash(sensorId)) % numBuckets;
            String assignedNode = bucketToNode.get(bucketId);
            Map<Integer, Integer> bucketCountMap = nodeBucketSensorCount.get(assignedNode);
            bucketCountMap.put(bucketId, bucketCountMap.getOrDefault(bucketId, 0) + 1);
        }
    }

    public void printStats() {
        if(outputFile != null) {
            FileUtil.writeBucketStats(outputFile, nodeBucketSensorCount);
        } else {
            FileUtil.printBucketStats(nodeBucketSensorCount);
        }
    }
}
