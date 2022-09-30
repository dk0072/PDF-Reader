package com.dktechnology.pdfreader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class Initialization extends AppCompatActivity {

    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initialization);

        tv = findViewById(R.id.rotatind);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.rotate);
        tv.setAnimation(anim);

        ActivityManager.TaskDescription description = new ActivityManager.TaskDescription("PDF Reader", BitmapFactory.decodeResource(this.getResources(),R.drawable.pdf_logo));
        this.setTaskDescription(description);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

             startActivity(new Intent(Initialization.this,MainActivity.class));

            }
        },2500);
    }
}