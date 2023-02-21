package com.example.gpt_connect.sqlite;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//import android.support.annotation.Nullable;
/*
kết nối với sqlite database
 */
public class AppData extends SQLiteOpenHelper {
    public static final String DB_Name = "GPTConnect";
    public static final int version = 1 ;
    // constructor
    public AppData(@Nullable Context context) {
        super(context, DB_Name, null, version);
    }

    // tạo table
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String Foldersql = "CREATE TABLE Folder(Fid INTEGER PRIMARY KEY AUTOINCREMENT not null, Fname text not null);";
        String Itemsql = "CREATE TABLE Item(IiD INTEGER PRIMARY KEY AUTOINCREMENT not null,Fid INTEGER not null, Iname text not null,Icontent text not null,FOREIGN KEY(Fid) REFERENCES Folder(Fid))";
        sqLiteDatabase.execSQL(Foldersql);
        sqLiteDatabase.execSQL(Itemsql);
    }
    // cập nhật/ nâng cấp csdl
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String sql = "DROP TABLE IF EXISTS Folder";
        String sql2 = "DROP TABLE IF EXISTS Item";
        sqLiteDatabase.execSQL(sql);
        sqLiteDatabase.execSQL(sql2);
        onCreate(sqLiteDatabase);
    }
}
