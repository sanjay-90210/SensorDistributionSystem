package yourpackage;
import java.util.*;

public class ConsistentHashRing {
    private final TreeMap<Integer, String> ring = new TreeMap<>();
    private final int VIRTUAL_NODES = 1000;
    private final Hashfunction hashFunction;

    public int getRingSize() {
    return ring.size();
}

    public ConsistentHashRing(List<String> nodes, Hashfunction hashFunction) {
        this.hashFunction = hashFunction;
        for (String node : nodes) {
            for (int i = 0; i < VIRTUAL_NODES; i++) {
                int hash = hashFunction.hash(node + "-VN" + i);
                ring.put(hash, node);
            }
        }
    }

    public String getAssignedNode(int bucketId) {
        int hash = hashFunction.hash("Bucket-" + bucketId);
        Map.Entry<Integer, String> entry = ring.ceilingEntry(hash);
        return (entry != null) ? entry.getValue() : ring.firstEntry().getValue();
    }
}
