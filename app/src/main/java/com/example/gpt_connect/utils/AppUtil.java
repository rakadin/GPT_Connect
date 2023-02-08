package com.example.gpt_connect.utils;

import android.text.format.DateFormat;

import java.util.Calendar;
import java.util.Locale;

public class AppUtil {
    //Lớp này có hàm getTimeFormat đề đưa time dạng long qua String để người dùng dễ nhìn.
    public static String getTimeFormat(long timeStamp) {
        try {
            Calendar cal = Calendar.getInstance(Locale.US);
            cal.setTimeInMillis(timeStamp);
            String date = DateFormat.format("HH:mm", cal).toString();
            return date;
        } catch (Exception e) {
        }
        return "";
    }

}