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
    private long compressedLength;
    private long uncompressedLength;

    public Message(int messageID, String senderName, Date sendTime, String content) {
        this.messageID = messageID;
        this.senderName = senderName;
        this.sendTime = sendTime;
        this.content = content;
    }

    public Message(int messageID, String senderName, Date sendTime, CompressedContent comCont) {
        this.messageID = messageID;
        this.senderName = senderName;
        this.sendTime = sendTime;
        this.content = comCont.content;
        this.compressedLength = comCont.compressed;
        this.uncompressedLength = comCont.uncompressed;

    }

    public long getCompressedLength() {
        return compressedLength;
    }

    public long getUncompressedLength() {
        return uncompressedLength;
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
