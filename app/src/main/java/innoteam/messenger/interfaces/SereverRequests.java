package innoteam.messenger.interfaces;

import java.util.ArrayList;

import innoteam.messenger.models.Chat;
import innoteam.messenger.models.Message;

/**
 * Created by ivan on 25.10.16.
 */

public interface SereverRequests {
    public ArrayList<Integer> getMyChatIDs();

    public ArrayList<Chat> getAllChats();

    public ArrayList<Integer> getChatMessegesIds(int ChatID);

    public ArrayList<Integer> getChatMessagesIdsById(int chatId);

    public ArrayList<Message> getChatMesseges(int chatId);

    public Message getMessageContentById(int messegeId);

    public Message getMessegeInfo(int messegeId);
}
