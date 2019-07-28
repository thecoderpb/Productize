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
import android.widget.Toast;

import com.pratik.productize.R;
import com.pratik.productize.utils.PrefManager;
import com.pratik.productize.adapters.ActiveTaskRecyclerAdapter;
import com.pratik.productize.ui.RecyclerViewClickListener;
import com.pratik.productize.database.Tasks;
import com.pratik.productize.ui.SimpleTouchHelper;
import com.pratik.productize.ui.TaskViewModel;

import java.util.List;

import static com.pratik.productize.utils.Constants.TAG_HOME;

public class ActiveTaskActivity extends AppCompatActivity implements RecyclerViewClickListener {

    private TaskViewModel viewModel;
    private ActiveTaskRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        final PrefManager prefManager = new PrefManager(this);

        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        RecyclerView recyclerView  = findViewById(R.id.taskRecyclerView);
        adapter = new ActiveTaskRecyclerAdapter(this,this);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layout);
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if(prefManager.getActiveTag() == TAG_HOME){
            viewModel.getHomeTasks().observe(this, new Observer<List<Tasks>>() {
                @Override
                public void onChanged(List<Tasks> tasks) {
                    adapter.setTasksList(tasks);
                }
            });
        }else{
            viewModel.getWorkTasks().observe(this, new Observer<List<Tasks>>() {
                @Override
                public void onChanged(List<Tasks> tasks) {
                    adapter.setTasksList(tasks);
                }
            });

        }

        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback = new SimpleTouchHelper(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        findViewById(R.id.task_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActiveTaskActivity.this,OnGoingTaskActivitiy.class);
                startActivity(intent);
                prefManager.setTaskOngoing(true);
                finish();

            }
        });

    }

    @Override
    public void recyclerViewClicked(View v, int position) {
        switch (v.getId()){
            case R.id.deleteNotes:
                Tasks task = adapter.getTaskAtPosition(position);
                viewModel.delete(task);
                adapter.notifyItemRangeChanged(0,adapter.getItemCount());
                break;
            case R.id.editNote:
                Toast.makeText(this, "edit note" + position, Toast.LENGTH_SHORT).show();
                break;
            case R.id.card_layout:
                Tasks tasks = adapter.getTaskAtPosition(position);
                viewModel.delete(tasks);
                adapter.notifyItemRangeChanged(0,adapter.getItemCount());
                break;

        }
    }


}
