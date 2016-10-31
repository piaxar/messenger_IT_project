package innoteam.messenger.interfaces;

import android.content.Intent;

import java.util.ArrayList;

import innoteam.messenger.models.Chat;
import innoteam.messenger.models.Message;

/**
 * Created by ivan on 25.10.16.
 */

public interface SereverRequests {
    public ArrayList<Chat> getAllChats();
    public ArrayList<Message> getChatMessages(int chatID);
    public ArrayList<Integer> getChatIDs();
    public Message getMessageById(int messageId);
}
