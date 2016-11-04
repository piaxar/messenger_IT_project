package innoteam.messenger.fragments;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import java.util.ArrayList;

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
    LinearLayoutManager mLayoutManager;
    OnChatSelectedListener mListener;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        chats  = new ArrayList<>();
        initDataset();
    }

    private void initDataset() {
        chats.clear();
        chats.addAll(ServerAdapter.INSTANCE.getAllChats());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        adapter = new ChatsAdapter(chats);
        cnt = getContext();
        mLayoutManager = new LinearLayoutManager(getActivity());
        btnWriteMessage = (FloatingActionButton) view.findViewById(R.id.btnAddChat);
        searchView = (SearchView) view.findViewById(R.id.search_bar);
        rvChats = (RecyclerView) view.findViewById(R.id.rvChats);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        Button logOutBtn = (Button) view.findViewById(R.id.btnLogOut);
        addChatBtn = (FloatingActionButton) view.findViewById(R.id.btnAddChat);

        rvChats.setAdapter(adapter);
        rvChats.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        rvChats.setLayoutManager(mLayoutManager);
        rvChats.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvChats, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO send chat id instead of position
                // but seems to be alright
                mListener.onChatSelected(chats.get(position));
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // ...
            }
        }));
        btnWriteMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "On button click");
            }
        });
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // TODO add refresher
                Log.d(TAG, "Refreshed");
                initDataset();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

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
                showChangeLangDialog();
            }
        });
        return view;
    }

    public void showChangeLangDialog() {
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

}
