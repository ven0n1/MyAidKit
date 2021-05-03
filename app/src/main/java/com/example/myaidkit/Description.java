package com.example.myaidkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class Description extends AppCompatActivity {
    final String NAME = "name";
    final String FORM = "form";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        LinearLayout layout = findViewById(R.id.desc);
        TextView name = findViewById(R.id.DescName);
        TextView form = findViewById(R.id.DescForm);
        Button btn = findViewById(R.id.DescButton);
        name.setText(getIntent().getStringExtra(NAME));
        form.setText(getIntent().getStringExtra(FORM));
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
            v.setText("Note that we used View.GONE and not View.INVISIBLE. We would usually want to use the former for two main reasons.  Firstly, because  we wouldn’t want our expandable View to take up space while invisible. Secondly, using View.GONE provides at least to some extent an expanding/collapsing effect, since the space occupied expands & retracts along with its contents");
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