package com.sedlo.mapp1.Activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.sedlo.mapp1.R;
import com.sedlo.mapp1.SedloQL.Player;
import com.sedlo.mapp1.Tab.playerAdapter;
import com.sedlo.mapp1.Tab.playerTab;
import com.sedlo.mapp1.UserConnection.PrefManager;

import java.util.ArrayList;
import java.util.List;

public class PlayersListActivity extends AppCompatActivity
{
    ListView listView;
    SQLiteDatabase mydatabase;
    Button buttonNewPlayer;
    PrefManager prefManager;

    ArrayList<Player> playerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        playerArrayList =new ArrayList<>();

        init();

        refreshList();
    }


    void init()
    {

        listView = findViewById(R.id.listview_players);

        buttonNewPlayer = findViewById(R.id.btn_new_player);

        prefManager = PrefManager.getInstance(this);

        mydatabase = openOrCreateDatabase(prefManager.getBDName(),MODE_PRIVATE,null);

        prefManager.setMydatabase(mydatabase);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS "+prefManager.getDbNamePlayers()+"(id INTEGER PRIMARY KEY AUTOINCREMENT ,name varchar(255) NOT NULL);");

        buttonNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
              mydatabase.execSQL("INSERT INTO "+prefManager.getDbNamePlayers()+" VALUES(NULL,'New Player');");
              refreshList();
            }
        });

        prefManager.setPlayersListActivity(this);

    }


    public void refreshList()
    {

        Cursor cursor = mydatabase.rawQuery("Select * from "+prefManager.getDbNamePlayers(),null);

        cursor.moveToFirst();

        playerArrayList =new ArrayList<>();

        playerAdapter playerAdapter=new playerAdapter(this,playerArrayList);
        if(cursor != null && cursor.moveToFirst())
        {
            do {

                playerArrayList.add(new Player(cursor.getString(1),cursor.getString(0)));
            }
            while (cursor.moveToNext());

           listView.setAdapter(playerAdapter);

           playerAdapter.notifyDataSetChanged();
        }
        else
            listView.setAdapter(null);
    }


}
