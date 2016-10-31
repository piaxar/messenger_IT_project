package innoteam.messenger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import innoteam.messenger.R;

/**
 * Created by ivan on 24.10.16.
 */

public class MessagesFragment extends Fragment{
    private TextView tvHeader;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        tvHeader = (TextView) view.findViewById(R.id.tvHeader);
        return view;
    }

    public void setChat(int chatId) {
        tvHeader.setText("This is chat " + chatId);
    }
}
