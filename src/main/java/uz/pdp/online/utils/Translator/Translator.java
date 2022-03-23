package uz.pdp.online.utils.Translator;

import com.google.gson.Gson;
import uz.pdp.online.utils.WordHistory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import static uz.pdp.online.Database.wordHistories;
import static uz.pdp.online.utils.service.GsonOperation.wordHistoryToJson;

public class Translator {
    public static final String apiKey = "dict.1.1.20211211T055537Z.ba9cd28e187c76d4.fb04b2db1b4da7046b73168e732f5ee39f307f62";
    public static String lookUp(String chatId, String word, String language) {
        String res = "";
        int i = 1;
        try {
            Gson gson = new Gson();
            URL url = new URL("https://dictionary.yandex.net/api/v1/dicservice.json/lookup?key=" + apiKey + "&lang=" + language + "&text=" + word);

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()))) {

                Head head = gson.fromJson(reader, Head.class);

                if (head.getDef().size() > 0) {
                    wordHistories.add(new WordHistory(chatId, language, word));
                    wordHistoryToJson();
                    for (Def def : head.getDef()) {
                        for (Tr tr : def.getTr()) {
                            System.out.println(tr.getText());
                            res += "" + (i++) + ") " + tr.getText() + "\n";
                        }
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
