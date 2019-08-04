package com.pratik.productize.activites;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Intent;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.pratik.productize.R;
import com.pratik.productize.utils.PrefManager;

import java.util.Calendar;
import java.util.Objects;


import static android.graphics.Color.WHITE;
import static com.pratik.productize.utils.Constants.TAG_HOME;

public class ScheduleTask extends AppCompatActivity {

    PrefManager prefManager;
    Button button15m,button30m,button60m;
    Button timeHome,timeWork;
    TextView minTextView;
    private static int minutes = 0;
    private int activeTag = TAG_HOME;
    private RadioGroup radioGroup;
    private Button buttonSave;
    ImageView avatarImage;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_task);

        Objects.requireNonNull(getSupportActionBar()).setBackgroundDrawable(new ColorDrawable(WHITE));
        getWindow().setStatusBarColor(WHITE);

        prefManager = new PrefManager(this);
        button15m = findViewById(R.id.chip15m);
        button30m = findViewById(R.id.chip30m);
        button60m = findViewById(R.id.chip60m);
        minTextView = findViewById(R.id.textView4);
        radioGroup = findViewById(R.id.radioGroup);
        timeHome = findViewById(R.id.setTimeHome);
        timeWork = findViewById(R.id.setTimeWork);
        avatarImage = findViewById(R.id.avatar_img);
        buttonSave = findViewById(R.id.buttonSave);


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

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                //clearCheck();
                radioGroup.check(i);
                if(radioGroup.getCheckedRadioButtonId() == R.id.work_radio){
                    avatarImage.setImageResource(R.drawable.work_man_avatar);
                }else
                    avatarImage.setImageResource(R.drawable.home_man_avatar);
            }
        });

        LinearLayout layout1 = findViewById(R.id.linear_layout1);
        LinearLayout layout2 = findViewById(R.id.linear_layout2);
        LinearLayout layout3 = findViewById(R.id.linear_layout3);
        LinearLayout layout4 = findViewById(R.id.linear_layout4);
        LinearLayout layout5 = findViewById(R.id.linear_layout5);

//        layout1.setVisibility(View.VISIBLE);
//        layout2.setVisibility(View.INVISIBLE);
//        layout3.setVisibility(View.INVISIBLE);
//        layout4.setVisibility(View.INVISIBLE);
//        layout5.setVisibility(View.INVISIBLE);


        if(!prefManager.isTaskScheduled()){

            getSupportActionBar().setTitle("Let's Start");

            //do some fancy animations
//            layout1.setY(100f);
//            layout1.setAlpha(1f);
//            layout1.animate().translationYBy(-100f).setStartDelay(200).setDuration(600).start();





        }else {

            getSupportActionBar().setTitle("Schedule Tasks");
            //user has entered this activity from nav bar. save button should close activity not start new activity

            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.VISIBLE);
            layout3.setVisibility(View.VISIBLE);
            layout4.setVisibility(View.VISIBLE);
            layout5.setVisibility(View.VISIBLE);
        }



        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(radioGroup.getCheckedRadioButtonId() == R.id.home_radio)
                    activeTag = 0;
                else
                    activeTag = 1;


                prefManager.setTaskScheduled(true);
                prefManager.setActiveTag(activeTag);
                prefManager.setHours(minutes*60*1000);

                Intent intent2 = getIntent();
                Intent intent = new Intent(ScheduleTask.this,MainActivity.class);
                if(!intent2.hasExtra("nav_schedule") && isAllOptionsSelected()){
                    startActivity(intent);
                    finish();
                }
                else if(!isAllOptionsSelected()){
                    Toast.makeText(ScheduleTask.this, "Please answer all questions in order to proceed", Toast.LENGTH_SHORT).show();
                }else
                    finish();

            }
        });
    }

    private boolean isAllOptionsSelected() {

        return prefManager.getHours() != 0 && prefManager.getHomeTime() != null && prefManager.getWorkTime() != null;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
