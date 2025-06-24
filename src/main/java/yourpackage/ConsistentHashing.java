package yourpackage;

import java.util.List;

public interface ConsistentHashing {
    void hashNodes(List<String> nodes);
    void hashSensors(List<Long> sensors);
    void printStats();
}
