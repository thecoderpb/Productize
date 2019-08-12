package com.pratik.productize.activites;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pratik.productize.R;
import com.pratik.productize.asynchronous.OnGoingTaskService;
import com.pratik.productize.database.Tasks;
import com.pratik.productize.ui.TaskViewModel;
import com.pratik.productize.utils.Constants;
import com.pratik.productize.utils.Converters;
import com.pratik.productize.utils.PrefManager;

import java.util.Objects;

import static com.pratik.productize.adapters.ActiveTaskRecyclerAdapter.tasksList;

public class OnGoingTaskActivity extends AppCompatActivity {


    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private TextView taskText,timeText;
    private static int currentTaskCount = 0;
    private TaskViewModel viewModel;
    private PrefManager prefManager;
    private Intent service;
    private static CountDownTimer timer;
    public static boolean currentTaskActive = false;
    private  Converters converters;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        if(tasksList == null){
            startActivity(new Intent(this,ActiveTaskActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        service = new Intent(this,OnGoingTaskService.class);

        if(tasksList == null){
            startActivity(new Intent(this,ActiveTaskActivity.class));
            finish();
        }else {


            setContentView(R.layout.activity_on_going_task_activitiy);

            viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

            mVisible = true;
            mControlsView = findViewById(R.id.fullscreen_content_controls);
            mContentView = findViewById(R.id.fullscreen_content);
            prefManager = new PrefManager(this);
            taskText = findViewById(R.id.task_text);
            timeText = findViewById(R.id.time_left);


            // Set up the user interaction to manually show or hide the system UI.
            mContentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggle();
                }
            });

            converters = new Converters();

            findViewById(R.id.exit_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentTaskCount++;
                    showTask();
                }
            });

            startService(service);

            showTask();

        }


    }

    private void showTask() {

        if(currentTaskCount < tasksList.size()){

            Objects.requireNonNull(getSupportActionBar()).setTitle("Task "+(currentTaskCount +1) + "/" + tasksList.size());
            Log.i(Constants.TAG,"current task " + (currentTaskCount +1) + " duration " + tasksList.get(currentTaskCount).getDuration());

            taskText.setText(tasksList.get(currentTaskCount).getTaskText());
            String text = converters.timeLongToTimerFormat(tasksList.get(currentTaskCount).getDuration());
            Log.i(Constants.TAG,"time " + text);
            timeText.setText(text);
            startTimer(tasksList.get(currentTaskCount).getDuration());
        }else {
            for(Tasks task : tasksList){
                viewModel.updateTaskCompleted(task.getId(),true);
                viewModel.updateTaskPerformed(task.getId(),false);
            }

            stopService(service);
            prefManager.setTaskOngoing(false);
            prefManager.setTaskActive(false);
            currentTaskCount = 0;
            Toast.makeText(this, "All task(s) done for the day!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }


    }



    private void startTimer(long duration) {

        currentTaskActive = true;

        if (timer != null) {
            timer.cancel();
        }

        timer = new CountDownTimer(duration, 1000) {
            @Override
            public void onTick(long l) {

                timeText.setText(converters.timeLongToTimerFormat(l));
            }

            @Override
            public void onFinish() {
                Log.i(Constants.TAG,"Task completed");
                show();
                currentTaskActive = false;
                timeText.setVisibility(View.GONE);
            }
        };

        timer.start();
    }

    @Override
    public void onBackPressed() {

//        if(currentTaskActive){
//            new AlertDialog.Builder(this)
//                    .setTitle("Task Active")
//                    .setMessage("are you sure you want to quit?")
//                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//
//                        }
//                    })
//                    .setNegativeButton("NO",null)
//                    .show();
//
//        }else
            super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, 200);
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        // Show the system bar
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

}
