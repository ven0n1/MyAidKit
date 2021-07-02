package com.example.myaidkit;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class Description extends AppCompatActivity
        implements DatePickerDialog.OnDateSetListener {
    final String NAME = "name";
    final String LINK = "link";
    final String FORM = "form";
    final String TYPE = "type";
    final String DATE = "date input";
    final int INTERNET = 1;
    final int HOME = 2;
    String[] info;
    SQLiteDatabase mydatabase;
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
        int type = getIntent().getIntExtra(TYPE, 0);
        n = getIntent().getStringExtra(NAME);
        f = getIntent().getStringExtra(FORM);
        link = getIntent().getStringExtra(LINK);
        name.setText(n);
        form.setText(f);
        File dbpath = getApplicationContext().getDatabasePath("medicines");
        if (!Objects.requireNonNull(dbpath.getParentFile()).exists()) {
            dbpath.getParentFile().mkdirs();
        }
        mydatabase = SQLiteDatabase.openOrCreateDatabase(dbpath,null);
        if(type == INTERNET){
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
//                    Medicine medicine = new Medicine(n, link, f, info[0], info[1], info[2], info[3], info[4], info[5], info[6], info[7]);
                    CalendarFragment calendarFragment = new CalendarFragment();
                    calendarFragment.show(getSupportFragmentManager(), DATE);
                }
            });
        } else if(type == HOME){
            aod.setText("Удалить");
            Cursor cursor = mydatabase.rawQuery("SELECT * FROM Description WHERE Link = " + DatabaseUtils.sqlEscapeString(link) + ";", null);
            cursor.moveToFirst();
            composition.setText(cursor.getString(3));
            influence.setText(cursor.getString(4));
            kinetics.setText(cursor.getString(5));
            indication.setText(cursor.getString(6));
            dosage.setText(cursor.getString(7));
            side_effects.setText(cursor.getString(8));
            contra.setText(cursor.getString(9));
            special.setText(cursor.getString(10));
            textDate.setVisibility(View.VISIBLE);
            Date.setVisibility(View.VISIBLE);
            Date.setText(cursor.getString(11));
            cursor.close();
            aod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mydatabase.delete("Description", "Link = " + DatabaseUtils.sqlEscapeString(link), null);
                    setResult(RESULT_OK);
//                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.dashboard);
//                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//                    fragmentTransaction.detach(currentFragment);
//                    fragmentTransaction.attach(currentFragment);
//                    fragmentTransaction.commit();
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
        mydatabase.execSQL("INSERT INTO Description(Name, Link, Form, Composition, Influence, Kinetics, Indication, Dosage, SideEffects, Contra, Special, Date) VALUES(" +
                DatabaseUtils.sqlEscapeString(n) + ", " +
                DatabaseUtils.sqlEscapeString(link) + ", " +
                DatabaseUtils.sqlEscapeString(f) + ", " +
                DatabaseUtils.sqlEscapeString(info[0]) + ", " +
                DatabaseUtils.sqlEscapeString(info[1]) + ", " +
                DatabaseUtils.sqlEscapeString(info[2]) + ", " +
                DatabaseUtils.sqlEscapeString(info[3]) + ", " +
                DatabaseUtils.sqlEscapeString(info[4]) + ", " +
                DatabaseUtils.sqlEscapeString(info[5]) + ", " +
                DatabaseUtils.sqlEscapeString(info[6]) + ", " +
                DatabaseUtils.sqlEscapeString(info[7]) +", " +
                DatabaseUtils.sqlEscapeString(date) +
                ");");
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