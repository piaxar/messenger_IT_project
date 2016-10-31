package innoteam.messenger.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import innoteam.messenger.fragments.MessagesFragment;
import innoteam.messenger.fragments.ChatsFragment;

/**
 * Created by ivan on 24.10.16.
 */

public class MyPagerAdapter extends FragmentPagerAdapter {

    MessagesFragment messagesFragment;
    ChatsFragment chatsFragment;

    public MyPagerAdapter(FragmentManager fm, ChatsFragment chatsFragment, MessagesFragment messagesFragment) {
        super(fm);
        this.messagesFragment = messagesFragment;
        this.chatsFragment = chatsFragment;

    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0){
            return chatsFragment;
        } else {
            return messagesFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
