package innoteam.messenger.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import innoteam.messenger.R;
import innoteam.messenger.adapters.ChatsAdapter;
import innoteam.messenger.adapters.ServerAdapter;
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
    LinearLayoutManager mLayoutManager;
    OnChatSelectedListener mListener;
    SearchView searchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDataset();
    }

    private void initDataset() {
        chats = ServerAdapter.INSTANCE.getAllChats();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        adapter = new ChatsAdapter(chats);
        mLayoutManager = new LinearLayoutManager(getActivity());
        btnWriteMessage = (FloatingActionButton) view.findViewById(R.id.btnAddChat);
        searchView = (SearchView) view.findViewById(R.id.search_bar);
        rvChats = (RecyclerView) view.findViewById(R.id.rvChats);
        rvChats.setAdapter(adapter);
        rvChats.setLayoutManager(mLayoutManager);
        rvChats.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), rvChats, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // TODO send chat id instead of position
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
        return view;
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
