package uz.pdp.online.utils.service;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uz.pdp.online.utils.ChatId;
import uz.pdp.online.utils.UserMessage;
import uz.pdp.online.utils.UserPerson;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

import static uz.pdp.online.Database.*;

public class GsonOperation {
    public static void chatIdsToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("src/main/resources/chatId.json")));

            String dataToJson = gson.toJson(chatIds);
            writer.write(dataToJson);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void chatIdsFromJson() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String path = "src/main/resources/chatId.json";
            BufferedReader reader = new BufferedReader(new FileReader(path));

            Type type = new TypeToken<List<ChatId>>() {
            }.getType();

            chatIds.clear();

            String dataFrom = reader.lines().collect(Collectors.joining());

            chatIds = gson.fromJson(dataFrom, type);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void userToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/userList.json"));

            String dataToJson = gson.toJson(userList);

            writer.write(dataToJson);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void userFromJson() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/userList.json"));

            Type type = new TypeToken<List<UserPerson>>() {
            }.getType();

            userList.clear();

            String dataFrom = reader.lines().collect(Collectors.joining());

            userList = gson.fromJson(dataFrom, type);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void wordHistoryToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File("src/main/resources/wordHistory.json")));

            String dataToJson = gson.toJson(wordHistories);

            writer.write(dataToJson);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void wordHistoryFromJson() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/wordHistory.json"));

            Type type = new TypeToken<List<UserPerson>>() {
            }.getType();

            wordHistories.clear();

            String dataFrom = reader.lines().collect(Collectors.joining());

            wordHistories = gson.fromJson(dataFrom, type);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void messageToJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("src/main/resources/message.json"));

            String dataToJson = gson.toJson(userMessages);

            writer.write(dataToJson);

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void messageFromJson() {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/message.json"));

            Type type = new TypeToken<List<UserMessage>>() {
            }.getType();

            userMessages.clear();

            String dataFrom = reader.lines().collect(Collectors.joining());

            userMessages = gson.fromJson(dataFrom, type);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }

}
