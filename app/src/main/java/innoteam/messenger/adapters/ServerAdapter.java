package innoteam.messenger.adapters;

import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

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
        // /gethatinfo
        ArrayList<Integer> ids = getMyChatIDs();
        ArrayList<Chat> contacts = new ArrayList<Chat>();
        for (int i = 0; i < ids.size(); i++) {
            Log.i("Server Adapter/ getAllChats:", "i,m here");
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost p = new HttpPost(Config.GET_CHAT_INFO + Integer.toString(ids.get(i)));
            try{
                p.setHeader("Authorization", Config.TOKEN);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpResponse response = httpClient.execute(p);
                Log.i("Server Adapter/ getAllChats / Statuse code:", String.valueOf(response.getStatusLine().getStatusCode()));

                if (response != null) {
                    if (response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES) {
                        String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                        JSONObject jsonObj = new JSONObject(json);
                        String name = jsonObj.getString("name");
                        int chatId = jsonObj.getInt("chatId");
                        int lastMessageId = jsonObj.getInt("lastMessageId");
                        //System.out.println(name + " " + chatId + " " + lastMessageId);
                        contacts.add(new Chat(name, lastMessageId, chatId));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        /*
        ArrayList<Chat> contacts2 = new ArrayList<>();
        for (int i = 0; i <= 20; i++) {
            contacts2.add(new Chat("Chat name", i, i));
        }*/
        return contacts;
    }


    @Override
    public ArrayList<Integer> getMyChatIDs() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.GET_MY_CHATS);
        ArrayList<Integer> ids =  new ArrayList<Integer>();
        try {
            p.setHeader("Authorization", Config.TOKEN);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            if (response != null) {
                Log.i("Get My Chats IDs / Status code", String.valueOf(response.getStatusLine().getStatusCode()));
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
        //System.out.println(ids);
        return ids;
    }


    @Override
    public ArrayList<Integer> getChatMessegesIds(int chatID) {
        ArrayList<Integer> messegesIDs = new ArrayList<Integer>();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.GET_MY_CHATS_MESSEGES_IDS + String.valueOf(chatID));
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
                    JSONArray allMesseges = new JSONArray(tokener);
                    if (allMesseges != null) {
                        for (int i = 0; i < allMesseges.length(); i++) {
                            messegesIDs.add((int) allMesseges.get(i));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messegesIDs;
    }


    @Override
    public ArrayList<Integer> getChatMessagesIdsById(int chatId) {

        ArrayList<Integer> messageIds = new ArrayList<>();

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.GET_CHAT_MESSAGES + Integer.toString(chatId));
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
                    JSONArray allMesseges = new JSONArray(tokener);
                    //Log.i("Get Chat Messages by Id", "here");
                    if (allMesseges != null) {
                        for (int i = 0; i <allMesseges.length(); i++){
                            messageIds.add((int) allMesseges.get(i));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
 /*
        for (int i = 0; i < 15; i++){
            messages.add(getMessageById(i));
        }
    */
        return messageIds;
    }

    @Override
    public ArrayList<Message> getChatMesseges(int chatId) {
        ArrayList<Integer> messageIds = getChatMessagesIdsById(chatId);
        ArrayList<Message> messeges = new ArrayList<Message>();
        for (int i = 0; i < messageIds.size(); i++) {
            Log.i("Server Adapter/ getChatMesseges:", "i,m here");
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost p = new HttpPost(Config.GET_CHAT_INFO + Integer.toString(messageIds.get(i)));
            try{
                p.setHeader("Authorization", Config.TOKEN);
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpResponse response = httpClient.execute(p);
                Log.i("Server Adapter/ getchatMesseges / Statuse code:", String.valueOf(response.getStatusLine().getStatusCode()));

                if (response != null) {
                    if (response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES) {
                        String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                        JSONObject jsonObj = new JSONObject(json);
                        int messageId = jsonObj.getInt("messageId");
                        int sentChatId = jsonObj.getInt("chatId");
                        String fromUSerName = jsonObj.getString("fromUSerId");
                        long sendTime = jsonObj.getLong("sentTime");
                        //System.out.println(name + " " + chatId + " " + lastMessageId);
                        Message messege = getMessageContentById(messageId); // Incorrect, may be tranfer ids to getMessegeById and here create ArrayList<Messenge> ???
                        messeges.add(new Message(messageId, fromUSerName, new Date(sendTime * 1000), messege.getContent()));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return messeges;
    }

    @Override
    public Message getMessageContentById(int messageId) {
        // TODO also use encoder class

        Message newMessage = new Message(
                messageId, "Sender Name", new Date(2016, 4, 12, 12, 0), "Message with id "+ messageId+"" +
                "wery long streeng to watch how layout is well balanced. I think that it is enough.");
        return newMessage;
    }

    @Override
    public Message getMessegeInfo(int messegeId) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.GET_MESSEGE_INFO + String.valueOf(messegeId));
        Message newMessege = null;
        try {
            p.setHeader("Authorization", Config.TOKEN);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES) {
                    String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                    JSONObject jsonObj = new JSONObject(json);
                    int sentmessegeId = jsonObj.getInt("messageId");
                    int chatId = jsonObj.getInt("chatId");
                    int fromUserId = jsonObj.getInt("fromUserId");
                    String fromUserName = jsonObj.getString("fromUserName");
                    long sendTime = jsonObj.getLong("sentTime");
                    newMessege = new Message(sentmessegeId, fromUserName, new Date(sendTime * 1000), getMessageContentById(messegeId).toString()); ///!!!!
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
        return newMessege;
    }


}
