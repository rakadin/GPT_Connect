package com.example.gpt_connect.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gpt_connect.MainActivity;
import com.example.gpt_connect.R;
import com.example.gpt_connect.adapter.FolderDataAdapter;
import com.example.gpt_connect.adapter.ItemDataAdapter;
import com.example.gpt_connect.model.FolderData;
import com.example.gpt_connect.model.Item;
import com.example.gpt_connect.model.ItemData;
import com.example.gpt_connect.sqlite.ItemDAO;

import java.util.ArrayList;
import java.util.List;

public class Item_Activity extends AppCompatActivity {
    ImageButton backbut;
    TextView txtFName;
    private int FID;
    private String fName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        getSupportActionBar().hide();
        getid();
        ItemDAO itemDAO = new ItemDAO(Item_Activity.this);
        // lấy FID từ hoạt động trước
        Intent intent = getIntent();
        FID = intent.getIntExtra("F_ID",0);
        fName = intent.getStringExtra("F_Name");
        txtFName.setText(fName+" Folder");// set textview3 name
        ListView listView = (ListView) findViewById(R.id.lv_item);

        addEvent();
        List<Item> itemDataList = new ArrayList<>();
        itemDAO.getItemsFromFID(FID,itemDataList);
//        folderDataList.add(folderData);
        ItemDataAdapter adapter = new ItemDataAdapter(this, R.layout.item_name_list,itemDataList);
        listView.setAdapter(adapter);//set adapter to lv
    }


    private void getid() {
        backbut = findViewById(R.id.backbutton);
        txtFName = findViewById(R.id.textView3);
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