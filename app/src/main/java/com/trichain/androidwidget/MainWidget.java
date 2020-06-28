package com.trichain.androidwidget;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.trichain.androidwidget.room.config.DatabaseClient;
import com.trichain.androidwidget.room.tables.AlarmTable;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import static com.trichain.androidwidget.util.Util.doubleToDate;
import static com.trichain.androidwidget.util.Util.getCurrentTimeHHMM;
import static com.trichain.androidwidget.util.Util.getCustomDateYMD;
import static com.trichain.androidwidget.util.Util.getFormatedDateHHMM;
import static com.trichain.androidwidget.util.Util.longToDate;
import static com.trichain.androidwidget.util.Util.readCalendarEvent;
import static com.trichain.androidwidget.util.Util.readCalendarRecentEvent;
import static com.trichain.androidwidget.util.Util.readCalendarRecentEventGreaterThanTomorrow;
import static com.trichain.androidwidget.util.Util.readCalendarRecentEventLessThanTomorrow;

/**
 * Implementation of App Widget functionality.
 */
public class MainWidget extends AppWidgetProvider {

    private static final String TAG = "MainWidget";
    static String thisTime=getCurrentTimeHHMM().toString();
    static RemoteViews remoteViews;
    static AppWidgetManager appWidgetManager1;
    static int appWidgetId1;
    Intent alarmClockIntent;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        appWidgetManager1=appWidgetManager;
        appWidgetId1=appWidgetId;
        CharSequence widgetText = context.getString(R.string.appwidget_text);
        // Construct the RemoteViews object
        remoteViews = new RemoteViews(context.getPackageName(), R.layout.main_widget);
        setupBlue(context);
        setupGreen(context);
        setupUpcomingAppointments(context);

