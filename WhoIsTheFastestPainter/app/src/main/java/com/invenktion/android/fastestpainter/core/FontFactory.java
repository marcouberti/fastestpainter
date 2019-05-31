package com.invenktion.android.fastestpainter.core;

import android.content.Context;
import android.graphics.Typeface;

public class FontFactory {
	public static String FONT1 = "edosz.ttf";
	//public static String FONT2 = "BrushScriptStd.otf";
	private static Typeface font1 = null;
	
	public static Typeface getFont1(Context context) {
		if(font1 == null) {
			font1 = Typeface.createFromAsset(context.getAssets(), FontFactory.FONT1);
		}
		return font1;
	}
}
