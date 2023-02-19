package com.example.gpt_connect.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gpt_connect.sqlite_model.Folder;
import com.example.gpt_connect.sqlite_model.Item;

public class FolderDAO {
    private SQLiteDatabase db;
    // khởi tạo trường user
    public FolderDAO(Context context) { // phải truyền context vào
        AppData appData = new AppData(context); // khai bao de ket noi
        this.db = appData.getWritableDatabase();
    }
    /*
    thêm user mới nào DB
     */
    public long insertFolder(Folder folder) // them folder moi
    {
        ContentValues values = new ContentValues();// tạo values
        if(checkFname(folder.getFname()) == false)
        {
            values.put("Fname", folder.getFname());
            return  db.insert("Folder",null, values);// gửi value kia vào database
        }
        else
        {
            return 0;
        }
    }
    /*
  validate ten folder đã có chưa
   */
    public boolean checkFname(String s)
    {
        Cursor cursor =  db.rawQuery("SELECT * FROM Folder where Fname = ?", new String[]{String.valueOf(s)});
        if(cursor.getCount() == 0)
        {
            return false;

        }
        else return true;
    }
}
