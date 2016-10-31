package innoteam.messenger.configs;

/**
 * Created by olejeglejeg on 27.10.16.
 */

public class Config {
    public static final String URL = "http://192.168.0.101:56789";

    public static final String REGISTER_REQUEST_URL =  URL + "/signup";

    public static final String LOGIN_REQUEST_URL = URL + "/signin";

    public static final String LOGIN_SUCCES = "200";

    public static final String LOGIN_FAULT = "403";

    public static final String NAME_ALREDY_EXISTS = "409";

    public static final String SHARED_PREF_NAME = "myloginapp";

    public static final String LOGIN_SHARED_PREF = "login";

    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
}
