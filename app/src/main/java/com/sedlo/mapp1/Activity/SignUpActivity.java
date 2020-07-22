package com.sedlo.mapp1.Activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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



public class SignUpActivity extends AppCompatActivity {
    EditText editTextUsername, editTextEmail, editTextPassword;

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        findViewById(R.id.textViewLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
            }
        });

    }

    private void registerUser() {
        final String username = editTextUsername.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        //validations
        if (TextUtils.isEmpty(username)) {
            editTextUsername.setError(getString(R.string.invalid_login));
            editTextUsername.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError(getString(R.string.invalid_login));
            editTextEmail.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.invalid_login));
            editTextEmail.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError(getString(R.string.invalid_login));
            editTextPassword.requestFocus();
            return;
        }


        RegisterUser ru = new RegisterUser(username,email,password);
        ru.execute();
    }
    private class RegisterUser extends AsyncTask<Void, Void, String> {
        private ProgressBar progressBar;
        private String username,email,password;
        RegisterUser(String username,String email, String password){
            this.username = username;
            this.password = password;
            this.email = email;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar = findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
        }
        @Override
        protected String doInBackground(Void... voids) {
            //creating request handler object
            RequestHandler requestHandler = new RequestHandler();

            //creating request parameters
            HashMap<String, String> params = new HashMap<>();

            params.put("username", username);
            params.put("email", email);
            params.put("password", password);

            //returing the response
            return requestHandler.sendPostRequest(URLS.URL_REGISTER, params);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i(getString(R.string.singin)," : "+s);
            //hiding the progressbar after completion
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
                    Toast.makeText(getApplicationContext(), "Some error occurred", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
