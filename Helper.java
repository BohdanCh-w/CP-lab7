import java.util.List;
import java.util.Map;

public class Helper {
    public static void PrintList(List<?> lst) {
        if(lst.size() == 0) {
            System.out.println("<Void List>");
        }
        for(var obj : lst) {
            System.out.println(obj);
        }
        System.out.println();
    }

    public static void PrintMap(Map<?, ?> map) {
        if(map.size() == 0) {
            System.out.println("<Void Map>");
        }
        for(var obj : map.entrySet()) {
            System.out.println(String.format("%s: %s", obj.getKey(), obj.getValue()));
        }
        System.out.println();
    }
}
