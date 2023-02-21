package com.example.gpt_connect;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gpt_connect.activity.Folder_Activity;
import com.example.gpt_connect.activity.HowToUse_Activity;
import com.example.gpt_connect.activity.LiveChat_Activity;

public class MainActivity extends AppCompatActivity {

    ImageButton butsave, butchat,buthtu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        butchat = findViewById(R.id.chat_but);
        butsave = findViewById(R.id.saved_button);
        buthtu = findViewById(R.id.use_but);
        // chức năng chp but chat live
        butchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), LiveChat_Activity.class);
                startActivity(intent);
            }
        });

        butsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), Folder_Activity.class);
                startActivity(intent);
            }
        });
        buthtu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), HowToUse_Activity.class);
                startActivity(intent);
            }
        });
    }
}