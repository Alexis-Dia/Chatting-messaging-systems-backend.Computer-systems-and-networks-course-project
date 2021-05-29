package alexeyd.com.util;

import alexeyd.com.model.Message;
import alexeyd.com.model.User;

public class CryptoUtils {

    public static Message encryptWholeObject(int key, Message message) throws Exception {

        String topicWithoutEncryption = message.getTopic();
        String encodedTopic = SDESCypherUtils.encodePhrase(key, topicWithoutEncryption);
        message.setTopic(encodedTopic);

        String textWithoutEncryption = message.getText();
        String encodedText = SDESCypherUtils.encodePhrase(key, textWithoutEncryption);
        message.setText(encodedText);

        String authorWithoutEncryption = message.getAuthor();
        String encodedAuthor = SDESCypherUtils.encodePhrase(key, authorWithoutEncryption);
        message.setAuthor(encodedAuthor);

        String creationDateWithoutEncryption = message.getCreationDate().toString();
        String encodedCreationDate = SDESCypherUtils.encodePhrase(key, creationDateWithoutEncryption);
        message.setCreationDate(encodedCreationDate);

        return message;
    }

    public static Message decryptWholeObject(int key, Message message) throws Exception {

        String topicWithEncryption = message.getTopic();
        String decodedTopic = SDESCypherUtils.decodePhrase(key, topicWithEncryption);
        message.setTopic(decodedTopic);

        String textWithEncryption = message.getText();
        String decodedText = SDESCypherUtils.decodePhrase(key, textWithEncryption);
        message.setText(decodedText);

        String authorWithEncryption = message.getAuthor();
        String decodedAuthor = SDESCypherUtils.decodePhrase(key, authorWithEncryption);
        message.setAuthor(decodedAuthor);

        String creationDateWithEncryption = message.getCreationDate();
        String decodedCreationDate = SDESCypherUtils.decodePhrase(key, creationDateWithEncryption);
        message.setCreationDate(decodedCreationDate);

        return message;
    }

    public static User encryptWholeObject(int key, User user) throws Exception {

        String emailWithoutEncryption = user.getEmail();
        String encodedEmail = SDESCypherUtils.encodePhrase(key, emailWithoutEncryption);
        user.setEmail(encodedEmail);

        String userRoleWithoutEncryption = user.getUserRole();
        String encodedUserRole = SDESCypherUtils.encodePhrase(key, userRoleWithoutEncryption);
        user.setUserRole(encodedUserRole);

        String nameWithoutEncryption = user.getName();
        String encodedName = SDESCypherUtils.encodePhrase(key, nameWithoutEncryption);
        user.setName(encodedName);

        String passwordWithoutEncryption = user.getPassword();
        String encodedPassword = SDESCypherUtils.encodePhrase(key, passwordWithoutEncryption);
        user.setPassword(encodedPassword);

        return user;
    }

    public static User decryptWholeObject(int key, User user) throws Exception {

        String emailWithEncryption = user.getEmail();
        String decodedEmail = SDESCypherUtils.decodePhrase(key, emailWithEncryption);
        user.setEmail(decodedEmail);

        String userRoleWithEncryption = user.getUserRole();
        String decodedUserRoleWithEncryption = SDESCypherUtils.decodePhrase(key, userRoleWithEncryption);
        user.setUserRole(decodedUserRoleWithEncryption);

        String nameWithEncryption = user.getName();
        String decodedNameWithEncryption = SDESCypherUtils.decodePhrase(key, nameWithEncryption);
        user.setName(decodedNameWithEncryption);

        String passwordWithEncryption = user.getPassword();
        String decodedPAsswordWithEncryption = SDESCypherUtils.decodePhrase(key, passwordWithEncryption);
        user.setPassword(decodedPAsswordWithEncryption);

        return user;
    }
}
