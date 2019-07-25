package com.pratik.productize.fragments;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pratik.productize.R;

import com.pratik.productize.adapters.RecyclerViewClickListener;
import com.pratik.productize.adapters.TaskRecyclerAdapter;
import com.pratik.productize.database.Tasks;
import com.pratik.productize.ui.TaskViewModel;

import java.util.List;

import static com.pratik.productize.Utils.Constants.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeScreenFragment extends Fragment implements RecyclerViewClickListener {

    private TaskViewModel viewModel;
    private TaskRecyclerAdapter adapter;

    public HomeScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_home_screen,container,false);

        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.my_recycler_view_home);
        adapter = new TaskRecyclerAdapter(getActivity(),  this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(false);

        viewModel.getHomeTasks().observe(this, new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                adapter.setTasksList(tasks);
                if(adapter.getItemCount() == 0)
                    view.findViewById(R.id.empty_notes_view).setVisibility(View.VISIBLE);
                else
                    view.findViewById(R.id.empty_notes_view).setVisibility(View.INVISIBLE);
            }
        });

        viewModel.getHomeDurationCount().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                Log.i(TAG,"all duration "+ aLong);
            }
        });

        viewModel.getHomeTaskCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.i(TAG,"home count "+ integer);
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
        adapter.notifyDataSetChanged();
    }

}
