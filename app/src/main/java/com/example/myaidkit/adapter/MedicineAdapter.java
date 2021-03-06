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
import com.example.myaidkit.entity.Medicine;

public class MedicineAdapter extends ArrayAdapter <Medicine> {

    public MedicineAdapter(@NonNull Context context, @NonNull Medicine[] objects) {
        super(context, R.layout.medicine_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Medicine medicine = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.medicine_item, null);
        }
        if (position%2==0){
            convertView.setBackgroundColor(Color.argb(190, 127, 255, 0));
        } else {
            convertView.setBackgroundColor(Color.argb(190, 152, 251, 152));
        }
        ((TextView) convertView.findViewById(R.id.name_medicine)).setText(medicine.getName());
        ((TextView) convertView.findViewById(R.id.form_medicine)).setText(medicine.getForm());
        return convertView;
    }
}
