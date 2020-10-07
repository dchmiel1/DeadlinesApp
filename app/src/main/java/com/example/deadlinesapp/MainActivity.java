package com.example.deadlinesapp;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    public static DbHelper dbHelper;
    private ListView listView;
    private ImageView add;
    private TextView nthToShow;
    private ImageView editImageView;
    private ImageView deleteImageView;
    private ImageView calendarImageView;
    private BottomNavigationView navView;
    private boolean isDone;
    private View optionsView;
    private View darkView;
    private int selected;
    private int[] ids;

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        theme.applyStyle(R.style.AppThemeWithoutBar, true);
        return theme;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add = findViewById(R.id.addImageView);
        listView = findViewById(R.id.listView);
        nthToShow = findViewById(R.id.nthToShwTextView);
        deleteImageView = findViewById(R.id.deleteImageVIew);
        editImageView = findViewById(R.id.editImageView);
        navView = findViewById(R.id.navView);
        darkView = findViewById(R.id.darkView);
        optionsView = findViewById(R.id.optionsView);
        calendarImageView = findViewById(R.id.calendarImageView);

        final Animation slideUp = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);

        if(dbHelper == null)
            dbHelper = new DbHelper(this, this);

        dbHelper.showAll();

        showList(false);

        add.setOnClickListener(view ->{
            Intent showAddingIntent = new Intent(getApplicationContext(), AddingTaskActivity.class);
            startActivity(showAddingIntent);
        });

        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            selected = i;
            view.setBackgroundColor(Color.parseColor("#AA1d1d"));
            deleteImageView.setVisibility(View.VISIBLE);
            editImageView.setVisibility(View.VISIBLE);
            darkView.setVisibility(View.VISIBLE);
            optionsView.setVisibility(View.VISIBLE);
            editImageView.startAnimation(slideUp);
            deleteImageView.startAnimation(slideUp);
            optionsView.startAnimation(slideUp);
            return true;
        });

        navView.setOnNavigationItemSelectedListener(item -> {
            if(item.toString().equals("To do"))
                isDone = false;
            else
                isDone = true;
            showList(isDone);
            darkView.setVisibility(View.GONE);
            optionsView.setVisibility(View.GONE);
            deleteImageView.setVisibility(View.GONE);
            editImageView.setVisibility(View.GONE);
            return true;
        });

        darkView.setOnClickListener(view ->{
            listView.getChildAt(selected).setBackgroundColor(0);
            darkView.setVisibility(View.GONE);
            optionsView.setVisibility(View.GONE);
            deleteImageView.setVisibility(View.GONE);
            editImageView.setVisibility(View.GONE);
        });

        deleteImageView.setOnClickListener(view ->{
            dbHelper.deleteById(ids[selected]);
            darkView.setVisibility(View.GONE);
            optionsView.setVisibility(View.GONE);
            deleteImageView.setVisibility(View.GONE);
            editImageView.setVisibility(View.GONE);
            showList(isDone);
        });

        editImageView.setOnClickListener(view ->{
            listView.getChildAt(selected).setBackgroundColor(0);
            darkView.setVisibility(View.GONE);
            optionsView.setVisibility(View.GONE);
            deleteImageView.setVisibility(View.GONE);
            editImageView.setVisibility(View.GONE);
            Intent editIntent = new Intent(getApplicationContext(), AddingTaskActivity.class);
            editIntent.putExtra("id", ids[selected]);
            startActivity(editIntent);
        });

        calendarImageView.setOnClickListener(view -> {
            Intent calendarIntent = new Intent(getApplicationContext(), CalendarActivity.class);
            startActivity(calendarIntent);
        });

    }

    public void showList(boolean isDone){
        String[] subjects = new String[0];
        String[] descriptions = new String[0];
        String[] dates = new String[0];
        ids = new int[0];
        Cursor c = dbHelper.getTasks(isDone);
        if(c.getCount() > 0) {
            subjects = new String[c.getCount()];
            descriptions = new String[c.getCount()];
            dates = new String[c.getCount()];
            ids = new int[c.getCount()];
            int i = 0;
            while (c.moveToNext()) {
                dates[i] = c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_DATE));
                subjects[i] = c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_SUBJECT));
                descriptions[i] = c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_DESCRIPTION));
                ids[i] = c.getInt(c.getColumnIndexOrThrow(DbNames._ID));
                ++i;
            }
            nthToShow.setVisibility(View.GONE);
        }else{
            nthToShow.setVisibility(View.VISIBLE);
        }

        TasksAdapter tasksAdapter = new TasksAdapter(dates, subjects, descriptions, ids, isDone, this);
        listView.setAdapter(tasksAdapter);

    }
}