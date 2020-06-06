import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.HashMap;

public class CurrencyBot extends TelegramLongPollingBot {
    static HashMap<Long, String> usersDefault;

    static {
        try {
            usersDefault = FileHelper.getUsersDefault();
        } catch (IOException e) {
            usersDefault = new HashMap<>();
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) { //вынести все в поля класса
        if (update.hasMessage() && update.getMessage().hasText()) {
            String userFirstName = update.getMessage().getChat().getFirstName();
            Long userId = update.getMessage().getChat().getId();
            String msgText = update.getMessage().getText();

            if (msgText.equals("/start")) {
                String strInfo = "Привет, " + userFirstName + "! \nЭтот бот показывает актуальный курс валют для доллара, евро и российского рубля. Также он может конвертировать валюту.\n" +
                        "Отправь /help чтобы увидеть доступные команды!";
                msgSend(update.getMessage(), strInfo);
            } else if (msgText.equals("/help")) {
                String strInfo = "/rate - показывает актуальный курс для доллара, евро и российского рубля. По умолчанию курс Belarusbank.\n\n" +
                        "/default - позволяет установить/сменить валюту по умолчанию.\n\tНапример, /default usd\n\n" +
                        "\tКурс Беларусбанк/БПС-Сбербанк/Приорбанк - показывает курс валюты в этом банке.\n\n" +
                        "\tДля конвертации валют введите значения в формате \"ЗНАЧЕНИЕ ИЗ_ВАЛЮТЫ В_ВАЛЮТУ\". Например \"4 usd byn\" переведет 4 доллара в белорусские рубли.\n" +
                        "\tОбозначения для валют:\n    Белорусский рубль - BYN\n    Доллар США - USD\n    Евро - EUR\n    Российский рубль - RUB\n";
                msgSend(update.getMessage(), strInfo);
            } else if (msgText.contains("/default")) { //ставим дефолтную валюту, в которую будем конвертировать
                String str = msgText.trim().split("/default ")[1];
                Convertation.addUsersDefault(userId, str, usersDefault);


                String strInfo = "\tТеперь ваша валюта по умолчанию - " + str;
                msgSend(update.getMessage(), strInfo);
                new Thread(() -> {
                    try {
                        FileHelper.saveToFile(usersDefault);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            } else if (msgText.toLowerCase().startsWith("курс")) {
                if (msgText.toLowerCase().contains("беларусбанк") ||
                        msgText.toLowerCase().contains("беларусбанка") ||
                        msgText.toLowerCase().contains("беларусьбанк") ||
                        msgText.toLowerCase().contains("беларусьбанка")) {
                    msgSend(update.getMessage(), "Курс в Беларусбанке:\n" + getInfo(Main.banks.get("беларусбанк")));
                }
                if (msgText.toLowerCase().contains("бпс") || //ДОБАВИТЬ СБЕРБАНК
                        msgText.toLowerCase().contains("бпс-банк") ||
                        msgText.toLowerCase().contains("бпс-банка")) {
                    msgSend(update.getMessage(), "Курс в БПС-Сбербанк:\n" + getInfo(Main.banks.get("бпс-сбербанк")));
                }
                if (msgText.toLowerCase().contains("приорбанк") ||
                        msgText.toLowerCase().contains("приорбанка") ||
                        msgText.toLowerCase().contains("приор")) {
                    msgSend(update.getMessage(), "Курс в Приорбанке:\n" + getInfo(Main.banks.get("приорбанк")));
                }
            } else if (msgText.equals("/rate")) {
                msgSend(update.getMessage(), "Курс в Беларусбанке:\n" + getInfo(Main.banks.get("беларусбанк")));
            } else if(msgText.toLowerCase().contains("usd") || msgText.toLowerCase().contains("eur") || msgText.toLowerCase().contains("byn") || msgText.toLowerCase().contains("rub")) {
                if (Convertation.isDefaultSet(userId, usersDefault) && (msgText.split(" ").length==2)) {
                    msgText = msgText + " " + usersDefault.get(userId);

                }
                System.out.println(msgText);
                String flag = Convertation.getFlag(msgText);
                Double value = Convertation.getValue(msgText);
                double res = Converter.convert(value, Main.banks.get("беларусбанк"), flag);
                msgSend(update.getMessage(), new String().format("%.2f ",res) + flag.split("_")[2]);
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

    public String getInfo(Bank setBank) {
        String info = "USD:\n" + "   $ Покупка: " + Double.toString(setBank.getCurrency().get("usdbuy")) + "\n   $ Продажа: " +
                Double.toString(setBank.getCurrency().get("usdsell")) + "\nEUR\n   € Покупка: " +
                Double.toString(setBank.getCurrency().get("eurbuy")) + "\n   € Продажа: " +
                Double.toString(setBank.getCurrency().get("eursell")) + "\nRYB\n   ₽ Покупка: " +
                Double.toString(setBank.getCurrency().get("rubbuy")) + "\n   ₽ Продажа: " +
                Double.toString(setBank.getCurrency().get("rubsell"));
        return info;
    }


}