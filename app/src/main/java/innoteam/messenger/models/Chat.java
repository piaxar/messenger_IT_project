package innoteam.messenger.models;

import android.util.Log;

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
    private final String TAG = "Chat model";


    public Chat(String chatName, int lastMessageId, int chatId) {
        this.chatName = chatName;
        this.lastMessageId = lastMessageId;
        this.chatId = chatId;
        lastMessage = getMessageById(lastMessageId);
    }

    public ArrayList<Message> getAllMessages() {
        Log.d(TAG, "Getting all messages to chat");
        messages =  ServerAdapter.INSTANCE.getChatMessagesById(chatId);
        for (Message msg: messages){
            Log.d(TAG, "Get message with content: "+ msg.getContent());
        }
        Log.d(TAG, "Finish getting");
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
        return ServerAdapter.INSTANCE.getMessageById(messageId);
    }
}
