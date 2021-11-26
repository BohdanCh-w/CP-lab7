import java.io.File;
import java.util.Map;
import java.util.TreeMap;


public class Main { 
    public static void main(String[] args) throws Exception {
        System.out.println();
        File path = new File("G:\\Bohdan-G");
        // TestPrintDirectory(path);
        // TestAnaliseDirectory(path);
        // TestAnaliseDirectoryMThread(path);
        // TestAnaliseDirectoryMRunnable(path);
        TestAnaliseDirectoryCompare(path);
    }

    public static void TestPrintDirectory(File file) throws Exception {
        DirAnaliser.PrintDirectory(file);
    }

    public static void TestAnaliseDirectory(File file) throws Exception {
        System.out.println("One Threaded directory analiser test : ");

        long startTime = System.nanoTime();
        var stat = DirAnaliser.AnaliseDirectory(file, 1000, ".*\\.psd$");
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.println(stat);
        System.out.println("Method execution time: " + duration + "ms\n\n");
    }

    public static void TestAnaliseDirectoryMThread(File file) throws Exception {
        System.out.println("Multithreading directory analiser extending thread test : ");

        long startTime = System.nanoTime();
        var stat = DirAnaliserMT.AnaliseDirectory(file, 1000, ".*\\.psd$", 16);
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.println(stat);
        System.out.println("Method execution time: " + duration + "ms\n\n");
    }

    public static void TestAnaliseDirectoryMRunnable(File file) throws Exception {
        System.out.println("Multithreading directory analiser implementing runnable test : ");

        long startTime = System.nanoTime();
        var stat = DirAnaliserMR.AnaliseDirectory(file, 1000, ".*\\.psd$", 16);
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.println(stat);
        System.out.println("Method execution time: " + duration + "ms\n\n");
    }

    public static void TestAnaliseDirectoryCompare(File file) throws Exception {
        Map<String, Long> executionTime = new TreeMap<String, Long>();

        long startTime = System.nanoTime();
        DirAnaliser.AnaliseDirectory(file, 1000, ".*\\.psd$");
        long duration = System.nanoTime() - startTime;
        executionTime.put("ST", duration);
        System.out.println("Single Thread Time : " + duration / 1_000_000.0 + "ms");

        for(int num = 0; num < 5; ++num) {
            int threadNum = 1 << num;

            startTime = System.nanoTime();
            DirAnaliserMT.AnaliseDirectory(file, 1000, ".*\\.psd$", threadNum);
            duration = System.nanoTime() - startTime;
        
            executionTime.put("MT" + threadNum, duration);
            System.out.println("Extended Thread " + threadNum + " : " + duration / 1_000_000.0 + "ms");
        }

        for(int num = 0; num < 5; ++num) {
            int threadNum = 1 << num;

            startTime = System.nanoTime();
            DirAnaliserMR.AnaliseDirectory(file, 1000, ".*\\.psd$", threadNum);
            duration = System.nanoTime() - startTime;
        
            executionTime.put("MR" + threadNum, duration);
            System.out.println("Implemented Runnable " + threadNum + " : " + duration / 1_000_000.0 + "ms");
        }
    }
}