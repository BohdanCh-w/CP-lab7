import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirAnaliser {
    public static DirStat AnaliseDirectory(File path, long bigParam, String match) {
        Map<Boolean, List<File>> content = listDirectory(path);
        List<File> directories = content.get(true);
        List<File> files = content.get(false);

        DirStat stat = new DirStat();
        for(var dir : directories) {
            stat = stat.add(AnaliseDirectory(dir, bigParam, match));
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

    public static DirStat AnaliseDirectoryMThread(File path, long bigParam, String match, int threadNum) {
        return new DirStat();
    }

    public static Map<Boolean, List<File>> listDirectory(File dir) {
        return Stream.of(dir.listFiles()).collect(Collectors.partitioningBy(file -> file.isDirectory()));
    }

    public static void PrintDirectory(File path) {
         PrintDirectory(path, "");
    }

    public static void PrintDirectory(File path, String indent) {
        Map<Boolean, List<File>> content = listDirectory(path);
        List<File> directories = content.get(true);
        List<File> files = content.get(false);
        System.out.println(indent + path.getName() + "/");
        for(var dir : directories) {
            PrintDirectory(dir, indent + "│   ");
        }
        PrintList(files, indent);
    }

    public static void PrintList(List<File> lst, String indent) {
        if(lst.size() == 0) {
            System.out.println(indent + "└───" + "<Empty Directory>");
            return;
        }
        var iter = lst.iterator();
        while(true) {
            var obj = iter.next().getName();
            if(iter.hasNext()) {
                System.out.println(indent + "├───" + obj);
            } else {
                System.out.println(indent + "└───" + obj);
                break;
            }
        }
    }
}
