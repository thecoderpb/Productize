package com.pratik.productize.activites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;


import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.pratik.productize.R;

import com.pratik.productize.fragments.EditFragment;
import com.pratik.productize.fragments.StatsFragment;
import com.pratik.productize.utils.Converters;
import com.pratik.productize.utils.PrefManager;

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
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.view.Menu;


import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import java.sql.Date;
import java.util.List;
import java.util.Objects;


import static com.pratik.productize.utils.Constants.HIDE;
import static com.pratik.productize.utils.Constants.SHOW;
import static com.pratik.productize.utils.Constants.TASK_ID;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,View.OnKeyListener {

    private PrefManager prefManager;
    private BottomSheetBehavior bottomSheetBehavior;
    private FloatingActionButton fab;
    private BottomAppBar bottomAppBar;
    private EditText bottomSheetString;
    private int priority,duration,tags = -1;
    private ChipGroup chipGroup;
    private FloatingActionButton p1,p2,p3,p4,p5,p6,p7;
    public TextView durationTitleTv, taskTitleTv, titleTv;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefManager = new PrefManager(this);

        drawer = findViewById(R.id.drawer_layout);

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
        durationTitleTv = findViewById(R.id.toolbar_title);
        taskTitleTv = findViewById(R.id.toolbar_titles2);
        titleTv = findViewById(R.id.toolbar_title3);

        Button bottomSheetHomeButton = findViewById(R.id.bottomSheetHomeButton);
        Button bottomSheetWorkButton = findViewById(R.id.bottomSheetWorkButton);
        Button bottomSheetOtherButton = findViewById(R.id.bottomSheetOtherButton);
        chipGroup = findViewById(R.id.bottomSheetChipGroup);

        initializePriorities();

        NotificationHandler notificationHandler = new NotificationHandler(this);
        notificationHandler.createNotificationChannel();

        fab.setOnClickListener(this);

        bottomSheetHomeButton.setOnClickListener(this);
        bottomSheetOtherButton.setOnClickListener(this);
        bottomSheetWorkButton.setOnClickListener(this);

        p1.setOnClickListener(this);
        p2.setOnClickListener(this);
        p3.setOnClickListener(this);
        p4.setOnClickListener(this);
        p5.setOnClickListener(this);
        p6.setOnClickListener(this);
        p7.setOnClickListener(this);

        chipGroup.setSingleSelection(true);
        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {
                chipGroup.check(i);

            }
        });

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
                    bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
                    showBottomSheet();
                }else {
                    bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
                    hideBottomSheet();
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
            }
        });

        bottomSheetString.setOnClickListener(this);
        bottomSheetString.setOnKeyListener(this);

    }

    private void hideBottomSheet() {

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        fab.setImageResource(R.drawable.ic_edit);
        dimBackground(false);
    }

    private void showBottomSheet() {

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_END);
        fab.setImageResource(R.drawable.ic_arrow_down);
        bottomSheetString.requestFocus();
        bottomSheetString.requestFocus();
        showKeyboard();

        dimBackground(true);

    }

    private void fabClick(){
        if(bottomAppBar.getFabAlignmentMode() == BottomAppBar.FAB_ALIGNMENT_MODE_CENTER){

            bottomSheetBehavior.setState( BottomSheetBehavior.STATE_EXPANDED);
            showBottomSheet();

        }
        else{
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            hideBottomSheet();

        }

        bottomAppBar.setVisibility(View.VISIBLE);
    }

    private void initializePriorities() {

        p1 = findViewById(R.id.priorityLvl1);
        p2 = findViewById(R.id.priorityLvl2);
        p3 = findViewById(R.id.priorityLvl3);
        p4 = findViewById(R.id.priorityLvl4);
        p5 = findViewById(R.id.priorityLvl5);
        p6 = findViewById(R.id.priorityLvl6);
        p7 = findViewById(R.id.priorityLvl7);
    }

    private void dimBackground(boolean b) {

        Fragment fragment = getVisibleFragment();
        if( fragment instanceof MainScreenFragment){
            View view = Objects.requireNonNull(fragment.getView()).findViewById(R.id.dim_bg);
            if(b){
                view.setVisibility(View.VISIBLE);
                findViewById(R.id.card_layout).setClickable(false);
            }else{
                view.setVisibility(View.GONE);
                findViewById(R.id.card_layout).setClickable(true);
            }

        }

    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = MainActivity.this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        for(Fragment fragment : fragments){
            if(fragment != null && fragment.isVisible())
                return fragment;
        }
        return null;
    }


    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Productize");
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
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @SuppressLint("RestrictedApi")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;

        switch (id){
            case R.id.nav_stats :
                fragment = new StatsFragment();
                displayFragment(fragment);
                toggleBottomBarVisibility(HIDE);
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
                toggleBottomBarVisibility(HIDE);
                break;

            case R.id.nav_work :
                fragment = new WorkScreenFragment();
                displayFragment(fragment);
                toggleBottomBarVisibility(HIDE);
                break;

            case R.id.nav_other :
                fragment = new OtherScreenFragment();
                displayFragment(fragment);
                toggleBottomBarVisibility(HIDE);
                break;

            case R.id.nav_share :
                prefManager.setTaskActive(true);
                break;

            case R.id.nav_about :
                aboutDialog();
                break;

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void aboutDialog() {

        new AlertDialog.Builder(this)
                .setTitle("About")
                .setMessage("Please review the terms and privacy policy here")
                .setPositiveButton("OK",null)
                .show();
    }

    public void displayFragment(Fragment fragment){
        if(fragment!=null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.content_main,fragment)
                    .addToBackStack("FRAG_BACK")
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .commit();
        }
    }

    public void resetBottomSheet(){

        bottomSheetString.setText("");
        chipGroup.clearCheck();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

    }

    public void showKeyboard(){

        final InputMethodManager imm;
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (imm != null) {
                    imm.showSoftInput(bottomSheetString, InputMethodManager.SHOW_FORCED);
                }
                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
            }
        },200);

    }

    public void saveUserDetailBottomSheet(View view){

        Date date = Converters.toDate(System.currentTimeMillis());
        String taskText = bottomSheetString.getText().toString();

        TaskViewModel viewModel = ViewModelProviders.of(getVisibleFragment()).get(TaskViewModel.class);
        Tasks task = new Tasks(date,taskText,priority,duration,tags,false,false);

        if(taskText.equals("")){
            Toast.makeText(this, "Task cannot be empty", Toast.LENGTH_SHORT).show();
        }else if(getVisibleFragment() instanceof MainScreenFragment){

            viewModel.insert(task);
            resetBottomSheet();

        }else {
            for(int i =0 ; i< getSupportFragmentManager().getBackStackEntryCount() ; i++)
                getSupportFragmentManager().popBackStack();
            viewModel.insert(task);

        }

    }

    public void editTask(long id){

        Fragment fragment = new EditFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        toggleBottomBarVisibility(HIDE);

        Bundle args = new Bundle();
        args.putLong(TASK_ID,id);

        fragment.setArguments(args);
        ft.replace(R.id.content_main,fragment).addToBackStack("FRAG_EDIT").commit();

//        final InputMethodManager imm;
//        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.showSoftInput(getVisibleFragment().getView().findViewById(R.id.edit_task), InputMethodManager.SHOW_FORCED);
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

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.priorityLvl1: priority = 1; break;
            case R.id.priorityLvl2: priority = 2; break;
            case R.id.priorityLvl3: priority = 3; break;
            case R.id.priorityLvl4: priority = 4; break;
            case R.id.priorityLvl5: priority = 5; break;
            case R.id.priorityLvl6: priority = 6; break;
            case R.id.priorityLvl7: priority = 7; break;

            case R.id.bottomSheetHomeButton: tags=0;break;
            case R.id.bottomSheetWorkButton: tags=1;break;
            case R.id.bottomSheetOtherButton: tags=-1;break;

           // case R.id.bottomSheetText: showKeyboard();

            case R.id.fab : fabClick();
        }
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {

        if(keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER){
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(),0);
            }

            bottomSheetString.clearFocus();

        }
        return false;
    }
}