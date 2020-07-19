package com.sedlo.mapp1.Tab;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.sedlo.mapp1.R;
import com.sedlo.mapp1.SedloQL.Player;

public class playerTab extends AppCompatActivity
{
    Button delete,edit;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        init();
    }


    void init()
    {
         delete = findViewById(R.id.btn_player_delete);
         edit = findViewById(R.id.btn_player_edit);
         editText = findViewById(R.id.edittext_player_name);


    }


}
