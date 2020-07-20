package com.sedlo.mapp1.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sedlo.mapp1.R;
import com.sedlo.mapp1.SedloQL.Player;
import com.sedlo.mapp1.Tab.playerAbsAdapter;
import com.sedlo.mapp1.Tab.playerAdapter;
import com.sedlo.mapp1.UserConnection.PrefManager;
import com.sedlo.mapp1.UserConnection.User;

import java.util.ArrayList;

public class AbsActivity extends AppCompatActivity
{
    String date_id;

    ListView listView;

    SQLiteDatabase mydatabase;

    PrefManager prefManager;

    ArrayList<Player> playerArrayList;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_abs);

        init();
    }

    void init(){



            prefManager = PrefManager.getInstance(this);

            mydatabase = openOrCreateDatabase(prefManager.getBDName(),MODE_PRIVATE,null);

            prefManager.setMydatabase(mydatabase);



            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS  "+prefManager.getDbNameAbs()+ " (" +
                    "abs_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "p_id VARCHAR(255) NOT NULL, " +
                    "d_id INT NOT NULL, " +
                    "reason VARCHAR(255), " +
                    "FOREIGN KEY (d_id)" +
                    "REFERENCES trainings (id)" +
                    "ON DELETE CASCADE," +
                    "FOREIGN KEY (p_id)" +
                    "REFERENCES players (id)" +
                    "ON DELETE CASCADE" +
                    ");");



            listView = findViewById(R.id.listview_players_abs);

            User user = PrefManager.getInstance(this).getUser();

            Bundle bundle = getIntent().getExtras();

            if(bundle.getString("date_id")!= null)
            {
                date_id=
                bundle.getString("date_id");
            }

           refreshList();


    }
    public void refreshList()
    {

        Cursor cursor = mydatabase.rawQuery("Select * from "+prefManager.getDbNamePlayers(),null);

        cursor.moveToFirst();

        playerArrayList =new ArrayList<>();

        playerAbsAdapter playerAdapter=new playerAbsAdapter(this,playerArrayList,date_id);

        if(cursor != null && cursor.moveToFirst())
        {
            do
                {

                    Player player=new Player(cursor.getString(1),cursor.getString(0));

                    Cursor cursor2 = mydatabase.rawQuery("Select * from "+prefManager.getDbNameAbs()+" WHERE d_id = "+ date_id+" AND p_id = "+player.getID()+";",null);

                    cursor2.moveToFirst();


                    if(cursor2 != null && cursor2.moveToFirst())
                    {
                        do {

                           player.aBoolean=true;
                        }
                        while (cursor2.moveToNext());

                    }

                    playerArrayList.add(player);


                }

            while (cursor.moveToNext());

            listView.setAdapter(playerAdapter);

            playerAdapter.notifyDataSetChanged();
        }
        else
            listView.setAdapter(null);

    }




}
