package innoteam.messenger.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.ToxicBakery.viewpager.transforms.CubeOutTransformer;

import innoteam.messenger.R;
import innoteam.messenger.adapters.MyPagerAdapter;
import innoteam.messenger.configs.Config;
import innoteam.messenger.fragments.ChatsFragment;
import innoteam.messenger.fragments.MessagesFragment;
import innoteam.messenger.interfaces.OnChatSelectedListener;
import innoteam.messenger.models.Chat;
import innoteam.messenger.network.NetworkHelper;

public class MainActivity extends AppCompatActivity implements OnChatSelectedListener{
    private static final int REQUEST_SIGNUP = 200;
    boolean IS_LOGED = false;
    MyPagerAdapter adapter;
    MessagesFragment messagesFragment;
    ChatsFragment chatsFragment;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        chatsFragment = new ChatsFragment();
        messagesFragment = new MessagesFragment();
        adapter = new MyPagerAdapter(getSupportFragmentManager(), chatsFragment, messagesFragment);
        viewPager.setAdapter(adapter);
        viewPager.setPageTransformer(true, new CubeOutTransformer());
        SharedPreferences sharedPreferences = getSharedPreferences(Config.SHARED_PREF_NAME, MODE_PRIVATE);

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
        //System.out.println(ServerAdapter.INSTANCE.getChatMessagesIdsById(2));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChatSelected(Chat chat) {

        messagesFragment.setChat(chat);
        // Switch viewPager to messages fragment
        viewPager.setCurrentItem(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_SIGNUP) {
            IS_LOGED = true;
            onResume();
        }
    }
}
