package com.invenktion.android.whoisthefastestpainter.lite.bean;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

import com.invenktion.android.whoisthefastestpainter.lite.core.ApplicationManager;
import com.invenktion.android.whoisthefastestpainter.lite.utils.ColorUtils;
import com.invenktion.android.whoisthefastestpainter.lite.utils.FilterUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.widget.ArrayAdapter;

public class PictureBean {
	private int sectionIndex;
	private int levelIndex;
	private int coloredPicture;
	private int contourPicture;
	private float timeToComplete;
	private int[] colors;
	private String univoqueCode;
	private String title;
	private String immagineNome;
	private boolean isTheFirstOfSection;
	private SoftReference<Bitmap> coloredImageSoftReference= new SoftReference<Bitmap>(null);
	private int maxRisultatoFisico;

	public void setImmagineNome(String immagineNome) {
		this.immagineNome = immagineNome;
	}

	public String getImmagineNome() {
		return immagineNome;
	}
	
	public int getMaxRisultatoFisico() {
		return maxRisultatoFisico;
	}

	public void clearSoftReferences() {
		coloredImageSoftReference.clear();
	}

	public int getLevelIndex() {
		return levelIndex;
	}

	public void setLevelIndex(int levelIndex) {
		this.levelIndex = levelIndex;
	}

	public int getSectionIndex() {
		return sectionIndex;
	}

	public void setSectionIndex(int sectionIndex) {
		this.sectionIndex = sectionIndex;
	}

	public Bitmap getColoredPicture(Context context) {
		int H = (int)(ApplicationManager.SCREEN_W*0.13);
		if(H > ApplicationManager.GALLERY_MAX_BITMAP_SIZE_DP) H = ApplicationManager.GALLERY_MAX_BITMAP_SIZE_DP;//Dimensione massima per i quadri.
		Bitmap data = coloredImageSoftReference.get();
		if(data == null) {
			Options opts = new BitmapFactory.Options();
	    	opts.inSampleSize=1;
	    	Bitmap.Config conf = Bitmap.Config.RGB_565;
	    	opts.inPreferredConfig = conf;
	    	opts.inTempStorage = new byte[16*1024];
			Bitmap rawdata = BitmapFactory.decodeResource(context.getResources(), getColoredPicture(),opts);
			data = Bitmap.createScaledBitmap(rawdata, H, H, true);
			coloredImageSoftReference = new SoftReference<Bitmap>(data);
			if(data != rawdata) {
				rawdata.recycle();
				rawdata = null;
			}
		}else{
			//Log.d("","SOFTED");
		}
		return data;
	}
	
	//Costructor
	public PictureBean(String imgName,String univoqueCode,String title,int coloredPicture, int contourPicture,int[] colors, float timeToComplete,boolean first,int maxResult) {
		this.coloredPicture = coloredPicture;
		this.contourPicture = contourPicture;
		this.timeToComplete = timeToComplete;
		this.univoqueCode = univoqueCode;
		this.title = title;
		this.colors = colors;
		this.isTheFirstOfSection = first;
		this.maxRisultatoFisico = maxResult;
		this.immagineNome = imgName;
	}

	public boolean isTheFirstOfSection() {
		return isTheFirstOfSection;
	}

	public void setTheFirstOfSection(boolean isTheFirstOfSection) {
		this.isTheFirstOfSection = isTheFirstOfSection;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUnivoqueCode() {
		return univoqueCode;
	}

	public void setUnivoqueCode(String univoqueCode) {
		this.univoqueCode = univoqueCode;
	}

	public int[] getColors() {
		return colors;
	}

	public float getTimeToComplete() {
		return timeToComplete;
	}
	public void setTimeToComplete(float timeToComplete) {
		this.timeToComplete = timeToComplete;
	}
	public int getColoredPicture() {
		return coloredPicture;
	}
	public void setColoredPicture(int coloredPicture) {
		this.coloredPicture = coloredPicture;
	}
	public int getContourPicture() {
		return contourPicture;
	}
	public void setContourPicture(int contourPicture) {
		this.contourPicture = contourPicture;
	}
	
	public static float computeStartingSuccessPercentage(Bitmap contour, Bitmap colored) {
			int width = colored.getWidth();
	        int heigth = colored.getHeight();
	        
	        int[][] mask = new int[width][heigth];
	        
	        for(int w=0; w<width; w++) {
	        	for(int h=0; h<heigth; h++) {
	        		//Lascio una soglia di tolleranza minima
	        		//e controllo se il colore è presente in un intorno di NxN pixel per gli errori di confronto
	        		//dovuti alle zone in sovrapposizione vicino ai bordi.
	        		if(ColorUtils.compareColorArea(contour,colored,w,h)) {
	        			//resultBitmap.setPixel(w, h, Color.GREEN);
	        			mask[w][h] = 0;
	        		}else {
	        			//resultBitmap.setPixel(w, h, Color.RED);
	        			mask[w][h] = 1;
	        		}
	        	}
	        }
	        
	        int[][] erodedMask = FilterUtils.intorno(mask);
	        int countErrors = 0;
	        for(int w=0; w<width; w++) {
	        	for(int h=0; h<heigth; h++) {
	        		if(erodedMask[w][h] == 0) {
	        		}else {
	        			countErrors++;
	        		}
	        	}
	        }
	        //Calcolo la percentuale di errore
	        int totalPixel = width * heigth;
	        float percentage = (float)(100 * countErrors)/(float)totalPixel;
	        
	        return 100 - percentage;
	}
	
	public float computeRelativePercentage(float absolutePercentage, float startPercentageComplete) {
		float nonNormalizzato = ((absolutePercentage-(startPercentageComplete))*100)/(100-startPercentageComplete);
		//adesso controllo il massimo risultato fisico ottenibile sul quadro e poi lo normalizzo tra 0 e 100%.
		float normalizzato = (nonNormalizzato * 100)/maxRisultatoFisico;
		if(normalizzato > 100f) normalizzato = 100f;
		return normalizzato;
	}

	public boolean isBlocked(Context context,String gamemode) {
		if(!ApplicationManager.DEBUG_MODE) {
			//Controllo se il livello è stato già sbloccato o meno
			//Nell'atelier si possono usare i quadri sbloccati nella modalità arcade
			if(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode)) gamemode=ApplicationManager.ARCADE;
			
	    	SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
	        String isUnlocked = settings.getString(this.getUnivoqueCode()+"_"+gamemode+"_unlocked", "false");
	        if("true".equalsIgnoreCase(isUnlocked)) {
	        	return false;
	        }else {
	        	return true;
	        }
		}else{
			return false;
		}
	}
	
	public int getBestResultEver(Context context,String gamemode) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
		return settings.getInt(this.getUnivoqueCode()+"_"+gamemode+"_best", 0);
	}
	
	public int getLastResult(Context context,String gamemode) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
		return settings.getInt(this.getUnivoqueCode()+"_"+gamemode+"_last", 0);
	}

	public void setLastResult(Context context, String gamemode,
			int parseInt) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(this.getUnivoqueCode()+"_"+gamemode+"_last", parseInt);
        //Commit the edits!
        editor.commit();
	}

	public void setBestResult(Context context, String gamemode,
			int parseInt) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(this.getUnivoqueCode()+"_"+gamemode+"_best", parseInt);
        //Commit the edits!
        editor.commit();
	}

	public void unlockLevel(Context context, String gamemode) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
		editor.putString(this.getUnivoqueCode()+"_"+gamemode+"_unlocked", "true");
		//Commit the edits!
		editor.commit();
	}
}
