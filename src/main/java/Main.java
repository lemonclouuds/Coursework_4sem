import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.util.HashMap;

public class Main {
    public static HashMap <String, Bank> banks = new HashMap<>();
    public static void main(String[] args) {
        ApiContextInitializer.init();
        banks = Parser.initialize();
        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new CurrencyBot());
        } catch (TelegramApiRequestException e) {
           e.printStackTrace();
        }

        System.out.println("Bot is running!");
    }
}
