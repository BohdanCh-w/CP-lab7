import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirAnaliserMR implements Runnable {
    private static ThreadCounter threadCounter;
    private File file_path;
    private long bigSize;
    private String name_match;
    private volatile DirStat result;
    
    public DirAnaliserMR(File path, long bigParam, String match) {
        file_path = path;
        bigSize = bigParam;
        name_match = match;
    }

    public void run() {
        Map<Boolean, List<File>> content = listDirectory(file_path);
        List<File> directories = content.get(true);
        List<File> files = content.get(false);
        result = new DirStat();

        Map<Thread, DirAnaliserMR> openedThreads = new HashMap<Thread, DirAnaliserMR>();

        for(var dir : directories) {
            if(threadCounter.acquire()) {
                var analiser = new DirAnaliserMR(dir, bigSize, name_match);
                var thread = new Thread(analiser);
                thread.setName(dir.getName());
                thread.start();
                threadCounter.addThread(thread);
                openedThreads.put(thread, analiser);
            } else {
                result = result.add(OneThreaded(dir, bigSize, name_match));
            }
        }

        for(var entry : openedThreads.entrySet()) {
            try { 
                entry.getKey().join();
            } catch(InterruptedException e){System.out.println(e);} 
            threadCounter.release();
            threadCounter.removeThread(entry.getKey());
            result = result.add(entry.getValue().GetResult());
        }

        for(var file : files) {
            long size = file.length();
            result.totalSize += size;
            if(size > bigSize) { result.bigFiles++; }
            if(file.getName().matches(name_match)) { result.specialFiles++; }
        }

        result.totalNumberOfSubdirectories += directories.size();
        result.totalNumberOfFiles += files.size();
    }

    public static DirStat AnaliseDirectory(File path, long bigParam, String match, int threadNum) throws Exception {
        DirAnaliserMR.threadCounter = new ThreadCounter(threadNum);
        DirAnaliserMR.threadCounter.acquire();
        DirAnaliserMR analiser = new DirAnaliserMR(path, bigParam, match);
        Thread mainThread = new Thread(analiser);
        mainThread.start();
        mainThread.join();
        DirAnaliserMR.threadCounter.release();
        return analiser.GetResult();
    }

    private static DirStat OneThreaded(File path, long bigParam, String match) {
        Map<Boolean, List<File>> content = listDirectory(path);
        List<File> directories = content.get(true);
        List<File> files = content.get(false);

        DirStat stat = new DirStat();
        for(var dir : directories) {
            stat = stat.add(OneThreaded(dir, bigParam, match));
        }
        System.out.print("");
        for(var file : files) {
            long size = file.length();
            stat.totalSize += size;
            if(size > bigParam) { stat.bigFiles++; }
            if(file.getName().matches(match)) { stat.specialFiles++; }
        }
        stat.totalNumberOfSubdirectories += directories.size();
        stat.totalNumberOfFiles += files.size();

        return stat;
    }

    private static Map<Boolean, List<File>> listDirectory(File dir) {
        return Stream.of(dir.listFiles()).collect(Collectors.partitioningBy(file -> file.isDirectory()));
    }

    public DirStat GetResult() { return result; }
}
