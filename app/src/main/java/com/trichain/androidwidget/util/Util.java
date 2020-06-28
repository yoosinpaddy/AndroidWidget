package com.trichain.androidwidget.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import com.trichain.androidwidget.EventModel;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Util {

    private static final String TAG = "Util";
    public static ArrayList<String> nameOfEvent = new ArrayList<String>();
    public static ArrayList<String> startDates = new ArrayList<String>();
    public static ArrayList<String> stopDates = new ArrayList<String>();
    public static ArrayList<SpannableString> starRealtDates = new ArrayList<SpannableString>();
    public static ArrayList<String> startDatesl = new ArrayList<String>();
    public static ArrayList<String> eventLocationl = new ArrayList<String>();
    public static ArrayList<String> endDates = new ArrayList<String>();
    public static ArrayList<String> descriptions = new ArrayList<String>();

    public static String readCalendarEvent(Context context) {
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        boolean hasEvent=false;
        int eventid=-1;
        for (int i = 0; i < CNames.length; i++) {

            nameOfEvent.add(cursor.getString(1));
            startDates.add(getFormatedDateHHMMl(Long.parseLong(cursor.getString(3))));
            startDatesl.add(cursor.getString(3));
//            endDates.add(getFormatedDateHHMMl(Long.parseLong(cursor.getString(4))));
            descriptions.add(cursor.getString(2));
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();
            if (isToday(Long.parseLong(startDatesl.get(i)))){
                Log.e(TAG, "readCalendarEvent: has event" );
                hasEvent=true;
                eventid=i;
            }
            Log.e(TAG, "readCalendarEvent: date"+startDates.get(i) );
            Log.e(TAG, "readCalendarEvent: name"+CNames[i]);
        }
        if (hasEvent){
            return nameOfEvent.get(eventid);
        }else {
            return "no events today";
        }
    }
    public static List<EventModel> readCalendarRecentEvent(Context context) {
        List<EventModel> events=new ArrayList<>();
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        boolean hasEvent=false;
        int eventid=-1;
        for (int i = 0; i < CNames.length; i++) {

            nameOfEvent.add(cursor.getString(1));
            startDates.add(getFormatedDateHHMMl(Long.parseLong(cursor.getString(3))));
            if (cursor.getString(4)!=null){
                stopDates.add(getFormatedDateHHMMl(Long.parseLong(cursor.getString(4))));
            }else {
                stopDates.add("00:00");

            }
            starRealtDates.add(getFormatedDateHHMMlR(Long.parseLong(cursor.getString(3))));
            startDatesl.add(cursor.getString(3));
            if (cursor.getString(5)!=null){
                eventLocationl.add(cursor.getString(5));
            }else {
                eventLocationl.add("");
            }
//            endDates.add(getFormatedDateHHMMl(Long.parseLong(cursor.getString(4))));
            descriptions.add(cursor.getString(2));
            CNames[i] = cursor.getString(1);
            cursor.moveToNext();
            if (isTodayOrGreater(Long.parseLong(startDatesl.get(i)))){
                Log.e(TAG, "readCalendarEvent: has event" );
                hasEvent=true;
                eventid=i;
                SpannableString start= new SpannableString(startDates.get(i));
                SpannableString stop= new SpannableString(stopDates.get(i));
                SpannableString name= new SpannableString(nameOfEvent.get(i));
                SpannableString loc= new SpannableString(eventLocationl.get(i));
                SpannableString date= starRealtDates.get(i);
                if (start!=null){
                    start=makeSpanDate(start);
                }
                if (stop!=null){
                    stop=makeSpanDate(stop);
                }
                if(name.toString().toLowerCase().contains("appointment")){
                    events.add(new EventModel(start,stop,name,date,loc));
                }
            }
            Log.e(TAG, "readCalendarEvent: date"+startDates.get(i) );
            Log.e(TAG, "readCalendarEvent: name"+CNames[i]);
        }
        return events;
    }
    public static List<EventModel> readCalendarTodayEvent(Context context) {
        List<EventModel> events=new ArrayList<>();
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        boolean hasEvent=false;
        int eventid=-1;
        for (int i = 0; i < CNames.length; i++) {

            nameOfEvent.add(cursor.getString(1));
            startDates.add(getFormatedDateHHMMl(Long.parseLong(cursor.getString(3))));
            starRealtDates.add(getFormatedDateHHMMlR(Long.parseLong(cursor.getString(3))));
            startDatesl.add(cursor.getString(3));
            if (cursor.getString(5)!=null){
                eventLocationl.add(cursor.getString(5));
            }else {
                eventLocationl.add("");
            }
//            endDates.add(getFormatedDateHHMMl(Long.parseLong(cursor.getString(4))));
            descriptions.add(cursor.getString(2));
            CNames[i] = cursor.getString(1);
            if (isToday(Long.parseLong(startDatesl.get(i)))){
                Log.e(TAG, "readCalendarEvent: has event" );
                hasEvent=true;
                eventid=i;
                SpannableString stop;
                if (cursor.getString(4)!=null){
                    stop= new SpannableString(getFormatedDateHHMMl(Long.parseLong(cursor.getString(4))));
                }else {
                    stop= new SpannableString("00:00");

                }
                SpannableString start= new SpannableString(startDates.get(i));
                SpannableString name= new SpannableString(nameOfEvent.get(i));
                SpannableString loc= new SpannableString(eventLocationl.get(i));
                SpannableString date= starRealtDates.get(i);
                if (start!=null){
                    start=makeSpanDate(start);
                }
                if (stop!=null){
                    stop=makeSpanDate(stop);
                }
                if(name.toString().toLowerCase().contains("appointment")){
                    events.add(new EventModel(start,stop,name,date,loc));
                }
            }
            cursor.moveToNext();
            Log.e(TAG, "readCalendarEvent: date"+startDates.get(i) );
            Log.e(TAG, "readCalendarEvent: name"+CNames[i]);
        }
        return events;
    }
    public static List<EventModel> readCalendarRecentEventLessThanTomorrow(Context context) {
        List<EventModel> events=new ArrayList<>();
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        boolean hasEvent=false;
        int eventid=-1;
        for (int i = 0; i < CNames.length; i++) {

            nameOfEvent.add(cursor.getString(1));
            startDates.add(getFormatedDateHHMMl(Long.parseLong(cursor.getString(3))));
            starRealtDates.add(getFormatedDateHHMMlR(Long.parseLong(cursor.getString(3))));
            startDatesl.add(cursor.getString(3));
            if (cursor.getString(5)!=null){
                eventLocationl.add(cursor.getString(5));
            }else {
                eventLocationl.add("");
            }
//            endDates.add(getFormatedDateHHMMl(Long.parseLong(cursor.getString(4))));
            descriptions.add(cursor.getString(2));
            CNames[i] = cursor.getString(1);
            if (isTomorrowOrLess(Long.parseLong(startDatesl.get(i)))){
                Log.e(TAG, "readCalendarEvent: has event" );
                hasEvent=true;
                eventid=i;
                SpannableString stop;
                if (cursor.getString(4)!=null){
                    stop= new SpannableString(getFormatedDateHHMMl(Long.parseLong(cursor.getString(4))));
                }else {
                    stop= new SpannableString("00:00");

                }
                SpannableString start= new SpannableString(startDates.get(i));
                SpannableString name= new SpannableString(nameOfEvent.get(i));
                SpannableString loc= new SpannableString(eventLocationl.get(i));
                SpannableString date= starRealtDates.get(i);
                if (start!=null){
                    start=makeSpanDate(start);
                }
                if (stop!=null){
                    stop=makeSpanDate(stop);
                }
                if(name.toString().toLowerCase().contains("appointment")){
                    events.add(new EventModel(start,stop,name,date,loc));
                }
            }
            cursor.moveToNext();
            Log.e(TAG, "readCalendarRecentEventLessThanTomorrow: date"+startDates.get(i) );
            Log.e(TAG, "readCalendarRecentEventLessThanTomorrow: name"+CNames[i]);
        }
        return events;
    }
    public static List<EventModel> readCalendarRecentEventGreaterThanTomorrow(Context context) {
        Log.e(TAG, "readCalendarRecentEventGreaterThanTomorrow: start" );
        List<EventModel> events=new ArrayList<>();
        Cursor cursor = context.getContentResolver()
                .query(
                        Uri.parse("content://com.android.calendar/events"),
                        new String[] { "calendar_id", "title", "description",
                                "dtstart", "dtend", "eventLocation" }, null,
                        null, null);
        cursor.moveToFirst();
        Log.e(TAG, "readCalendarRecentEventGreaterThanTomorrow: cursor"+cursor.getCount() );
        // fetching calendars name
        String CNames[] = new String[cursor.getCount()];

        // fetching calendars id
        nameOfEvent.clear();
        startDates.clear();
        endDates.clear();
        descriptions.clear();
        boolean hasEvent=false;
        int eventid=-1;
        for (int i = 0; i < CNames.length; i++) {

            nameOfEvent.add(cursor.getString(1));
            startDates.add(getFormatedDateHHMMl(Long.parseLong(cursor.getString(3))));
            starRealtDates.add(getFormatedDateHHMMlR(Long.parseLong(cursor.getString(3))));
            startDatesl.add(cursor.getString(3));
            if (cursor.getString(5)!=null){
                eventLocationl.add(cursor.getString(5));
            }else {
                eventLocationl.add("");
            }
//            endDates.add(getFormatedDateHHMMl(Long.parseLong(cursor.getString(4))));
            descriptions.add(cursor.getString(2));
            CNames[i] = cursor.getString(1);
            if (isGreaterThanTomorrow(Long.parseLong(startDatesl.get(i)))){
                Log.e(TAG, "readCalendarRecentEventGreaterThanTomorrow: has event" );
                hasEvent=true;
                eventid=i;
                SpannableString stop;
                if (cursor.getString(4)!=null){
                    stop= new SpannableString(getFormatedDateHHMMl(Long.parseLong(cursor.getString(4))));
                }else {
                    stop= new SpannableString("00:00");

                }
                SpannableString start= new SpannableString(startDates.get(i));
                SpannableString name= new SpannableString(nameOfEvent.get(i));
                SpannableString loc= new SpannableString(eventLocationl.get(i));
                SpannableString date= starRealtDates.get(i);
                if (start!=null){
                    start=makeSpanDate(start);
                }
                if (stop!=null){
                    stop=makeSpanDate(stop);
                }
                if(name.toString().toLowerCase().contains("appointment")){
                    Log.e(TAG, "readCalendarRecentEventGreaterThanTomorrow: namex:"+name );
                    events.add(new EventModel(start,stop,name,date,loc));
                }else {
                    Log.e(TAG, "readCalendarRecentEventGreaterThanTomorrow: name:"+name );
                }
            }
            cursor.moveToNext();
            Log.e(TAG, "readCalendarRecentEventGreaterThanTomorrow: date"+startDates.get(i) );
            Log.e(TAG, "readCalendarRecentEventGreaterThanTomorrow: name"+CNames[i]);
        }
        return events;
    }
    public static SpannableString makeSpanDate(SpannableString a) {
        SpannableString ss1 = a;
        if (a.toString().length()>3){
            ss1.setSpan(new RelativeSizeSpan(2f), 0, 2, 0); // set size
            return ss1;
        }else
            return new SpannableString("");
    }
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

    public static String getFormatedDateHHMMl(long l) {
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(new Date(l));
    }

    public static SpannableString getFormatedDateHHMMlR(long l) {
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("MM-dd");
        String s = format.format(new Date(l));
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(2f), 3, 5, 0); // set size
        return ss1;
    }
    public static boolean isToday(long l) {
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date(l)).contentEquals(format.format(new Date()));
    }
    public static boolean isTodayOrGreater(long l) {
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date(l)).contentEquals(format.format(new Date()))||l>new Date().getTime();
    }
    public static boolean isTomorrowOrLess(long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 1);  // number of days to add
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("yyyyMMdd");
        return format.format(new Date(l)).contentEquals(format.format(new Date()))||format.format(new Date(l)).contentEquals(format.format(new Date(c.getTimeInMillis())));
    }
    public static boolean isGreaterThanTomorrow(long l) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 2);  // number of days to add
        c.set(Calendar.HOUR_OF_DAY,0);
        @SuppressLint("SimpleDateFormat") DateFormat format = new SimpleDateFormat("yyyyMMdd");
        boolean yes= l>c.getTimeInMillis();
        if (yes){
            Log.e(TAG, "isGreaterThanTomorrow: "+c.getTime().toString() );
        }
        return yes;
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
