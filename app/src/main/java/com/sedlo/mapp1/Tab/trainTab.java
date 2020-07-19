package com.sedlo.mapp1.Tab;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.sedlo.mapp1.R;

public class trainTab extends AppCompatActivity
{
    Button delete,edit;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_train);

        getWindow().setFormat(PixelFormat.TRANSLUCENT);

        init();
    }


    void init()
    {
         delete = findViewById(R.id.btn_train_delete);
         edit = findViewById(R.id.btn_train_edit);
         editText = findViewById(R.id.edittext_train_date);

    }


}
