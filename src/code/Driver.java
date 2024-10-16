package code;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.function.Supplier;

/**
 * A testing class
 */
public class Driver {

    public static void main(String[] args) throws InterruptedException {
        String initialState = "8;" +
                "6;" +
                "g,g,g,r,r,r;" +
                "g,y,r,o,o,o;" +
                "o,r,o,y,y,y;" +
                "y,o,y,b,b,b;" +
                "r,b,b,b,g,g;" +
                "e,e,e,e,e,e;" +
                "e,e,e,e,e,e;" +
                "e,e,e,e,e,e;";

//        String initialState = "4;" +
//                "4;" +
//                "g,r,b,y;" +
//                "r,g,b,o;" +
//                "y,o,g,y;" +
//                "e,e,e,e";

//        System.out.println(WaterSortSearch.solve(initialState, "BF", false));
//        System.out.println(WaterSortSearch.solve(initialState, "DF", false));
//        System.out.println(WaterSortSearch.solve(initialState, "UC", false));
//        System.out.println(WaterSortSearch.solve(initialState, "ID", false));
//        System.out.println(WaterSortSearch.solve(initialState, "GR1", false));
//        System.out.println(WaterSortSearch.solve(initialState, "GR2", false));
//        System.out.println(WaterSortSearch.solve(initialState, "AS1", false));
//        System.out.println(WaterSortSearch.solve(initialState, "AS2", false));

//        measureAndPrintPerformance(() -> WaterSortSearch.solve(initialState, "BF", false), "Breadth First Search");
        measureAndPrintPerformance(() -> WaterSortSearch.solve(initialState, "DF", true), "Depth First Search");
//        measureAndPrintPerformance(() -> WaterSortSearch.solve(initialState, "UC", false), "Uniform Cost Search");
//        measureAndPrintPerformance(() -> WaterSortSearch.solve(initialState, "ID", false), "Iterative Deepening Search");
//        measureAndPrintPerformance(() -> WaterSortSearch.solve(initialState, "GR1", true), "Greedy Search 1");
//        measureAndPrintPerformance(() -> WaterSortSearch.solve(initialState, "GR2", false), "Greedy Search 2");
//        measureAndPrintPerformance(() -> WaterSortSearch.solve(initialState, "AS1", false), "A* Search 1");
//        measureAndPrintPerformance(() -> WaterSortSearch.solve(initialState, "AS2", false), "A* Search 2");
    }

    private static void measureAndPrintPerformance(Supplier<String> strategy, String strategyName) {
        ThreadMXBean bean = ManagementFactory.getThreadMXBean();

        long startCpuTime = bean.getCurrentThreadCpuTime();
        long startMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long startTime = System.nanoTime();

        String result = strategy.get();

        long endCpuTime = bean.getCurrentThreadCpuTime();
        long endMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long endTime = System.nanoTime();

        long cpuTime = (endCpuTime - startCpuTime) / 1_000_000; // Convert to milliseconds
        long memoryUsed = (endMemory - startMemory) / 1024; // Convert to kilobytes
        long elapsedTime = (endTime - startTime) / 1_000_000; // Convert to milliseconds
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        double cpuUtilization = (double) (endCpuTime - startCpuTime) / (elapsedTime * availableProcessors * 1_000_000) * 100;

        String formattedResult = formatResult(result, 100);
        System.out.println(ConsoleColors.RED_BOLD + strategyName + ConsoleColors.GREEN_BOLD + " - CPU Time: " + ConsoleColors.RESET + cpuTime + " ms," + ConsoleColors.BLUE_BOLD + " Memory Used: " + ConsoleColors.RESET + memoryUsed + " KB," + ConsoleColors.PURPLE_BOLD + " CPU Utilization: " + ConsoleColors.RESET + String.format("%.2f", cpuUtilization) + " %");
        System.out.println(ConsoleColors.YELLOW_BOLD + "Path: " + ConsoleColors.RESET + result);
    }

    private static String formatResult(String result, int maxLength) {
        StringBuilder formatted = new StringBuilder();
        int length = result.length();
        for (int i = 0; i < length; i += maxLength) {
            if (i + maxLength < length) {
                formatted.append(result, i, i + maxLength).append("\n");
            } else {
                formatted.append(result.substring(i));
            }
        }
        return formatted.toString();
    }
}