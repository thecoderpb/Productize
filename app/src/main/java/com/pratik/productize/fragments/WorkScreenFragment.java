package com.pratik.productize.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pratik.productize.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkScreenFragment extends Fragment {


    public WorkScreenFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_work_screen, container, false);
    }

}
