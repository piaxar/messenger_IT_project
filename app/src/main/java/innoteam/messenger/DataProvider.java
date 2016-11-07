package innoteam.messenger;

import android.util.Log;

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
    private ArrayList<Chat> chats;

    public static final DataProvider INSTANCE = new DataProvider();

    public static DataProvider getInstance() {
        return INSTANCE;
    }

    private DataProvider(){
    }

    public ArrayList<Chat> getChats() {
        Log.d(TAG, "get chats");
        for (Chat chat: chats){
            Log.d(TAG, chat.getChatName());
        }

        return chats;
    }

    private void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    public void initDataset() {
        chats = new ArrayList<>();
        chats.addAll(ServerAdapter.INSTANCE.getAllChats());
        Log.d(TAG, "init dataset");
        for (Chat chat: chats){
            Log.d(TAG, chat.getChatName());
        }
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
