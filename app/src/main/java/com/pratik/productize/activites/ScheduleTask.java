package com.pratik.productize.activites;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pratik.productize.R;
import com.pratik.productize.Utils.PrefManager;


import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;

public class ScheduleTask extends AppCompatActivity {

    PrefManager prefManager;
    boolean option = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_task);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(WHITE));
        getWindow().setStatusBarColor(WHITE);

        prefManager = new PrefManager(this);

        if(!prefManager.isTaskScheduled()){

            //do some fancy animations
        }else {

            //user has entered this activity from nav bar. save button should close activity not start new activity
        }

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                prefManager.setTaskScheduled(true);
                Intent intent2 = getIntent();
                Intent intent = new Intent(ScheduleTask.this,MainActivity.class);
                if(!intent2.hasExtra("nav_schedule")){
                    startActivity(intent);
                    finish();
                }
                else
                    finish();

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();



    }
}
