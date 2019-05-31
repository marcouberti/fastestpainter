package com.invenktion.android.whoisthefastestpainter.lite;

import com.invenktion.android.whoisthefastestpainter.lite.utils.Foreground;

import android.app.Application;


public class MyApplication extends Application {
    public void onCreate(){
    	super.onCreate();
        Foreground.init(this);
    }
}
