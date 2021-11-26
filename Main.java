import java.io.File;
import java.util.Map;
import java.util.TreeMap;

public class Main { 
    public static void main(String[] args) throws Exception {
        String path = "G:\\Bohdan-G\\translate\\translate-skripts";
        // TestPrintDirectory(path);
        // TestAnaliseDirectory(path);
        TestAnaliseDirectoryMThread(path);
    }

    public static void TestPrintDirectory(String path) throws Exception {
        var file = new File(path);
        DirAnaliser.PrintDirectory(file);
    }

    public static void TestAnaliseDirectory(String path) throws Exception {
        var file = new File(path);

        long startTime = System.nanoTime();
        var stat = DirAnaliser.AnaliseDirectory(file, 1000, ".*\\.psd$");
        long endTime = System.nanoTime();
        double duration = (endTime - startTime) / 1_000_000.0;

        System.out.println(stat);
        System.out.println("Method execution time: " + duration + "ms");
    }

    public static void TestAnaliseDirectoryMThread(String path) throws Exception {
        var file = new File(path);

        Map<Integer, Double> executionTime = new TreeMap<Integer, Double>();
        for(int num = 0; num < 5; ++num) {
            int threadNum = 1 << num;

            long startTime = System.nanoTime();
            DirAnaliser.AnaliseDirectoryMThread(file, 1000, ".*\\.psd$", threadNum);
            long endTime = System.nanoTime();
        
            executionTime.put(threadNum, (endTime - startTime) / 1_000_000.0);
        }
        
        Helper.PrintMap(executionTime);
    }
}