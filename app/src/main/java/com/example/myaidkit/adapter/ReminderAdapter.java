package com.example.myaidkit.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.myaidkit.R;
import com.example.myaidkit.entity.Reminder;

import java.util.Calendar;

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
            convertView.setBackgroundColor(Color.argb(190, 127, 255, 0));
        } else {
            convertView.setBackgroundColor(Color.argb(190, 152, 251, 152));
        }
        ((TextView) convertView.findViewById(R.id.name_reminder)).setText(reminder.getName());
        ((TextView) convertView.findViewById(R.id.quantity_reminder)).setText(reminder.getQuantity().toString());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reminder.getTime());
        ((TextView) convertView.findViewById(R.id.time_reminder)).setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE));
        return convertView;
    }
}
