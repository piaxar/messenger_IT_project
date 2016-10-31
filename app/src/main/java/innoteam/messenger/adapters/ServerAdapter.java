package innoteam.messenger.adapters;

import android.os.StrictMode;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import innoteam.messenger.configs.Config;
import innoteam.messenger.interfaces.SereverRequests;
import innoteam.messenger.models.Chat;
import innoteam.messenger.models.Message;

/**
 * Created by ivan on 25.10.16.
 */

public class  ServerAdapter implements SereverRequests{

    public static final ServerAdapter INSTANCE = new ServerAdapter();

    @Override
    public ArrayList<Chat> getAllChats() {
        ArrayList<Integer> ids = getChatIDs();
        ArrayList<Chat> contacts = new ArrayList<Chat>();
        for (int i = 0; i < ids.size(); i++) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost p = new HttpPost(Config.GET_CHAT_INFO + Integer.toString(ids.get(i)));
            try{
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpResponse response = httpClient.execute(p);
                if (response != null) {
                    if (response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                        String json = reader.readLine();
                        JSONTokener tokener = new JSONTokener(json);
                        JSONArray allChats = new JSONArray(tokener);

                        int chatId = allChats.getInt(Integer.parseInt("ChatId"));
                        String name = allChats.getString(Integer.parseInt("Name"));
                        int lastMessageId = allChats.getInt(Integer.parseInt("LastMessageId"));
                        Chat chat = new Chat(name, lastMessageId, chatId);
                        contacts.add(chat);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
       /* ArrayList<Chat> contacts = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            contacts.add(new Chat("Chat name", i, i));
        }*/
        return contacts;
    }



    @Override
    public ArrayList<Integer> getChatIDs() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.GET_MY_CHATS);
        ArrayList<Integer> ids =  new ArrayList<Integer>();
        try {
            p.setHeader("Authorization", Config.TOKEN);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    String json = reader.readLine();
                    JSONTokener tokener = new JSONTokener(json);
                    JSONArray allChats = new JSONArray(tokener);
                    if (allChats != null) {
                        for (int i = 0; i <allChats.length(); i++){
                            ids.add((int) allChats.get(i));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ids;
    }

    @Override
    public ArrayList<Message> getChatMessages(int chatID) {
        ArrayList<Message> messages = new ArrayList<>();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.GET_CHAT_MESSAGES + Integer.toString(chatID));
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    String json = reader.readLine();
                    JSONTokener tokener = new JSONTokener(json);
                    JSONArray allMesseges = new JSONArray(tokener);
                    //TODO: Convert JSONArray to ArrayList<Chat> and return it
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
