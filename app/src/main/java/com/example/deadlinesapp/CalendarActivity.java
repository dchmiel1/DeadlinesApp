package com.example.deadlinesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.roomorama.caldroid.CellView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CalendarActivity extends AppCompatActivity {

    private DbHelper dbHelper;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        dbHelper = MainActivity.dbHelper;
        dateFormat = new SimpleDateFormat("yyyy.MM.dd");

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.cal_container, caldroidFragment);
        t.commit();

        ColorDrawable redLight = new ColorDrawable(Color.parseColor("#a60c0c"));
        ColorDrawable redDark = new ColorDrawable(Color.parseColor("#950b0b"));
        Cursor c = dbHelper.getTasks(false);
            while(c.moveToNext()){
                String dateString = c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_DATE)).substring(0, 10);
                Date date;
                try {
                    date = dateFormat.parse(dateString);
                    caldroidFragment.setBackgroundDrawableForDate(redLight,  date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
        }
        c = dbHelper.getTasks(true);
        while(c.moveToNext()){
            String dateString = c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_DATE)).substring(0, 10);
            Date date;
            try {
                date = dateFormat.parse(dateString);
                caldroidFragment.setBackgroundDrawableForDate(redDark,  date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), String.valueOf(date),
                        Toast.LENGTH_SHORT).show();
            }

        };
        caldroidFragment.setCaldroidListener(listener);

    }
}