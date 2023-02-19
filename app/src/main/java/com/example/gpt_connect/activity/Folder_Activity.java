package com.example.gpt_connect.activity;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gpt_connect.MainActivity;
import com.example.gpt_connect.R;
import com.example.gpt_connect.adapter.FolderDataAdapter;
import com.example.gpt_connect.model.FolderData;

import java.util.ArrayList;
import java.util.List;

public class Folder_Activity extends AppCompatActivity {
    ImageButton backbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);
        getSupportActionBar().hide();
        ListView listView = (ListView) findViewById(R.id.lv_folder);
        getid();
        addEvent();
        List<FolderData> folderDataList = new ArrayList<>();
        //sample
        FolderData folderData = new FolderData();
        folderData.setId(1);
        folderData.setF_name("button 1");
        folderData.setId_2(2);
        folderData.setF_name_2("button2");
        //sample
        FolderData folderData2 = new FolderData();
        folderData2.setId(1);
        folderData2.setF_name("button 3");
        folderData2.setId_2(2);
        folderData2.setF_name_2("button4");
        //sample
        FolderData folderData3 = new FolderData();
        folderData3.setId(1);
        folderData3.setF_name("button odd");

        folderDataList.add(folderData);
        folderDataList.add(folderData2);
        folderDataList.add(folderData2);
        folderDataList.add(folderData);
        folderDataList.add(folderData2);
        folderDataList.add(folderData3);
//        folderDataList.add(folderData);
        FolderDataAdapter adapter = new FolderDataAdapter(this, R.layout.item_two_column_button, folderDataList);
        listView.setAdapter(adapter);//set adapter to lv
    }


    private void getid() {
        backbut = findViewById(R.id.backbutton);
    }
    private void addEvent() {
        backbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

}