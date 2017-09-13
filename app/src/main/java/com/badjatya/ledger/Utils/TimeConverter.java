package com.badjatya.ledger.Utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Tanmay on 11/6/2016.
 */
public class TimeConverter {
    public static long convertDateStringToEpoch(String dateString)
    {
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar=Calendar.getInstance();
        try
        {
            calendar.setTime(format.parse(dateString));
            Date date=calendar.getTime();
            Log.d("Voldemort", date.getTime()+" this is the converted date string"+ dateString);
            return date.getTime();
        }
        catch (java.text.ParseException jpe)
        {
            jpe.printStackTrace();
            Log.d("Voldemort", "date parsing error brah!");
            return 0;
        }
    }

    public static String convertEpochToDateString(Long epoch)
    {
        Date date=new Date(epoch);
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        return format.format(date);

    }


}
