package innoteam.messenger.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import innoteam.messenger.configs.Config;

/**
 * Created by olejeglejeg on 01.11.16.
 */

public class NetworkHelper {

    public boolean isTokenFresh(Context context){
        boolean res = true;
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.GET_MY_CHATS);
        try {
            p.setHeader("Authorization", Config.TOKEN);
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            Log.i("Is Token Fresh / Status code", String.valueOf(response.getStatusLine().getStatusCode()));
            if (response.getStatusLine().getStatusCode() == Config.INCORRECT_INPUT_DATA || response.getStatusLine().getStatusCode() == Config.BAD_REQUEST) {
                res = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    return res;
    }

    public void tokenRefresher(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);

        String hashPassword = new String(Hex.encodeHex(DigestUtils.md5(context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(Config.PASSWORD_SHARED_PREF, null))));
        String login = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE).getString(Config.USER_NAME_SHARED_PREF, null);

        Map<String, String> params = new HashMap<>();
        params.put("Login", login);
        params.put("Hash", hashPassword);


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.LOGIN_REQUEST_URL);
        JSONObject object = new JSONObject(params);

        try {
            p.setEntity(new StringEntity(object.toString()));
            p.setHeader("Content-type", "application/json");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            if (response != null) {
                Log.i("Token refresher", "refresh");
                Log.i("Status code of TokenRefresh:", String.valueOf(response.getStatusLine().getStatusCode()));
                if (response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES) {
                    Header[] headers = response.getAllHeaders();
                    for (Header header: headers) {
                        if (header.getName().equals("Authorization")) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Config.TOKEN_SHARED_PREF, header.getValue());
                            Config.TOKEN = header.getValue();
                            editor.commit();
                        }
                    }
                }
                if (response.getStatusLine().getStatusCode() == Config.INCORRECT_INPUT_DATA) {
                    Toast.makeText(context, "Username or password is incorrect", Toast.LENGTH_LONG).show();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}