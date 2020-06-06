public class Converter {
    private static double res = 0;

    public static double convert(Double value, Bank bank, String flag) {
            if(flag.equals("USD_TO_BYN"))
                res = value * bank.getCurrency().get("usdbuy");
            if(flag.equals("USD_TO_EUR"))
                res = value * bank.getCurrency().get("usdbuy") / bank.getCurrency().get("eursell");
            if(flag.equals("USD_TO_RUB"))
                res = value * bank.getCurrency().get("usdbuy") / bank.getCurrency().get("rubsell") * 100;
            if(flag.equals("EUR_TO_BYN"))
                res = value * bank.getCurrency().get("eurbuy");
            if(flag.equals("EUR_TO_USD"))
                res = value * bank.getCurrency().get("eurbuy") / bank.getCurrency().get("usdsell");
            if(flag.equals("EUR_TO_RUB"))
                res = value * bank.getCurrency().get("eurbuy") / bank.getCurrency().get("rubsell") * 100;
            if(flag.equals("RUB_TO_BYN"))
                res = value * bank.getCurrency().get("rubbuy") / 100;
            if(flag.equals("RUB_TO_USD"))
                res = value * bank.getCurrency().get("rubbuy") / bank.getCurrency().get("usdsell") / 100;
            if(flag.equals("RUB_TO_EUR"))
                res = value * bank.getCurrency().get("rubbuy") / bank.getCurrency().get("eursell") / 100;
            if(flag.equals("BYN_TO_USD"))
                res = value / bank.getCurrency().get("usdsell");
            if(flag.equals("BYN_TO_EUR"))
                res = value / bank.getCurrency().get("eursell");
            if(flag.equals("BYN_TO_RUB"))
                res = value / bank.getCurrency().get("rubsell") * 100;
        return res;
    }

}