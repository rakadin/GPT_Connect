package com.example.gpt_connect.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.gpt_connect.model.FolderData;
import com.example.gpt_connect.model.SpinnerFolder;

import java.util.ArrayList;
import java.util.List;

public class FolderDAO {
    private SQLiteDatabase db;
    // khởi tạo trường Folder
    public FolderDAO(Context context) { // phải truyền context vào
        AppData appData = new AppData(context); // khai bao de ket noi
        this.db = appData.getWritableDatabase();
    }
    /*
    thêm folder mới nào DB
     */
    public long insertFolder(String folderName) // them folder moi
    {
        ContentValues values = new ContentValues();// tạo values
        if(checkFname(folderName) == false)
        {
            values.put("Fname", folderName);
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
    /*
    trả về ID theo folder name
   */
    public int returnID(String s)
    {
        Cursor cursor =  db.rawQuery("SELECT Fid FROM Folder where Fname = ?", new String[]{String.valueOf(s)});
        cursor.moveToNext();
        int id = cursor.getInt(0);
        return id;
    }
    /*
   truy vấn folder data
    */
    public void getFolder(List<SpinnerFolder> listFolder)
    {
        Cursor cursor =  db.rawQuery("SELECT * FROM Folder",new String[]{} );

        for(int i =0;i<cursor.getCount();i++)
        {
            SpinnerFolder spinnerFolder = new SpinnerFolder();
            cursor.moveToNext();
            spinnerFolder.setFid(cursor.getInt(0));
            spinnerFolder.setFname(cursor.getString(1));
            listFolder.add(spinnerFolder);
        }
    }
    /*
   truy vấn folder data cho activity folderActivity
    */
    public void getFolderActivityData(List<FolderData> listFolder)
    {
        Cursor cursor =  db.rawQuery("SELECT * FROM Folder",new String[]{} );

        for(int i =0;i<cursor.getCount();i+=2)
        {
           FolderData folderData = new FolderData();
           cursor.moveToNext();
           folderData.setId(cursor.getInt(0));
           folderData.setF_name(cursor.getString(1));
           if(cursor.moveToNext() == true)
           {
               folderData.setId_2(cursor.getInt(0));
               folderData.setF_name_2(cursor.getString(1));
           }
           listFolder.add(folderData);
        }
    }
}
