package innoteam.messenger.models;

import java.util.ArrayList;

import innoteam.messenger.adapters.ServerAdapter;

/**
 * Created by ivan on 24.10.16.
 */

public class Chat {
    private int firstMessageId;
    private int messagesDownloaded;
    private boolean allMessagesDownloaded;
    private int chatId;
    private String chatName;
    private int lastMessageId;
    private int updates;

    public boolean isDataUpdated;
    public boolean updateFound = false;

    private ArrayList<Message> messages;
    private ArrayList<Integer> messagesIds;

    private final int MESSAGES_OFFSET = 15;
    private final String TAG = "Chat model";


    public Chat(String chatName, int lastMessageId, int chatId) {
        this.chatName = chatName;
        this.lastMessageId = lastMessageId;
        this.chatId = chatId;

        messages = new ArrayList<>();
        messagesIds = new ArrayList<>();
        messagesIds.addAll(ServerAdapter.INSTANCE.getChatMessagesIdsById(chatId));

        if (messagesIds.size() > MESSAGES_OFFSET){
            firstMessageId = messagesIds.get(messagesIds.size() - MESSAGES_OFFSET + 1);
            messagesDownloaded = MESSAGES_OFFSET;
            allMessagesDownloaded = false;
        } else {
            firstMessageId = messagesIds.get(0);
            allMessagesDownloaded = true;
        }

        isDataUpdated = false;

    }

    public Chat(String chatName, int chatId){
        this.chatName = chatName;
        this.chatId = chatId;

        messages = new ArrayList<>();
        messagesIds = new ArrayList<>();
        allMessagesDownloaded = true;
        isDataUpdated = true;
    }

    public ArrayList<Message> getAllMessages() {
        updateData();
        return messages;
    }

    public String getLastMessageContent(){
        if (!messagesIds.isEmpty())
            return getMessageById(lastMessageId).getContent();
        else return "";
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

    public void updateData(){
       if (!isDataUpdated){
           messagesIds.clear();
           messagesIds.addAll(ServerAdapter.INSTANCE.getChatMessagesIdsById(chatId));
           updates = 0;
           if(messagesIds.size() == messages.size()){
               isDataUpdated = true;
           } else {
               for (int i = messages.size(); i < messagesIds.size(); i++){
                   messages.add(getMessageById(messagesIds.get(i)));
                   updates++;
               }
               lastMessageId = messagesIds.get(messagesIds.size()-1);
           }
           isDataUpdated = true;
           updateFound = false;
       }
    }

    public int getUpdatedCount(){
        int value = updates;
        updates = 0;
        return  value;
    }

    public void checkForUpdates(){
        if (ServerAdapter.INSTANCE.getChatMessagesIdsByIdsFromCurentId(chatId, lastMessageId).size() > 0){
            isDataUpdated = false;
            updateFound = true;
        }
    }
}
