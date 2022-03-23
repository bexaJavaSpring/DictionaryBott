package uz.pdp.online;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import static uz.pdp.online.utils.service.GsonOperation.chatIdsFromJson;
import static uz.pdp.online.utils.service.GsonOperation.userFromJson;

public class Main {
    public static void main(String[] args) {
        chatIdsFromJson();
        userFromJson();
        try {
            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new DictionaryBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        System.out.printf("Bot started");
    }
}
