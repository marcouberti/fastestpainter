package com.invenktion.android.whoisthefastestpainter.lite.utils;

import java.text.DecimalFormat;

import android.os.Debug;
import android.util.Log;

public class LogUtils {
	public static void logHeap() {
		/*
	    Double allocated = new Double(Debug.getNativeHeapAllocatedSize())/new Double((1048576));
	    Double available = new Double(Debug.getNativeHeapSize()/1048576.0);
	    Double free = new Double(Debug.getNativeHeapFreeSize()/1048576.0);
	    DecimalFormat df = new DecimalFormat();
	    df.setMaximumFractionDigits(2);
	    df.setMinimumFractionDigits(2);

	    Log.d("LogUtils", "debug. =================================");
	    Log.d("LogUtils", "debug.heap native: allocated " + df.format(allocated) + "MB of " + df.format(available) + "MB (" + df.format(free) + "MB free) in []");
	    Log.d("LogUtils", "debug.memory: allocated: " + df.format(new Double(Runtime.getRuntime().totalMemory()/1048576)) + "MB of " + df.format(new Double(Runtime.getRuntime().maxMemory()/1048576))+ "MB (" + df.format(new Double(Runtime.getRuntime().freeMemory()/1048576)) +"MB free)");
		*/
	}

}
