package com.pratik.productize.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.pratik.productize.R;
import com.pratik.productize.activites.MainActivity;
import com.pratik.productize.adapters.ChartDataAdapter;
import com.pratik.productize.database.Tasks;
import com.pratik.productize.ui.TaskViewModel;
import com.pratik.productize.ui.chartItems.BarChartItem;
import com.pratik.productize.ui.chartItems.ChartItem;
import com.pratik.productize.ui.chartItems.LineChartItem;
import com.pratik.productize.ui.chartItems.PieChartItem;

import java.util.ArrayList;
import java.util.List;


public class StatsFragment extends Fragment implements View.OnClickListener {


    private TextView textView,textView2,textView3,weekText;
    private int currentWeek = 0;

    public StatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_stats, container, false);
        TaskViewModel viewModel = ViewModelProviders.of(this).get(TaskViewModel.class);

        viewModel.getCompleteTasks().observe(this, new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
//                textView = view.findViewById(R.id.stats_tv);
//                textView.setText("Completed tasks " + tasks.size());
            }
        });

        viewModel.getIncompleteTasks().observe(this, new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
//                textView2 = view.findViewById(R.id.stats_tv2);
//                textView2.setText("Incomplete tasks " + tasks.size());
            }
        });

        viewModel.getAllTaskCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
//                textView3 = view.findViewById(R.id.stats_tv3);
//                textView3.setText("Tasks remaining today: " + integer);

            }
        });

        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Statistics");

        setChart(view);

      //  weekText = view.findViewById(R.id.week_text);
        ((MainActivity)(getActivity())).getSupportActionBar().hide();

        return view;
    }



    private void setChart(View view) {

        ListView lv = view.findViewById(R.id.listView1);

        ArrayList<ChartItem> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {

            if(i % 3 == 0) {
                list.add(new LineChartItem(generateDataLine(), getActivity()));
            } else if(i % 3 == 1) {
                list.add(new BarChartItem(generateDataBar(i + 1), getActivity()));
            } else if(i % 3 == 2) {
                list.add(new PieChartItem(generateDataPie(), getActivity()));
            }
        }

        ChartDataAdapter cda = new ChartDataAdapter(getContext(), list);
        lv.setAdapter(cda);

    }


    private LineData generateDataLine() {

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

        for (int i = 0; i < 12; i++) {
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


    private BarData generateDataBar(int cnt) {

        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            entries.add(new BarEntry(i, (int) (Math.random() * 70) + 30));
        }

        BarDataSet d = new BarDataSet(entries, "New DataSet " + cnt);
        d.setColors(ColorTemplate.MATERIAL_COLORS);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.9f);
        return cd;
    }


    private PieData generateDataPie() {

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            entries.add(new PieEntry((float) ((Math.random() * 70) + 30), "Quarter " + (i+1)));
        }

        PieDataSet d = new PieDataSet(entries, "");

        // space between slices
        d.setSliceSpace(2f);
        d.setColors(ColorTemplate.MATERIAL_COLORS);

        return new PieData(d);
    }



    @Override
    public void onClick(View view) {

        Toast.makeText(getContext(), "clicked view", Toast.LENGTH_SHORT).show();

        switch (view.getId()){
            case R.id.week_button_left : changeWeekText(-1,currentWeek--);break;
            case R.id.week_button_right : changeWeekText(1,currentWeek++);break;
        }

    }

    private void changeWeekText(int i , int currWeek) {

        if(i == 1){
            //positive week change
        }else {
            //negative week change
        }

        Toast.makeText(getContext(), "week changed " + currWeek, Toast.LENGTH_SHORT).show();

    }

}
