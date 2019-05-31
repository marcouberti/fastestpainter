package com.invenktion.android.fastestpainter.utils;

import android.graphics.Bitmap;
import android.util.Log;

public class ColorUtils {

	//Controllo se il colore combacia
	public static boolean compareColorArea(Bitmap userFinalBitmap,
			Bitmap colored, int w, int h) {
		if(Math.abs(userFinalBitmap.getPixel(w, h) - (colored.getPixel(w, h))) <= 100) {
			return true;
		}
		return false;
	}

}
