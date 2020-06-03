import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botsApi = new TelegramBotsApi();

       try {
         botsApi.registerBot(new ShopBot());
       } catch (TelegramApiRequestException e) {
           e.printStackTrace();
       }
        System.out.println("Bot is running!");
    }
}
