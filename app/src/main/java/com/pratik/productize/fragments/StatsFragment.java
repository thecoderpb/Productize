package com.pratik.productize.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pratik.productize.R;
import com.pratik.productize.database.Tasks;
import com.pratik.productize.ui.TaskViewModel;

import java.util.List;

import io.fabric.sdk.android.services.concurrency.Task;


public class StatsFragment extends Fragment {


    private TaskViewModel viewModel;
    private TextView textView,textView2,textView3,textView4;

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_stats, container, false);
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        viewModel.getCompleteTasks().observe(this, new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                textView = view.findViewById(R.id.stats_tv);
                textView.setText("Completed tasks " + tasks.size());
            }
        });

        viewModel.getIncompleteTasks().observe(this, new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                textView2 = view.findViewById(R.id.stats_tv2);
                textView2.setText("Incomplete tasks " + tasks.size());
            }
        });

        viewModel.getAllTaskCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textView3 = view.findViewById(R.id.stats_tv3);
                textView3.setText("Tasks remaining today: " + integer);
            }
        });





        return view;
    }

}
