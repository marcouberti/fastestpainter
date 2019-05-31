package com.invenktion.android.fastestpainter.bean;

import java.lang.ref.SoftReference;
import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

import com.invenktion.android.fastestpainter.core.ApplicationManager;

/**
 * @author Marco Uberti
 * E' una lista di PictureBean con delle propriet
 */
public class SectionArrayList<T> extends ArrayList<com.invenktion.android.fastestpainter.bean.PictureBean>{
	private String sectionName;
	private int bossResourceNormal;
	private int bossResourceSuccess;
	private int bossResourceFailure;
	private int presentationImage;
	private int storyboardImage;
	private int lockedImage;
	private int telaImage;
	private int corniceImage;
	private int sfondoImage;
	private int number;
	private SoftReference<Bitmap> presentaionImageSoftReference= new SoftReference<Bitmap>(null);
	private SoftReference<Bitmap> lockedImageSoftReference= new SoftReference<Bitmap>(null);

	public void clearSoftReferences() {
		presentaionImageSoftReference.clear();
		lockedImageSoftReference.clear();
	}
	
	public Bitmap getPresentaionImage(Context context) {
		int H = (int)(ApplicationManager.SCREEN_H*0.6);
		if(H > ApplicationManager.GALLERY_MAX_BITMAP_SIZE_DP) H = ApplicationManager.GALLERY_MAX_BITMAP_SIZE_DP;//Dimensione massima per le immagini.
		Bitmap data = presentaionImageSoftReference.get();
		if(data == null) {
			Options opts = new BitmapFactory.Options();
	    	opts.inSampleSize=1;
	    	Bitmap.Config conf = Bitmap.Config.RGB_565;
	    	opts.inPreferredConfig = conf;
	    	opts.inTempStorage = new byte[16*1024];
			Bitmap rawdata = BitmapFactory.decodeResource(context.getResources(), getPresentationImage(),opts);
			data = Bitmap.createScaledBitmap(rawdata, H, H, true);
			presentaionImageSoftReference = new SoftReference<Bitmap>(data);
			if(data != rawdata) {
				rawdata.recycle();
				rawdata = null;
			}
		}else{
			//Log.d("","SOFTED");
		}
		return data;
	}

	public Bitmap getLockedImage(Context context) {
		int H = (int)(ApplicationManager.SCREEN_H*0.6);
		if(H > ApplicationManager.GALLERY_MAX_BITMAP_SIZE_DP) H = ApplicationManager.GALLERY_MAX_BITMAP_SIZE_DP;//Dimensione massima per le immagini.
		Bitmap data = lockedImageSoftReference.get();
		if(data == null) {
			Options opts = new BitmapFactory.Options();
	    	opts.inSampleSize=1;
	    	Bitmap.Config conf = Bitmap.Config.RGB_565;
	    	opts.inPreferredConfig = conf;
	    	opts.inTempStorage = new byte[16*1024];
			Bitmap rawdata = BitmapFactory.decodeResource(context.getResources(), getLockedImage(),opts);
			data = Bitmap.createScaledBitmap(rawdata, H, H, true);
			lockedImageSoftReference = new SoftReference<Bitmap>(data);
			if(data != rawdata) {
				rawdata.recycle();
				rawdata = null;
			}
		}else{
			//Log.d("","SOFTED");
		}
		return data;
	}
	
	
	public int getSfondoImage() {
		return sfondoImage;
	}

	public void setSfondoImage(int sfondoImage) {
		this.sfondoImage = sfondoImage;
	}

	public int getStoryboardImage() {
		return storyboardImage;
	}

	public void setStoryboardImage(int storyboardImage) {
		this.storyboardImage = storyboardImage;
	}

	public int getTelaImage() {
		return telaImage;
	}
	public void setTelaImage(int telaImage) {
		this.telaImage = telaImage;
	}
	public int getCorniceImage() {
		return corniceImage;
	}
	public void setCorniceImage(int corniceImage) {
		this.corniceImage = corniceImage;
	}
	public int getLockedImage() {
		return lockedImage;
	}
	public void setLockedImage(int lockedImage) {
		this.lockedImage = lockedImage;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public int getPresentationImage() {
		return presentationImage;
	}
	public void setPresentationImage(int presentationImage) {
		this.presentationImage = presentationImage;
	}
	public String getSectionName() {
		return sectionName;
	}
	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public int getBossResourceNormal() {
		return bossResourceNormal;
	}
	public void setBossResourceNormal(int bossResourceNormal) {
		this.bossResourceNormal = bossResourceNormal;
	}
	public int getBossResourceSuccess() {
		return bossResourceSuccess;
	}
	public void setBossResourceSuccess(int bossResourceSuccess) {
		this.bossResourceSuccess = bossResourceSuccess;
	}
	public int getBossResourceFailure() {
		return bossResourceFailure;
	}
	public void setBossResourceFailure(int bossResourceFailure) {
		this.bossResourceFailure = bossResourceFailure;
	}
	public int getNumber() {
		return number;
	}
	public boolean isUnlocked(Context context, String gamemode) {
		if(!ApplicationManager.DEBUG_MODE) {
			//Nell'atelier si possono usare i quadri sbloccati nella modalit arcade
			if(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode)) gamemode="arcade";
			
			//Controllo se il livello  stato gi sbloccato o meno
	    	SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
	        String isUnlocked = settings.getString("section_"+this.getSectionName()+"_"+gamemode+"_unlocked", "false");
	        if("true".equalsIgnoreCase(isUnlocked)) {
	        	return true;
	        }else {
	        	return false;
	        }
		}else{
			return true;
		}
	}
	public void unlockSection(Context context, String gamemode) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
		editor.putString("section_"+this.getSectionName()+"_"+gamemode+"_unlocked", "true");
		//Commit the edits!
		editor.commit();
	}
	public String getGainedStars(Context context, String gamemode) {
		int total = 0;
		for(PictureBean pic:this) {
			int best = pic.getBestResultEver(context, gamemode);
			if(best >= ApplicationManager.THREE_STAR_PERCENTAGE) {
				total = total +3;
			}else if(best >= ApplicationManager.TWO_STAR_PERCENTAGE) {
				total = total +2;
			}else if(best >=ApplicationManager.ONE_STAR_PERCENTAGE) {
				total = total +1;
			}
		}
		return ""+total;
	}

}
