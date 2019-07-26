package com.pratik.productize.Utils;

import android.util.Log;

import androidx.room.TypeConverter;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Converters {

    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }


    public long formatToMill(String s) {

        SimpleDateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");
        try {
            java.util.Date date = df.parse(s);
            Log.i(TAG + "sysTime", String.valueOf(System.currentTimeMillis()));
            Log.i(TAG + "newTime", String.valueOf(date.getTime()));

            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
