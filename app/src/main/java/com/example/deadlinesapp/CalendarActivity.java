package com.example.deadlinesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ListView;
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
    private ListView calendarListView;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    private View viewToClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        ColorDrawable redLight = new ColorDrawable(Color.parseColor("#a60c0c"));
        ColorDrawable redDark = new ColorDrawable(Color.parseColor("#510909"));
        calendarListView = findViewById(R.id.listViewCalendar);

        dbHelper = MainActivity.dbHelper;

        CaldroidFragment caldroidFragment = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
        args.putInt(CaldroidFragment.START_DAY_OF_WEEK, CaldroidFragment.MONDAY);

        caldroidFragment.setArguments(args);

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.cal_container, caldroidFragment);
        t.commit();

        colourDates(true, redDark, caldroidFragment);
        colourDates(false, redLight, caldroidFragment);

        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(Date date, View view) {
                String[] descriptions = new String[0];
                String[] subjects = new String[0];
                String[] hours = new String[0];
                if(viewToClear != null) {
                    viewToClear.setAlpha(1);
                }
                viewToClear = view;
                view.setAlpha((float) 0.25);

                Cursor c = dbHelper.getByDate(dateFormat.format(date));
                if(c.getCount() > 0){
                    descriptions = new String[c.getCount()];
                    subjects = new String[c.getCount()];
                    hours = new String[c.getCount()];
                    int i = 0;
                    while (c.moveToNext()){
                            subjects[i] = c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_SUBJECT));
                            descriptions[i] = c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_DESCRIPTION));
                            String dateAndHour = c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_DATE));
                            if(dateAndHour.length() > 13)
                                hours[i] = dateAndHour.substring(13);
                            else
                                hours[i] = "";
                            ++i;
                    }
                }
                CalendarTasksAdapter calendarTasksAdapter = new CalendarTasksAdapter(subjects, hours, descriptions, getApplicationContext());
                calendarListView.setAdapter(calendarTasksAdapter);
            }

        };
        caldroidFragment.setCaldroidListener(listener);

    }


    private void colourDates(boolean isDone, ColorDrawable colorDrawable, CaldroidFragment caldroidFragment){
        Cursor c = dbHelper.getTasks(isDone);
        while(c.moveToNext()){
            String dateString = c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_DATE)).substring(0, 10);
            Date date;
            try {
                date = dateFormat.parse(dateString);
                caldroidFragment.setBackgroundDrawableForDate(colorDrawable,  date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}