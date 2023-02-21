package com.example.gpt_connect.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gpt_connect.model.SpinnerFolder;

import java.util.List;

public class SpinnerAdapter extends BaseAdapter {

    private List<SpinnerFolder> folders;
    private Context context;

    public SpinnerAdapter(Context context, List<SpinnerFolder> folders) {
        this.context = context;
        this.folders = folders;
    }

    @Override
    public int getCount() {
        return folders.size();
    }

    @Override
    public Object getItem(int position) {
        return folders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return folders.get(position).getFid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);
        }

        SpinnerFolder folder = folders.get(position);

        TextView nameTextView = convertView.findViewById(android.R.id.text1);
        nameTextView.setText(folder.getFname());

        return convertView;
    }
}
