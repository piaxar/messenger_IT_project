package innoteam.messenger.models;

import java.util.ArrayList;

/**
 * Created by ivan on 24.10.16.
 */

public class Chat {
    private String chatName;
    private String lastMessage;
    private int ChatID;

    public Chat(String chatName, String lastMessage, int chatId) {
        this.chatName = chatName;
        this.lastMessage = lastMessage;
        this.ChatID = chatId;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public int getChatID() {
        return ChatID;
    }

    public void setChatID(int chatID) {
        ChatID = chatID;
    }

    public static ArrayList<Chat> createContactsList(int numContacts) {
        ArrayList<Chat> contacts = new ArrayList<Chat>();

        for (int i = 0; i <= numContacts; i++) {
            contacts.add(new Chat("Chat number "+i,"some text", i));
        }

        return contacts;
    }


}
