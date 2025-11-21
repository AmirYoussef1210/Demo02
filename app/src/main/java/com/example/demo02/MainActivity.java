package com.example.demo02;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends MasterClass {
    TimePicker tP;
    TextView tV;
    String formattedHour, formattedMinute;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    private int ALARM_RQST_CODE = 1;
    String text = "ALARM ALARM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tP = findViewById(R.id.tP);
        tV = findViewById(R.id.tV);

        tP.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                int hour = hourOfDay;
                String amPm;

                // Determine AM or PM and adjust hour
                if (hour == 0) {
                    hour += 12;
                    amPm = "AM";
                } else if (hour == 12) {
                    amPm = "PM";
                } else if (hour > 12) {
                    hour -= 12;
                    amPm = "PM";
                } else {
                    amPm = "AM";
                }
                // Format hour and minute for display
                formattedHour = (hour < 10) ? "0" + hour : String.valueOf(hour);
                formattedMinute = (minute < 10) ? "0" + minute : String.valueOf(minute);
            }
        });



    }





    public void setNotif(View view) {
        int hour = tP.getHour();
        int minute = tP.getMinute();

        Calendar calNow = Calendar.getInstance();
        Calendar calSet = (Calendar) calNow.clone();

        calSet.set(Calendar.HOUR_OF_DAY, hour);
        calSet.set(Calendar.MINUTE, minute);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if(calSet.compareTo(calNow) <= 0) {
            calSet.add(Calendar.DATE, 1);
        }
        setAlarm(calSet, text);
    }

    private void setAlarm(Calendar calSet, String text) {
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("text", text);
        alarmIntent = PendingIntent.getBroadcast(this, ALARM_RQST_CODE, intent, PendingIntent.FLAG_IMMUTABLE);
        alarmMgr = (AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
        alarmMgr.set(AlarmManager.RTC, calSet.getTimeInMillis(), alarmIntent);
        tV.setText("ALARM SET");
    }
}