package com.invenktion.android.whoisthefastestpainter.lite;

import com.invenktion.android.whoisthefastestpainter.lite.core.AmmoManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.AnimationFactory;
import com.invenktion.android.whoisthefastestpainter.lite.core.ApplicationManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.LevelManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.SoundManager;
import com.invenktion.android.whoisthefastestpainter.lite.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

//Schermata iniziale
public class SplashScreenActivity extends Activity{
	private boolean exit = false;
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		exit = true;
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	//Impedisco la chiusura
	        return false;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationManager.APPLICATION_KILLED = "APPLICATION_RUNNING";
        setContentView(R.layout.splashscreen);
        
        
        Display display = getWindowManager().getDefaultDisplay(); 
        int width = display.getWidth();
        int height = display.getHeight();
        if(width > height) {
        	ApplicationManager.SCREEN_H = height;
        	ApplicationManager.SCREEN_W = width;
        }else {
        	ApplicationManager.SCREEN_H = width;
        	ApplicationManager.SCREEN_W = height;
        }

        /*
        //Eliminate extra GCs during startup by setting the initial heap size to 8MB.
        int INITIAL_HEAP_SIZE = 8*1000*1000;
        VMRuntime.getRuntime().setMinimumHeapSize(INITIAL_HEAP_SIZE);
		*/
       
        startCountDownThread();
    }

    //Need handler for callbacks to the UI thread
    final Handler mHandler = new Handler();
    //Create runnable for posting
    final Runnable mUpdateResults = new Runnable() {
	        public void run() {
	            updateUI();
	        }
    };
    
	private void updateUI() {
		
		if(!exit) {
			Intent myIntent = new Intent(SplashScreenActivity.this, MenuActivity.class);
			SplashScreenActivity.this.startActivity(myIntent);
			finish();
			overridePendingTransition(0,0);
		}
	}
    
    protected void startCountDownThread() {
        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
            		//Inizializzo la mia applicazione
                	LevelManager.initializeLevels(getApplicationContext());
                	AmmoManager.initializeAmmo();
                	AnimationFactory.initializeAnimation(getApplicationContext());
                	SoundManager.initSounds(getApplicationContext());
                	
            		try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					mHandler.post(mUpdateResults);
					
            }
        };
        t.start();
    }
}
