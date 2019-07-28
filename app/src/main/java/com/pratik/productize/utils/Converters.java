package com.pratik.productize.utils;


import android.annotation.SuppressLint;
import android.util.Log;

import androidx.room.TypeConverter;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

import static com.pratik.productize.utils.Constants.TAG2;


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

        SimpleDateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy"); //TODO:check this if alarm manager has issues

        try {
            java.util.Date date = df.parse(s);
            Log.i(TAG2 , String.valueOf(System.currentTimeMillis()));
            Log.i(TAG2 , String.valueOf(date.getTime()));

            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public long time24HrToMillSec(String s) {

        java.util.Date currdate = GregorianCalendar.getInstance().getTime();

        Log.i(TAG2, currdate.toString());

        String s1 = currdate.toString().substring(0, 11);
        String s2 = currdate.toString().substring(16);
        String newVal = s1 + s + s2;
        Log.i(TAG2, newVal);
        long newTime = formatToMill(newVal);

        if (newTime < System.currentTimeMillis()) {
            Log.i(TAG2, "Day has passed. Setting alarm for next day");

            newTime += 1000 * 60 * 60 * 24;

        }

        return newTime ;//- (1000 * 60 * 5);
    }

    @SuppressLint("DefaultLocale")
    public String unitConversion(long l){

        if(l/(60*1000) > 60 )
            return String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(l),
                    TimeUnit.MILLISECONDS.toMinutes(l) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)),
                    TimeUnit.MILLISECONDS.toSeconds(l) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));
        else
            return String.format("%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(l) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)),
                    TimeUnit.MILLISECONDS.toSeconds(l) -
                            TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(l)));


    }

}
