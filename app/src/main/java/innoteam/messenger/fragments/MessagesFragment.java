package innoteam.messenger.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import innoteam.messenger.R;
import innoteam.messenger.adapters.MessagesAdapter;
import innoteam.messenger.adapters.ServerAdapter;
import innoteam.messenger.listeners.RecyclerItemClickListener;
import innoteam.messenger.models.Chat;
import innoteam.messenger.models.Message;

/**
 * Created by ivan on 24.10.16.
 */

public class MessagesFragment extends Fragment{
    private final String TAG = "Messages fragment";
    private ArrayList<Message> messages;
    private MessagesAdapter adapter;
    private RecyclerView rvMessages;
    private LinearLayoutManager mLayoutManager;
    private TextView chatName;
    private Button sendBtn;
    private EditText messageField;
    private Chat chat;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        messages = new ArrayList<>();

        adapter = new MessagesAdapter(messages);
        mLayoutManager = new LinearLayoutManager(getActivity());
        chatName = (TextView) view.findViewById(R.id.tvChatName);
        sendBtn = (Button) view.findViewById(R.id.btnSendMessage);
        messageField = (EditText) view.findViewById(R.id.etMessageBox);

        rvMessages = (RecyclerView) view.findViewById(R.id.rvMessages);
        rvMessages.setAdapter(adapter);
        rvMessages.setLayoutManager(mLayoutManager);
        rvMessages.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvMessages, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                //
                Log.d("MessageFragment", "On item "+ position + " clicked");
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvMessages.smoothScrollToPosition(adapter.getItemCount());
                String msg = messageField.getText().toString();
                if (!msg.equals("")){
                    ServerAdapter.INSTANCE.sendMessage(chat.getChatId(), msg);
                    messageField.setText("");
                    Log.d(TAG, "message send");
                    messages.clear();
                    messages.addAll(chat.getAllMessages());
                    adapter.notifyDataSetChanged();
                    rvMessages.smoothScrollToPosition(adapter.getItemCount());

                }

            }
        });

        return view;
    }

    // TODO send messages

    public void setChat(Chat chats) {
        this.chat = chats;
        chatName.setText(chat.getChatName());
        messages.clear();
        messages.addAll(chat.getAllMessages());
        Log.d(TAG, "in set chat");
        adapter.notifyDataSetChanged();
        rvMessages.smoothScrollToPosition(adapter.getItemCount());
        Log.d(TAG,"" + adapter.getItemCount());
    }
}
