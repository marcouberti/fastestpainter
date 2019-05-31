package com.invenktion.android.whoisthefastestpainter.lite;

import com.invenktion.android.whoisthefastestpainter.lite.core.AnimationFactory;
import com.invenktion.android.whoisthefastestpainter.lite.core.ApplicationManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.FontFactory;
import com.invenktion.android.whoisthefastestpainter.lite.core.LevelManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.SoundManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.TimeManager;
import com.invenktion.android.whoisthefastestpainter.lite.utils.ActivityHelper;
import com.invenktion.android.whoisthefastestpainter.lite.utils.SharedPreferencesUtils;
import com.invenktion.android.whoisthefastestpainter.lite.R;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;


public class CreditsActivity extends Activity{
	//Typeface font; 
	float DENSITY = 1.0f;
    private boolean exit = false;
    private boolean fingerDown = false;
    ScrollView scrollView;
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	exit = true;
			finish();
			overridePendingTransition(0,0);
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

    @Override
    protected void onResume() {
    	super.onResume();
    	//Rilancio la musica se e solo se non è già attiva
		//Questo ci permette di utilizzare la stessa traccia musicale tra Activity differenti, oltre
		//al metodo presente nel onPause che controlla se siamo o no in background
		KeyguardManager keyguardManager = (KeyguardManager)getApplicationContext().getSystemService(Activity.KEYGUARD_SERVICE);  
    	boolean bloccoSchermoAttivo = keyguardManager.inKeyguardRestrictedInputMode();
		if(!bloccoSchermoAttivo && !SoundManager.isBackgroundMusicPlaying()) {
			SoundManager.playBackgroundMusic(getApplicationContext());
		}
    }
    
    @Override
	protected void onPause() {
		super.onPause();
		
	}
    
    private boolean checkApplicationKill() {
		if(ApplicationManager.APPLICATION_KILLED == null) {
			finish();
			return true;
		}
		return false;
	}
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean killed = checkApplicationKill();
        if(killed) return;
        setContentView(R.layout.credits);
        this.DENSITY = getApplicationContext().getResources().getDisplayMetrics().density;
        //font = Typeface.createFromAsset(getAssets(), FontFactory.FONT1); 
        //FrameLayout frameLayout = (FrameLayout)findViewById(R.id.homelayout);
        /*
        ImageView logo = (ImageView)findViewById(R.id.logoimage);
        double proportion = (double)630/(double)145;
        int H = (int)(ApplicationManager.SCREEN_H*0.30);
        int W = (int)(H*proportion);
        logo.setLayoutParams(new LinearLayout.LayoutParams(W,H));
        */
        
        ImageView fb = (ImageView)findViewById(R.id.facebook);
        fb.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.facebook.com/pages/Who-Is-The-Fastest-Painter/255336731154242"));
		        startActivity(myIntent);
			}
		});
        ImageView tw = (ImageView)findViewById(R.id.twitter);
        tw.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://twitter.com/#!/invenktion"));
		        startActivity(myIntent);
			}
		});
        
        
        TextView leadartisttext = (TextView)findViewById(R.id.leadartisttext);
        TextView leadprogrammertext = (TextView)findViewById(R.id.leadprogrammertext);
        TextView gamedesigntext = (TextView)findViewById(R.id.gamedesigntext);
        TextView leadartisttextvalue = (TextView)findViewById(R.id.leadartisttextvalue);
        TextView leadprogrammertextvalue = (TextView)findViewById(R.id.leadprogrammertextvalue);
        TextView gamedesigntextvalue = (TextView)findViewById(R.id.gamedesigntextvalue);
        TextView gamedesigntextvalue2 = (TextView)findViewById(R.id.gamedesigntextvalue2);
        TextView dev1 = (TextView)findViewById(R.id.developed1);
        TextView dev2 = (TextView)findViewById(R.id.developed2);
        TextView sound1 = (TextView)findViewById(R.id.sound1);
        TextView sound2 = (TextView)findViewById(R.id.sound2);
        TextView follow1 = (TextView)findViewById(R.id.follow1);
        TextView whois1 = (TextView)findViewById(R.id.whois1);
        
        leadartisttext.setTypeface(FontFactory.getFont1(getApplicationContext()));
        leadprogrammertext.setTypeface(FontFactory.getFont1(getApplicationContext()));
        gamedesigntext.setTypeface(FontFactory.getFont1(getApplicationContext()));
        leadartisttextvalue.setTypeface(FontFactory.getFont1(getApplicationContext()));
        leadprogrammertextvalue.setTypeface(FontFactory.getFont1(getApplicationContext()));
        gamedesigntextvalue.setTypeface(FontFactory.getFont1(getApplicationContext()));
        gamedesigntextvalue2.setTypeface(FontFactory.getFont1(getApplicationContext()));
        dev1.setTypeface(FontFactory.getFont1(getApplicationContext()));
        dev2.setTypeface(FontFactory.getFont1(getApplicationContext()));
        sound1.setTypeface(FontFactory.getFont1(getApplicationContext()));
        sound2.setTypeface(FontFactory.getFont1(getApplicationContext()));
        follow1.setTypeface(FontFactory.getFont1(getApplicationContext()));
        whois1.setTypeface(FontFactory.getFont1(getApplicationContext()));

        scrollView = (ScrollView)findViewById(R.id.scrollcredits);
      
        //Imposto l'altezza delle immagini di inizio e fine trasparenti nella scrollview
        ImageView topTrasp = (ImageView)findViewById(R.id.scrolltrasptop);
        ImageView bottomTrasp = (ImageView)findViewById(R.id.scrolltraspbottom);
        topTrasp.setLayoutParams(new LinearLayout.LayoutParams(10,ApplicationManager.SCREEN_H));
        bottomTrasp.setLayoutParams(new LinearLayout.LayoutParams(10,ApplicationManager.SCREEN_H));
        
        dev2.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.invenktion.com"));
		        startActivity(myIntent);
			}
		});
        whois1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent myIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("http://www.invenktion.com"));
		        startActivity(myIntent);
			}
		});
        
        startScrollingThread();
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
		if(!exit && !fingerDown) {
			//Log.d("","Y = "+scrollView.getScrollY());
			//Log.d("","MAXAUMOUNT  ="+scrollView.getMaxScrollAmount());
			//Log.d("","CHILD = "+scrollView.getChildAt(0).getHeight());
			if(scrollView.getScrollY() >= (scrollView.getChildAt(0).getHeight()-scrollView.getMeasuredHeight())) {
				scrollView.smoothScrollTo(0,0);
			}else {
				scrollView.smoothScrollTo(0,scrollView.getScrollY()+1);
			}
		}
	}
    
    protected void startScrollingThread() {
        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
            	while(!exit) {
            		try {
						Thread.sleep(30);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					
					mHandler.post(mUpdateResults);
            	}
            }
        };
        t.start();
    }
}
