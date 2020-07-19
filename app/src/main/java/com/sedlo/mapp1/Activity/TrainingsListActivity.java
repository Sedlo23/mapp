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
import com.sedlo.mapp1.SedloQL.Player;
import com.sedlo.mapp1.SedloQL.Train;
import com.sedlo.mapp1.Tab.playerAdapter;
import com.sedlo.mapp1.Tab.trainAdapter;
import com.sedlo.mapp1.UserConnection.PrefManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TrainingsListActivity extends AppCompatActivity
{
    ListView listView;
    SQLiteDatabase mydatabase;
    Button buttonNewPlayer;
    PrefManager prefManager;

    ArrayList<Train> trainArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_train);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        trainArrayList =new ArrayList<>();

        init();

        refreshList();
    }


    void init()
    {

        listView = findViewById(R.id.listview_train);

        buttonNewPlayer = findViewById(R.id.btn_new_tain);

        prefManager = PrefManager.getInstance(this);

        mydatabase = openOrCreateDatabase(prefManager.getBDName(),MODE_PRIVATE,null);

        prefManager.setMydatabase(mydatabase);

        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS "+prefManager.getDbNameTrainings()+"(id INTEGER PRIMARY KEY AUTOINCREMENT ,date varchar(255) NOT NULL);");

        buttonNewPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.");
                String currentDateandTime = sdf.format(new Date());
              mydatabase.execSQL("INSERT INTO "+prefManager.getDbNameTrainings()+" VALUES(NULL,'"+ currentDateandTime+" ');");
              refreshList();
            }
        });

        prefManager.setTrainingsListActivity(this);

    }


    public void refreshList()
    {

        Cursor cursor = mydatabase.rawQuery("Select * from "+prefManager.getDbNameTrainings(),null);

        cursor.moveToFirst();

        trainArrayList =new ArrayList<>();


        trainAdapter trainAdapter=new trainAdapter(this, trainArrayList);


        if(cursor != null && cursor.moveToFirst())
        {
            do {
                trainArrayList.add(new Train(cursor.getString(1),cursor.getString(0)));
            }
            while (cursor.moveToNext());

           listView.setAdapter(trainAdapter);

           trainAdapter.notifyDataSetChanged();
        }
        else
            listView.setAdapter(null);

    }


}
