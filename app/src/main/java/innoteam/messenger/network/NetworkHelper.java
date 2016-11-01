package innoteam.messenger.network;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

import innoteam.messenger.configs.Config;

import static innoteam.messenger.configs.Config.GET_MY_CHATS;
import static innoteam.messenger.configs.Config.INCORRECT_INPUT_DATA;
import static innoteam.messenger.configs.Config.TOKEN;

/**
 * Created by olejeglejeg on 01.11.16.
 */

public class NetworkHelper extends Application{
    private static NetworkHelper instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static NetworkHelper getInstance() {
        return  instance;
    }

    public boolean isTokenFresh(){
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(GET_MY_CHATS);
        try {
            p.setHeader("Authorization", TOKEN);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            if (response.getStatusLine().getStatusCode() == INCORRECT_INPUT_DATA) {
                String hashPassword = new String(Hex.encodeHex(DigestUtils.md5(NetworkHelper.getInstance().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(Config.PASSWORD_SHARED_PREF, null))));
                String login = NetworkHelper.getInstance().getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(Config.USER_NAME_SHARED_PREF, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    return true;
    }
}
