package innoteam.messenger.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ToxicBakery.viewpager.transforms.DepthPageTransformer;

import innoteam.messenger.DataProvider;
import innoteam.messenger.R;
import innoteam.messenger.adapters.MyPagerAdapter;
import innoteam.messenger.configs.Config;
import innoteam.messenger.fragments.ChatsFragment;
import innoteam.messenger.fragments.MessagesFragment;
import innoteam.messenger.interfaces.OnChatSelectedListener;
import innoteam.messenger.network.NetworkHelper;

public class MainActivity extends AppCompatActivity implements OnChatSelectedListener{
    private static final int REQUEST_SIGNUP = 200;
    boolean IS_LOGED = false;
    MyPagerAdapter adapter;
    MessagesFragment messagesFragment;
    ChatsFragment chatsFragment;
    ViewPager viewPager;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Inflate
        viewPager = (ViewPager) findViewById(R.id.viewPager);

        // objects initiation
        chatsFragment = new ChatsFragment();
        messagesFragment = new MessagesFragment();
        adapter = new MyPagerAdapter(getSupportFragmentManager(), chatsFragment, messagesFragment);
        sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);

        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new DepthPageTransformer());


        if (sharedPreferences.contains(Config.TOKEN_SHARED_PREF) == false) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            NetworkHelper networkHelper = new NetworkHelper();


            if (!networkHelper.isTokenFresh(this)){
                networkHelper.tokenRefresher(this);
            }
        }

        DataProvider.getInstance().initDataset();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onChatSelected(int chatId) {

        chatsFragment.getHeader().setText("Updating...");
        messagesFragment.setChat(chatId);
        chatsFragment.getHeader().setText("Messenger");
        viewPager.setCurrentItem(1);

        // Switch viewPager to messages fragment

    }

    @Override
    public void onLogOut() {
        sharedPreferences.edit().clear().commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_SIGNUP) {
            IS_LOGED = true;
            onResume();
        }
    }

    class Loader extends AsyncTask<Integer, Void, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            chatsFragment.getHeader().setText("Updating...");
        }

        @Override
        protected Void doInBackground(Integer... params) {
            for(Integer in: params){
                messagesFragment.setChat(in);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
}
