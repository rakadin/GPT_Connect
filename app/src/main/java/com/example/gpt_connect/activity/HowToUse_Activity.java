package com.example.gpt_connect.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gpt_connect.MainActivity;
import com.example.gpt_connect.R;

public class HowToUse_Activity extends AppCompatActivity {
    private Button previous,next;
    private ImageButton homebut;
    private TextView imgNum;
    private ImageView imgView;
    private int now_position =1;
    private final int img[] = {R.drawable.htu_1,R.drawable.htu_2,R.drawable.htu_3,R.drawable.htu_4,R.drawable.htu_5,R.drawable.htu_6,R.drawable.htu_7,R.drawable.htu_8};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_to_use);
        getSupportActionBar().hide();
        getID();
        addEvent();
    }
    public void getID()
    {
        previous = findViewById(R.id.preBut);
        next = findViewById(R.id.nextBut);
        homebut = findViewById(R.id.homeBut);
        imgNum = findViewById(R.id.imgNum);
        imgView = findViewById(R.id.imgView);
    }
    public void addEvent()
    {
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now_position -=1;
                if(now_position<1)
                {
                    now_position =1;
                }
                changeIMG();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now_position +=1;
                if(now_position>8)
                {
                    now_position =8;
                }
                changeIMG();
            }
        });
        homebut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

    }
    public void changeIMG()
    {
        int tem = now_position -1;
        imgView.setImageResource(img[tem]);
        imgNum.setText(now_position+"/8");
    }
}