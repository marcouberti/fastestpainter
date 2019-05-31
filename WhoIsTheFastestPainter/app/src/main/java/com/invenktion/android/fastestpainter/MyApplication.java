package com.invenktion.android.fastestpainter;

import com.invenktion.android.fastestpainter.utils.Foreground;

import android.app.Application;


public class MyApplication extends Application {
    public void onCreate(){
    	super.onCreate();
        Foreground.init(this);
    }
}
