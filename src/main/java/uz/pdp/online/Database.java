package uz.pdp.online;

import uz.pdp.online.utils.ChatId;
import uz.pdp.online.utils.UserMessage;
import uz.pdp.online.utils.UserPerson;
import uz.pdp.online.utils.WordHistory;

import java.util.ArrayList;
import java.util.List;

public class Database {
    public static List<UserPerson> userList = new ArrayList<>();
    public static List<ChatId> chatIds = new ArrayList<>();
    public static List<WordHistory> wordHistories = new ArrayList<>();
    public static List<UserMessage> userMessages = new ArrayList<>();
}
