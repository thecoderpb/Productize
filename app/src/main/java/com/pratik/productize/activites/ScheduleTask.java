package com.pratik.productize.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.pratik.productize.R;
import com.pratik.productize.Utils.PrefManager;

public class ScheduleTask extends AppCompatActivity {

    PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_task);


        prefManager = new PrefManager(this);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prefManager.setTaskScheduled(true);
                Intent intent = new Intent(ScheduleTask.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
