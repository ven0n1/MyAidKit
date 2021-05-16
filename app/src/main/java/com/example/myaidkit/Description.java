package com.example.myaidkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class Description extends AppCompatActivity {
    final String NAME = "name";
    final String LINK = "link";
    final String FORM = "form";
    final String TYPE = "type";
    final int INTERNET = 1;
    final int HOME = 2;
    String[] info;
    SQLiteDatabase mydatabase;

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
        String n = getIntent().getStringExtra(NAME);
        String f = getIntent().getStringExtra(FORM);
        String link = getIntent().getStringExtra(LINK);
        name.setText(n);
        form.setText(f);
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
                    mydatabase = openOrCreateDatabase("medicines", MODE_PRIVATE,null);
                    mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Description(" +
                            "Name TEXT, " +
                            "Link TEXT PRIMARY KEY, " +
                            "Form TEXT, " +
                            "Composition TEXT, " +
                            "Influence TEXT, " +
                            "Kinetics TEXT, " +
                            "Indication TEXT, " +
                            "Dosage TEXT, " +
                            "SideEffects TEXT, " +
                            "Contra TEXT, " +
                            "Special TEXT" +
                            ");");
                    mydatabase.execSQL("INSERT INTO Description(Name, Link, Form, Composition, Influence, Kinetics, Indication, Dosage, SideEffects, Contra, Special) VALUES(" +
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
                            DatabaseUtils.sqlEscapeString(info[7]) +
                            ");");
                }
            });
        } else if(type == HOME){
            aod.setText("Удалить");
            mydatabase = SQLiteDatabase.openOrCreateDatabase("/data/data/com.example.myaidkit/databases/medicines",null);
            Cursor cursor = mydatabase.rawQuery("SELECT * FROM Description WHERE Link = " + DatabaseUtils.sqlEscapeString(link) + ";", null);
            cursor.moveToFirst();
            new Thread(){
                @Override
                public void run() {
                    composition.setText(cursor.getString(3));
                    influence.setText(cursor.getString(4));
                    kinetics.setText(cursor.getString(5));
                    indication.setText(cursor.getString(6));
                    dosage.setText(cursor.getString(7));
                    side_effects.setText(cursor.getString(8));
                    contra.setText(cursor.getString(9));
                    special.setText(cursor.getString(10));
                }
            }.start();
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