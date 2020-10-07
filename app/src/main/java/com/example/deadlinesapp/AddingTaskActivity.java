package com.example.deadlinesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.shawnlin.numberpicker.NumberPicker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddingTaskActivity extends AppCompatActivity {

    private EditText dateEditText;
    private EditText subjectEditText;
    private EditText descriptionEditText;
    private EditText hourEditText;
    private RelativeLayout datePickersLayout;
    private RelativeLayout hourPickersLayout;
    private Button okButton;
    private Button addButton;
    private String[] heights;
    private String[] days;
    private String[] months;
    private String[] hours;
    private String[] minutes;
    private NumberPicker datePicker1;
    private NumberPicker datePicker2;
    private NumberPicker datePicker3;
    private NumberPicker hourPicker;
    private NumberPicker minutePicker;
    private DbHelper dbHelper;
    private ImageView deleteHourImageView;
    private boolean update;
    private int idToUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adding_task_activity);

        datePicker1 = findViewById(R.id.datePicker1);
        datePicker2 = findViewById(R.id.datePicker2);
        datePicker3 = findViewById(R.id.datePicker3);
        hourPicker = findViewById(R.id.hourPicker);
        minutePicker = findViewById(R.id.minutePicker);
        dateEditText = findViewById(R.id.dateEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        subjectEditText = findViewById(R.id.subjectEditText);
        hourEditText = findViewById(R.id.hourEditText);
        addButton = findViewById(R.id.addButton);
        okButton = findViewById(R.id.okButton);
        datePickersLayout = findViewById(R.id.datePickersLayout);
        hourPickersLayout = findViewById(R.id.hourPickersLayout);
        deleteHourImageView = findViewById(R.id.deleteHourImageView);
        update = false;

        dbHelper = MainActivity.dbHelper;
        setPickers();

        if(getIntent().hasExtra("id")){
            update = true;
            idToUpdate = getIntent().getExtras().getInt("id");
            Cursor c = dbHelper.getById(idToUpdate);
            c.moveToNext();
            subjectEditText.setText(c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_SUBJECT)));
            descriptionEditText.setText(c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_DESCRIPTION)));
            String dateAndHour = c.getString(c.getColumnIndexOrThrow(DbNames.COLUMN_NAME_DATE));
            if(dateAndHour.length() > 14){
                hourEditText.setText(dateAndHour.substring(13));
                dateEditText.setText(dateAndHour.substring(0, 10));
                deleteHourImageView.setVisibility(View.VISIBLE);
            }else{
                dateEditText.setText(dateAndHour);
            }
        }

        dateEditText.setOnClickListener(view -> {
            datePickersLayout.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.VISIBLE);
            subjectEditText.setEnabled(false);
            descriptionEditText.setEnabled(false);
            hourEditText.setEnabled(false);
            addButton.setEnabled(false);
        });

        hourEditText.setOnClickListener(view -> {
            hourPickersLayout.setVisibility(View.VISIBLE);
            okButton.setVisibility(View.VISIBLE);
            subjectEditText.setEnabled(false);
            descriptionEditText.setEnabled(false);
            dateEditText.setEnabled(false);
            addButton.setEnabled(false);
        });

        okButton.setOnClickListener(view -> {
            if(datePickersLayout.getVisibility() == View.VISIBLE){
                datePickersLayout.setVisibility(View.GONE);
                String date = datePicker3.getValue() + "." + months[datePicker2.getValue()-1] + "." + days[datePicker1.getValue()-1];
                dateEditText.setText(date);
                hourEditText.setEnabled(true);
            }else{
                hourPickersLayout.setVisibility(View.GONE);
                String hour = hours[hourPicker.getValue()] + ":" + minutes[minutePicker.getValue()];
                hourEditText.setText(hour);
                dateEditText.setEnabled(true);
                deleteHourImageView.setVisibility(View.VISIBLE);
            }
            addButton.setEnabled(true);
            okButton.setVisibility(View.GONE);
            descriptionEditText.setEnabled(true);
            subjectEditText.setEnabled(true);
        });

        addButton.setOnClickListener(view ->{
            if(!subjectEditText.getText().toString().equals("") && !descriptionEditText.getText().toString().equals("") && !dateEditText.getText().toString().equals("")) {
                if(update)
                    dbHelper.update(idToUpdate, String.valueOf(subjectEditText.getText()), String.valueOf(dateEditText.getText() + "   " + hourEditText.getText()), String.valueOf(descriptionEditText.getText()));
                else
                    dbHelper.insertTask(String.valueOf(subjectEditText.getText()), String.valueOf(dateEditText.getText() + "   " + hourEditText.getText()), String.valueOf(descriptionEditText.getText()));
                Intent showMainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(showMainActivityIntent);
            }else{
                Toast.makeText(this, "Enter data", Toast.LENGTH_SHORT).show();
            }
        });

        deleteHourImageView.setOnClickListener(view -> {
            hourEditText.setText("");
            deleteHourImageView.setVisibility(View.GONE);
        });
    }

    private void setPickers(){
        String[] years = new String[100];
        hours = new String[24];
        minutes = new String[60];
        heights = new String[150];
        days = new String[31];
        months = new String[12];

        for(int i = 0; i < heights.length; i ++)
            heights[i] = i+100 + " cm";
        for(int i = 0; i < years.length; i ++)
            years[i] = String.valueOf(i + 2010);
        for(int i = 0; i < days.length; i++){
            if(i +1 < 10)
                days[i] = "0" + (i+1);
            else
                days[i] = i+1 + "";
        }
        for(int i = 0; i < months.length; i++){
            if(i +1 < 10)
                months[i] = "0" + (i+1);
            else
                months[i] = i+1 + "";
        }
        for(int i = 0; i < hours.length; i++){
            if(i < 10)
                hours[i] = "0" + i;
            else
                hours[i] = i + "";
        }
        for(int i = 0; i < minutes.length; i++){
            if(i < 10)
                minutes[i] = "0" + i;
            else
                minutes[i] = i + "";
        }
        Date date = new Date();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");
        datePicker3.setDisplayedValues(years);
        datePicker2.setDisplayedValues(months);
        datePicker1.setDisplayedValues(days);
        datePicker3.setValue(Integer.parseInt(yearFormat.format(date)));
        datePicker2.setValue(Integer.parseInt(monthFormat.format(date)));
        datePicker1.setValue(Integer.parseInt(dayFormat.format(date)));

        hourPicker.setDisplayedValues(hours);
        minutePicker.setDisplayedValues(minutes);

    }
}