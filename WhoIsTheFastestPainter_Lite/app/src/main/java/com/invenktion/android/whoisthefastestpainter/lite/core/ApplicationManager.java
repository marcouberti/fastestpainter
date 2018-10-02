package com.invenktion.android.whoisthefastestpainter.lite.core;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.invenktion.android.whoisthefastestpainter.lite.view.GlassPaneDrawableView;
import com.invenktion.android.whoisthefastestpainter.lite.view.GommaImageView;
import com.invenktion.android.whoisthefastestpainter.lite.view.PennelloImageView;

public class ApplicationManager {
	
	public static final boolean DEBUG_MODE = false;
	public static String APPLICATION_KILLED = null;

	public static final String ARCADE = "ARCADE_MODE";
	public static final String ATELIER = "ATELIER_MODE";
	//Strumenti
	public static final String TOOL_PENNELLO = "TOOL_PENNELLO";
	public static final String TOOL_GOMMA = "TOOL_GOMMA";
	public static final String PREFS_NAME = "DATA_BEST_LEVELS_RESULT";
	public static final double DISTURB_FREQUENCE = 10;
	public static final int ONE_STAR_PERCENTAGE = 70;
	public static final int TWO_STAR_PERCENTAGE = 80;
	public static final int THREE_STAR_PERCENTAGE = 90;
	public static final int AMMO_MIN_PRICE_VALUE = 500;
	public static int SCREEN_W = -1;
	public static int SCREEN_H = -1;
	private static int PAINT_SIZE = 1;
	//private static int GOMMA_PAINT_SIZE =1;
	private static boolean SHOW_PAINT_SIZE = false;
	private static String TOOL = ApplicationManager.TOOL_PENNELLO;
	private static int CURRENT_COLOR = -1;
	public static int TRANSPARENT_COLOR = Color.argb(0, Color.red(0), Color.green(0), Color.blue(0));
	
	public static int GALLERY_MAX_BITMAP_SIZE_DP = 400;
	
	public static int SECTION_GALLERY_MAX_APPARENT_SIZE_DP = 400;
	public static int AMMO_GALLERY_MAX_APPARENT_SIZE_DP = 450;
	public static int LEVEL_GALLERY_MAX_APPARENT_SIZE_DP = 350;
	public static int LAVAGNA_MAX_APPARENT_SIZE_DP = 500;
	public static int DIALOG_MAX_APPARENT_SIZE_DP = 500;
	//public static int BORDER_IMAGES_APPARENT_SIZE_DP = 300;
	public static int TAVOLOZZA_MAX_APPARENT_HEIGHT_DP = 400;
	public static int STRUMENTO_MAX_APPARENT_HEIGHT_DP = 100;
	
	//Ricordarsi di rilasciarli sull'onDestroy della DrawChallengeActivity
	//private static PennelloImageView PENNELLO_ICON;//il mio widget
	//private static GommaImageView GOMMA_ICON;//il mio widget
	//private static ImageView PENNELLO_TEXT;//immagine con solo il numerino sopra
	//private static ImageView GOMMA_TEXT;//immagine con solo il numerino sopra
	private static GlassPaneDrawableView GLASS_PANE;
	//Ricordarsi di rilasciarli sull'onDestroy
	public static boolean THERE_IS_A_NEW_UNLOCKED_AMMO = false;

	
	public static void setPaintSize(int paintSize) {
		PAINT_SIZE = paintSize;
	}
	
	public static int getPaintSize() {
		
			return PAINT_SIZE;
	
	}
	
	public static void setShowPaintSize(boolean b) {
		SHOW_PAINT_SIZE = b;
	}
	
	public static boolean isShowPaintSize() {
		return SHOW_PAINT_SIZE;
	}

	public static void refreshGlashPane() {
		if(GLASS_PANE != null) {
			GLASS_PANE.invalidate();
		}
		
	}

	public static GlassPaneDrawableView getGLASS_PANE() {
		return GLASS_PANE;
	}

	public static void setGLASS_PANE(GlassPaneDrawableView gLASS_PANE) {
		GLASS_PANE = gLASS_PANE;
	}

	public static String getTOOL() {
		return TOOL;
	}

	public static void setTOOL(String tOOL) {
		TOOL = tOOL;
	}

	
	public static void setCurrentColor(int colore) {
		CURRENT_COLOR = colore;
		/*
		if(ApplicationManager.getPENNELLO_ICON() != null) {
			ApplicationManager.getPENNELLO_ICON().setColorePennello(colore);
		}
		*/
	}
	

	public static int getCurrentColor() {
		return CURRENT_COLOR;
	}

	

	/*
	private static byte[] bigdata;
	public static void setBigData(byte[] bs) {
		bigdata = bs;
		//Log.d("bigdata size= ",""+bigdata.length);
	}
	*/
	
}
