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
    private final String TAG = "DataProvider";
    private static ArrayList<Chat> chats;

    public static final DataProvider INSTANCE = new DataProvider();

    public static DataProvider getInstance() {
        return INSTANCE;
    }

    private DataProvider(){
        chats = new ArrayList<>();
    }

    public ArrayList<Chat> getChats() {
        return chats;
    }

    private void setChats(ArrayList<Chat> chats) {
        this.chats = chats;
    }

    public void initDataset() {
        chats = new ArrayList<>();
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
