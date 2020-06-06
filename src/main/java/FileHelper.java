import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class FileHelper {
    public static void saveToFile(HashMap<Long,String> map) throws IOException {
        Properties properties = new Properties();

        for (Map.Entry<Long,String> entry : map.entrySet()) {
            properties.put(entry.getKey().toString(), entry.getValue());
        }

        properties.store(new FileOutputStream("data.properties"), null);
    }

    public static HashMap<Long,String> getUsersDefault() throws IOException {
        HashMap<Long, String> map = new HashMap<Long, String>();
        Properties properties = new Properties();
        properties.load(new FileInputStream("data.properties"));

        for (String key : properties.stringPropertyNames()) {
            map.put(Long.parseLong(key), properties.get(key).toString());
        }
        return map;
    }
}
