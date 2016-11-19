package innoteam.messenger.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import innoteam.messenger.DataProvider;
import innoteam.messenger.R;

public class MessagesFragment extends Fragment {
    private final String TAG = "Messages fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        TextView chatName = (TextView) view.findViewById(R.id.tvChatName);
        Button sendBtn = (Button) view.findViewById(R.id.btnSendMessage);
        EditText messageField = (EditText) view.findViewById(R.id.etMessageBox);
        RecyclerView rvMessages = (RecyclerView) view.findViewById(R.id.rvMessages);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvMessages.setLayoutManager(mLayoutManager);
        ProgressBarHandler mProgressBar = new ProgressBarHandler(getContext());

        DataProvider.getInstance().setMessagesFragment(rvMessages, chatName,
                sendBtn, messageField, mProgressBar);
        return view;
    }

}
