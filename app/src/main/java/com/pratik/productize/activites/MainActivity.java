package com.pratik.productize.activites;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Handler;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.pratik.productize.R;
import com.pratik.productize.fragments.EditFragment;
import com.pratik.productize.utils.Converters;
import com.pratik.productize.utils.PrefManager;
import com.pratik.productize.adapters.TaskRecyclerAdapter;
import com.pratik.productize.database.Tasks;
import com.pratik.productize.fragments.HomeScreenFragment;
import com.pratik.productize.fragments.MainScreenFragment;
import com.pratik.productize.fragments.OtherScreenFragment;
import com.pratik.productize.fragments.WorkScreenFragment;
import com.pratik.productize.ui.NotificationHandler;
import com.pratik.productize.ui.TaskViewModel;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.sql.Date;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.WHITE;
import static com.pratik.productize.utils.Constants.HIDE;
import static com.pratik.productize.utils.Constants.SHOW;
import static com.pratik.productize.utils.Constants.TASK_ID;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PrefManager prefManager;
    private BottomSheetBehavior bottomSheetBehavior;
    private FloatingActionButton fab;
    private BottomAppBar bottomAppBar;
    private EditText bottomSheetString;
    private boolean doubleBackToExitPressedOnce = false;
    private int priority,duration,tags = -1;
    private ChipGroup chipGroup;


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

        }else{
            Fragment fragment = new MainScreenFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).commitNow();
        }


        CoordinatorLayout layoutBottomSheet = findViewById(R.id.bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(layoutBottomSheet);
        fab = findViewById(R.id.fab);
        bottomAppBar = findViewById(R.id.bottom_app_bar);
        bottomSheetString = findViewById(R.id.bottomSheetText);
        SeekBar bottomSheetDuration = findViewById(R.id.seekBarDuration);
        SeekBar bottomSheetPriority = findViewById(R.id.seekBarPriority);
        Button bottomSheetHomeButton = findViewById(R.id.bottomSheetHomeButton);
        Button bottomSheetWorkButton = findViewById(R.id.bottomSheetWorkButton);
        Button bottomSheetOtherButton = findViewById(R.id.bottomSheetOtherButton);
        chipGroup = findViewById(R.id.bottomSheetChipGroup);

        NotificationHandler notificationHandler = new NotificationHandler(this);
        notificationHandler.createNotificationChannel();


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

                bottomAppBar.setVisibility(View.VISIBLE);

            }
        });

        bottomSheetDuration.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                if(i <= 5) {
                    duration = 1;//*60*1000;
                }else if( i<=15){
                    duration = 5;
                }else if(i<25){
                    duration = 10;
                }else if(i<35){
                    duration = 15;
                }else if(i<45){
                    duration = 20;
                }else duration =30;

                duration = duration*60*1000;
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

        bottomSheetOtherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tags =- 1;
            }
        });

        chipGroup.setSingleSelection(true);

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                chipGroup.check(i);

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

    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        toggleBottomBarVisibility(SHOW);

        int backStack = getSupportFragmentManager().getBackStackEntryCount();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
                fab.setImageResource(R.drawable.ic_edit);
                bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);


            }
            else if(backStack > 1){
                for(int i=0; i < backStack; i++ ){
                    getSupportFragmentManager().popBackStack();
                }
            }else{

               super.onBackPressed();
            }

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
    protected void onResume() {
        super.onResume();

        toggleBottomBarVisibility(SHOW);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;

        switch (id){
            case R.id.nav_stats :
                break;

            case R.id.nav_schedule_task :
                Intent intent = new Intent(MainActivity.this,ScheduleTask.class);
                intent.putExtra("nav_schedule",1);
                startActivity(intent);
                if(!prefManager.isTaskScheduled())
                    finish();
                break;

            case R.id.nav_home :
                fragment = new HomeScreenFragment();
                displayFragment(fragment);
                break;

            case R.id.nav_work :
                fragment = new WorkScreenFragment();
                displayFragment(fragment);
                break;

            case R.id.nav_other :
                fragment = new OtherScreenFragment();
                displayFragment(fragment);
                break;

            case R.id.nav_share :
                break;

            case R.id.nav_about :
                prefManager.setTaskActive(true);
                break;

        }

        toggleBottomBarVisibility(HIDE);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displayFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,fragment).addToBackStack("FRAG_BACK").commit();
        }
    }

    public void saveUserDetailBottomSheet(View view){

        Date date = Converters.toDate(System.currentTimeMillis());
        String taskText = bottomSheetString.getText().toString();
        if(taskText.equals("")){
            Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
        }else{
            final Tasks task = new Tasks(date,taskText,priority,duration,tags,false,false);

            MainScreenFragment fragment = (MainScreenFragment) getSupportFragmentManager().findFragmentById(R.id.content_main);
            assert fragment != null;
            TaskViewModel viewModel = fragment.getFragmentViewModel();
            TaskRecyclerAdapter adapter = fragment.getAdapter();
            viewModel.insert(task);
            adapter.notifyItemRangeChanged(0,adapter.getItemCount());

        }


    }

    public void editTask(long id){

        Fragment fragment = new EditFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Bundle args = new Bundle();
        args.putLong(TASK_ID,id);

        fragment.setArguments(args);
        ft.replace(R.id.content_main,fragment).addToBackStack("FRAG_EDIT").commit();
    }

    @SuppressLint("RestrictedApi")
    public void toggleBottomBarVisibility(boolean flag){

        if(flag){
            bottomAppBar.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
        }else {
            bottomAppBar.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);
        }

    }
}