package innoteam.messenger.configs;

import org.apache.http.HttpStatus;

/**
 * Created by olejeglejeg on 27.10.16.
 */

public class Config {
    public static final String URL = "http://10.90.104.20/messenger";

    public static final String REGISTER_REQUEST_URL =  URL + "/signup";

    public static final String LOGIN_REQUEST_URL = URL + "/signin";

    public static final String  GET_MY_CHATS = URL + "/mychats";

    public static final String  GET_CHAT_INFO = URL + "/getchatinfo/"; //get short info about chat  /getchatinfo/3938  // chatID

    public static final String GET_CHAT_MESSAGES = URL + "/getchatmessages/"; //get array messages in chat  /getchatmessages/3938  // chatID

    public static final String GET_MY_CHATS_MESSAGES_IDS = URL + "/getchatmessages/";

    public static final String GET_MESSAGE_INFO = URL + "/getmessageinfo/";

    public static final String GET_MESSAGE_CONTENT = URL + "/getmessagecontent/";

    public static final String SEND_MESSAGE = URL + "/sendmessage/";

    public static final String CREATE_NEW_PRIVATE_CHAT = URL + "/newprivatechat/";

    public static final String GET_CHAT_USERS = URL + "/getchatusers/";

    public static final int LOGIN_SUCCES = HttpStatus.SC_OK;

    public static final int LOGIN_FAULT = HttpStatus.SC_FORBIDDEN;

    public static final int INCORRECT_INPUT_DATA = HttpStatus.SC_UNAUTHORIZED;

    public static final int USER_NAME_ALREDY_EXISTS = HttpStatus.SC_CONFLICT;

    public static final String SHARED_PREF_NAME = "innoteamMessanger";

    public static final String USER_NAME_SHARED_PREF = "login";

    public static final int BAD_REQUEST = HttpStatus.SC_BAD_REQUEST;

    public static final String PASSWORD_SHARED_PREF = "password";

    public static final String TOKEN_SHARED_PREF = "token";

    public static String TOKEN = "";

    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}
