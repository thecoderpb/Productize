package com.pratik.productize.utils;

import android.content.Context;

import com.google.android.gms.ads.MobileAds;

public class ProMode {

    private Context context;

    public ProMode(Context context){

        this.context = context;

    }

    public void initAds(){

        MobileAds.initialize(context);

    }

}
