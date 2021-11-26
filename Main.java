import java.io.File;
import java.util.List;

public class Main { 
    public static void main(String[] args) throws Exception {
        String path = "G:\\Bohdan-G\\translate\\translate-skripts";
        TestPrintDirectory(path);
    }

    public static void TestPrintDirectory(String path) throws Exception {
        var file = new File(path);
        DirAnaliser.PrintDirectory(file);
    }

    public static void TestAnaliseDirectory(String path) throws Exception {
        var file = new File(path);
        var stat = DirAnaliser.AnaliseDirectory(file, 1000, ".*\\.psd$");
        System.out.println(stat);
    }

    public static void PrintList(List<?> lst) {
        if(lst.size() == 0) {
            System.out.println("<Void List>");
        }
        for(var obj : lst) {
            System.out.println(obj);
        }
        System.out.println();
    }
}