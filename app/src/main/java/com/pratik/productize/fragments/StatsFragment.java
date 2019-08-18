package com.pratik.productize.fragments;



import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ListView;

import android.widget.TextView;


import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import com.pratik.productize.R;
import com.pratik.productize.activites.MainActivity;
import com.pratik.productize.adapters.ChartDataAdapter;
import com.pratik.productize.ui.TaskViewModel;
import com.pratik.productize.ui.chartItems.BarChartItem;
import com.pratik.productize.ui.chartItems.ChartItem;
import com.pratik.productize.ui.chartItems.LineChartItem;
import com.pratik.productize.ui.chartItems.PieChartItem;


import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;

import static com.pratik.productize.utils.Constants.WEEK_IN_LONG;


public class StatsFragment extends Fragment implements View.OnClickListener {


    private int currentWeek = 0;
    private TaskViewModel viewModel;
    private TextView weekText;
    private ImageButton leftButton,rightButton;
    private View view;
    private  long startDate = System.currentTimeMillis() - WEEK_IN_LONG;
    private long endDate = System.currentTimeMillis();

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_stats, container, false);
        viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        Objects.requireNonNull(((MainActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).setTitle("Statistics");

        setChart(view,currentWeek);

        leftButton = view.findViewById(R.id.week_button_left);
        rightButton = view.findViewById(R.id.week_button_right);
        weekText = view.findViewById(R.id.week_text);

        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);

        Objects.requireNonNull(((MainActivity) (getActivity())).getSupportActionBar()).hide();

        return view;
    }



    private void setChart(View view,int weekInt) {

       // view.findViewById(R.id.no_stats_view).setVisibility(View.INVISIBLE);

        ListView lv = view.findViewById(R.id.listView1);
        ArrayList<ChartItem> list = new ArrayList<>();

        list.add(new LineChartItem(generateDataLine(weekInt), Objects.requireNonNull(getActivity())));
        list.add(new BarChartItem(generateDataBar(weekInt), getActivity()));
        list.add(new PieChartItem(generateDataPie(weekInt), getActivity()));

        ChartDataAdapter cda = new ChartDataAdapter(getContext(), list);
        lv.setAdapter(cda);

    }




    private LineData generateDataLine(int currentWeek) {

        ArrayList<Entry> values1 = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            values1.add(new Entry(i, (int) (Math.random() * 65) + 40));
        }

        

        LineDataSet d1 = new LineDataSet(values1, "Last week");
        d1.setLineWidth(2.5f);
        d1.setCircleRadius(4.5f);
        d1.setHighLightColor(Color.rgb(244, 117, 117));
        d1.setDrawValues(false);

        ArrayList<Entry> values2 = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            values2.add(new Entry(i, values1.get(i).getY() - 30));
        }

        LineDataSet d2 = new LineDataSet(values2, "This week");
        d2.setLineWidth(2.5f);
        d2.setCircleRadius(4.5f);
        d2.setHighLightColor(Color.rgb(244, 117, 117));
        d2.setColor(ColorTemplate. MATERIAL_COLORS[0]);
        d2.setCircleColor(ColorTemplate.MATERIAL_COLORS[0]);
        d2.setDrawValues(false);

        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);

        return new LineData(sets);
    }



    private BarData generateDataBar(int currentWeek) {

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));
        }

        BarDataSet d = new BarDataSet(entries, "");
        d.setColors(ColorTemplate.MATERIAL_COLORS);
        d.setHighLightAlpha(255);
        d.setLabel("Week trends");

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }


    private PieData generateDataPie(int currentWeek) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Home " ));
        entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Work " ));

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.MATERIAL_COLORS);

        return new PieData(d);
    }



    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.week_button_left : changeWeekText(-1,--currentWeek);break;
            case R.id.week_button_right : changeWeekText(1,++currentWeek);break;
        }

    }

    private void changeWeekText(int i , int currWeek) {

        if(i == 1){
            if(currWeek <= 0){
                weekText.setText("Week " + currWeek);
                setChart(view,currWeek);
            }else currentWeek = 0;
        }else {
            weekText.setText("Week " + currWeek);
            setChart(view,currWeek);
        }

    }

}
