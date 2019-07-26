package com.pratik.productize.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.pratik.productize.R;
import com.pratik.productize.Utils.PrefManager;

import java.util.Calendar;

import static android.graphics.Color.WHITE;
import static com.pratik.productize.Utils.Constants.TAG_HOME;

public class ScheduleTask extends AppCompatActivity {

    PrefManager prefManager;
    Button button15m,button30m,button60m;
    Button timeHome,timeWork;
    TextView minTextView;
    private static int minutes = 0;
    private int activeTag = TAG_HOME;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_task);

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(WHITE));
        getWindow().setStatusBarColor(WHITE);

        prefManager = new PrefManager(this);
        button15m = findViewById(R.id.button15m);
        button30m = findViewById(R.id.button30m);
        button60m = findViewById(R.id.button60m);
        minTextView = findViewById(R.id.textView4);
        radioGroup = findViewById(R.id.radioGroup);
        timeHome = findViewById(R.id.setTimeHome);
        timeWork = findViewById(R.id.setTimeWork);

        button15m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minutes+= 15;
                minTextView.setText(minutes + " min");
            }
        });

        button30m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minutes+=30;
                minTextView.setText(minutes + " min");
            }
        });

        button60m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minutes+=60;
                minTextView.setText(minutes + " min");
            }
        });


        timeHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Calendar mcurrentDate = Calendar.getInstance();
                int mHours = mcurrentDate.get(Calendar.HOUR_OF_DAY);
                int mMin = mcurrentDate.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(ScheduleTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(ScheduleTask.this, ""+ hourOfDay + " : " + minute , Toast.LENGTH_SHORT).show();
                        prefManager.setHomeTime(hourOfDay+":"+minute);

                    }
                },mHours,mMin,false);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        timeWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                int mHours = mcurrentDate.get(Calendar.HOUR_OF_DAY);
                int mMin = mcurrentDate.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(ScheduleTask.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Toast.makeText(ScheduleTask.this, ""+ hourOfDay + " : " + minute , Toast.LENGTH_SHORT).show();
                        prefManager.setWorkTime(hourOfDay+":"+minute);

                    }
                },mHours,mMin,false);

                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });



        if(!prefManager.isTaskScheduled()){

            getSupportActionBar().setTitle("Let's Start");
            //do some fancy animations
        }else {


            getSupportActionBar().setTitle("Schedule Tasks");
            //user has entered this activity from nav bar. save button should close activity not start new activity
        }



        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(radioGroup.getCheckedRadioButtonId() == R.id.home_radio)
                    activeTag = 0;
                else
                    activeTag = 1;


                prefManager.setTaskScheduled(true);
                prefManager.setActiveTag(activeTag);

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
