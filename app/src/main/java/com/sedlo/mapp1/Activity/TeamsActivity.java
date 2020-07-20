package com.sedlo.mapp1.Activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.sedlo.mapp1.R;
import com.sedlo.mapp1.SedloQL.Team;
import com.sedlo.mapp1.SedloQL.Train;
import com.sedlo.mapp1.Tab.teamAdapter;
import com.sedlo.mapp1.Tab.trainAdapter;
import com.sedlo.mapp1.UserConnection.PrefManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TeamsActivity extends AppCompatActivity
{
    ListView listView;
    SQLiteDatabase mydatabase;
    Button buttonNewTeam;
    PrefManager prefManager;

    ArrayList<Team> teamArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_team);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        teamArrayList =new ArrayList<>();

        init();

        refreshList();
    }


    void init()
    {

        listView = findViewById(R.id.listview_team);

        buttonNewTeam = findViewById(R.id.btn_new_team);

        prefManager = PrefManager.getInstance(this);

        mydatabase = openOrCreateDatabase(prefManager.getBDName(),MODE_PRIVATE,null);

        prefManager.setMydatabase(mydatabase);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS "+prefManager.getDbNameTeams()+"(id INTEGER PRIMARY KEY AUTOINCREMENT ,name varchar(255) NOT NULL);");

        buttonNewTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.");

              mydatabase.execSQL("INSERT INTO "+prefManager.getDbNameTeams()+" VALUES(NULL,'New Team');");
              refreshList();
            }
        });

        prefManager.setTeamsActivity(this);

    }


    public void refreshList()
    {

        Cursor cursor = mydatabase.rawQuery("Select * from "+prefManager.getDbNameTeams(),null);

        cursor.moveToFirst();

        teamArrayList =new ArrayList<>();


        teamAdapter trainAdapter=new teamAdapter(this, teamArrayList);


        if(cursor != null && cursor.moveToFirst())
        {
            do {
                teamArrayList.add(new Team(cursor.getString(1),cursor.getString(0)));
            }
            while (cursor.moveToNext());

           listView.setAdapter(trainAdapter);

           trainAdapter.notifyDataSetChanged();
        }
        else
            listView.setAdapter(null);

    }


}
