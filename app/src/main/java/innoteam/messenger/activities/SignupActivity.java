package innoteam.messenger.activities;

import android.app.ProgressDialog;
import android.content.Intent;
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
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;
import innoteam.messenger.R;
import innoteam.messenger.configs.Config;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";

    @Bind(R.id.input_login)
    EditText _loginText;
    @Bind(R.id.input_name)
    EditText _nameText;
    @Bind(R.id.input_surname)
    EditText _surnameText;
    @Bind(R.id.input_password)
    EditText _passwordText;
    @Bind(R.id.input_reEnterPassword)
    EditText _reEnterPasswordText;
    @Bind(R.id.btn_signup)
    Button _signupButton;
    @Bind(R.id.link_login)
    TextView _loginLink;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        final String login = _loginText.getText().toString();
        final String name = _nameText.getText().toString();
        final String surname = _surnameText.getText().toString();
        final String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        // TODO: Implement your own signup logic here.

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onSignupSuccess or onSignupFailed
                        // depending on success
                        onSignupSuccess(login, name, surname, password);
                        // onSignupFailed();
                        progressDialog.dismiss();
                    }
                }, 3000);
    }


    public void onSignupSuccess(String login, String name, String surname, String password) {
        _signupButton.setEnabled(true);
        setResult(RESULT_OK, null);
        String hashPassword = new String(Hex.encodeHex(DigestUtils.md5(password)));
        Map<String,String> params = new HashMap<>();
        params.put("Login",login);
        params.put("Hash", hashPassword);
        params.put("FirstName", name);
        params.put("LastName", surname);

        String token;
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost p = new HttpPost(Config.REGISTER_REQUEST_URL);

        JSONObject object = new JSONObject(params);
        try {
            p.setEntity(new StringEntity(object.toString()));
            p.setHeader("Content-type", "application/json");
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpResponse response = httpClient.execute(p);
            if (response != null) {
                if (response.getStatusLine().getStatusCode() == Config.LOGIN_SUCCES) {
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                if (response.getStatusLine().getStatusCode() == Config.USER_NAME_ALREDY_EXISTS) {
                    Toast.makeText(getBaseContext(), "User with this name alredy exists", Toast.LENGTH_LONG).show();
                }
            }
           // Log.d("Status line", "" + response.getStatusLine().getStatusCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        _signupButton.setEnabled(true);
    }


    public boolean validate() {
        boolean valid = true;

        String login = _loginText.getText().toString();
        String name = _nameText.getText().toString();
        String surname = _surnameText.getText().toString();
        String password = _passwordText.getText().toString();
        String reEnterPassword = _reEnterPasswordText.getText().toString();

        final Pattern pattern_login = Pattern.compile("^[a-zA-Z][a-zA-Z0-9-_\\.]{2,20}$");
       // final Pattern pattern_password = Pattern.compile("(?=^.{4,40}$)((?=.*\\d)|(?=.*\\W+))(?![.\\n])(?=.*[A-Z])(?=.*[a-z]).*$");
        Matcher matcher_login = null;
        Matcher matcher_password = null;
        matcher_login = pattern_login.matcher(login);
        //matcher_password = pattern_password.matcher(password);

        if ( matcher_login.matches() != true ) {
            _loginLink.setError("your input has illegal elements");
            valid = false;
        } else {
            _loginText.setError(null);
        }

        if ( login.isEmpty() ) {
            _loginText.setError("at least 2 characters");
            valid = false;
        } else if (login.length() < 2) {
            _loginText.setError("at least 2 characters");
            valid = false;
        } else if (login.length() > 20) {
            _loginText.setError("less than 20 characters");
            valid = false;
        } else {
            _loginText.setError(null);
        }

    /*    if ( matcher_password.matches() != true) {
            _passwordText.setError("your input has illegal elements");
            valid = false;
        } else {
            _passwordText.setError(null);
        }
*/
        if (password.isEmpty() || password.length() < 4 || password.length() > 40) {
            _passwordText.setError("between 4 and 40 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        if (reEnterPassword.isEmpty() || reEnterPassword.length() < 4 || reEnterPassword.length() > 40 || !(reEnterPassword.equals(password))) {
            _reEnterPasswordText.setError("Password Do not match");
            valid = false;
        } else {
            _reEnterPasswordText.setError(null);
        }

        return valid;
    }
}