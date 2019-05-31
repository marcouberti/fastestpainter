package com.invenktion.android.fastestpainter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.invenktion.android.fastestpainter.core.AnimationFactory;
import com.invenktion.android.fastestpainter.core.ApplicationManager;
import com.invenktion.android.fastestpainter.core.FontFactory;
import com.invenktion.android.fastestpainter.core.LevelManager;
import com.invenktion.android.fastestpainter.core.SoundManager;
import com.invenktion.android.fastestpainter.core.TimeManager;
import com.invenktion.android.fastestpainter.utils.ActivityHelper;
import com.invenktion.android.fastestpainter.utils.LogUtils;
import com.invenktion.android.fastestpainter.utils.SharedPreferencesUtils;
import com.invenktion.android.fastestpainter.view.FingerPaintDrawableView;
import com.invenktion.android.fastestpainter.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Debug;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MenuActivity extends FragmentActivity{
	
	private static final String TAG = "MenuActivity";
	
	//Typeface font; 
	float DENSITY = 1.0f;
	
	static final int DIALOG_EXIT_APPLICATION = 0;
	private boolean waiting = false;
	private boolean waitingAudio = false;


	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onDestroy() {

		//Fermo le animazioni
		ImageView im1 = ((ImageView)findViewById(R.id.roteableimage));
		ImageView im2 = ((ImageView)findViewById(R.id.roteableimage2));
		ImageView im3 = ((ImageView)findViewById(R.id.roteableimage3));
		ImageView im4 = ((ImageView)findViewById(R.id.roteableimage4));
		ImageView im5 = ((ImageView)findViewById(R.id.roteableimage5));
		ImageView puntoImage = (ImageView)findViewById(R.id.puntoimage);
		
		if(im1 != null){
			im1.clearAnimation();
			im1.setAnimation(null);
		}
		if(im2 != null){
			im2.clearAnimation();
			im2.setAnimation(null);
		}
		if(im3 != null){
			im3.clearAnimation();
			im3.setAnimation(null);
		}
		if(im4 != null){
			im4.clearAnimation();
			im4.setAnimation(null);
		}
		if(im5 != null){
			im5.clearAnimation();
			im5.setAnimation(null);
		}
		if(puntoImage != null){
			puntoImage.clearAnimation();
			puntoImage.setAnimation(null);
		}
	
		//Rilascio tutte le risorse audio del SoundPool
		SoundManager.finalizeSounds();
		LevelManager.clearAllCachedImage();
		//AnimationFactory.releaseAllAnimation();
		//Log.d("Sound finalized!","### Sound finalized! ###");

		//Log.e("MenuActivity","DESTROY MenuActivity ####################");
		super.onDestroy();
	}
	
	//Crea il particolare dialog una volta sola
    //Per riconfigurarlo usare onPrepareDialog
    protected Dialog onCreateDialog(int id) {
        final Dialog dialog;
        switch(id) {
        case DIALOG_EXIT_APPLICATION:
        	// prepare the custom dialog
			dialog = new Dialog(this);//con l'app context non si aprono
			dialog.setCancelable(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.exit_application_dialog);
			//dialog.setTitle("Custom Dialog");
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogbg);
			
			TextView textExit = (TextView)dialog.findViewById(R.id.textexit);
			textExit.setTypeface(FontFactory.getFont1(getApplicationContext()));
			
			final ImageView yesButton = (ImageView) dialog.findViewById(R.id.yesButton);
			yesButton.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	yesButton.setEnabled(false);
				        	//Carico l'animazioncina
							Animation selectionAnimation = AnimationFactory.getLevelSelectionAnimation(getApplicationContext());
							//Al termine dell'animazione
							selectionAnimation.setAnimationListener(new AnimationListener() {
								public void onAnimationStart(Animation animation) {	}
								
								public void onAnimationRepeat(Animation animation) {}
								
								public void onAnimationEnd(Animation animation) {
									dialog.dismiss();
									finish();
									overridePendingTransition(0,0);
								}
							});
							//Eseguo l'animazione
							v.startAnimation(selectionAnimation);
				            break;
					}
					return true;
				}
			});
			
			final ImageView noButton = (ImageView) dialog.findViewById(R.id.noButton);
			noButton.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	noButton.setEnabled(false);
				        	//Carico l'animazioncina
							Animation selectionAnimation = AnimationFactory.getLevelSelectionAnimation(getApplicationContext());
							//Al termine dell'animazione
							selectionAnimation.setAnimationListener(new AnimationListener() {
								
								public void onAnimationStart(Animation animation) {	}
								
								public void onAnimationRepeat(Animation animation) {}
								
								public void onAnimationEnd(Animation animation) {
									waiting = false;
									noButton.setEnabled(true);
									dialog.dismiss();
								}
							});
							//Eseguo l'animazione
							v.startAnimation(selectionAnimation);
				            break;
					}
					return true;
				}
			});
            break;
        default:
            dialog = null;
        }
        return dialog;
    }
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	//Log.e("KEY BACK PRESSED","KEY BACK PRESSED");
	    	try {
	    		if(waiting) return false;
		    	else {
		    		waiting = true;
		    		showDialog(DIALOG_EXIT_APPLICATION);
					return true;
		    	}
			}catch (Exception e) {
				e.printStackTrace();
			}
	        return false;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	protected void onResume() {
		super.onResume();
		/*
		ImageView im1 = ((ImageView)findViewById(R.id.roteableimage));
		ImageView im2 = ((ImageView)findViewById(R.id.roteableimage2));
		ImageView im3 = ((ImageView)findViewById(R.id.roteableimage3));
		ImageView im4 = ((ImageView)findViewById(R.id.roteableimage4));
		ImageView im5 = ((ImageView)findViewById(R.id.roteableimage5));
		
		//Creo le 5 cornici in modo casuale
		im1.setImageBitmap(getRandomCornice());
		im2.setImageBitmap(getRandomCornice());
		im3.setImageBitmap(getRandomCornice());
		im4.setImageBitmap(getRandomCornice());
		im5.setImageBitmap(getRandomCornice());
	
		//Faccio partire l'animazione di rotazione dei quadri
        Animation rotAnim = AnimationFactory.getRotationAnimation_1(getApplicationContext());
        Animation rotAnim2 = AnimationFactory.getRotationAnimation_2(getApplicationContext());
        Animation rotAnim3 = AnimationFactory.getRotationAnimation_3(getApplicationContext());
        Animation rotAnim4 = AnimationFactory.getRotationAnimation_4(getApplicationContext());
        Animation rotAnim5 = AnimationFactory.getRotationAnimation_5(getApplicationContext());
        
        rotAnim.setFillAfter(true);rotAnim.setFillBefore(true);
        rotAnim2.setFillAfter(true);rotAnim2.setFillBefore(true);
        rotAnim3.setFillAfter(true);rotAnim3.setFillBefore(true);
        rotAnim4.setFillAfter(true);rotAnim4.setFillBefore(true);
        rotAnim5.setFillAfter(true);rotAnim5.setFillBefore(true);
        
        im1.setAnimation(rotAnim);
		im2.setAnimation(rotAnim2);
		im3.setAnimation(rotAnim3);
		im4.setAnimation(rotAnim4);
		im5.setAnimation(rotAnim5);
		*/
		
		//Se  stata sbloccata una nuova arma animo il menu di TRICKS & TRAP
		ImageView puntoImage = (ImageView)findViewById(R.id.puntoimage);
		if(ApplicationManager.THERE_IS_A_NEW_UNLOCKED_AMMO) {
			puntoImage.setVisibility(View.VISIBLE);
			//puntoImage.setLayoutParams(new LinearLayout.LayoutParams((int)(40*DENSITY+0.5f), (int)(40*DENSITY+0.5f)));
			puntoImage.setAnimation(AnimationFactory.getButtonDialogAnimation(getApplicationContext()));
		}else {
			puntoImage.setVisibility(View.GONE);
			//puntoImage.setLayoutParams(new LinearLayout.LayoutParams(1, 1));
			puntoImage.setAnimation(null);
		}

		//Rilancio la musica se e solo se non  gi attiva
		//Questo ci permette di utilizzare la stessa traccia musicale tra Activity differenti, oltre
		//al metodo presente nel onPause che controlla se siamo o no in background
		KeyguardManager keyguardManager = (KeyguardManager)getApplicationContext().getSystemService(Activity.KEYGUARD_SERVICE);  
    	boolean bloccoSchermoAttivo = keyguardManager.inKeyguardRestrictedInputMode();
		if(!bloccoSchermoAttivo && !SoundManager.isBackgroundMusicPlaying()) {
			SoundManager.playBackgroundMusic(getApplicationContext());
		}

		waiting = false;
		waitingAudio = false;
		LogUtils.logHeap();
	}
	
	private Bitmap getRandomCornice() {
		return LevelManager.getRandomCorniceBitmap(getApplicationContext());
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/*
	@Override
    public void onStart() {
        super.onStart();
        Session.getActiveSession().addCallback(callback);
    }

    @Override
    public void onStop() {
        super.onStop();
        Session.getActiveSession().removeCallback(callback);
    }
	*/
	
	private boolean checkApplicationKill() {
		if(ApplicationManager.APPLICATION_KILLED == null) {
			Intent myIntent = new Intent(MenuActivity.this, SplashScreenActivity.class);
    		MenuActivity.this.startActivity(myIntent);
    		overridePendingTransition(0,0);
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
        
        //uiHelper = new UiLifecycleHelper(this, callback);
        //uiHelper.onCreate(savedInstanceState);
        
        setContentView(R.layout.home);
        this.DENSITY = getApplicationContext().getResources().getDisplayMetrics().density;
        //font = Typeface.createFromAsset(getAssets(), FontFactory.FONT1); 
        
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.homelayout);

        ImageView mascotteImage = (ImageView)findViewById(R.id.mascotteimage);
        mascotteImage.setLayoutParams(new LinearLayout.LayoutParams((int)(ApplicationManager.SCREEN_H/2.5), (int)(ApplicationManager.SCREEN_H/2.5)));

        //SOLO LITE
        mascotteImage.setSoundEffectsEnabled(false);
        /*
        mascotteImage.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent goToMarket = null;
	        	goToMarket = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.invenktion.android.whoisthefastestpainter"));
	        	startActivity(goToMarket);
	        	SoundManager.pauseBackgroundMusic();
			}
		});
		*/
        
        ImageView logo = (ImageView)findViewById(R.id.logoimage);
        double proportion = (double)630/(double)145;
        int H = (int)(ApplicationManager.SCREEN_H*0.30);
        int W = (int)(H*proportion);
        logo.setLayoutParams(new LinearLayout.LayoutParams(W,H));
        
        TextView continua = (TextView)findViewById(R.id.textuno);
        //TextView nuovaPartita = (TextView)findViewById(R.id.textdue);
        TextView atelier = (TextView)findViewById(R.id.texttre);
        //TextView tutorial = (TextView)findViewById(R.id.textquattro);
        TextView armi = (TextView)findViewById(R.id.textcinque);
        
        continua.setTypeface(FontFactory.getFont1(getApplicationContext()));
        //tutorial.setTypeface(FontFactory.getFont1(getApplicationContext()));
        atelier.setTypeface(FontFactory.getFont1(getApplicationContext()));
        //opzioni.setTypeface(FontFactory.getFont1(getApplicationContext()));
        armi.setTypeface(FontFactory.getFont1(getApplicationContext()));
        
        
        continua.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
			        case MotionEvent.ACTION_UP:
			        	if(waiting) return false;
			        	waiting = true;
			        	Intent myIntent = new Intent(MenuActivity.this, SectionChoosingActivity.class);
		        		myIntent.putExtra("gamemode", "arcade");
		        		MenuActivity.this.startActivity(myIntent);
		        		//Set the transition -> method available from Android 2.0 and beyond  
		        		overridePendingTransition(0,0); 
			            break;
				}
				return true;
			}
		});
        atelier.setOnTouchListener(new OnTouchListener() {
			
			
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
			        case MotionEvent.ACTION_UP:
			        	if(waiting) return false;
			        	waiting = true;
			        	Intent myIntent = new Intent(MenuActivity.this, AtelierChoosingPictureActivity.class);
		        		MenuActivity.this.startActivity(myIntent);
		        		overridePendingTransition(0,0);
			            break;
				}
				return true;
			}
		});
        armi.setOnTouchListener(new OnTouchListener() {
			
			
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
			        case MotionEvent.ACTION_UP:
			        	if(waiting) return false;
			        	waiting = true;
			        	Intent myIntent = new Intent(MenuActivity.this, AmmoActivity.class);
		        		MenuActivity.this.startActivity(myIntent);
		        		overridePendingTransition(0,0);
			            break;
				}
				return true;
			}
		});
        
        //Configuro l'animazione di rotazione dei quadri
        /*
        Animation rotAnim = AnimationFactory.getRotationAnimation_1(getApplicationContext());
        Animation rotAnim2 = AnimationFactory.getRotationAnimation_2(getApplicationContext());
        Animation rotAnim3 = AnimationFactory.getRotationAnimation_3(getApplicationContext());
        Animation rotAnim4 = AnimationFactory.getRotationAnimation_4(getApplicationContext());
        Animation rotAnim5 = AnimationFactory.getRotationAnimation_5(getApplicationContext());
        */
        
        ImageView rotImg = (ImageView)findViewById(R.id.roteableimage);
        ImageView rotImg2 = (ImageView)findViewById(R.id.roteableimage2);
        ImageView rotImg3 = (ImageView)findViewById(R.id.roteableimage3);
        ImageView rotImg4 = (ImageView)findViewById(R.id.roteableimage4);
        ImageView rotImg5 = (ImageView)findViewById(R.id.roteableimage5);
        
        int quadrelliH = (int)(ApplicationManager.SCREEN_H * 0.15);
        
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)(quadrelliH*DENSITY+0.5f), (int)(quadrelliH*DENSITY+0.5f));
        params.leftMargin = 50;
        params.topMargin = 60;
        
        RelativeLayout.LayoutParams params2 = new RelativeLayout.LayoutParams((int)(quadrelliH*DENSITY+0.5f), (int)(quadrelliH*DENSITY+0.5f));
        params2.leftMargin = 250;
        params2.topMargin = 160;
        
        RelativeLayout.LayoutParams params3 = new RelativeLayout.LayoutParams((int)(quadrelliH*DENSITY+0.5f), (int)(quadrelliH*DENSITY+0.5f));
        params3.leftMargin = 0;
        params3.topMargin = 260;
        
        RelativeLayout.LayoutParams params4 = new RelativeLayout.LayoutParams((int)(quadrelliH*DENSITY+0.5f), (int)(quadrelliH*DENSITY+0.5f));
        params4.leftMargin = 20;
        params4.topMargin = 360;
        
        RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams((int)(quadrelliH*DENSITY+0.5f), (int)(quadrelliH*DENSITY+0.5f));
        params5.leftMargin = 100;
        params5.topMargin = 30;
        
        rotImg.setLayoutParams(params);
        rotImg2.setLayoutParams(params2);
        rotImg3.setLayoutParams(params3);
        rotImg4.setLayoutParams(params4);
        rotImg5.setLayoutParams(params5);
        
        
        //Creo le 5 cornici in modo casuale
        rotImg.setImageBitmap(getRandomCornice());
        rotImg2.setImageBitmap(getRandomCornice());
        rotImg3.setImageBitmap(getRandomCornice());
        rotImg4.setImageBitmap(getRandomCornice());
        rotImg5.setImageBitmap(getRandomCornice());
	
		//Faccio partire l'animazione di rotazione dei quadri
        Animation rotAnim = AnimationFactory.getRotationAnimation_1(getApplicationContext());
        Animation rotAnim2 = AnimationFactory.getRotationAnimation_2(getApplicationContext());
        Animation rotAnim3 = AnimationFactory.getRotationAnimation_3(getApplicationContext());
        Animation rotAnim4 = AnimationFactory.getRotationAnimation_4(getApplicationContext());
        Animation rotAnim5 = AnimationFactory.getRotationAnimation_5(getApplicationContext());
        
        rotAnim.setFillAfter(true);rotAnim.setFillBefore(true);
        rotAnim2.setFillAfter(true);rotAnim2.setFillBefore(true);
        rotAnim3.setFillAfter(true);rotAnim3.setFillBefore(true);
        rotAnim4.setFillAfter(true);rotAnim4.setFillBefore(true);
        rotAnim5.setFillAfter(true);rotAnim5.setFillBefore(true);
        
        rotImg.setAnimation(rotAnim);
        rotImg2.setAnimation(rotAnim2);
        rotImg3.setAnimation(rotAnim3);
        rotImg4.setAnimation(rotAnim4);
        rotImg5.setAnimation(rotAnim5);
        /*
        rotImg.startAnimation(rotAnim);
        rotImg2.startAnimation(rotAnim2);
        rotImg3.startAnimation(rotAnim3);
        rotImg4.startAnimation(rotAnim4);
        rotImg5.startAnimation(rotAnim5);
        */
        
        final ImageView creditsImage = (ImageView)findViewById(R.id.creditsimage);
        creditsImage.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
			        case MotionEvent.ACTION_UP:
			        	if(waiting) return false;
			        	waiting = true;
			        	Intent myIntent = new Intent(MenuActivity.this, CreditsActivity.class);
		        		MenuActivity.this.startActivity(myIntent);
		        		overridePendingTransition(0,0);
			            break;
				}
				return true;
			}
		});
        
        final ImageView soundImage = (ImageView)findViewById(R.id.soundimage);
        
        //Imposto l'immagine sulla base della preferenza dell'utente (sound on/off)
        String soundState = SoundManager.getSoundPreference(getApplicationContext());
        if(SoundManager.SOUND_ENABLED.equalsIgnoreCase(soundState)) {
        	soundImage.setImageResource(R.drawable.soundon);
        }else {
        	soundImage.setImageResource(R.drawable.soundoff);
        }
        
        soundImage.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
			        case MotionEvent.ACTION_UP:
			        	if(waitingAudio) return false;
			        	waitingAudio = true;
			        	new Thread() {
							public void run() {
								if(SoundManager.SOUND_ON) {
									SoundManager.SOUND_ON = false;
									SoundManager.pauseBackgroundMusic();
									SoundManager.saveSoundPreference(SoundManager.SOUND_DISABLED, getApplicationContext());
									runOnUiThread(new Runnable() {
										public void run() {
											soundImage.setImageResource(R.drawable.soundoff);
										}
									});
								}else {
									SoundManager.SOUND_ON = true;
									SoundManager.playBackgroundMusic(getApplicationContext());
									SoundManager.saveSoundPreference(SoundManager.SOUND_ENABLED, getApplicationContext());
									runOnUiThread(new Runnable() {
										public void run() {
											soundImage.setImageResource(R.drawable.soundon);
										}
									});
								}
								waitingAudio = false;
							};
						}.start();
			            break;
				}
				return true;
			}
		});
        
        //Imposto il volume all'inizio, cosi l'utente poi lo controlla solo con i tasti del device
        SoundManager.initVolume(getApplicationContext());
        
        //Rimossa pubblicita del nuovo gioco 1/11/2013
        //showMarketingDialog();
    }

}
