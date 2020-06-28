package com.trichain.androidwidget;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.trichain.androidwidget.databinding.ActivityMainBinding;

import yuku.ambilwarna.AmbilWarnaDialog;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Check & request calendar permission
        checkPermission(111, Manifest.permission.READ_CALENDAR);

        initUI();

    }

    private void initUI() {
        b.llFontColor.setOnClickListener(this);
        b.llFontSize.setOnClickListener(this);
        b.llDateColor.setOnClickListener(this);
        b.llTimeColor.setOnClickListener(this);
        b.llAppointmentColor.setOnClickListener(this);
        b.llLocationColor.setOnClickListener(this);
        b.llDateFormat.setOnClickListener(this);
        b.llTimeFormat.setOnClickListener(this);
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
        if (v == b.llFontColor) {
            showColorPickerDialog(b.imgFontColor);
        }
    }

    private void showColorPickerDialog(ImageButton targetView) {
        new AmbilWarnaDialog(this, R.color.colorPrimary,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {

                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        targetView.setBackgroundColor(color);
                        //TODO: Save colot to shared prefs
                    }
                }).show();
    }
}
