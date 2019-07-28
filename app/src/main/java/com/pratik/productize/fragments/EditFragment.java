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
import android.widget.Toast;

import com.pratik.productize.R;


import com.pratik.productize.database.Tasks;

import com.pratik.productize.ui.TaskViewModel;

import static com.pratik.productize.utils.Constants.TASK_ID;

public class EditFragment extends Fragment {


    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final long id = getArguments().getLong(TASK_ID);

        TaskViewModel viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        LiveData<Tasks> tasks = viewModel.getTask(id);
        tasks.observe(this, new Observer<Tasks>() {
            @Override
            public void onChanged(Tasks tasks) {

                if(tasks!= null){
                    setEditContent(tasks.getTaskText(),tasks.getDuration(),tasks.getPriority());
                }else {
                    Toast.makeText(getActivity(), "Expected null task", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    private void setEditContent(String taskText, long duration, int priority) {

        Toast.makeText(getActivity(), taskText + " " + duration + " " + priority, Toast.LENGTH_SHORT).show();
    }


}
