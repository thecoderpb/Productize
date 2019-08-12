package com.pratik.productize.activites;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.pratik.productize.R;
import com.pratik.productize.adapters.MarkItemSwiped;
import com.pratik.productize.utils.Converters;
import com.pratik.productize.utils.PrefManager;
import com.pratik.productize.adapters.ActiveTaskRecyclerAdapter;
import com.pratik.productize.ui.RecyclerViewClickListener;
import com.pratik.productize.database.Tasks;
import com.pratik.productize.ui.SimpleTouchHelper;
import com.pratik.productize.ui.TaskViewModel;

import java.util.List;
import java.util.Objects;

import static com.pratik.productize.utils.Constants.TAG_HOME;

public class ActiveTaskActivity extends AppCompatActivity implements
        RecyclerViewClickListener,
        MarkItemSwiped,
        View.OnClickListener{

    private TaskViewModel viewModel;
    private ActiveTaskRecyclerAdapter adapter;
    private PrefManager prefManager;
    private Converters converter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        prefManager = new PrefManager(this);
        converter = new Converters();

        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.taskRecyclerView);
        adapter = new ActiveTaskRecyclerAdapter(this, this, this);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (prefManager.getActiveTag() == TAG_HOME) {

            Objects.requireNonNull(getSupportActionBar()).setTitle("HOME");

            viewModel.getHomeTasks().observe(this, new Observer<List<Tasks>>() {
                @Override
                public void onChanged(List<Tasks> tasks) {
                    adapter.setTasksList(tasks);
                    setToolbarTitleText(String.valueOf(tasks.size()));
                    if(tasks.size() == 0){
                        Toast.makeText(ActiveTaskActivity.this, "No more tasks to perform", Toast.LENGTH_SHORT).show();
                        prefManager.setTaskActive(false);
                        prefManager.setTaskOngoing(false);
                        startActivity(new Intent(ActiveTaskActivity.this,WelcomeActivity.class));
                        finish();
                    }
                }
            });

            viewModel.getHomeDurationCount().observe(this, new Observer<Long>() {
                @Override
                public void onChanged(Long aLong) {
                    if(aLong != null)
                        setToolbarSubtitleText(String.valueOf(converter.timeLongToMin(aLong)));
                }
            });
        } else {

            Objects.requireNonNull(getSupportActionBar()).setTitle("WORK");

            viewModel.getWorkTasks().observe(this, new Observer<List<Tasks>>() {
                @Override
                public void onChanged(List<Tasks> tasks) {
                    adapter.setTasksList(tasks);
                    setToolbarTitleText(String.valueOf(tasks.size()));
                    if(tasks.size() == 0){
                        Toast.makeText(ActiveTaskActivity.this, "No more tasks to perform", Toast.LENGTH_SHORT).show();
                        prefManager.setTaskActive(false);
                        prefManager.setTaskOngoing(false);
                        startActivity(new Intent(ActiveTaskActivity.this,WelcomeActivity.class));
                        finish();
                    }
                }
            });

            viewModel.getWorkDurationCount().observe(this, new Observer<Long>() {
                @Override
                public void onChanged(Long aLong) {
                    if(aLong != null)
                        setToolbarSubtitleText(String.valueOf(converter.timeLongToMin(aLong)));
                }
            });


        }

        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleTouchHelper(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        findViewById(R.id.task_fab).setOnClickListener(this);

    }

    @Override
    public void recyclerViewClicked(View v, int position) {
        switch (v.getId()) {
            case R.id.deleteNotes:
                Tasks task = adapter.getTaskAtPosition(position);
                viewModel.delete(task);
                adapter.notifyItemRangeChanged(0, adapter.getItemCount());
                break;
            case R.id.editNote:
                Toast.makeText(this, "edit note" + position, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void itemSwiped(Tasks task) {

        viewModel.updateTaskPerformed(task.getId(), false);
        viewModel.updateTaskCompleted(task.getId(), false);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(ActiveTaskActivity.this, OnGoingTaskActivity.class);
        startActivity(intent);
        prefManager.setTaskOngoing(true);
        finish();

    }

    private void setToolbarSubtitleText(String duration) {

        TextView toolbarSubTitle = findViewById(R.id.current_toolbar_titles_task3);

        toolbarSubTitle.setText("Duration: " + duration);
    }

    private void setToolbarTitleText(String count) {

        TextView toolbarTitle = findViewById(R.id.current_toolbar_titles_task2);

        toolbarTitle.setText("Tasks: " + count);
    }

}

