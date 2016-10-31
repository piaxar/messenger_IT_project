package innoteam.messenger.interfaces;

import java.util.ArrayList;

import innoteam.messenger.models.Chat;

/**
 * Created by ivan on 25.10.16.
 */

public interface SereverRequests {
    public ArrayList<Chat> getAllChats();
    public ArrayList<Chat> getChatMessages(int chatID);
}