        OtherAppointment1(context);
        OtherAppointment2(context);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }
    public void launchCalendar(Context c)
    {
        Intent LaunchIntent = c.getPackageManager().getLaunchIntentForPackage(" com.android.calendar");
        c.startActivity(LaunchIntent);
    }
    public void launchAlarm(Context context)
    {
        // Verify clock implementation
        String clockImpls[][] = {
                {"HTC Alarm Clock", "com.htc.android.worldclock", "com.htc.android.worldclock.WorldClockTabControl" },
                {"Standar Alarm Clock", "com.android.deskclock", "com.android.deskclock.AlarmClock"},
                {"Froyo Nexus Alarm Clock", "com.google.android.deskclock", "com.android.deskclock.DeskClock"},
                {"Moto Blur Alarm Clock", "com.motorola.blur.alarmclock",  "com.motorola.blur.alarmclock.AlarmClock"},
                {"Samsung Galaxy Clock", "com.sec.android.app.clockpackage","com.sec.android.app.clockpackage.ClockPackage"} ,
                {"Sony Ericsson Xperia Z", "com.sonyericsson.organizer", "com.sonyericsson.organizer.Organizer_WorldClock" },
                {"ASUS Tablets", "com.asus.deskclock", "com.asus.deskclock.DeskClock"}

        };

        boolean foundClockImpl = false;

        for(int i=0; i<clockImpls.length; i++) {
            String vendor = clockImpls[i][0];
            String packageName = clockImpls[i][1];
            String className = clockImpls[i][2];
            try {
                ComponentName cn = new ComponentName(packageName, className);
                ActivityInfo aInfo = context.getPackageManager().getActivityInfo(cn, PackageManager.GET_META_DATA);
                alarmClockIntent.setComponent(cn);
                Log.e(TAG, "launchAlarm: Found " + vendor + " --> " + packageName + "/" + className);
                foundClockImpl = true;
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(TAG, "launchAlarm: "+vendor + " does not exists");
            }
        }

        if (foundClockImpl) {
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, alarmClockIntent, 0);

            // add pending intent to your component
            // ....
        }
    }
    static void setupGreen(Context context){

        //Current date
        remoteViews.setTextViewText(R.id.dateGreenTv, getCustomDateYMD());
        new Handler().postDelayed(n,1000);

        //Day of week
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String s = "w"+calendar.get(Calendar.WEEK_OF_YEAR)+"mo tu we th fr sa su";
        SpannableString ss1 = new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(2f), 1, 3, 0); // set size
        switch (day) {
            case Calendar.MONDAY:
                if (String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)).length()==2){
                    ss1.setSpan(new RelativeSizeSpan(2f), 3, 5, 0); // set size
                }else{
                    ss1.setSpan(new RelativeSizeSpan(2f), 2, 4, 0); // set size
                }
                break;
            case Calendar.TUESDAY:
                if (String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)).length()==2){
                    ss1.setSpan(new RelativeSizeSpan(2f), 6, 8, 0); // set size
                }else{
                    ss1.setSpan(new RelativeSizeSpan(2f), 5, 7, 0); // set size
                }
                break;
            case Calendar.WEDNESDAY:
                if (String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)).length()==2){
                    ss1.setSpan(new RelativeSizeSpan(2f), 9, 11, 0); // set size
                }else{
                    ss1.setSpan(new RelativeSizeSpan(2f), 8, 10, 0); // set size
                }
                break;
            case Calendar.THURSDAY:
                if (String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)).length()==2){
                    ss1.setSpan(new RelativeSizeSpan(2f), 12, 14, 0); // set size
                }else{
                    ss1.setSpan(new RelativeSizeSpan(2f), 11, 13, 0); // set size
                }
                break;
            case Calendar.FRIDAY:
                if (String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)).length()==2){
                    ss1.setSpan(new RelativeSizeSpan(2f), 15, 17, 0); // set size
                }else{
                    ss1.setSpan(new RelativeSizeSpan(2f), 14, 16, 0); // set size
                }
                break;
            case Calendar.SATURDAY:
                if (String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)).length()==2){
                    ss1.setSpan(new RelativeSizeSpan(2f), 18, 20, 0); // set size
                }else{
                    ss1.setSpan(new RelativeSizeSpan(2f), 17, 19, 0); // set size
                }
                break;
            case Calendar.SUNDAY:
                if (String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR)).length()==2){
                    ss1.setSpan(new RelativeSizeSpan(2f), 21, 23, 0); // set size
                }else{
                    ss1.setSpan(new RelativeSizeSpan(2f), 20, 22, 0); // set size
                }
                break;
        }
        remoteViews.setTextViewText(R.id.weekGreen, ss1);
        Log.e(TAG, "setupGreen: "+getCustomDateYMD() );
        Log.e(TAG, "setupGreen: "+ss1 );

    }
    static void setupBlue(Context context){

        //Current time
        remoteViews.setTextViewText(R.id.tvCurrentTime, getCurrentTimeHHMM());
        new Handler().postDelayed(n,1000);

        //timezone
        String time="";
        TimeZone tz = TimeZone.getDefault();
        time=tz.getID()+" "+tz.getDisplayName(false, TimeZone.SHORT);
        remoteViews.setTextViewText(R.id.timeZone, time);

        //alarm
        if (context.getSystemService(AlarmManager.class).getNextAlarmClock()==null){
            String alarmt="No Alarms";
            Log.e(TAG, "updateAppWidget Alarm: "+alarmt     );
            remoteViews.setTextViewText(R.id.tvAlarm, "alarm "+alarmt);
        }else {
            String alarmt=longToDate(context.getSystemService(AlarmManager.class).getNextAlarmClock().getTriggerTime() );
            Log.e(TAG, "updateAppWidget Alarm: "+alarmt     );
            remoteViews.setTextViewText(R.id.tvAlarm, "alarm "+alarmt);
        }



    }
    static void OtherAppointment1(Context context){

        List<EventModel> a= readCalendarRecentEventLessThanTomorrow(context);
        if (a.size()>1){
            remoteViews.setTextViewText(R.id.tvAppt2Time, a.get(0).getStartD());
            remoteViews.setTextViewText(R.id.tvAppt2Title, a.get(0).getName());
            remoteViews.setTextViewText(R.id.tvAppt2Date, a.get(0).getDate());
        }else {
            remoteViews.setTextViewText(R.id.tvAppt2Time, "");
            remoteViews.setTextViewText(R.id.tvAppt2Title, "");
            remoteViews.setTextViewText(R.id.tvAppt2Date, "");
        }


    }
    static void OtherAppointment2(Context context){

        List<EventModel> a= readCalendarRecentEventGreaterThanTomorrow(context);
        if (a.size()>1){
            remoteViews.setTextViewText(R.id.tvAppt3Time, a.get(0).getStartD());
            remoteViews.setTextViewText(R.id.tvAppt3Title, a.get(0).getName());
            remoteViews.setTextViewText(R.id.tvAppt3Date, a.get(0).getDate());
        }else {
            remoteViews.setTextViewText(R.id.tvAppt3Time, "");
            remoteViews.setTextViewText(R.id.tvAppt3Title, "");
            remoteViews.setTextViewText(R.id.tvAppt3Date, "");
        }


    }
    static void setupUpcomingAppointments(Context context){

        //Upcoming
        List<EventModel> a= readCalendarRecentEvent(context);
        if (a.size()>=1){
            remoteViews.setTextViewText(R.id.startUpcoming, a.get(0).getStartD());
            remoteViews.setTextViewText(R.id.endUpcoming, a.get(0).getEndD());
            remoteViews.setTextViewText(R.id.nameUpcomming, a.get(0).getName());
            remoteViews.setTextViewText(R.id.appointmentDate, a.get(0).getDate());
            remoteViews.setTextViewText(R.id.tvUpcomingAppointmentlocation, a.get(0).getLocation());
        }else {
            remoteViews.setTextViewText(R.id.startUpcoming, "not set");
            remoteViews.setTextViewText(R.id.endUpcoming, "not set");
            remoteViews.setTextViewText(R.id.nameUpcomming, "no upcoming appointment");
            remoteViews.setTextViewText(R.id.appointmentDate, "");
            remoteViews.setTextViewText(R.id.tvUpcomingAppointmentlocation,"");
        }
//        new Handler().postDelayed(n,1000);


    }
    static Runnable n= new Runnable() {
        @Override
        public void run() {

            Log.e(TAG, "Looping: out" );
            if (!thisTime.contentEquals(getCurrentTimeHHMM().toString())){
                Log.e(TAG, "Looping: in" );
                thisTime=getCurrentTimeHHMM().toString();
                remoteViews.setTextViewText(R.id.tvCurrentTime, getCurrentTimeHHMM());
                appWidgetManager1.updateAppWidget(appWidgetId1, remoteViews);
            }
            new Handler().postDelayed(n,1000);
        }

    };

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
    private static void setTheLatestAlarm(Context c, RemoteViews remoteViews) {
        //TODO Must be done in background
        @SuppressLint("StaticFieldLeak")
        class DelPeople extends AsyncTask<Void, Void, AlarmTable> {

            @Override
            protected AlarmTable doInBackground(Void... voids) {
                return DatabaseClient
                        .getInstance(c)
                        .getAppDatabase()
                        .alarmDao()
                        .getLatestAlarm();

            }

            @Override
            protected void onPostExecute(AlarmTable v) {
                if (v.getTime()==0){
                    String time="alarm "+ getFormatedDateHHMM(doubleToDate(202012312323L));
                    remoteViews.setTextViewText(R.id.tvAlarm, time);
                }else {
                    String time="alarm "+ getFormatedDateHHMM(doubleToDate(v.getTime()));
                    remoteViews.setTextViewText(R.id.tvAlarm, time);
                }
            }
        }

        DelPeople gh = new DelPeople();
        gh.execute();
    }

}

