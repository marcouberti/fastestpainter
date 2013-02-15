package com.invenktion.android.whoisthefastestpainter.lite.bean;

import java.lang.ref.SoftReference;

import com.invenktion.android.whoisthefastestpainter.lite.core.AmmoManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.ApplicationManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Log;

public class AmmoBean {

	private int enableCreditTrigger;
	private String name;
	private int presentationImage;
	private int galleryImage;
	private int lockedImage;
	private int instructionImage;
	private boolean positiveAmmo;
	private AmmoBean antiWeapon;
	private SoftReference<Bitmap> galleryImageSoftReference= new SoftReference<Bitmap>(null);

	public AmmoBean(String name,int credits,int presentationImage,int lockedImage,int instructionImage,boolean positiveAmmo, AmmoBean anti, int galleryImage) {
		this.name = name;
		this.enableCreditTrigger = credits;
		this.presentationImage = presentationImage;
		this.instructionImage = instructionImage;
		this.lockedImage = lockedImage;
		this.positiveAmmo = positiveAmmo;
		this.antiWeapon = anti;
		this.galleryImage = galleryImage;
	}

	public void clearSoftReferences() {
		galleryImageSoftReference.clear();
	}
	
	public Bitmap getGalleryPicture(Context context) {
		int H = (int)(ApplicationManager.SCREEN_H*0.8);
		if(H > ApplicationManager.GALLERY_MAX_BITMAP_SIZE_DP) H = ApplicationManager.GALLERY_MAX_BITMAP_SIZE_DP;//Dimensione massima per i quadri.
		Bitmap data = galleryImageSoftReference.get();
		if(data == null) {
			Options opts = new BitmapFactory.Options();
	    	opts.inSampleSize=1;
	    	Bitmap.Config conf = Bitmap.Config.RGB_565;
	    	opts.inPreferredConfig = conf;
	    	opts.inTempStorage = new byte[16*1024];
			Bitmap rawdata = BitmapFactory.decodeResource(context.getResources(), getGalleryImage(),opts);
			data = Bitmap.createScaledBitmap(rawdata, H, H, true);
			galleryImageSoftReference = new SoftReference<Bitmap>(data);
			if(data != rawdata) {
				rawdata.recycle();
				rawdata = null;
			}
		}else{
			//Log.d("","SOFTED");
		}
		return data;
	}
	
	public int getGalleryImage() {
		return galleryImage;
	}

	public void setGalleryImage(int galleryImage) {
		this.galleryImage = galleryImage;
	}



	public AmmoBean getAntiWeapon() {
		return antiWeapon;
	}

	public boolean isPositiveAmmo() {
		return positiveAmmo;
	}

	public int getInstructionImage() {
		return instructionImage;
	}



	public boolean isUnlocked(Context context) {
		
		//Alcune armi sono SEMPRE sbloccate sin dall'inizio.
		if(getEnableCreditTrigger() == ApplicationManager.AMMO_MIN_PRICE_VALUE){
			return true;
		}
		
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
        String isUnlocked = settings.getString("ammo_"+this.getName()+"_unlocked", "false");
        if("true".equalsIgnoreCase(isUnlocked)) {
        	return true;
        }else {
        	return false;
        }
	}

	public int getEnableCreditTrigger() {
		return enableCreditTrigger;
	}

	public int getPresentationImage() {
		return presentationImage;
	}

	public int getLockedImage() {
		return lockedImage;
	}

	public String getName() {
		return name;
	}

	public void unlock(Context context) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
		editor.putString("ammo_"+this.getName()+"_unlocked", "true");
		//Commit the edits!
		editor.commit();
	}

	public String getNumberOfProbability(Context context) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
        int number = settings.getInt("ammo_"+this.getName()+"_howmany", 0);
        
        if(number == 0 && getEnableCreditTrigger() == ApplicationManager.AMMO_MIN_PRICE_VALUE) {
        	number = 1;
        }
        
        return ""+number;
	}
	
	public void incrementNumberOfProbability(Context context) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        int number = Integer.parseInt(getNumberOfProbability(context)) +1;
		editor.putInt("ammo_"+this.getName()+"_howmany", number);
		//Commit the edits!
		editor.commit();
	}
	
}
