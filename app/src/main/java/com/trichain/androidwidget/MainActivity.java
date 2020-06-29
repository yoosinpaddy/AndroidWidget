package com.trichain.androidwidget;

import android.Manifest;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.trichain.androidwidget.databinding.ActivityMainBinding;
import com.trichain.androidwidget.util.SharedPrefsManager;

import yuku.ambilwarna.AmbilWarnaDialog;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding b;
    private float fontSize = 11.0f;
    private String strFontSize = "small";
    private String prefsFontSize = "small";
    private String selectedDateFormat = "MM/DD/YY";
    private String selectedTimeFormat = "12h";
    private SharedPrefsManager sharedPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Check & request calendar permission
        checkPermission(111, Manifest.permission.READ_CALENDAR);

        //Init shared prefs manager
        sharedPrefsManager = SharedPrefsManager.getInstance(this);

        initUI();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this, MainWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                sendBroadcast(intent);
                new Handler().postDelayed(this,1000);
            }
        }, 1000);
    }

    private void initUI() {

        //Initialize views from shared prefs
        loadData();

        b.llFontColor.setOnClickListener(this);
        b.llFontSize.setOnClickListener(this);
        b.llDateColor.setOnClickListener(this);
        b.llTimeColor.setOnClickListener(this);
        b.llAppointmentColor.setOnClickListener(this);
        b.llLocationColor.setOnClickListener(this);
        b.llDateFormat.setOnClickListener(this);
        b.llTimeFormat.setOnClickListener(this);
    }

    private void loadData() {
        b.imgFontColor.setBackgroundColor(sharedPrefsManager.getIntPref(SharedPrefsManager.KEY_FONT_COLOR));
        b.imgDateColor.setBackgroundColor(sharedPrefsManager.getIntPref(SharedPrefsManager.KEY_DATE_COLOR));
        b.imgTimeColor.setBackgroundColor(sharedPrefsManager.getIntPref(SharedPrefsManager.KEY_TIME_COLOR));
        b.imgAppointmentColor.setBackgroundColor(sharedPrefsManager.getIntPref(SharedPrefsManager.KEY_APPOINTMENT_COLOR));
        b.imglocationColor.setBackgroundColor(sharedPrefsManager.getIntPref(SharedPrefsManager.KEY_LOCATION_COLOR));
        b.tvDateFormat.setText(sharedPrefsManager.getStringPref(SharedPrefsManager.KEY_DATE_FORMAT));
        b.tvTimeFormat.setText(sharedPrefsManager.getStringPref(SharedPrefsManager.KEY_TIME_FORMAT));


        if (sharedPrefsManager.getFloatPref(SharedPrefsManager.KEY_FONT_SIZE) == 11.0f) {
            prefsFontSize = "Small";
        } else if (sharedPrefsManager.getFloatPref(SharedPrefsManager.KEY_FONT_SIZE) == 12.5f) {
            prefsFontSize = "Medium";
        } else if (sharedPrefsManager.getFloatPref(SharedPrefsManager.KEY_FONT_SIZE) == 14f) {
            prefsFontSize = "Large";
        }

        b.tvFontSize.setText(prefsFontSize);
    }

    private void checkPermission(int callbackId, String... permissionsId) {
        boolean permissions = true;
        for (String p : permissionsId) {
            permissions = permissions && ContextCompat.checkSelfPermission(this, p) == PERMISSION_GRANTED;
        }

        if (!permissions)
            ActivityCompat.requestPermissions(this, permissionsId, callbackId);
    }


    @Override
    public void onClick(View v) {
        if (v == b.llFontSize) {
            showFontSizeDialog(b.tvFontSize);
        } else if (v == b.llFontColor) {
            showColorPickerDialog(b.imgFontColor, SharedPrefsManager.KEY_FONT_COLOR);
        } else if (v == b.llDateColor) {
            showColorPickerDialog(b.imgDateColor, SharedPrefsManager.KEY_DATE_COLOR);
        } else if (v == b.llTimeColor) {
            showColorPickerDialog(b.imgTimeColor, SharedPrefsManager.KEY_TIME_COLOR);
        } else if (v == b.llAppointmentColor) {
            showColorPickerDialog(b.imgAppointmentColor, SharedPrefsManager.KEY_APPOINTMENT_COLOR);
        } else if (v == b.llLocationColor) {
            showColorPickerDialog(b.imglocationColor, SharedPrefsManager.KEY_LOCATION_COLOR);
        } else if (v == b.llDateFormat) {
            showDateFormatAlertDialog(b.tvDateFormat);
        } else if (v == b.llTimeFormat) {
            showTimeFormatAlertDialog(b.tvTimeFormat);
        }
    }

    private void showTimeFormatAlertDialog(TextView tvTimeFormat) {
        String[] timeFormats = new String[]{"12h", "24h"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setSingleChoiceItems(timeFormats, -1, (dialog, which) -> {
            selectedTimeFormat = timeFormats[which];
            tvTimeFormat.setText(selectedTimeFormat);

            sharedPrefsManager.savePref(SharedPrefsManager.KEY_TIME_FORMAT, selectedTimeFormat);

            dialog.dismiss();
        });
        builder.show();

    }

    private void showDateFormatAlertDialog(TextView tvDateFormat) {
        String[] dateFormats = new String[]{"MM/DD/YY", "DD/MM/YY"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setSingleChoiceItems(dateFormats, -1, (dialog, which) -> {
            selectedDateFormat = dateFormats[which];
            tvDateFormat.setText(selectedDateFormat);

            sharedPrefsManager.savePref(SharedPrefsManager.KEY_DATE_FORMAT, selectedDateFormat);

            dialog.dismiss();
        });
        builder.show();
    }

    private void showFontSizeDialog(TextView tvFontSize) {
        AlertDialog.Builder dialogFontSize = new AlertDialog.Builder(this);
        View convertView = LayoutInflater.from(this).inflate(R.layout.dialog_font_size, null);
        dialogFontSize.setView(convertView);

        SeekBar seekBarFontSize = convertView.findViewById(R.id.seekBarFontSize);
        seekBarFontSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (progress) {
                    case 0:
                        fontSize = 11.0f;
                        strFontSize = "Small";
                        break;
                    case 1:
                        fontSize = 12.5f;
                        strFontSize = "Medium";
                        break;
                    case 2:
                        fontSize = 14f;
                        strFontSize = "Large";
                        break;
                }

                sharedPrefsManager.savePref(SharedPrefsManager.KEY_FONT_SIZE, fontSize);

                tvFontSize.setText(strFontSize);
                tvFontSize.setTextSize(fontSize);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        dialogFontSize.show();
    }

    private void showColorPickerDialog(ImageView targetView, String PREFS_KEY) {
        new AmbilWarnaDialog(this, -12411905,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {

                        targetView.setBackgroundColor(color);

                        sharedPrefsManager.savePref(PREFS_KEY, color);
                    }
                }).show();
    }
}
