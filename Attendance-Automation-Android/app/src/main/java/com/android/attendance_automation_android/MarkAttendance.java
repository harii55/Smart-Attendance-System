package com.android.attendance_automation_android;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MarkAttendance extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance_mark);

        // Retrieve token, SSID & BSSID from Intent
        String token = getIntent().getStringExtra("TOKEN");
        String ssid = getIntent().getStringExtra("SSID");
        String bssid = getIntent().getStringExtra("BSSID");

        Log.i("API_RESPONSE", "Token: " + token);
        Log.i("SSID_RESPONSE","SSID: " + ssid);
        Log.i("BSSID_RESPONSE","BSSID: " + bssid);
    }

    public void markAttendance(View view){
        ImageView attendanceStatus = (ImageView) findViewById(R.id.alert);
        attendanceStatus.setImageResource(R.drawable.baseline_check_circle_24);

        TextView attendanceStatusText = (TextView) findViewById(R.id.attendance_status);
        attendanceStatusText.setText("DSA Attendance Marked!");

        TextView attendanceButton = (TextView) findViewById(R.id.attendanceButton);
        attendanceButton.setVisibility(View.GONE);
    }
}
