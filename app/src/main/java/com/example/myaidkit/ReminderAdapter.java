package com.example.myaidkit;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ReminderAdapter extends ArrayAdapter <Reminder> {

    public ReminderAdapter(@NonNull Context context, @NonNull Reminder[] objects) {
        super(context, R.layout.reminder_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reminder reminder = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reminder_item, null);
        }
        if (position%2==0){
            convertView.setBackgroundColor(Color.argb(150, 150, 150, 150));
        } else {
            convertView.setBackgroundColor(Color.argb(150, 255, 255, 255));
        }
        ((TextView) convertView.findViewById(R.id.name_reminder)).setText(reminder.getName());
        ((TextView) convertView.findViewById(R.id.quantity_reminder)).setText(reminder.getQuantity().toString());
        ((TextView) convertView.findViewById(R.id.time_reminder)).setText(reminder.getTime());
        return convertView;
    }
}
