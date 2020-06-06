import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class Parser {
    //count - количество слов, которые мы уберем с конца
    public static String removeLastValue(String str, int count) {
        for (int i = 0; i < count; i++) {
            str = str.substring(0, str.lastIndexOf(" "));
        }
        return str;
    }

    private static Document getPage() throws IOException{
        String url = "https://myfin.by/currency/minsk";
        final Document doc = Jsoup.connect(url).get();
        return doc;
    }

    public static HashMap<String, Bank> initialize() {
        HashMap<String, Bank> parsed = new HashMap<String, Bank>();
        try {
            Document doc = getPage();
            Elements info = doc.select("[data-bank_id]");
            int i = 0;
            for (Element elem: info) {
                if (elem.text().startsWith("Беларусбанк")) {
                    parsed.put("беларусбанк", new Bank(removeLastValue(elem.text(), 2)));
                } else if (elem.text().startsWith("БПС-Сбербанк")) {
                    parsed.put("бпс-сбербанк", new Bank(removeLastValue(elem.text(), 2)));
                } else if (elem.text().startsWith("Приорбанк")) {
                    parsed.put("приорбанк", new Bank(removeLastValue(elem.text(), 2)));
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return parsed;
    }
}
