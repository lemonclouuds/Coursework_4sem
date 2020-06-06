import java.util.HashMap;

public class Bank {
    public String name;
    private HashMap<String, Double> currency = new HashMap<>();

    public HashMap<String, Double> getCurrency() {
        return currency;
    }

    public Bank(String str) {
        String arrStr[] = str.split(" ", 7);
        name = arrStr[0];
        currency.put("usdbuy", Double.parseDouble(arrStr[1]));
        currency.put("usdsell", Double.parseDouble(arrStr[2]));
        currency.put("eurbuy", Double.parseDouble(arrStr[3]));
        currency.put("eursell", Double.parseDouble(arrStr[4]));
        currency.put("rubbuy", Double.parseDouble(arrStr[5]));
        currency.put("rubsell", Double.parseDouble(arrStr[6]));
    }
}


