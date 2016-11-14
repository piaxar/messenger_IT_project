package innoteam.messenger.interfaces;

import java.util.ArrayList;

import innoteam.messenger.models.Chat;
import innoteam.messenger.models.CompressedContent;
import innoteam.messenger.models.Message;

/**
 * Created by ivan on 25.10.16.
 */

public interface SereverRequests {
    public ArrayList<Integer> getMyChatIDs();

    public ArrayList<Chat> getAllChats();

    public ArrayList<Integer> getMessagesIdsByChatId(int ChatID);

    public ArrayList<Integer> getChatMessagesIdsById(int chatId);

    public ArrayList<Integer> getChatMessagesIdsByIdsFromCurentId(int chatId, int startMessageId);

    public ArrayList<Message> getChatMesseges(int chatId);

    public CompressedContent getMessageContentById(int messegeId);

    public Message getMessageById(int messegeId);

    public void sendMessage(int chatId, String message);

    public void createNewPrivateChat(String userLogin);
}
