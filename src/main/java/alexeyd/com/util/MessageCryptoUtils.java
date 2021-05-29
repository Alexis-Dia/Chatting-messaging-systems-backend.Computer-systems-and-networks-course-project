package alexeyd.com.util;

import alexeyd.com.model.Message;

public class MessageCryptoUtils {

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

        return message;
    }
}
