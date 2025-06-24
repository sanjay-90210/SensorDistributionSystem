package yourpackage;

import java.util.Properties;

public class ConsistentHashingFactory {
    public static ConsistentHashing getConsistentHashing(String type, Properties props) {
        switch (type) {
            case "simple":
                return new SimpleConsistentHashing(props);

            case "bucket":
                return new BucketBasedConsistentHashing(props);

            case "baseline":
                return new BaselineConsistentHashing(props);

            default:
                throw new IllegalArgumentException("Unknown type: " + type);
        }
    }
}
