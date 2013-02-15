package com.invenktion.android.whoisthefastestpainter.lite.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;

public class ActivityHelper {
	
	/**
	* Controlla se la nostra applicazione è in background (ossia se è dietro un'altra Activity di un'altra applicazione).
	*
	* @param context
	* @return true se un'altra applicazione è davanti alla nostra.
	*/
	public static boolean isApplicationBroughtToBackground(final Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName())) {
                return true;
            }
        }
        return false;
	}
	
	public static String getTopActivityClassNameWithPackage(final Context context) {
		 ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	        List<RunningTaskInfo> tasks = am.getRunningTasks(1);
	        if (!tasks.isEmpty()) {
	            ComponentName topActivity = tasks.get(0).topActivity;
	            return topActivity.getClassName();
	        }
	        return null;
	}
}
