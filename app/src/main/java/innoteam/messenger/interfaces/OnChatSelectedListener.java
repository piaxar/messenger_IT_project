package innoteam.messenger.interfaces;

import innoteam.messenger.models.Chat;

/**
 * Created by ivan on 24.10.16.
 */

public interface OnChatSelectedListener {
    public void onChatSelected(Chat chat);
    public void onLogOut();
}
