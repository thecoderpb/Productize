package com.pratik.productize.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;

import androidx.room.TypeConverter;

import com.pratik.productize.R;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static com.pratik.productize.utils.Constants.TAG2;
import static com.pratik.productize.utils.Constants.TAG_HOME;


public class Converters {

    @TypeConverter
    public static Date toDate(Long dateLong){
        return dateLong == null ? null: new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date){
        return date == null ? null : date.getTime();
    }

    private long strFormatToMill(String s) {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat df = new SimpleDateFormat("EEE MMM d HH:mm:ss Z yyyy");

        try {
            java.util.Date date = df.parse(s);
            Log.i(TAG2 , String.valueOf(System.currentTimeMillis()));
            Log.i(TAG2 , String.valueOf(Objects.requireNonNull(date).getTime()));

            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public long time24HrToMillSec(String s) {

        long newTime = timeStrToMillSec(s);

        if (newTime < System.currentTimeMillis()) {
            Log.i(TAG2, "Day has passed. Setting alarm for next day");

            newTime += 1000 * 60 * 60 * 24;

        }

        return newTime ;//- (1000 * 60 * 5);
    }

    private long timeStrToMillSec(String s){


        java.util.Date currdate = GregorianCalendar.getInstance().getTime();

        Log.i(TAG2, currdate.toString());

        String s1 = currdate.toString().substring(0, 11);
        String s2 = currdate.toString().substring(16);
        String newVal = s1 + s + s2;
        Log.i(TAG2, newVal);

        return strFormatToMill(newVal);

    }

    @SuppressLint("DefaultLocale")
    public String timeLongToTimerFormat(long l){

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

    public String timeLongToMin(long l){
        if(l/(60*1000) > 60 )
            return String.format(Locale.getDefault(),"%01d hr %2d mins",
                    TimeUnit.MILLISECONDS.toHours(l),
                    TimeUnit.MILLISECONDS.toMinutes(l) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)));
        else if( l/(60*1000) == 60 ){

            return String.format(Locale.getDefault(),"%2d hr",TimeUnit.MILLISECONDS.toHours(l));

        }else
            return String.format(Locale.getDefault(),"%2d mins",
                    TimeUnit.MILLISECONDS.toMinutes(l) -
                            TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(l)));
    }


    public String convertTagToText(int tag){

        if( tag == 0){
            return "Home";
        }else if( tag == 1){
            return " Work";
        }

        return "Others";

    }

    public String calculateResetTime(Context context){

        PrefManager prefManager = new PrefManager(context);
        String time;
        if(prefManager.getActiveTag() == TAG_HOME)
            time = prefManager.getHomeTime();
        else
            time = prefManager.getWorkTime();

        long millsecTime = timeStrToMillSec(time);
        millsecTime += prefManager.getHours();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millsecTime);
        String fullTime = calendar.getTime().toString();



     return fullTime;
    }

    public int getColorResource(int priority) {

        switch (priority){
            case 6 : return R.drawable.card_border_yellow;

            case 7 : return R.drawable.card_border_red;

            default: return R.drawable.card_border;
        }

    }


    public int getLocationTagImage(int tag){

        int resId;

        switch (tag){
            case -1 :
                resId = R.drawable.ic_other_fill_small;
                break;
            case 0 :
                resId = R.drawable.ic_home_fill_small;
                break;
            case 1 :
                resId = R.drawable.ic_work_fill_small;
                break;
            default: resId = R.drawable.ic_nav_other_hollow;
        }

        return resId;
    }
}
