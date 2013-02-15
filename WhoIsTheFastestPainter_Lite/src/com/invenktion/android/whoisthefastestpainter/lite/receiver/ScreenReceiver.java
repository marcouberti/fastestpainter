package com.invenktion.android.whoisthefastestpainter.lite.receiver;
import com.invenktion.android.whoisthefastestpainter.lite.DrawChallengeActivity;
import com.invenktion.android.whoisthefastestpainter.lite.core.SoundManager;
import com.invenktion.android.whoisthefastestpainter.lite.utils.ActivityHelper;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
 
public class ScreenReceiver extends BroadcastReceiver {
	private final static String TAG = "ScreenReceiver";
    //private static boolean screenOff;
 
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            //screenOff = true;
            SoundManager.pauseBackgroundMusic();
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
        	KeyguardManager keyguardManager = (KeyguardManager)context.getSystemService(Activity.KEYGUARD_SERVICE);  
        	boolean bloccoSchermoAttivo = keyguardManager.inKeyguardRestrictedInputMode();
            //screenOff = false;
        	if(!bloccoSchermoAttivo && !ActivityHelper.isApplicationBroughtToBackground(context)) {
	        	String activityName = ActivityHelper.getTopActivityClassNameWithPackage(context);
	        	//Log.d(TAG,DrawChallengeActivity.class.getName());
	        	if(!DrawChallengeActivity.class.getName().equalsIgnoreCase(activityName)) {
	        		if(!SoundManager.isBackgroundMusicPlaying()) {
	        			SoundManager.playBackgroundMusic(context);
	        		}
	        	}else {
	        		//Durante il gioco ci sarà la musica?
	        		/*
	        		if(!SoundManager.isBackgroundMusicPlaying()) {
	        			SoundManager.playBackgroundMusic(context);
	        		}
	        		*/
	        	}
        	}
        }else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
        	if(!ActivityHelper.isApplicationBroughtToBackground(context)) {
        		//Log.d(TAG,"TO BACKGROUND NO");
	        	String activityName = ActivityHelper.getTopActivityClassNameWithPackage(context);
	        	//Log.d(TAG,DrawChallengeActivity.class.getName());
	        	if(!DrawChallengeActivity.class.getName().equalsIgnoreCase(activityName)) {
	        		if(!SoundManager.isBackgroundMusicPlaying()) {
	        			SoundManager.playBackgroundMusic(context);
	        		}
	        	}else {
	        		//Durante il gioco ci sarà la musica?
	        		/*
	        		if(!SoundManager.isBackgroundMusicPlaying()) {
	        			SoundManager.playBackgroundMusic(context);
	        		}
	        		*/
	        	}
        	}else {
        		//Log.d(TAG,"TO BACKGROUND si");
        	}
        }
    }
 
}