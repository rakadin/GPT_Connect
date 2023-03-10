package com.example.gpt_connect.adapter;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gpt_connect.R;
import com.example.gpt_connect.activity.Folder_Activity;
import com.example.gpt_connect.activity.Item_Activity;
import com.example.gpt_connect.model.FolderData;

import java.util.List;

public class FolderDataAdapter extends ArrayAdapter<FolderData> {
    private LayoutInflater inflater;
    private int layout;
    private List<FolderData> folderDataList;

    public FolderDataAdapter(Context context, int resource, List<FolderData> folderDataList) {
        super(context, resource, folderDataList);
        this.folderDataList = folderDataList;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(this.layout, parent, false);

        TextView column1 = (TextView) view.findViewById(R.id.column1);
        TextView column2 = (TextView) view.findViewById(R.id.column2);

        FolderData folderData = getItem(position);
        column1.setText(folderData.getF_name());
        column2.setText(folderData.getF_name_2());

        if (folderData.getF_name_2() == null) {
            column2.setVisibility(View.GONE);
        }

        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            View child = ((ViewGroup) view).getChildAt(i);
            child.setFocusable(false);
            child.setFocusableInTouchMode(false);
        }
        // set on click cho cot 1
        column1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FolderData clickedFolder = getItem(position);
                String message = clickedFolder.getF_name();
                int Fid = clickedFolder.getId();// get id from column 1
//                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                openItemActivity(Fid,v,message);
            }
        });
        //set on click cho cot 2
        column2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FolderData clickedFolder = getItem(position);
                String message = clickedFolder.getF_name_2();
                int Fid = clickedFolder.getId_2();// get id from column 2
//                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                openItemActivity(Fid,v,message);
            }
        });
        return view;
    }
    void openItemActivity(int id_travel, View view, String FolderName)
    {
        Intent intent = new Intent();
        intent.setClass(view.getContext(), Item_Activity.class);
        intent.putExtra("F_ID",id_travel);// put Foler ID to item_activity
        intent.putExtra("F_Name",FolderName);// put Folder name to item_activity
        view.getContext().startActivity(intent);
    }
}

