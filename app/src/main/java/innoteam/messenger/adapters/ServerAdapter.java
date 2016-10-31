package innoteam.messenger.adapters;

import android.os.StrictMode;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;

import innoteam.messenger.interfaces.SereverRequests;
import innoteam.messenger.models.Chat;

/**
 * Created by ivan on 25.10.16.
 */

public class  ServerAdapter implements SereverRequests{

    public static final ServerAdapter INSTANCE = new ServerAdapter();

    @Override
    public ArrayList<Chat> getAllChats() {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.GET_MY_CHATS);

        try {
            p.setHeader("Authorization", Config.TOKEN);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            if (response != null) {
                System.out.println(response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES) {
                    Header[] headers = response.getAllHeaders();
                    String responseBody = EntityUtils.toString(response.getEntity());

                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Chat.createContactsList(10);
    }



    @Override
    public ArrayList<Chat> getChatMessages(int chatID) {
        return null;
    }


}
