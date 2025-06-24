package yourpackage;

import java.io.*;
import java.util.*;

public class FileUtil {
    private static final String DEFAULT_PORT = "8080";

    public static Properties loadProperties(File file) {
        try (InputStream input = new FileInputStream(file)) {
            Properties prop = new Properties();
            prop.load(input);
            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
            return new Properties();
        }
    }

    public static List<Long> readSensorsFromFile(String fileName) {
        List<Long> sensors = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    sensors.add(Long.parseLong(line.trim()));
                } catch (NumberFormatException ignored) {}
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sensors;
    }

    public static List<String> parseNodesFromFile(String fileName) {
        List<String> nodes = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if(line.isEmpty() || line.charAt(0) == '#') {
                    continue;
                }
                if (!line.contains(":")) line += ":" + DEFAULT_PORT;
                nodes.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return nodes;
    }

    public static void writeSimpleStats(Map<String, Integer> nodeToSensorCount, String fileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (Map.Entry<String, Integer> entry : nodeToSensorCount.entrySet()) {
                writer.println("Node: " + entry.getKey());
                writer.println("  Sensor Count: " + entry.getValue());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeBucketStats(String fileName, Map<String, Map<Integer, Integer>> nodeBucketSensorCount) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            for (String node : nodeBucketSensorCount.keySet()) {
                Map<Integer, Integer> bucketMap = nodeBucketSensorCount.get(node);
                List<Integer> counts = new ArrayList<>(bucketMap.values());

                int min = counts.stream().mapToInt(i -> i).min().orElse(0);
                int max = counts.stream().mapToInt(i -> i).max().orElse(0);
                double avg = counts.stream().mapToInt(i -> i).average().orElse(0);
                double std = calculateStdDev(counts, avg);

                writer.println(node + ":");
                writer.printf("  Minimum sensors per bucket: %d\n", min);
                writer.printf("  Maximum sensors per bucket: %d\n", max);
                writer.printf("  Average sensors per bucket: %.2f\n", avg);
                writer.printf("  Std deviation of sensors per bucket: %.2f\n\n", std);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void printSimpleStats(Map<String, Integer> nodeToSensorCount) {
        System.out.println("=== Simple Consistent Hashing Stats ===");
        for (Map.Entry<String, Integer> entry : nodeToSensorCount.entrySet()) {
            System.out.println("Node: " + entry.getKey());
            System.out.println("  Sensor Count: " + entry.getValue());
        }
        System.out.println("========================================");
    }

    public static void printBucketStats(Map<String, Map<Integer, Integer>> nodeBucketSensorCount) {
        System.out.println("=== Bucket-Based Consistent Hashing Stats ===");
        for (String node : nodeBucketSensorCount.keySet()) {
            Map<Integer, Integer> bucketMap = nodeBucketSensorCount.get(node);
            List<Integer> counts = new ArrayList<>(bucketMap.values());

            int min = counts.stream().mapToInt(i -> i).min().orElse(0);
            int max = counts.stream().mapToInt(i -> i).max().orElse(0);
            double avg = counts.stream().mapToInt(i -> i).average().orElse(0);
            double std = calculateStdDev(counts, avg);

            System.out.println("Node: " + node);
            System.out.printf("  Minimum sensors per bucket: %d\n", min);
            System.out.printf("  Maximum sensors per bucket: %d\n", max);
            System.out.printf("  Average sensors per bucket: %.2f\n", avg);
            System.out.printf("  Std deviation of sensors per bucket: %.2f\n\n", std);
        }
        System.out.println("=============================================");
    }


    private static double calculateStdDev(List<Integer> values, double mean) {
        if (values.isEmpty()) return 0;
        double variance = 0;
        for (int v : values) variance += Math.pow(v - mean, 2);
        return Math.sqrt(variance / values.size());
    }
}
