package com.pratik.productize.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pratik.productize.R;
import com.pratik.productize.asynchronous.AppExecutor;
import com.pratik.productize.database.Tasks;
import com.pratik.productize.database.TasksDB;

import static com.pratik.productize.utils.Constants.TASK_ID;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {

    private Tasks tasks ;

    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final long id = getArguments().getLong(TASK_ID);
        final TasksDB tasksDB = TasksDB.getInstance(getActivity());
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Tasks tasks = tasksDB.taskDAO().getActiveTask(id);
            }
        });
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    private void setEditContent(String taskText, long duration, int priority) {

        Toast.makeText(getActivity(), taskText + " " + duration + " " + priority, Toast.LENGTH_SHORT).show();
    }


}
