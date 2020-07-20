import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.IOException;
import java.util.HashMap;

public class CurrencyBot extends TelegramLongPollingBot {
    static HashMap<Long, String> usersDefault;
    String strInfo;
    String flag;
    Double value;
    double res;

    static {
        try {
            usersDefault = FileHelper.getUsersDefault();
        } catch (IOException e) {
            usersDefault = new HashMap<>();
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long userId = update.getMessage().getChat().getId();
            String msgText = update.getMessage().getText();

            if (msgText.equals("/start")) {
                strInfo = "Привет! \nЭтот бот показывает актуальный курс валют для доллара, евро и российского рубля. Также он может конвертировать валюту.\n\n" +
                        "/rate - показывает актуальный курс для доллара, евро и российского рубля. По умолчанию курс Belarusbank.\n\n" +
                        "/default - позволяет установить/сменить валюту по умолчанию.\nНапример, /default usd\n\n" +
                        "/sadcat - покажет информацию о грустном котике на главном фото бота :с\n\n" +
                        "\tКурс Беларусбанк/БПС-Сбербанк/Приорбанк - показывает курс валюты в этом банке.\n\n" +
                        "\tДля конвертации валют введите значения в формате \"ЗНАЧЕНИЕ ИЗ_ВАЛЮТЫ В_ВАЛЮТУ\". Например \"4 usd byn\" переведет 4 доллара в белорусские рубли.\n\n" +
                        "\tОбозначения для валют:\n    Белорусский рубль - BYN\n    Доллар США - USD\n    Евро - EUR\n    Российский рубль - RUB\n";
                msgSend(update.getMessage(), strInfo);

            } else if (msgText.contains("/default")) { //ставим дефолтную валюту, в которую будем конвертировать
                String str = msgText.trim().split("/default ")[1];
                ConvertationInfo.addUsersDefault(userId, str, usersDefault);
                strInfo = "Теперь ваша валюта по умолчанию - " + str;
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
                if (msgText.toLowerCase().contains("бпс") ||
                        msgText.toLowerCase().contains("бпс-банк") ||
                        msgText.toLowerCase().contains("сбербанк") ||
                        msgText.toLowerCase().contains("сбербанка") ||
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

            } else if (msgText.equals("/sadcat")) {
                msgSend(update.getMessage(), "Котик грустит из-за того, что автор сего бота как всегда начинает всё делать в последний момент и ничего не успевает! :<");

            } else if(msgText.toLowerCase().contains("usd") || msgText.toLowerCase().contains("eur") || msgText.toLowerCase().contains("byn") || msgText.toLowerCase().contains("rub")) {
                if (ConvertationInfo.isDefaultSet(userId, usersDefault) && (msgText.split(" ").length==2)) { //ветка для запроса с установленным дефолтом
                    msgText = msgText + " " + usersDefault.get(userId);
                }

                System.out.println(msgText);
                flag = ConvertationInfo.getFlag(msgText);
                if (flag.split("_")[0].equals(flag.split("_")[2])) res  = value;
                value = ConvertationInfo.getValue(msgText);
                if (flag.split("_")[0].equals(flag.split("_")[2])) //если происходит конвертация из валюты в неё же (usd usd и так далее)
                    res  = value;
                else
                    res = Converter.convert(value, Main.banks.get("беларусбанк"), flag);
                msgSend(update.getMessage(), new String().format("%.2f ",res) + flag.split("_")[2]);
            }
        }
    }

    @Override
    public String getBotUsername() {
        return "CurrCoBot";
    }

    @Override
    public String getBotToken() {
        return "1169714346:AAGRBKGa5EKNiRRvw1Qo7P6gkzn11t-aG34";
    }

    private void msgSend(Message message, String text) {
        SendMessage sendMsg = new SendMessage();
        sendMsg.setChatId(message.getChatId());
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