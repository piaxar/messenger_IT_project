package innoteam.messenger.adapters;

import java.util.ArrayList;

import innoteam.messenger.interfaces.SereverRequests;
import innoteam.messenger.models.Chat;

/**
 * Created by ivan on 25.10.16.
 */

public class ServerAdapter implements SereverRequests{

    public static final ServerAdapter INSTANCE = new ServerAdapter();

    @Override
    public ArrayList<Chat> getAllChats() {
        //TODO replace with server
        return Chat.createContactsList(10);
    }

    @Override
    public ArrayList<Chat> getChatMessages(int chatID) {
        return null;
    }
}
