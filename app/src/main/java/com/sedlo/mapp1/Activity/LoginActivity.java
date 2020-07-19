package com.sedlo.mapp1.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.sedlo.mapp1.UserConnection.PrefManager;
import com.sedlo.mapp1.R;
import com.sedlo.mapp1.UserConnection.RequestHandler;
import com.sedlo.mapp1.UserConnection.URLS;
import com.sedlo.mapp1.UserConnection.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import static com.sedlo.mapp1.R.id.videoViewL;

public class LoginActivity extends AppCompatActivity  {

    EditText editTextUsername, editTextPassword;

    @Override
    protected void onResume() {
        super.onResume();
        VideoView v = (VideoView) findViewById(videoViewL);
        v.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.logo));


        v.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });

        v.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
    }
    void init(){
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        //if user presses on login calling the method login
        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });
        //if user presses on not registered
        findViewById(R.id.textViewRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open register screen
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                finish();
            }
        });
    }
    private void userLogin() {
        //first getting the values
        final String username = editTextUsername.getText().toString();
        final String password = editTextPassword.getText().toString();
        //validating inputs
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError("Please enter username");
            editTextUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Please enter password");
            editTextPassword.requestFocus();
            return;
        }
        //if everything is fine
        UserLogin ul = new UserLogin(username,password);
        ul.execute();
    }
    class UserLogin extends AsyncTask<Void, Void, String> {
        ProgressBar progressBar;
        String username, password;
        UserLogin(String username,String password) {
            this.username = username;
            this.password = password;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.GONE);
            try {
                //converting response to json object
                JSONObject obj = new JSONObject(s);

                //if no error in response
                if (!obj.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), obj.getString("message"), Toast.LENGTH_SHORT).show();

                    //getting the user from the response
                    JSONObject userJson = obj.getJSONObject("user");

                    //creating a new user object
                    User user = new User(
                            userJson.getInt("id"),
                            userJson.getString("username"),
                            userJson.getString("email")
                    );

                    //storing the user in shared preferences
                    PrefManager.getInstance(getApplicationContext()).setUserLogin(user);

                    //starting the profile activity
                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);

            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_LOGIN, params);
        }
    }
}