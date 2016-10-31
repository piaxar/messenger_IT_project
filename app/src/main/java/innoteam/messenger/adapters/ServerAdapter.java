package innoteam.messenger.adapters;

import java.util.ArrayList;
import java.util.Date;

import innoteam.messenger.interfaces.SereverRequests;
import innoteam.messenger.models.Chat;
import innoteam.messenger.models.Message;

/**
 * Created by ivan on 25.10.16.
 */

public class ServerAdapter implements SereverRequests{

    public static final ServerAdapter INSTANCE = new ServerAdapter();

    @Override
    public ArrayList<Chat> getAllChats() {
        //TODO replace with server
        ArrayList<Chat> contacts = new ArrayList<>();

        for (int i = 0; i <= 20; i++) {
            contacts.add(new Chat("Chat name", i, i));
        }

        return contacts;
    }

    @Override
    public ArrayList<Message> getChatMessages(int chatID) {
        ArrayList<Message> messages = new ArrayList<>();
        for (int i = 0; i < 15; i++){
            messages.add(getMessageById(i));
        }
        return messages;
    }

    @Override
    public Message getMessageById(int messageId) {
        // TODO also use encoder class
        Message newMessage = new Message(
                messageId, "Sender Name", new Date(2016, 4, 12, 12, 0), "Message with id "+ messageId);
        return newMessage;
    }


}
