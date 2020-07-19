package com.sedlo.mapp1.Tab;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.sedlo.mapp1.R;

public class playerAbsTab extends AppCompatActivity
{
    Switch sw;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.tab_player_abs);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        init();
    }


    void init()
    {
         sw = findViewById(R.id.switch_abs);
         editText = findViewById(R.id.edittext_player_name_abs);



    }


}
