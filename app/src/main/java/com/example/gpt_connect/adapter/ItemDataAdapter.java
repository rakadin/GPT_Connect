package com.example.gpt_connect.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.gpt_connect.R;
import com.example.gpt_connect.model.ItemData;

import java.util.List;

public class ItemDataAdapter extends ArrayAdapter<ItemData> {
    private LayoutInflater inflater;
    private int layout;
    private List<ItemData> itemDataList;
    public ItemDataAdapter(Context context, int resource, List<ItemData> itemDataList) {
        super(context, resource, itemDataList);
        this.itemDataList = itemDataList;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(this.layout, parent, false);

        TextView item = (TextView) view.findViewById(R.id.item1);

        ItemData itemData = getItem(position);
        item.setText(itemData.getIName());

        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            View child = ((ViewGroup) view).getChildAt(i);
            child.setFocusable(false);
            child.setFocusableInTouchMode(false);
        }
        //set on click cho item
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ItemData clickedItem = getItem(position);
                String message = "Clicked item: " + clickedItem.getIName();
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
}
