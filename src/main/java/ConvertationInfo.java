import com.google.inject.internal.cglib.core.$CollectionUtils;

import java.util.HashMap;

public class ConvertationInfo {

    public static void addUsersDefault(Long userId, String value, HashMap<Long, String> map) {
        map.put(userId, value);
    }

    public static boolean isDefaultSet(Long userId, HashMap<Long, String> map) {
        return  map.containsKey(userId);
    }


    public static String getFlag(String userInput) {
        userInput = userInput.replaceAll("[\\s]{2,}"," ").replaceAll(",",".");
        String[] strArray = userInput.split(" ");
        String flag = "";
        if (strArray.length == 3){
        if (strArray[1].equals("eur")) flag += "EUR_TO_";
            else if (strArray[1].equals("usd")) flag += "USD_TO_";
                else if (strArray[1].equals("rub")) flag += "RUB_TO_";
                    else if (strArray[1].equals("byn")) flag += "BYN_TO_";
        if (strArray[2].equals("eur")) flag += "EUR";
            else if (strArray[2].equals("usd")) flag += "USD";
                else if (strArray[2].equals("rub")) flag += "RUB";
                     else if (strArray[2].equals("byn")) flag += "BYN";}
        return flag;
    }

    public static Double getValue(String userInput) {
        userInput = userInput.replaceAll("[\\s]{2,}"," ").replaceAll(",",".");
        String[] strArray = userInput.split(" ");
        return Double.parseDouble(strArray[0]);
    }
}
