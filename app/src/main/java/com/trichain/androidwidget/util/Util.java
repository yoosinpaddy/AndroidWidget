package com.trichain.androidwidget.util;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Build;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Util {

    private static final String TAG = "Util";

    public static SpannableString getCustomDateYMD() {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String s = format.format(c.getTime());
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(2f), 8, 10, 0); // set size
        return ss1;
    }

    public static String getCustomDateDD() {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("dd");
        return format.format(c.getTime());
    }

    public static String getCustomWeek() {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("dd");
        return String.valueOf(c.get(Calendar.WEEK_OF_YEAR));
    }

    public static String getCustomDOWeek() {
        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("dd");
        return String.valueOf(c.get(Calendar.DAY_OF_WEEK));
    }

    public static double getCurrentDayofWeek(Date d) {
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("HH:mm");
        return Double.parseDouble(format.format(d));
    }

    public static double getFormatedDateHHMM(Date d) {
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("HH:mm");
        return Double.parseDouble(format.format(d));
    }

    public static SpannableString getCurrentTimeHHMM() {
        Date d = new Date();
        @SuppressLint("SimpleDateFormat")
        DateFormat format = new SimpleDateFormat("HHmm");
        String s = format.format(d);
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(2f), 0, 2, 0); // set size
//        ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);// set color
        return ss1;
    }

    public static double getFormatedDate(Date d) {
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
        return Double.parseDouble(format.format(d));
    }

    public static Date doubleToDate(long d) {
        Log.d(TAG, "doubleToDate: " + d);
        //Option2, use decimalFormat.
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        String sDate1 = df.format(d);
        Log.e(TAG, "doubleToDate: " + sDate1);
        Date date1 = null;
        try {
            date1 = new SimpleDateFormat("yyyyMMddHHmm").parse(sDate1);
            Log.d(TAG, "doubleToDate: " + date1);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.d(TAG, "doubleToDate: " + e);
        }
        return date1;
    }

    public static String longToDate(long d) {
        Log.d(TAG, "doubleToDate: " + d);
        //Option2, use decimalFormat.
        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(8);
        String sDate1 = df.format(d);
        Log.e(TAG, "doubleToDate: " + sDate1);
        Date date1 = null;
        Date currentDate = new Date(d);
        @SuppressLint("SimpleDateFormat")
        DateFormat format = new SimpleDateFormat("HH:mm");
        Log.d(TAG, "doubleToDate: " + date1);
        return format.format(currentDate);

    }
}
