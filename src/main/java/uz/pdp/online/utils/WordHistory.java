package uz.pdp.online.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WordHistory {
    private String chatId;
    private String language;
    private String word;
}
