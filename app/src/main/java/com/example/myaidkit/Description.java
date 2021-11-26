package com.example.myaidkit;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.myaidkit.dao.MedicineDao;
import com.example.myaidkit.entity.Medicine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Description extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {
    String[] info;
    AppDatabase appDatabase;
    MedicineDao medicineDao;
    Medicine medicine;
    String date, n, f, link;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        LinearLayout layout = findViewById(R.id.desc);
        TextView name = findViewById(R.id.DescName);
        TextView form = findViewById(R.id.DescForm);
        Button btn = findViewById(R.id.DescButton);
        Button aod = findViewById(R.id.AddOrDelete);
        TextView composition = findViewById(R.id.compositionDesc);
        TextView influence = findViewById(R.id.influenceDesc);
        TextView kinetics = findViewById(R.id.kineticsDesc);
        TextView indication = findViewById(R.id.indicationDesc);
        TextView dosage = findViewById(R.id.dosageDesc);
        TextView side_effects = findViewById(R.id.side_effectsDesc);
        TextView contra = findViewById(R.id.contraDesc);
        TextView special = findViewById(R.id.specialDesc);
        TextView textDate = findViewById(R.id.descTextDate);
        TextView Date = findViewById(R.id.descDate);


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        int count = layout.getChildCount();
        for(int i=1; i<count; i+=2) {
            TextView v = (TextView) layout.getChildAt(i);
            v.setVisibility(View.GONE);
        }
        for(int i=0; i<count; i+=2){
            TextView v = (TextView) layout.getChildAt(i);
            TextView add = (TextView) layout.getChildAt(i+1);
            v.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
                @Override
                public void onClick(View view) {
                    toggle_contents(view, add, v);
                }
            });
        }
        int type = getIntent().getIntExtra(Constants.TYPE, 0);
        n = getIntent().getStringExtra(Constants.NAME);
        f = getIntent().getStringExtra(Constants.FORM);
        link = getIntent().getStringExtra(Constants.LINK);
        name.setText(n);
        form.setText(f);
        appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "medicine").build();
        medicineDao = appDatabase.medicineDao();
        if(type == Constants.INTERNET){
            aod.setText("Добавить");
            try {
                info = new MyTask().execute(link).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(){
                @Override
                public void run() {
                    composition.setText(info[0]);
                    influence.setText(info[1]);
                    kinetics.setText(info[2]);
                    indication.setText(info[3]);
                    dosage.setText(info[4]);
                    side_effects.setText(info[5]);
                    contra.setText(info[6]);
                    special.setText(info[7]);
                }
            }.start();
            aod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CalendarFragment calendarFragment = new CalendarFragment();
                    calendarFragment.show(getSupportFragmentManager(), Constants.DATE);
                }
            });
        } else if(type == Constants.HOME){
            aod.setText("Удалить");
            Thread thread = new Thread(){
                @Override
                public void run() {
                    medicine = medicineDao.getByLink(link);
                }
            };
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            composition.setText(medicine.getComposition());
            influence.setText(medicine.getInfluence());
            kinetics.setText(medicine.getKinetics());
            indication.setText(medicine.getIndication());
            dosage.setText(medicine.getDosage());
            side_effects.setText(medicine.getSide_effects());
            contra.setText(medicine.getContra());
            special.setText(medicine.getSpecial());
            textDate.setVisibility(View.VISIBLE);
            Date.setVisibility(View.VISIBLE);
            Date.setText(medicine.getDate());
//            cursor.close();
            aod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    mydatabase.delete("Description", "Link = " + DatabaseUtils.sqlEscapeString(link), null);
                    Thread thread = new Thread(){
                        @Override
                        public void run() {
                            medicine = medicineDao.getByLink(link);
                            medicineDao.delete(medicine);
                        }
                    };
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
    }

    public static void slide_down(Context ctx, View v){

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
        if(a != null){
            a.reset();
            if(v != null){
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public static void slide_up(Context ctx, View v) {

        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void toggle_contents(View v, TextView txt, TextView d){

        if(txt.isShown()){
            txt.setVisibility(View.GONE);
            slide_up(this, txt);
            d.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, android.R.drawable.arrow_down_float,0);
        }
        else{
            txt.setVisibility(View.VISIBLE);
            slide_down(this, txt);
            d.setCompoundDrawablesRelativeWithIntrinsicBounds(0,0, android.R.drawable.arrow_up_float,0);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        date = dayOfMonth + "." + (month + 1) + "." + year;
        medicine = new Medicine(n, link, f, info[0], info[1], info[2], info[3], info[4], info[5], info[6], info[7], date);
        Thread thread = new Thread(){
            @Override
            public void run() {
                medicineDao.insert(medicine);
            }
        };
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class MyTask extends AsyncTask<String, Void, String[]> {
    Document doc;
    @Override
    protected String[] doInBackground(String... urls) {
        String url = urls[0];
        try {
            doc = Jsoup.connect("https://www.vidal.ru" + url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String composition = doc.select("#composition .block-content").text();
        String influence = doc.select("#influence .block-content").text();
        String kinetics = doc.select("#kinetics .block-content").text();
        String indication = doc.select("#indication .block-content").text();
        String dosage = doc.select("#dosage .block-content").text();
        String side_effects = doc.select("#side_effects .block-content").text();
        String contra = doc.select("#contra .block-content").text();
        String special = doc.select("#special .block-content").text();
        return new String[]{composition, influence, kinetics, indication, dosage, side_effects, contra, special};
    }

    @Override
    protected void onPostExecute(String[] info) {
        super.onPostExecute(info);
    }
}