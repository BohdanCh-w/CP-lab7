import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirAnaliserMT extends Thread{
    public static volatile Integer activeThreadCount;
    static public int maxThreads;
    private File file_path;
    private long bigSize;
    private String name_match;
    private volatile DirStat result;
    
    public DirAnaliserMT(File path, long bigParam, String match) {
        file_path = path;
        bigSize = bigParam;
        name_match = match;
    }

    public void run() {
        Map<Boolean, List<File>> content = listDirectory(file_path);
        List<File> directories = content.get(true);
        List<File> files = content.get(false);
        result = new DirStat();

        List<DirAnaliserMT> openedThreads = new ArrayList<DirAnaliserMT>();

        for(var dir : directories) {
            if(activeThreadCount < maxThreads) {
                synchronized(DirAnaliserMT.activeThreadCount) {
                    DirAnaliserMT.activeThreadCount++;
                }
                var thread = new DirAnaliserMT(dir, bigSize, name_match);
                thread.setName(dir.getName());
                thread.start();
                openedThreads.add(thread);
            } else {
                result = result.add(OneThreaded(dir, bigSize, name_match));
            }
        }

        for(var thread : openedThreads) {
            try { 
                thread.join();
            } catch(InterruptedException e){System.out.println(e);} 
            synchronized(DirAnaliserMT.activeThreadCount) {
                DirAnaliserMT.activeThreadCount--;
            }
            result = result.add(thread.GetResult());
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
        DirAnaliserMT.maxThreads = threadNum;
        DirAnaliserMT.activeThreadCount = 1;
        DirAnaliserMT mainThread = new DirAnaliserMT(path, bigParam, match);
        mainThread.start();
        mainThread.join();
        DirAnaliserMT.activeThreadCount = 0;
        return mainThread.GetResult();
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
