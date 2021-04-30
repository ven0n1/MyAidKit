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

public class MedicineAdapter extends ArrayAdapter <Medicine> {

    public MedicineAdapter(@NonNull Context context, @NonNull Medicine[] objects) {
        super(context, R.layout.reminder_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Medicine reminder = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.medicine_item, null);
        }
        if (position%2==0){
            convertView.setBackgroundColor(Color.argb(150, 150, 150, 150));
        } else {
            convertView.setBackgroundColor(Color.argb(150, 255, 255, 255));
        }
        ((TextView) convertView.findViewById(R.id.name_medicine)).setText(reminder.getName());
        ((TextView) convertView.findViewById(R.id.form_medicine)).setText(reminder.getForm());
        return convertView;
    }
}
