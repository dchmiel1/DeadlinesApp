package com.example.deadlinesapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CalendarTasksAdapter extends BaseAdapter {

    String[] descriptions;
    String[] subjects;
    String[] hours;
    LayoutInflater inflater;

    static class ViewHolder{
        TextView hour;
        TextView subject;
        TextView description;
    }

    public CalendarTasksAdapter(String[] subjects, String[] hours, String[] descriptions, Context c){
        this.subjects = subjects;
        this.hours = hours;
        this.descriptions = descriptions;
        this.inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            view = inflater.inflate(R.layout.listview_item_in_calendar, null);
            holder = new ViewHolder();
            holder.hour = view.findViewById(R.id.hourCal);
            holder.description = view.findViewById(R.id.descriptionCal);
            holder.subject = view.findViewById(R.id.subjectCal);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }

        holder.description.setText(descriptions[i]);
        holder.subject.setText(subjects[i]);
        holder.hour.setText(hours[i]);

        return view;
    }
}
