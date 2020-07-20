package com.sedlo.mapp1.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.sedlo.mapp1.SedloQL.Player;
import com.sedlo.mapp1.SedloQL.Team;
import com.sedlo.mapp1.Tab.playerAdapter;
import com.sedlo.mapp1.UserConnection.PrefManager;
import com.sedlo.mapp1.R;
import com.sedlo.mapp1.UserConnection.User;

import java.util.ArrayList;


public class ProfileActivity extends AppCompatActivity {

    TextView textViewId, textViewUsername, textViewEmail, textViewGender;
    Button buttonPlayers,buttonTr,buttonTeams;
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

        buttonTeams = findViewById(R.id.btn_profile_team);


        PrefManager.getInstance(this).setMydatabase(openOrCreateDatabase(PrefManager.getInstance(this).getBDName(),MODE_PRIVATE,null));


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



                Cursor cursor = PrefManager.getInstance(getApplicationContext()).getMydatabase().rawQuery("Select * from "+ PrefManager.getInstance(getApplicationContext()).getDbNameTeams(),null);

                cursor.moveToFirst();

                final ArrayList<String> ids =new ArrayList<>();
                ArrayList<String> names =new ArrayList<>();


                if(cursor != null && cursor.moveToFirst())
                {
                    do {

                        names.add(cursor.getString(1));
                        ids.add(cursor.getString(0));
                    }
                    while (cursor.moveToNext());


                }
                else
                  return;



                String[] array = names.toArray(new String[names.size()]);



                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);

                builder.setTitle("Make your selection");

                builder.setItems(array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        Intent intent = new Intent(ProfileActivity.this, TrainingsListActivity.class);

                        intent.putExtra("team", ids.get(item));

                        startActivity(intent);

                    }
                });

                AlertDialog alert = builder.create();

                alert.show();



            }
        });

        buttonTeams.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, TeamsActivity.class));
            }
        });

    }



}