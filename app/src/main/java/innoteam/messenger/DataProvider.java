package innoteam.messenger;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import innoteam.messenger.adapters.ChatsAdapter;
import innoteam.messenger.adapters.MessagesAdapter;
import innoteam.messenger.adapters.ServerAdapter;
import innoteam.messenger.fragments.ProgressBarHandler;
import innoteam.messenger.interfaces.MainActivityInterconnectionListener;
import innoteam.messenger.listeners.RecyclerItemClickListener;
import innoteam.messenger.models.Chat;
import innoteam.messenger.models.Message;

/**
 * Created by ivan on 06.11.16.
 */

public class DataProvider implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, RecyclerItemClickListener.OnItemClickListener {
    private static final String TAG = "DataProvider";
    private static DataProvider INSTANCE = null;

    private ArrayList<Chat> chats;
    private ArrayList<Message> messages;
    private int selectedChatPosition;
    private boolean chatAvailable = false;

    // Chats
    private RecyclerView rvChats;
    private ChatsAdapter chatsAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBarHandler progressBarChatsHandler;
    private MainActivityInterconnectionListener mListener;

    // Messages
    private MessagesAdapter msgAdapter;
    private RecyclerView rvMessages;
    private ProgressBarHandler progressBarMessagesHandler;
    private TextView tvChatName;
    private EditText etMessageContent;

    public void setChatsFragment(RecyclerView rv, SwipeRefreshLayout srl,
                                 FloatingActionButton addChatBtn, Context context,
                                 ProgressBarHandler progressBarHandler,
                                 MainActivityInterconnectionListener mListener){
        this.rvChats = rv;
        this.swipeRefreshLayout = srl;
        this.progressBarChatsHandler = progressBarHandler;
        this.mListener = mListener;

        chatsAdapter = new ChatsAdapter(chats);
        rvChats.setAdapter(chatsAdapter);

        rvChats.addOnItemTouchListener(new RecyclerItemClickListener(context, rvChats, this));

        addChatBtn.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        initDataSet();
    }


    public void setMessagesFragment(RecyclerView rv, TextView tvChatName,
                                    Button btnSendMessage, EditText etMessageField, ProgressBarHandler mProgressBar){
        this.rvMessages = rv;
        this.progressBarMessagesHandler = mProgressBar;
        this.tvChatName = tvChatName;
        this.etMessageContent = etMessageField;

        messages = new ArrayList<>();
        msgAdapter = new MessagesAdapter(messages);
        btnSendMessage.setOnClickListener(this);

        rvMessages.setAdapter(msgAdapter);


        final Handler h = new Handler();
        final int delay = 1000;
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (chatAvailable) {
                    Log.d(TAG, "post delayed");
                    chats.get(selectedChatPosition).checkForUpdates();
                    if (chats.get(selectedChatPosition).updateFound) {
                        ConstantUpdater mt = new ConstantUpdater();
                        mt.execute();
                    }
                }
                h.postDelayed(this, delay);
            }
        }, delay);

    }

    public static synchronized DataProvider getInstance() {
        if(INSTANCE == null)
        {
            INSTANCE = new DataProvider();
        }
        return INSTANCE;
    }

    public void initDataSet() {
        ChatsInitiatorAsyncTask mt = new ChatsInitiatorAsyncTask();
        mt.execute();
    }

    private DataProvider(){
        chats = new ArrayList<>();
    }

    public void createNewChat(String s) {
        DialogAdder mt = new DialogAdder();
        mt.execute(s);
        ChatsRefresherAsyncTask mtt = new ChatsRefresherAsyncTask();
        mtt.execute();

    }

    @Override
    public void onRefresh() {
        chatAvailable = false;
        ChatsRefresherAsyncTask mt = new ChatsRefresherAsyncTask();
        mt.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSendMessage:
                String msg = etMessageContent.getText().toString();
                if (!msg.equals("")){
                    MessageSender mt = new MessageSender();
                    mt.execute(msg);
                }
                break;
            case R.id.etMessageBox:

        }
    }

    @Override
    public void onItemClick(View view, int position) {
                mListener.onChatSelected();
                chatAvailable = true;
                selectedChatPosition = position;
                MessagesGetter mt = new MessagesGetter();
                mt.execute();
    }

    @Override
    public void onItemLongClick(View view, int position) {

    }

    private class ChatsInitiatorAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarChatsHandler.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            chats.clear();
            chats.addAll(ServerAdapter.INSTANCE.getAllChats());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            chatsAdapter.notifyDataSetChanged();
            progressBarChatsHandler.hide();
        }
    }

    private class ChatsRefresherAsyncTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            chats.clear();
            chats.addAll(ServerAdapter.INSTANCE.getAllChats());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            chatsAdapter.notifyDataSetChanged();
            swipeRefreshLayout.setRefreshing(false);
        }

    }

    private class DialogAdder extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... params) {
            ServerAdapter.INSTANCE.createNewPrivateChat(params[0]);
            return null;
        }
    }

    private class MessagesGetter extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tvChatName.setText(chats.get(selectedChatPosition).getChatName());
            progressBarMessagesHandler.show();
            rvMessages.setVisibility(View.INVISIBLE);

        }

        @Override
        protected Void doInBackground(Void... params) {
            messages.clear();
            messages.addAll(chats.get(selectedChatPosition).getAllMessages());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            msgAdapter.notifyDataSetChanged();
            rvMessages.scrollToPosition(msgAdapter.getItemCount() - 1 );
            rvMessages.setVisibility(View.VISIBLE);
            progressBarMessagesHandler.hide();
        }
    }

    private class MessageSender extends AsyncTask<String, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {
            ServerAdapter.INSTANCE.sendMessage(chats.get(selectedChatPosition).getChatId(), params[0]);
            chats.get(selectedChatPosition).checkForUpdates();
            messages.clear();
            messages.addAll(chats.get(selectedChatPosition).getAllMessages());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            msgAdapter.notifyItemInserted(msgAdapter.getItemCount() - 1);
            rvMessages.scrollToPosition(msgAdapter.getItemCount() - 1);
            etMessageContent.setText("");
        }
    }

    private class ConstantUpdater extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            chats.get(selectedChatPosition).updateData();
            messages.clear();
            messages.addAll(chats.get(selectedChatPosition).getAllMessages());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (int i = 0; i < chats.get(selectedChatPosition).getUpdatedCount(); i++){
                msgAdapter.notifyItemInserted(msgAdapter.getItemCount() - 1);
                rvMessages.scrollToPosition(msgAdapter.getItemCount() - 1);
            }
        }
    }
}
