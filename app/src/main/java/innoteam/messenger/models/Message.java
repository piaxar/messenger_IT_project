package innoteam.messenger.models;

import java.util.Date;

/**
 * Created by ivan on 25.10.16.
 */

public class Message {
    private final int messageID;
    private final String senderName;
    private final Date sendTime;
    private final String content;

    public Message(int messageID, String senderName, Date sendTime, String content) {
        this.messageID = messageID;
        this.senderName = senderName;
        this.sendTime = sendTime;
        this.content = content;
    }

    public String getSenderName() {
        return senderName;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public String getContent() {
        return content;
    }

    public int getMessageID() {
        return messageID;
    }
}
