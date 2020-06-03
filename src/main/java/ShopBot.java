import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public class ShopBot extends TelegramLongPollingBot {
    @Override
    public void onUpdateReceived(Update update){
        if (update.hasMessage() && update.getMessage().hasText()) {
            String userFirstName = update.getMessage().getChat().getFirstName();
            String userUsername = update.getMessage().getChat().getUserName();
            long userId = update.getMessage().getChat().getId();
            String msgText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();


            if (msgText.equals("/start")) {
                String strInfo = "Hello, "+ userFirstName +"! \nWhat do you want to know?";
                msgSend(update.getMessage(), strInfo);

            } else if (msgText.equals("/info")){
                String strInfo = "This bot helps you to do blah blah blah...";
                msgSend(update.getMessage(), strInfo);

            } else {
                msgSend(update.getMessage(), "Unknown command :<");
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "HotWaterMinskBot";
    }

    @Override
    public String getBotToken() {
        return "1184628933:AAGUHRbcWA9yGGOLRqaDe5jFFw4_nBd46dA";
    }

    private void msgSend(Message message, String text) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(message.getChatId());
        //sendMsg.enableMarkdown(true);
        sendMsg.setReplyToMessageId(message.getMessageId());
        sendMsg.setText(text);
        try {
            execute(sendMsg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
    
}