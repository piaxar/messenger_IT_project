package innoteam.messenger.models;

import java.util.ArrayList;

import innoteam.messenger.adapters.ServerAdapter;

/**
 * Created by ivan on 24.10.16.
 */

public class Chat {
    private int chatId;
    private String chatName;
    private int lastMessageId;
    private ArrayList<Message> messages;
    private Message lastMessage;


    public Chat(String chatName, int lastMessageId, int chatId) {
        this.chatName = chatName;
        this.lastMessageId = lastMessageId;
        this.chatId = chatId;
        lastMessage = getMessageById(lastMessageId);
    }

    public ArrayList<Message> getAllMessages() {
        //messages =  ServerAdapter.INSTANCE.getChatMessagesIdsById(chatId);
        return messages;
    }

    public String getLastMessageContent(){
        return lastMessage.getContent();
    }

    public String getChatName() {
        return chatName;
    }

    public int getChatId() {
        return chatId;
    }

    private Message getMessageById(int messageId){
        return ServerAdapter.INSTANCE.getMessageContentById(messageId);
    }
}
