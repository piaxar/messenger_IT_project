package innoteam.messenger;

import java.util.ArrayList;
import java.util.Collection;

import innoteam.messenger.adapters.ServerAdapter;
import innoteam.messenger.models.Chat;
import innoteam.messenger.models.Message;

/**
 * Created by ivan on 06.11.16.
 */

public class DataProvider {
    private static final String TAG = "DataProvider";
    public static DataProvider INSTANCE = null;
    private ArrayList<Chat> chats;

    public static synchronized DataProvider getInstance() {
        if(INSTANCE == null)
        {
            INSTANCE = new DataProvider();
        }
        return INSTANCE;
    }

    private DataProvider(){
        chats = new ArrayList<>();
        chats = ServerAdapter.INSTANCE.getAllChats();
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    public void initDataset() {
        chats.clear();
        chats.addAll(ServerAdapter.INSTANCE.getAllChats());
    }

    public Chat getChat(int chatId) {
        for (Chat chat: chats){
            if (chat.getChatId() == chatId){
                return chat;
            }
        }
        return null;
    }

    public Collection<? extends Message> getMessagesFrom(int chatId) {
        return getChat(chatId).getAllMessages();
    }

}
