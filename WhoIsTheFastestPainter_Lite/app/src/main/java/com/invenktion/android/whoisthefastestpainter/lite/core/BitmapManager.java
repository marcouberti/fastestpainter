package com.invenktion.android.whoisthefastestpainter.lite.core;

import java.lang.ref.SoftReference;

import com.invenktion.android.whoisthefastestpainter.lite.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapManager {
	static SoftReference<Bitmap> pen1,penMask;
	
	public static Bitmap getPennelloMask(Context context) {
		if(penMask == null || penMask.get() == null) {
			penMask = new SoftReference<Bitmap>(BitmapFactory.decodeResource(context.getResources(), R.drawable.pennellocolormask));
			return penMask.get();
		}else{
			return penMask.get();
		}
	}
	
	public static Bitmap getPennello1(Context context) {
		if(pen1 == null || pen1.get() == null) {
			pen1 = new SoftReference<Bitmap>(BitmapFactory.decodeResource(context.getResources(), R.drawable.pennello_size_1));
			return pen1.get();
		}else{
			return pen1.get();
		}
	}
	
	
}
