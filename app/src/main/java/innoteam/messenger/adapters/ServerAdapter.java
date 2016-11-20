package innoteam.messenger.adapters;

import android.os.StrictMode;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import innoteam.messenger.configs.Config;
import innoteam.messenger.interfaces.SereverRequests;
import innoteam.messenger.models.Chat;
import innoteam.messenger.models.CompressedContent;
import innoteam.messenger.models.Message;
import innoteam.messenger.models.User;

/**
 * Created by ivan on 25.10.16.
 */

public class  ServerAdapter implements SereverRequests{
    private final String TAG = "ServerAdapter";
    private final int MIN_COMPRESSED_STRING_LENGTH = 5;

    public static final ServerAdapter INSTANCE = new ServerAdapter();

    @Override
    public ArrayList<Chat> getAllChats() {
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
                        if (jsonObj.isNull("lastMessageId")) {
                            Log.d(TAG, "Chat added: chat name: " + name + " chatId: " + chatId);
                            contacts.add(new Chat(name, chatId));

                        }
                        else {
                            int lastMessageId = jsonObj.getInt("lastMessageId");
                            Log.d(TAG, "Chat added: chat name: " + name + " chatId: " + chatId + " lastMessageId: " + lastMessageId);
                            contacts.add(new Chat(name, lastMessageId, chatId));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
       // System.out.println(ids);
        return ids;
    }

    @Override
    public ArrayList<Integer> getMessagesIdsByChatId(int chatID) {
        ArrayList<Integer> messegesIDs = new ArrayList<Integer>();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.GET_MY_CHATS_MESSAGES_IDS + String.valueOf(chatID));
        try {
            p.setHeader("Authorization", Config.TOKEN);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            System.out.println(response.getStatusLine().getStatusCode());
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
                    String json = reader.readLine();
                    //System.out.println(json);
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

    public ArrayList<Message> getChatMessagesById(int chatId){
        Log.d(TAG, "Start getChatMessagesById");
        ArrayList<Message> messages = new ArrayList<Message>();
        ArrayList<Integer> arr = getMessagesIdsByChatId(chatId);
        for (Integer messageId: arr){
            Log.d(TAG, "message ID: " + messageId);
            Message message = getMessageById(messageId);
            messages.add(message);
            Log.d(TAG, "in getChatMessagesById, get message with content: "+ message.getContent());
        }
        Log.d(TAG, "Finish getChatMessagesById");
        return messages;
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

        return messageIds;
    }

    public ArrayList<Integer> getChatMessagesIdsByIdsFromCurentId(int chatId, int startMessageId) {
        ArrayList<Integer> messageIds = new ArrayList<>();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.GET_CHAT_MESSAGES + Integer.toString(chatId) + "/" + Integer.toString(startMessageId));
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
                        for (int i = 0; i <allMesseges.length(); i++){
                            messageIds.add((int) allMesseges.get(i));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                        int fromUserId = jsonObj.getInt("fromUserId");
                        String fromUSerName = jsonObj.getString("fromUserName");
                        long sendTime = jsonObj.getLong("sentTime");
                        messeges.add(new Message(messageId, fromUSerName, new Date(sendTime * 1000),getMessageContentById(messageId)));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return messeges;
    }

    @Override
    public CompressedContent getMessageContentById(int messageId) {
        CompressedContent comCont = new CompressedContent();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.GET_MESSAGE_CONTENT + String.valueOf(messageId));
        try {
            p.setHeader("Authorization", Config.TOKEN);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            if (response != null) {
                Log.i(TAG, "getMessageContent By Id: Response code:" + String.valueOf(response.getStatusLine().getStatusCode()));
                if (response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES) {
                    byte[] content = EntityUtils.toByteArray(response.getEntity());
                    if (content[0] == 1){
                        byte[] compressedMessage = Arrays.copyOfRange(content, 1, content.length);
                        Inflater decompresser = new Inflater();
                        decompresser.setInput(compressedMessage);

                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressedMessage.length);
                        byte[] result = new byte[1024];
                        while (!decompresser.finished()) {
                            int count = decompresser.inflate(result);
                            outputStream.write(result, 0, count);
                        }
                        outputStream.close();
                        byte[] output = outputStream.toByteArray();
                        decompresser.end();

                        Log.d(TAG, "Byte array has been decompressed!");
                        Log.d(TAG, "Size of original got array: " + compressedMessage.length);
                        Log.d(TAG, "Size of uncompresed array is: " + output.length);

                        comCont.content = new String(output, 0, output.length, "UTF-8");
                        comCont.compressed = content.length;
                        comCont.uncompressed = output.length;
                    } else {
                        byte[] compressedMessage = Arrays.copyOfRange(content, 1, content.length);
                        comCont.content = new String(compressedMessage, "UTF-8");
                        comCont.compressed = compressedMessage.length;
                        comCont.uncompressed = compressedMessage.length;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return comCont;
    }

    @Override
    public Message getMessageById(int messegeId) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.GET_MESSAGE_INFO + String.valueOf(messegeId));
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
                    newMessege = new Message(sentmessegeId, fromUserName, new Date(sendTime), getMessageContentById(messegeId));
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return newMessege;
    }

    public void sendMessage(int chatId, String message) {
        /* Encode messege */

        byte[] getByte = message.getBytes();
        Deflater compresser = new Deflater();
        compresser.setLevel(Deflater.BEST_COMPRESSION);
        compresser.setInput(getByte);

        ByteArrayOutputStream bos = new ByteArrayOutputStream(getByte.length);

        compresser.finish();

        byte[] buffer = new byte[1024];

        while (!compresser.finished()) {
            int bytesCompressed = compresser.deflate(buffer);
            bos.write(buffer, 0, bytesCompressed);
        }
        try {
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] compressedArray = bos.toByteArray();
        compresser.end();

        Log.d(TAG, "Byte array has been compressed!");
        Log.d(TAG, "Size of original array is:" + getByte.length);
        Log.d(TAG, "Size of compressed array is:" + compressedArray.length);

        if (compressedArray.length > message.length()){
            byte[] compressedByte = new byte[1];
            byte[] messageByteArray = message.getBytes();
            compressedByte[0] = 0;
            byte[] sendData = new byte[message.length() + 1];
            System.arraycopy(compressedByte, 0, sendData, 0, compressedByte.length);
            System.arraycopy(messageByteArray, 0, sendData, compressedByte.length, messageByteArray.length);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost p = new HttpPost(Config.SEND_MESSAGE + String.valueOf(chatId));
            try {
                p.setEntity(new ByteArrayEntity(sendData));
                p.addHeader("Authorization", Config.TOKEN);
                p.addHeader("Content-Type", "application/octet-stream");
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpResponse response = httpClient.execute(p);
                if (!(response != null || response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES)) {
                    Log.i(TAG, "Message not sent");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            byte[] compressedByte = new byte[1];
            compressedByte[0] = 1;
            byte[] sendData = new byte[compressedArray.length + 1];
            System.arraycopy(compressedByte, 0, sendData, 0, compressedByte.length);
            System.arraycopy(compressedArray, 0, sendData, compressedByte.length, compressedArray.length);
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost p = new HttpPost(Config.SEND_MESSAGE + String.valueOf(chatId));
            try {
                p.setEntity(new ByteArrayEntity(sendData));
                p.addHeader("Authorization", Config.TOKEN);
                p.addHeader("Content-Type", "application/octet-stream");
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpResponse response = httpClient.execute(p);
                if (!(response != null || response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES)) {
                    Log.i(TAG, "Message not sent");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

       /* if (message.length() > MIN_COMPRESSED_STRING_LENGTH) {
            byte[] getByte = message.getBytes();

            Deflater compresser = new Deflater();
            compresser.setLevel(Deflater.BEST_COMPRESSION);
            compresser.setInput(getByte);

            ByteArrayOutputStream bos = new ByteArrayOutputStream(getByte.length);

            compresser.finish();

            byte[] buffer = new byte[1024];

            while (!compresser.finished()) {
                int bytesCompressed = compresser.deflate(buffer);
                bos.write(buffer, 0, bytesCompressed);
            }
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            byte[] compressedArray = bos.toByteArray();
            compresser.end();
            Log.d(TAG, "Byte array has been compressed!");
            Log.d(TAG, "Size of original array is:" + getByte.length); //from this take values for Munir
            Log.d(TAG, "Size of compressed array is:" + compressedArray.length);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost p = new HttpPost(Config.SEND_MESSAGE + String.valueOf(chatId));
           // InputStream stream = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));
            try {
                // p.setEntity(new InputStreamEntity(stream, message.length()));
                p.setEntity(new ByteArrayEntity(compressedArray));
                p.addHeader("Authorization", Config.TOKEN);
                p.addHeader("Content-Type", "application/octet-stream");
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpResponse response = httpClient.execute(p);
                if (!(response != null || response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES)) {
                    Log.i(TAG, "Message not sent");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "Byte array has been compressed!");
            Log.d(TAG, "Size of original array is:" + message.length());
            Log.d(TAG, "Size of compressed array is:" + message.length());
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost p = new HttpPost(Config.SEND_MESSAGE + String.valueOf(chatId));
            InputStream stream = new ByteArrayInputStream(message.getBytes(StandardCharsets.UTF_8));
            try {
                p.setEntity(new InputStreamEntity(stream, message.length()));
                //p.setEntity(new ByteArrayEntity(compressedArray));
                p.addHeader("Authorization", Config.TOKEN);
                p.addHeader("Content-Type", "application/octet-stream");
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
                HttpResponse response = httpClient.execute(p);
                if (!(response != null || response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES)) {
                    Log.i(TAG, "Message not sent");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }*/
    }

    public void createNewPrivateChat(String userLogin) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.CREATE_NEW_PRIVATE_CHAT + userLogin);
        try {
            p.setHeader("Authorization", Config.TOKEN);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            if (!(response != null || response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES)){
                Log.i(TAG, "Chat not created");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<User> getAllUsers(int chatId){
        ArrayList<User> users = new ArrayList<User>();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.GET_CHAT_USERS + String.valueOf(chatId));
        try {
            p.setHeader("Authorization", Config.TOKEN);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            if (response != null && response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES) {
                String json = EntityUtils.toString(response.getEntity(), "UTF-8");
                JSONArray jsonArray = new JSONArray(json);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject data = jsonArray.getJSONObject(i);
                    int userId = data.getInt("id");
                    String userLogin = data.getString("login");
                    String fullName = data.getString("fullName");
                    Log.i(TAG, "id: " + String.valueOf(userId) + " " + "userLogin: " + userLogin + " " + "fullname: " + fullName + "\n");
                    users.add(new User(userId, userLogin, fullName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}
