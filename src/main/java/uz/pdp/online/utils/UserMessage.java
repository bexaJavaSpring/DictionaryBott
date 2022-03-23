package uz.pdp.online.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMessage {
    private String body;
    private String fromChatId;
    private String toChatId = "958536406";
}
