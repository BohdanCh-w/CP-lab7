import java.io.File;
import java.util.Map;
import java.util.TreeMap;


//"C:\Users\bohda\AppData\Local\Programs\Eclipse Foundation\jdk-11.0.12.7-hotspot\bin\java.exe" @C:\Users\bohda\AppData\Local\Temp\cp_1d0vznn3iz81z4vxbjx6icq7z.argfile Main


public class Main { 
    public static void main(String[] args) throws Exception {
        System.out.println();
        String path = "G:\\Bohdan-G";
        // TestPrintDirectory(path);
        // TestAnaliseDirectory(path);
        TestAnaliseDirectoryMThread(path);
        // TestAnaliseDirectoryCompare(path);
    }

    public static void TestPrintDirectory(String path) throws Exception {
        var file = new File(path);
        DirAnaliser.PrintDirectory(file);
    }

    public static void TestAnaliseDirectory(String path) throws Exception {
        System.out.println("One Threaded directory analiser test : ");
        var file = new File(path);

        long startTime = System.nanoTime();
        var stat = DirAnaliser.AnaliseDirectory(file, 1000, ".*\\.psd$");
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.println(stat);
        System.out.println("Method execution time: " + duration + "ms\n\n");
    }

    public static void TestAnaliseDirectoryMThread(String path) throws Exception {
        System.out.println("Multithreading directory analiser test : ");
        var file = new File(path);

        long startTime = System.nanoTime();
        var stat = DirAnaliserMT.AnaliseDirectory(file, 1000, ".*\\.psd$", 16);
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.println(stat);
        System.out.println("Method execution time: " + duration + "ms\n\n");
    }

    public static void TestAnaliseDirectoryCompare(String path) throws Exception {
        var file = new File(path);
        Map<String, Double> executionTime = new TreeMap<String, Double>();

        long startTimeSingle = System.nanoTime();
        DirAnaliser.AnaliseDirectory(file, 1000, ".*\\.psd$");
        long endTimeSingle = System.nanoTime();
        executionTime.put("SingleThreaded  ", (endTimeSingle - startTimeSingle) / 1_000_000.0);

        for(int num = 0; num < 5; ++num) {
            int threadNum = 1 << num;

            long startTime = System.nanoTime();
            DirAnaliserMT.AnaliseDirectory(file, 1000, ".*\\.psd$", threadNum);
            long endTime = System.nanoTime();
        
            executionTime.put(String.format("MultiThreaded %2d", threadNum), (endTime - startTime) / 1_000_000.0);
        }
        
        Helper.PrintMap(executionTime);
    }
}