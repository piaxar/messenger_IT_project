package innoteam.messenger.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import innoteam.messenger.DataProvider;
import innoteam.messenger.R;
import innoteam.messenger.activities.MainActivity;
import innoteam.messenger.adapters.ChatsAdapter;
import innoteam.messenger.adapters.ServerAdapter;
import innoteam.messenger.behaviors.SimpleDividerItemDecoration;
import innoteam.messenger.interfaces.OnChatSelectedListener;
import innoteam.messenger.listeners.RecyclerItemClickListener;
import innoteam.messenger.models.Chat;

/**
 * Created by ivan on 24.10.16.
 */

public class ChatsFragment extends Fragment {
    private final String TAG = "Chats fragment";
    private ArrayList<Chat> chats;
    private ChatsAdapter adapter;
    private RecyclerView rvChats;
    private FloatingActionButton btnWriteMessage;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Context cnt;
    private FloatingActionButton addChatBtn;
    private TextView header;
    LinearLayoutManager mLayoutManager;
    OnChatSelectedListener mListener;
    SearchView searchView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        initDataset();

        header = (TextView) view.findViewById(R.id.tvHeader);
        searchView = (SearchView) view.findViewById(R.id.search_bar);
        rvChats = (RecyclerView) view.findViewById(R.id.rvChats);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        Button logOutBtn = (Button) view.findViewById(R.id.btnLogOut);
        addChatBtn = (FloatingActionButton) view.findViewById(R.id.btnAddChat);
        adapter = new ChatsAdapter(chats);

        cnt = getContext();
        mLayoutManager = new LinearLayoutManager(getActivity());

        rvChats.setAdapter(adapter);
        rvChats.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        rvChats.setLayoutManager(mLayoutManager);
        rvChats.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvChats, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d(TAG," Chatfragment at on click:"+ chats.get(position).getChatId());
                mListener.onChatSelected(chats.get(position).getChatId());
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Updater updater = new Updater();
                updater.execute();

            }
        });
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onLogOut();
                Intent mStartActivity = new Intent(cnt, MainActivity.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(cnt, mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager)cnt.getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);
            }
        });
        addChatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateChatDialog();
                Updater updater = new Updater();
                updater.execute();
            }
        });
        return view;
    }

    class Updater extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {
            updateDataset();
            adapter.notifyDataSetChanged();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            swipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(aVoid);
        }
    }

    public void showCreateChatDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(cnt);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("New chat creation");
        dialogBuilder.setMessage("Enter user login below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ServerAdapter.INSTANCE.createNewPrivateChat(edt.getText().toString());
                initDataset();
                adapter.notifyDataSetChanged();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnChatSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnChatSelectedListener");
        }
    }

    private void initDataset() {
        chats = new ArrayList<>();
        chats.addAll(DataProvider.getInstance().getChats());
    }

    private void updateDataset(){
        chats.clear();
        chats.addAll(DataProvider.getInstance().getChats());
    }

}
