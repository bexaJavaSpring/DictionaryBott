package uz.pdp.online;

import com.twilio.Twilio;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.pdp.online.utils.ChatId;
import uz.pdp.online.utils.UserPerson;
import uz.pdp.online.utils.WordHistory;
import uz.pdp.online.utils.enums.UserStatus;
import uz.pdp.online.utils.service.BotSettings;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static uz.pdp.online.Database.*;
import static uz.pdp.online.utils.Translator.Translator.lookUp;
import static uz.pdp.online.utils.service.FileBuilder.*;
import static uz.pdp.online.utils.service.GsonOperation.chatIdsToJson;
import static uz.pdp.online.utils.service.GsonOperation.userToJson;

public class DictionaryBot extends TelegramLongPollingBot {
    private static final String TWILIO_ACCOUNT_SID = "AC6ae9783db3930616039185a910fb6844";
    private static final String TWILIO_AUTH_TOKEN = "69eff0873ace554eb2bb6150db403cc4";

    @Override
    public String getBotUsername() {
        return BotSettings.BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BotSettings.BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chatId = "";
        String firstName = "";
        if (update.hasMessage()) {
            chatId = update.getMessage().getChatId().toString();
            firstName = update.getMessage().getFrom().getFirstName();
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId().toString();
            firstName = update.getCallbackQuery().getMessage().getFrom().getFirstName();
        }

        UserPerson currentUser = new UserPerson();
        currentUser = getCurrentUser(chatId, firstName);
        System.out.println(currentUser.getFirstName());
        if (update.hasMessage()) {
            SendMessage sendMessage = new SendMessage();
            Message message = update.getMessage();
            sendMessage.setChatId(message.getChatId().toString());

            if (message.hasText()) {
                String text = message.getText();

                if (text.equalsIgnoreCase("Menu") || text.equalsIgnoreCase("/start")) {
                    currentUser.setRound(1);
                }
                if (text.equals("/start")) {
                    if (!currentUser.isRegister()) {
                        sendMessage.setText("Hi 🖐🖐 " + message.getFrom().getFirstName() + "\n" +
                                "to use this bot you need to register before\n☎please send your contact");
                        sendMessage.setReplyMarkup(getButton());
                    } else {
                        sendMessage.setText("Tap that button called Menu");
                        sendMessage.setReplyMarkup(getButton3());
                    }

                } else if (String.valueOf(currentUser.getConfirmation()).equals(text) || text.equalsIgnoreCase("Menu")) {

                    if (currentUser.getUserStatus().equals(UserStatus.USER)) {
                        sendMessage.setText("Welcome to User panel\n" +
                                "You are registrated");
                        currentUser.setRegister(true);
                        sendMessage.setReplyMarkup(getButton2());
                    } else if (currentUser.getUserStatus().equals(UserStatus.ADMIN)) {
                        currentUser.setRegister(true);
                        sendMessage.setText("Welcome to admin panel");
                        sendMessage.setReplyMarkup(getButton1());

                    }
                } else if (text.equalsIgnoreCase("Translate only word")) {
                    sendMessage.setText("💮Select Language💮");
                    sendMessage.setReplyMarkup(getButton4());
                } else if (text.equalsIgnoreCase("History")) {
                    boolean isAvailable = false;
                    for (WordHistory wordHistory : wordHistories) {
                        if (wordHistory.getChatId().equals(currentUser.getChatId())) {
                            isAvailable = true;
                        }
                    }
                    if (isAvailable) {
                        getCheck(currentUser);
                        SendDocument sendDocument = new SendDocument();
                        sendDocument.setChatId(chatId);
                        sendDocument.setCaption("Thank you for using our bot 😀😀😀😀");
                        sendDocument.setDocument(new InputFile(new File("src/main/resources/BotCheck.pdf")));
                        sendDocument.setReplyMarkup(getButton3());
                        try {
                            execute(sendDocument);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                    } else {
                        sendMessage.setText("You have not translated any word yet😁😁😁😁😁");
                    }
                } else if (text.equalsIgnoreCase("send message to admin")) {
                    Message message1=update.getMessage();
                    Integer  messageid=message1.getMessageId();
                    String text1=message1.getText();
                    User user=message1.getFrom();
                    System.out.println("firstname: "+user.getFirstName()+"username: "+user.getUserName()+"message:"+message1.getText());
                    SendMessage sendMessage1=new SendMessage();
                    sendMessage1.setText("Assalomu aleykum");
                    sendMessage1.setParseMode(ParseMode.MARKDOWN);
                    sendMessage1.setChatId(String.valueOf(message.getChatId()));
                    try {
                        execute(sendMessage1);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }

                    currentUser.setRound(2);
                    sendMessage.setReplyMarkup(getButton3());
                } else if (text.equalsIgnoreCase("All User")) {
                    getExcelFile();
                    SendDocument sendDocument = new SendDocument();
                    sendDocument.setChatId(chatId);
                    sendDocument.setCaption("Thank you for using our bot 😀😀😀😀");
                    sendDocument.setDocument(new InputFile(new File("src/main/resources/userList.xlsx")));
                    sendDocument.setReplyMarkup(getButton3());
                    try {
                        execute(sendDocument);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (text.equalsIgnoreCase("All Word")) {
                    getAllWordsForAdmin(currentUser);
                    SendDocument sendDocument = new SendDocument();
                    sendDocument.setChatId(chatId);
                    sendDocument.setCaption("Thank you for using our bot 😀😀😀😀");
                    sendDocument.setDocument(new InputFile(new File("src/main/resources/AllWord.pdf")));
                    sendDocument.setReplyMarkup(getButton3());
                    try {
                        execute(sendDocument);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (text.equalsIgnoreCase("reply message")) {
                   Message message1=update.getMessage();
                 Integer  messageid=message1.getMessageId();
                 String text1=message1.getText();
                 User user=message1.getFrom();
                    System.out.println("firstname: "+user.getFirstName()+"username: "+user.getUserName());


                    sendMessage.setText("This panel does not work yet 🧨✨🎉🎊🎃");
                    sendMessage.setReplyMarkup(getButton3());
                } else if (text.equalsIgnoreCase("en-ru")) {
                    currentUser.setLanguage(text);
                    sendMessage.setText("Input ENGLISH word to translate™");
                } else if (text.equalsIgnoreCase("ru-en")) {
                    currentUser.setLanguage(text);
                    sendMessage.setText("Input RUSSIAN word to translate™");
                } else if (text.equalsIgnoreCase("en-fr")) {
                    currentUser.setLanguage(text);
                    sendMessage.setText("Input ENGLISH word to translate");
                } else if (text.equalsIgnoreCase("fr-en")) {
                    currentUser.setLanguage(text);
                    sendMessage.setText("Input FRANCIAN word to translate");
                } else if (text.equalsIgnoreCase("en-tr")) {
                    currentUser.setLanguage(text);
                    sendMessage.setText("Input ENGLISH word to translate");
                } else if (text.equalsIgnoreCase("tr-en")) {
                    currentUser.setLanguage(text);
                    sendMessage.setText("Input Turkey word to translate");
                } else if (text.equalsIgnoreCase("en-cs")) {
                    currentUser.setLanguage(text);
                    sendMessage.setText("Input ENGLISH word to translate");
                } else if (text.equalsIgnoreCase("cs-en")) {
                    currentUser.setLanguage(text);
                    sendMessage.setText("Input Arabian word to translate");
                } else if (currentUser.getLanguage() != null) {
                    String s = lookUp(currentUser.getChatId(), text, currentUser.getLanguage());
                    if (!s.equals("")) {
                        sendMessage.setText(s);
                        currentUser.setLanguage(null);
                        currentUser.setWord(null);
                    } else
                        sendMessage.setText("✖This word not found");
                    sendMessage.setReplyMarkup(getButton3());
                } else if (currentUser.getRound() == 2) {
                    sendMessage.setText("Wrong option");
                } else {

                    sendMessage.setText("\uD83D\uDD31\uD83D\uDD31Wrong command\n" +
                            "Please try again");
                    sendMessage.setReplyMarkup(getButton3());
                }
            } else if (message.hasContact()) {
                String phoneNumber = message.getContact().getPhoneNumber();
                if (phoneNumber.equals("+998936207516") || phoneNumber.equals("998936207516")) {
                    currentUser.setUserStatus(UserStatus.ADMIN);
                } else {
                    currentUser.setUserStatus(UserStatus.USER);
                }
                int confirmation = (int) (Math.random() * 100000);
                String smscode = confirmation + "";
                Twilio.init(TWILIO_ACCOUNT_SID, TWILIO_AUTH_TOKEN);
                com.twilio.rest.api.v2010.account.Message message1 = com.twilio.rest.api.v2010.account.Message.creator(new com.twilio.type.PhoneNumber("+998936207516"), new com.twilio.type.PhoneNumber("+16672135275"), smscode).create();
                System.out.println(message1.getSid());


                currentUser.setConfirmation(Integer.parseInt(smscode));
                currentUser.setPhoneNumber(phoneNumber);
                currentUser.setRound(2);
                sendMessage.setText("You phone number is " + currentUser.getPhoneNumber() + "\n" +
                        "Please confirm this code: " + currentUser.getConfirmation());
                userToJson();
            }
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    public static ReplyKeyboardMarkup getButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardButton button = new KeyboardButton();
        button.setRequestContact(true);
        button.setText("📞Share Contact");
        row.add(button);
        rowList.add(row);
        replyKeyboardMarkup.setKeyboard(rowList);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getButton1() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row3 = new KeyboardRow();
        row.add("All User");
        row1.add("All Word");
        row3.add("reply message");
        rowList.add(row);
        rowList.add(row1);
        rowList.add(row3);
        replyKeyboardMarkup.setKeyboard(rowList);
        return replyKeyboardMarkup;

    }

    public static ReplyKeyboardMarkup getButton2() {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);

        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        KeyboardRow row1 = new KeyboardRow();
        KeyboardRow row2 = new KeyboardRow();
        row.add("Translate only word");
        row1.add("History");
        row2.add("Send message to admin");
        rowList.add(row);
        rowList.add(row1);
        rowList.add(row2);
        replyKeyboardMarkup.setKeyboard(rowList);
        return replyKeyboardMarkup;
    }

    public static ReplyKeyboardMarkup getButton3() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> rowList = new ArrayList<>();
        KeyboardRow row = new KeyboardRow();
        row.add("Menu");
        rowList.add(row);
        replyKeyboardMarkup.setKeyboard(rowList);
        return replyKeyboardMarkup;
    }


    public static ReplyKeyboardMarkup getButton4() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        List<KeyboardRow> rowList = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        KeyboardRow row1 = new KeyboardRow();
        row.add("en-ru");
        row.add("ru-en");
        row.add("en-fr");
        row.add("fr-en");
        row1.add("en-tr");
        row1.add("tr-en");
        row1.add("en-cs");
        row1.add("cs-en");
        rowList.add(row);
        rowList.add(row1);
        replyKeyboardMarkup.setKeyboard(rowList);

        return replyKeyboardMarkup;
    }

    public static UserPerson getCurrentUser(String chatId, String firstName) {
        UserPerson defaultUser = new UserPerson();
        boolean isFound = false;
        for (UserPerson userPerson : userList) {
            if (userPerson.getChatId().equals(chatId)) {
                return userPerson;
            }
        }
        if (!isFound) {
            defaultUser.setConfirmation(0);
            defaultUser.setChatId(chatId);
            defaultUser.setFirstName(firstName);
            defaultUser.setRegister(false);
            defaultUser.setUserStatus(null);
            defaultUser.setRound(1);
            defaultUser.setPhoneNumber(null);
            defaultUser.setWord(null);
            defaultUser.setLanguage(null);
            userList.add(defaultUser);
        }
        boolean isChatIdFound = false;
        for (ChatId id : chatIds) {
            if (id.getChatId().equals(chatId)) {
                isChatIdFound = true;
            }
        }

        if (!isChatIdFound) {
            chatIds.add(new ChatId(chatId));
        } else {
            System.out.println("Bu chat id allaqachon mavjud");
        }
        chatIdsToJson();
        userToJson();
        return defaultUser;
    }


}
