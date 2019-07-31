package com.pratik.productize.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.pratik.productize.R;


import com.pratik.productize.activites.MainActivity;
import com.pratik.productize.asynchronous.AppExecutor;
import com.pratik.productize.database.Tasks;

import com.pratik.productize.database.TasksDB;
import com.pratik.productize.ui.TaskViewModel;

import java.util.Objects;

import static com.pratik.productize.utils.Constants.SHOW;
import static com.pratik.productize.utils.Constants.TASK_ID;

public class EditFragment extends Fragment  {


    private EditText editTask;
    private Tasks task;

    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit,container,false);
        long id = 0;
        if (getArguments() != null) {
            id = getArguments().getLong(TASK_ID);
        }

        editTask = view.findViewById(R.id.edit_task);
        TaskViewModel viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        LiveData<Tasks> tasks = viewModel.getTask(id);
        tasks.observe(this, new Observer<Tasks>() {
            @Override
            public void onChanged(Tasks task) {

                if(task!= null){
                    setEditContent(task.getTaskText(),task.getDuration(),task.getPriority());
                    EditFragment.this.task = task;
                }else {
                    Toast.makeText(getActivity(), "Expected null task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        view.findViewById(R.id.saveEditTaskButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TasksDB db = TasksDB.getInstance(getActivity());

                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {

                        db.taskDAO().updateEditTask(editTask.getText().toString(),task.getPriority(),task.getDuration(),task.getId());
                    }
                });

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().popBackStack();
                ((MainActivity)getActivity()).toggleBottomBarVisibility(SHOW);
            }
        });

        return view;
    }

    private void setEditContent(String taskText, long duration, int priority) {

        Toast.makeText(getActivity(), taskText + " " + duration + " " + priority, Toast.LENGTH_SHORT).show();
        editTask.setText(taskText);
    }



}
