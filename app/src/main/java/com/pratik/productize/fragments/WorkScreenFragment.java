package com.pratik.productize.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pratik.productize.R;
import com.pratik.productize.adapters.HomeRecyclerAdapter;
import com.pratik.productize.adapters.RecyclerViewClickListener;
import com.pratik.productize.adapters.TaskRecyclerAdapter;
import com.pratik.productize.adapters.WorkRecyclerAdapter;
import com.pratik.productize.database.Tasks;
import com.pratik.productize.ui.TaskViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkScreenFragment extends Fragment implements RecyclerViewClickListener {

    private TaskViewModel viewModel;
    private WorkRecyclerAdapter adapter;

    public WorkScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_work_screen, container, false);

        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.my_recycler_view_work);
        adapter = new WorkRecyclerAdapter(getActivity(),  this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);

        viewModel.getWorkTasks().observe(this, new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                adapter.setTasksList(tasks);
            }
        });

        return view;
    }

    @Override
    public void recyclerViewClicked(View v, int position) {

        switch (v.getId()){
            case R.id.deleteNotes:
                Toast.makeText(getActivity(), "delete note" + position, Toast.LENGTH_SHORT).show();
                Tasks task = adapter.getTaskAtPosition(position);
                viewModel.delete(task);
                adapter.notifyItemRangeChanged(0,adapter.getItemCount());
                break;
            case R.id.editNote:
                Toast.makeText(getActivity(), "edit note" + position, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
