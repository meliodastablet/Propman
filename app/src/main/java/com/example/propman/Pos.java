package com.example.propman;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.service.autofill.TextValueSanitizer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Pos extends AppCompatActivity implements View.OnClickListener {
    String[] date_portions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pos);
        findViewById(R.id.pay).setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.pay) {
            Toast.makeText(Pos.this,
                    "Successful!!",
                    Toast.LENGTH_SHORT).show();

            Date time = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            date_portions = currentDateandTime.split("_");
            String year = date_portions[0];
            String month = date_portions[1];
            String day = date_portions[2];
            String hour = date_portions[3];
            String minute = date_portions[4];

            Calendar beginTime = Calendar.getInstance();
            beginTime.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute));
            Calendar endTime = Calendar.getInstance();
            endTime.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)+1, Integer.parseInt(hour), Integer.parseInt(minute));

            String title_value= "EV";
            Intent intent1 = new Intent(Intent.ACTION_INSERT)
                    .setData(CalendarContract.Events.CONTENT_URI)
                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTimeInMillis())
                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.getTimeInMillis())
                    .putExtra(CalendarContract.Events.TITLE, title_value)
                    .putExtra(CalendarContract.Events.DESCRIPTION, "Rent Date:"+time);
            startActivity(intent1);




        }

    }}