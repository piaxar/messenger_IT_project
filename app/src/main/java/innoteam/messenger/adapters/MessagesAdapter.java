package innoteam.messenger.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import innoteam.messenger.R;
import innoteam.messenger.models.Message;

/**
 * Created by ivan on 31.10.16.
 */

public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.ViewHolder> {
    private static final String TAG = "MessagesAdapter";
    private List<Message> mMessages;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView senderName;
        public final TextView messageContent;
        public final TextView messageTime;

        public ViewHolder(View itemView) {
            super(itemView);
            senderName = (TextView) itemView.findViewById(R.id.tvUserName);
            messageContent = (TextView) itemView.findViewById(R.id.tvMessage);
            messageTime = (TextView) itemView.findViewById(R.id.tvMessageTime);
        }
    }

    public MessagesAdapter(List<Message> messages) {
        mMessages = messages;
    }

    @Override
    public MessagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_message, parent, false);

        // Return a new holder instance
        MessagesAdapter.ViewHolder viewHolder = new MessagesAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MessagesAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        Message message = mMessages.get(position);

        // Set item views based on your views and data model
        TextView tvSenderName = viewHolder.senderName;
        TextView tvMessageContent = viewHolder.messageContent;
        TextView tvMessageTime = viewHolder.messageTime;

        tvSenderName.setText(message.getSenderName());
        tvMessageContent.setText(message.getContent());
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(message.getSendTime());
        tvMessageTime.setText(calendar.get(Calendar.HOUR_OF_DAY) +":"+ calendar.get(calendar.MINUTE));
        Log.d(TAG, "Element " + position + " set.");
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

}
