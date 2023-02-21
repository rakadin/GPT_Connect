package com.example.gpt_connect.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gpt_connect.model.Item;
import com.example.gpt_connect.model.ItemData;
import com.example.gpt_connect.model.SpinnerFolder;

import java.util.List;

public class ItemDAO {
    private SQLiteDatabase db;
    // khởi tạo trường Folder
    public ItemDAO(Context context) { // phải truyền context vào
        AppData appData = new AppData(context); // khai bao de ket noi
        this.db = appData.getWritableDatabase();
    }
    /*
    thêm item mới nào DB
     */
    public long insertItem(int Fid,String itemName,String iContent) // them item moi
    {
        ContentValues values = new ContentValues();// tạo values
            values.put("Fid", Fid);
            values.put("Iname", itemName);
            values.put("Icontent", iContent);
            return  db.insert("Item",null, values);// gửi value kia vào database
    }
    public void getItemsFromFID(int Fid,List<Item> itemDataList)
    {
        Cursor cursor =  db.rawQuery("SELECT * FROM Item where Fid = ?", new String[]{String.valueOf(Fid)} );

        for(int i =0;i<cursor.getCount();i++)
        {
            Item item = new Item();
            cursor.moveToNext();
            item.setItemID(cursor.getInt(0));
            item.setFolderID(cursor.getInt(1));
            item.setItemName(cursor.getString(2));
            item.setItemContent(cursor.getString(3));
            itemDataList.add(item);
        }
    }
    /*
    delete selected item
    */
    public void deleteItem(int Iid)
    {
        db.execSQL("DELETE FROM Item where IiD = ?", new String[]{String.valueOf(Iid)});
    }
    /*
set things by id
*/
    public void updateByID(String name,String content,int id)
    {
        db.execSQL("UPDATE Item SET Iname = ?,Icontent = ? where IiD = ?", new String[]{name,content,String.valueOf(id)});
    }
}
