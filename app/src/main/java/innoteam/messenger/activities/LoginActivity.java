package innoteam.messenger.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import innoteam.messenger.R;
import innoteam.messenger.configs.Config;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 200;
    private static final String URL = "";
    private int mStatusCode = 0;

    @Bind(R.id.input_login)
    EditText _loginText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.btn_login)
    Button _loginButton;
    @Bind(R.id.link_signup)
    TextView _signupLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String login = _loginText.getText().toString();
        final String password = _passwordText.getText().toString();

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed

                        onLoginSuccess(login, password);
                        // onLoginFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {

                // TODO: Implement successful signup logic here
                // By default we just finish the Activity and log them in automatically
                this.finish();
            }
        }
    }*/
    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }



    public void onLoginSuccess(final String login, final String password) {
        _loginButton.setEnabled(true);
        String hashPassword = new String(Hex.encodeHex(DigestUtils.md5(password)));
        Map<String,String> params = new HashMap<>();
        params.put("Login",login);
        params.put("Hash", hashPassword);
        String token;

        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.LOGIN_REQUEST_URL);
        JSONObject object = new JSONObject(params);

        try {
            p.setEntity(new StringEntity(object.toString()));
            p.setHeader("Content-type", "application/json");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            if (response != null) {
                System.out.println(response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES) {
                    Header[] headers = response.getAllHeaders();
                    for (Header header: headers) {
                        Log.d("Header:", header.toString() + "\n");
                        if (header.getName().equals("Authorization")) {
                            storeData(header.getValue());
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivityForResult(intent, REQUEST_SIGNUP);
                        }
                    }
                }
                if (response.getStatusLine().getStatusCode() == Config.INCORRECT_INPUT_DATA) {
                    Toast.makeText(getBaseContext(), "Username or password is incorrect", Toast.LENGTH_LONG).show();
                }
                //Log.d("Status line", "" + response.getStatusLine().getStatusCode());
            }
        } catch (Exception e) {
                e.printStackTrace();
        }
    }

    public void storeData(String token) {
        SharedPreferences sharedPreferences = LoginActivity.this.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Config.USER_NAME_SHARED_PREF, _loginText.getText().toString());
        editor.putString(Config.PASSWORD_SHARED_PREF, _passwordText.getText().toString());
        editor.putString(Config.TOKEN_SHARED_PREF, token);
        Config.TOKEN = token;
        editor.commit();
    }

    public void onLoginFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String login = _loginText.getText().toString();
        String password = _passwordText.getText().toString();

        if (login.isEmpty() || login.length() < 2 ) {
            _loginText.setError("at least 2 characters");
            valid = false;
        } else if (login.length() < 2 ) {
            _loginText.setError("at least 2 characters");
            valid = false;
        } else if (login.length() > 20){
            _loginText.setError("less than 20 characters");
            valid = false;
        } else {
            _loginText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 40) {
            _passwordText.setError("between 4 and 40 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }
}