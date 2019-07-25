package com.pratik.productize.activites;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.pratik.productize.R;
import com.pratik.productize.Utils.Converters;
import com.pratik.productize.Utils.PrefManager;
import com.pratik.productize.adapters.RecyclerViewClickListener;
import com.pratik.productize.adapters.TaskRecyclerAdapter;
import com.pratik.productize.database.Tasks;
import com.pratik.productize.ui.TaskViewModel;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.sql.Date;

import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RecyclerViewClickListener {

    private PrefManager prefManager;
    private BottomSheetBehavior bottomSheetBehavior;
    private FloatingActionButton fab;
    private BottomAppBar bottomAppBar;
    private TaskViewModel viewModel;
    private EditText bottomSheetString;
    private SeekBar bottomSheetPriority;
    private SeekBar bottomSheetDuration;
    private Button bottomSheetHomeButton,bottomSheetWorkButton;
    private int priority,duration,tags = -1;
    private TaskRecyclerAdapter adapter;



    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefManager = new PrefManager(this);
        if(!prefManager.isTaskScheduled()){
            startActivity(new Intent(this,ScheduleTask.class));
            finish();

        }


        CoordinatorLayout layoutBottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        fab = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottom_app_bar);
        bottomSheetString = findViewById(R.id.bottomSheetText);
        bottomSheetDuration = findViewById(R.id.seekBarDuration);
        bottomSheetPriority = findViewById(R.id.seekBarPriority);
        bottomSheetHomeButton = findViewById(R.id.bottomSheetHomeButton);
        bottomSheetWorkButton = findViewById(R.id.bottomSheetWorkButton);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(bottomAppBar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER){
                    bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
                    fab.setImageResource(R.drawable.ic_arrow_down);
                    bottomSheetBehavior.setState( BottomSheetBehavior.STATE_EXPANDED);
                }
                else{
                    bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                    fab.setImageResource(R.drawable.ic_edit);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }

            }
        });

        bottomSheetDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                duration = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bottomSheetPriority.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                priority = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        bottomSheetWorkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tags =1;
            }
        });

        bottomSheetHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tags=0;
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                    fab.setImageResource(R.drawable.ic_arrow_down);
                    bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
                }else {
                    fab.setImageResource(R.drawable.ic_edit);
                    bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });



        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        adapter = new TaskRecyclerAdapter(this,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);


        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        viewModel.getAllTasks().observe(this, new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                adapter.setTasksList(tasks);
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                fab.setImageResource(R.drawable.ic_edit);
                bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
            else
                super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id){
            case R.id.nav_stats : break;
            case R.id.nav_schedule_task :
                Intent intent = new Intent(MainActivity.this,ScheduleTask.class);
                intent.putExtra("nav_schedule",1);
                startActivity(intent);
                if(!prefManager.isTaskScheduled())
                    finish();
                break;
            case R.id.nav_home : break;
            case R.id.nav_work : break;
            case R.id.nav_other : break;
            case R.id.nav_share : break;
            case R.id.nav_about : break;
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void saveUserDetailBottomSheet(View view){

        Date date = Converters.toDate(System.currentTimeMillis());
        String taskText = bottomSheetString.getText().toString();
        if(taskText.equals("")){
            Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
        }
        final Tasks task = new Tasks(date,taskText,priority,duration,tags,false,false);

        viewModel.insert(task);

    }

    @Override
    public void recyclerViewClicked(View v, int position) {

       switch (v.getId()){
           case R.id.deleteNotes:
               Toast.makeText(this, "delete note" + position, Toast.LENGTH_SHORT).show();
               Tasks task = adapter.getTaskAtPosition(position);
               viewModel.delete(task);
               adapter.notifyDataSetChanged();
               break;
           case R.id.editNote:
               Toast.makeText(this, "edit note" + position, Toast.LENGTH_SHORT).show();
               break;
       }

    }
}