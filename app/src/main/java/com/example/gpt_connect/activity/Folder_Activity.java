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
import com.example.gpt_connect.sqlite.FolderDAO;

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
        FolderDAO folderDAO = new FolderDAO(Folder_Activity.this);
        getid();
        addEvent();
        List<FolderData> folderDataList = new ArrayList<>();
        folderDAO.getFolderActivityData(folderDataList);
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