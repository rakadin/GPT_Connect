package com.example.gpt_connect.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.gpt_connect.R;
import com.example.gpt_connect.activity.Folder_Activity;
import com.example.gpt_connect.activity.Item_Activity;
import com.example.gpt_connect.model.Item;
import com.example.gpt_connect.model.ItemData;
import com.example.gpt_connect.sqlite.FolderDAO;
import com.example.gpt_connect.sqlite.ItemDAO;

import java.util.List;

public class ItemDataAdapter extends ArrayAdapter<Item> {
    private LayoutInflater inflater;
    private int layout;
    private List<Item> itemDataList;
    public ItemDataAdapter(Context context, int resource, List<Item> itemDataList) {
        super(context, resource, itemDataList);
        this.itemDataList = itemDataList;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View view=inflater.inflate(this.layout, parent, false);

        TextView item = (TextView) view.findViewById(R.id.item1);

        Item itemData = getItem(position);
        item.setText(itemData.getItemName());

        for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
            View child = ((ViewGroup) view).getChildAt(i);
            child.setFocusable(false);
            child.setFocusableInTouchMode(false);
        }
        //set on click cho item
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Item clickedItem = getItem(position);
                Dialog dialog = new Dialog(v.getContext());
                String content = clickedItem.getItemContent();
                String name = clickedItem.getItemName();
                //Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                Item_see(content,name,dialog,v.getContext(),clickedItem.getItemID(),clickedItem);
            }
        });
        return view;
    }
    // this func is use to open item content dialog
    void Item_see(String string_content,String name, Dialog dialog,Context context,int ID,Item clickedItem)
    {
        dialog.setContentView(R.layout.item_data_dialog);
        Button fixbut,DeleteBut,xBut;
        TextView icontent,txtiName;

        fixbut = dialog.findViewById(R.id.updateBut);
        xBut = dialog.findViewById(R.id.xBut);
        icontent = dialog.findViewById(R.id.icontent);
        txtiName = dialog.findViewById(R.id.ItemTXT);
        DeleteBut = dialog.findViewById(R.id.deleteBut);
        // set text to textview icontent
        icontent.setText(string_content);
        txtiName.setText(name);
        xBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        fixbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // su kien alert dialog
                AlertDialog.Builder b = new AlertDialog.Builder(view.getContext());
                b.setTitle("Update item");
                b.setIcon(R.drawable.ic_baseline_update_24);
                b.setMessage("Do you want to update item ["+name+"]?");
                b.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel(); // huy dialog
                    }
                });
                b.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateDialog(dialog,clickedItem);
                    }
                });
                b.create().show();// show dialog
            }
        });
        //delete data from SQLITE
        DeleteBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // su kien alert dialog
                AlertDialog.Builder b = new AlertDialog.Builder(view.getContext());
                b.setTitle("Delete item");
                b.setIcon(R.drawable.ic_baseline_delete_forever_24);
                b.setMessage("Do you want to delete item ["+name+"]?");
                b.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel(); // huy dialog
                    }
                });
                b.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ItemDAO itemDAO = new ItemDAO(context);
                        itemDAO.deleteItem(ID);
                        Toast.makeText(context,"Success!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(view.getContext(), Folder_Activity.class);
                        view.getContext().startActivity(intent);
                    }
                });
                b.create().show();// show dialog
            }
        });

        dialog.show();
    }
    void updateDialog(Dialog dialog,Item clickeditem)
    {
        dialog.setContentView(R.layout.update_item_dialog);
        Button xBut,doneBut;
        EditText Iname,Icontent;
        xBut = dialog.findViewById(R.id.xBut);
        doneBut = dialog.findViewById(R.id.doneBut);
        Iname = dialog.findViewById(R.id.edtiname);
        Icontent = dialog.findViewById(R.id.edtContent);
        Iname.setText(clickeditem.getItemName());
        Icontent.setText(clickeditem.getItemContent());

        xBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        doneBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*
                confirm dialog to check update
                 */
                ItemDAO itemDAO = new ItemDAO(view.getContext());
                // su kien alert dialog
                AlertDialog.Builder b = new AlertDialog.Builder(view.getContext());
                b.setTitle("Confirm");
                b.setIcon(R.drawable.ic_baseline_check_box_24);
                b.setMessage("Do you want to update item ["+clickeditem.getItemName()+"]?");
                b.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel(); // huy dialog
                    }
                });
                b.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String nameAT = Iname.getText().toString().trim();
                        String contentAT = Icontent.getText().toString().trim();
                        itemDAO.updateByID(nameAT,contentAT,clickeditem.getItemID());
                        Toast.makeText(view.getContext(),"Success!",Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.setClass(view.getContext(), Folder_Activity.class);
                        view.getContext().startActivity(intent);
                    }
                });
                b.create().show();// show dialog
            }
        });
    }
}
