package innoteam.messenger.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import innoteam.messenger.R;
import innoteam.messenger.models.Chat;

/**
 * Created by ivan on 24.10.16.
 */

public class ChatsAdapter extends RecyclerView.Adapter<ChatsAdapter.ViewHolder> {
    private static final String TAG = "ChatsAdapter";
    private List<Chat> mChats;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView chatName;
        public final TextView lastMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            chatName = (TextView) itemView.findViewById(R.id.tvChatName);
            lastMessage = (TextView) itemView.findViewById(R.id.tvLastMessage);
        }
    }

    // Pass in the contact array into the constructor
    public ChatsAdapter(List<Chat> contacts) {
        mChats = contacts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_chat, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ChatsAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Chat contact = mChats.get(position);

        // Set item views based on your views and data model
        TextView tvChatName = viewHolder.chatName;
        TextView tvLastMessage = viewHolder.lastMessage;
        tvChatName.setText(contact.getChatName());
        tvLastMessage.setText(contact.getLastMessageContent());
        Log.d(TAG, "Element " + position + " set.");
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return mChats.size();
    }

}
