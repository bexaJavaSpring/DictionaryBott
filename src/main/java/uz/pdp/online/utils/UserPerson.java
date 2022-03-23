package uz.pdp.online.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.pdp.online.utils.enums.UserStatus;
import uz.pdp.online.utils.enums.UserStep;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserPerson {
    private String firstName;
    private String chatId;
    private String phoneNumber;
    private int round;
    private String word;
    private String language;
    private UserStatus userStatus;
    private boolean isRegister = false;
    private int confirmation;
    private String photoPath;
    private String photoCaption;
    private boolean hasAdvertisement = false;
    private String PhotoId;
    private List<UserMessage> userMessages = new ArrayList<>();
    private boolean isWritingMessage = false;
    private UserStep userStep;
}
