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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import innoteam.messenger.DataProvider;
import innoteam.messenger.R;
import innoteam.messenger.activities.MainActivity;
import innoteam.messenger.behaviors.SimpleDividerItemDecoration;
import innoteam.messenger.interfaces.MainActivityInterconnectionListener;

/**
 * Created by ivan on 24.10.16.
 */

public class ChatsFragment extends Fragment implements View.OnClickListener {
    private final String TAG = "Chats fragment";
    private MainActivityInterconnectionListener mListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        RecyclerView rvChats = (RecyclerView) view.findViewById(R.id.rvChats);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        Button logOutBtn = (Button) view.findViewById(R.id.btnLogOut);
        FloatingActionButton addChatBtn = (FloatingActionButton) view.findViewById(R.id.btnAddChat);
        TextView tvHeader = (TextView) view.findViewById(R.id.tvHeader);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvChats.addItemDecoration(new SimpleDividerItemDecoration(getContext()));
        rvChats.setLayoutManager(mLayoutManager);
        ProgressBarHandler mProgressBar = new ProgressBarHandler(getContext());

        DataProvider.getInstance().setChatsFragment(rvChats, swipeRefreshLayout, addChatBtn, getContext(), mProgressBar, mListener);

        logOutBtn.setOnClickListener(this);
        addChatBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnLogOut:
                mListener.onLogOut();
                Intent mStartActivity = new Intent(getContext(), MainActivity.class);
                int mPendingIntentId = 123456;
                PendingIntent mPendingIntent = PendingIntent.getActivity(getContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
                AlarmManager mgr = (AlarmManager)getContext().getSystemService(Context.ALARM_SERVICE);
                mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                System.exit(0);
                break;
            case R.id.btnAddChat:
                showCreateChatDialog();
                break;
        }

    }

    public void showCreateChatDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle("New chat creation");
        dialogBuilder.setMessage("Enter user login below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                DataProvider.getInstance().createNewChat(edt.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (MainActivityInterconnectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement MainActivityInterconnectionListener");
        }
    }
}
