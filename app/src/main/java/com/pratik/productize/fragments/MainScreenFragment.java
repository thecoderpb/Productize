package com.pratik.productize.fragments;



import android.content.Context;
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

import com.google.android.material.bottomappbar.BottomAppBar;
import com.pratik.productize.R;
import com.pratik.productize.utils.PrefManager;
import com.pratik.productize.activites.MainActivity;
import com.pratik.productize.ui.RecyclerViewClickListener;
import com.pratik.productize.adapters.TaskRecyclerAdapter;
import com.pratik.productize.asynchronous.Alarm;
import com.pratik.productize.asynchronous.JobHandler;
import com.pratik.productize.database.Tasks;
import com.pratik.productize.ui.TaskViewModel;

import java.util.List;

import static com.pratik.productize.utils.Constants.FLAG_ALARM1;
import static com.pratik.productize.utils.Constants.REQUEST_CODE_ALARM1;
import static com.pratik.productize.utils.Constants.TAG;
import static com.pratik.productize.utils.Constants.TAG1;
import static com.pratik.productize.utils.Constants.TAG_HOME;
import static com.pratik.productize.utils.Constants.TAG_WORK;

public class MainScreenFragment extends Fragment implements RecyclerViewClickListener{

    private TaskViewModel viewModel;
    private TaskRecyclerAdapter adapter;
    private Context context;
    private PrefManager pref;



    public MainScreenFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_main_screen,container,false);

        final BottomAppBar bottomAppBar = ((MainActivity)getActivity()).findViewById(R.id.bottom_app_bar);


        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.my_recycler_view_main);
        adapter = new TaskRecyclerAdapter(getActivity(), this);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        pref = new PrefManager(context);


        viewModel.getAllTasks().observe(this, new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                adapter.setTasksList(tasks);
                if(adapter.getItemCount() == 0)
                    view.findViewById(R.id.empty_notes_view).setVisibility(View.VISIBLE);
                else
                     view.findViewById(R.id.empty_notes_view).setVisibility(View.INVISIBLE);

                for(Tasks task : tasks){
                    Alarm alarm = new Alarm();
                     if (task.getTags() == pref.getActiveTag()){

                         JobHandler jobHandler = new JobHandler();

                         String time = getTagTime();
                         Log.i(TAG,"active time " + time);
                         jobHandler.manageJob(context,adapter.getItemCount());
                         if(time!=null)
                             alarm.manageAlarm(context,adapter.getItemCount(),time);

                         break;

                     }else {
                         alarm.cancelAlarm(context,REQUEST_CODE_ALARM1,FLAG_ALARM1);
                     }
                }

            }
        });

        viewModel.getAllTaskCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.i(TAG,"all tasks "+ integer);
            }
        });

        viewModel.getAllDurationCount().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                Log.i(TAG,"all duration "+ aLong);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(dy>0){
                    bottomAppBar.setVisibility(View.INVISIBLE);
                    Log.i(TAG1,"scroll up");
                }else {
                    bottomAppBar.setVisibility(View.VISIBLE);
                    Log.i(TAG1,"scroll down");
                }

            }
        });

        return view;

    }

    private String getTagTime() {


        int activeTag = pref.getActiveTag();
        Log.i(TAG,"active tag " + activeTag);
        switch (activeTag){
            case TAG_HOME :
                return pref.getHomeTime();
            case TAG_WORK :
                return pref.getWorkTime();
        }
        return pref.getHomeTime();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void recyclerViewClicked(View v, int position) {

        Tasks task = adapter.getTaskAtPosition(position);
        switch (v.getId()){
            case R.id.deleteNotes:

                viewModel.delete(task);
                adapter.notifyItemRangeChanged(0,adapter.getItemCount());
                break;
            case R.id.editNote:
                long id = task.getId();
                ((MainActivity)getActivity()).editTask(id);
                Toast.makeText(getActivity(), "edit note" + position, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public TaskViewModel getFragmentViewModel(){
        return viewModel;
    }

    public TaskRecyclerAdapter getAdapter() {
        return adapter;
    }
}
