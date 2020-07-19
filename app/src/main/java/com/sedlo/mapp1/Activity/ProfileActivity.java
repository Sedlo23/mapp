package com.sedlo.mapp1.Activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sedlo.mapp1.UserConnection.PrefManager;
import com.sedlo.mapp1.R;
import com.sedlo.mapp1.UserConnection.User;


public class ProfileActivity extends AppCompatActivity {

    TextView textViewId, textViewUsername, textViewEmail, textViewGender;
    Button buttonPlayers,buttonTr;
    SQLiteDatabase myDB;
    String DB_NAME;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        init();
    }

    void init(){
        textViewId = findViewById(R.id.textViewId);
        textViewUsername = findViewById(R.id.textViewUsername);
        textViewEmail = findViewById(R.id.textViewEmail);
        buttonPlayers = findViewById(R.id.btn_profile_players);
        buttonTr = findViewById(R.id.btn_profile_train);

        User user = PrefManager.getInstance(this).getUser();

        textViewId.setText(String.valueOf(user.getId()));
        textViewUsername.setText(user.getUsername());
        textViewEmail.setText(user.getEmail());


        findViewById(R.id.buttonLogout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                PrefManager.getInstance(getApplicationContext()).logout();
            }
        });


        buttonPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, PlayersListActivity.class));
            }
        });

        buttonTr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, TrainingsListActivity.class));
            }
        });



    }




}