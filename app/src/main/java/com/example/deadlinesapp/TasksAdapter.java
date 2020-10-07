package com.example.deadlinesapp;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TasksAdapter extends BaseAdapter {

    private String[] dates;
    private String[] subjects;
    private String[] descriptions;
    private int[] ids;
    private LayoutInflater inflater;
    private Context c;
    private Animation slideRight;
    private Animation slideLeft;
    private MainActivity mainActivity;
    private boolean done;

    static class ViewHolder{
        TextView dateTextView;
        TextView subjectTextView;
        TextView descriptionTextView;
        ImageView doneImageView;
    }

    public TasksAdapter(String[] dates, String[] subjects, String[] descriptions, int ids[], boolean done, MainActivity mainActivity){
        this.dates = dates;
        this.subjects = subjects;
        this.descriptions = descriptions;
        this.c = mainActivity.getApplicationContext();
        this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.slideRight = AnimationUtils.loadAnimation(c, R.anim.slide_right_out);
        this.slideLeft = AnimationUtils.loadAnimation(c, R.anim.slide_left_out);
        this.ids = ids;
        this.mainActivity = mainActivity;
        this.done = done;
    }
    @Override
    public int getCount() {
        return subjects.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null){
            view = inflater.inflate(R.layout.listview_item, null);
            holder = new ViewHolder();
            holder.dateTextView = view.findViewById(R.id.dateTextView);
            holder.descriptionTextView = view.findViewById(R.id.descriptionTextView);
            holder.subjectTextView = view.findViewById(R.id.subjectTextView);
            holder.doneImageView = view.findViewById(R.id.doneImageView);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        if(done) {
            holder.doneImageView.setImageResource(android.R.drawable.checkbox_on_background);
            holder.descriptionTextView.setTextColor(Color.parseColor("#888888"));
            holder.subjectTextView.setTextColor(Color.parseColor("#888888"));
            holder.dateTextView.setTextColor(Color.parseColor("#888888"));
        }
        else
            holder.doneImageView.setImageResource(android.R.drawable.checkbox_off_background);

        holder.dateTextView.setText(dates[i]);
        holder.subjectTextView.setText(subjects[i]);
        holder.descriptionTextView.setText(descriptions[i]);
        View finalView = view;
        holder.doneImageView.setOnClickListener(view1 ->{
            if(done) {
                holder.doneImageView.setImageResource(android.R.drawable.checkbox_off_background);
                finalView.startAnimation(slideLeft);
            }else{
                holder.doneImageView.setImageResource(android.R.drawable.checkbox_on_background);
                finalView.startAnimation(slideRight);
            }
            Handler handler = new Handler();
            handler.postDelayed(() -> {
                MainActivity.dbHelper.changeIsDone(!done, ids[i]);
                mainActivity.showList(done);
            }, 150);

        });
        return view;
    }
}
