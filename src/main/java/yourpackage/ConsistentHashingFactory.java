package yourpackage;

import java.util.Properties;

public class ConsistentHashingFactory {
    public static void getConsistentHashing(Properties props) {
        String mode = props.getProperty("ConsistentHashingMode", "simple").toLowerCase();

        switch (mode) {
            case "simple":
                System.out.println("Factory: Using SimpleConsistentHashing");
                new SimpleConsistentHashing(props).run();
                break;

            case "bucket":
                System.out.println("Factory: Using BucketBasedConsistentHashing");
                new BucketBasedConsistentHashing(props).run();
                break;

            case "baseline":
                System.out.println("Factory: Using Baseline");
                new Baseline(props).run();
                break;

            default:
                throw new IllegalArgumentException("Unknown mode: " + mode);
        }
    }
}
