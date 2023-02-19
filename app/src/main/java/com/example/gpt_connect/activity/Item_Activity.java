package com.example.gpt_connect.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.gpt_connect.MainActivity;
import com.example.gpt_connect.R;
import com.example.gpt_connect.adapter.FolderDataAdapter;
import com.example.gpt_connect.adapter.ItemDataAdapter;
import com.example.gpt_connect.model.FolderData;
import com.example.gpt_connect.model.ItemData;

import java.util.ArrayList;
import java.util.List;

public class Item_Activity extends AppCompatActivity {
    ImageButton backbut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getSupportActionBar().hide();
        ListView listView = (ListView) findViewById(R.id.lv_item);
        getid();
        addEvent();
        List<ItemData> itemDataList = new ArrayList<>();
        //sample
        ItemData itemData = new ItemData();
        itemData.setID(1);
        itemData.setIName("Java");

       itemDataList.add(itemData);
        itemDataList.add(itemData);
        itemDataList.add(itemData);
        itemDataList.add(itemData);
//        folderDataList.add(folderData);
        ItemDataAdapter adapter = new ItemDataAdapter(this, R.layout.item_name_list,itemDataList);
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
                intent.setClass(view.getContext(), Folder_Activity.class);
                startActivity(intent);
            }
        });
    }
}