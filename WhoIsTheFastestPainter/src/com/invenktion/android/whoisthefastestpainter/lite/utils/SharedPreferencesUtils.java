package com.invenktion.android.whoisthefastestpainter.lite.utils;

import com.invenktion.android.whoisthefastestpainter.lite.core.ApplicationManager;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesUtils {
	public static void deleteAllSharedPreferences(Context context) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        //Commit the edits!
        editor.commit();
	}
}
