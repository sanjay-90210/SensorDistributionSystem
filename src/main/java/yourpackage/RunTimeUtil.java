package yourpackage;

public class RunTimeUtil {

    public interface TimedTask {
        void execute();
    }

    public static void measure(String taskName, TimedTask task) {
        long start = System.nanoTime();

        task.execute();

        long end = System.nanoTime();
        double durationInSeconds = (end - start) / 1_000_000_000.0;
        System.out.printf("Task '%s' executed in %.4f seconds.%n", taskName, durationInSeconds);
    }
}
