package com.invenktion.android.whoisthefastestpainter.lite;


import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.FacebookRequestError;
import com.facebook.HttpMethod;
import com.facebook.LoggingBehavior;
import com.facebook.Request;
import com.facebook.RequestAsyncTask;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.UiLifecycleHelper;
import com.invenktion.android.whoisthefastestpainter.lite.bean.AmmoBean;
import com.invenktion.android.whoisthefastestpainter.lite.bean.PictureBean;
import com.invenktion.android.whoisthefastestpainter.lite.bean.SectionArrayList;
import com.invenktion.android.whoisthefastestpainter.lite.core.AmmoManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.AnimationFactory;
import com.invenktion.android.whoisthefastestpainter.lite.core.ApplicationManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.CreditsManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.FontFactory;
import com.invenktion.android.whoisthefastestpainter.lite.core.LevelManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.SoundManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.TimeManager;
import com.invenktion.android.whoisthefastestpainter.lite.view.ColorImageView;
import com.invenktion.android.whoisthefastestpainter.lite.view.FingerPaintDrawableView;
import com.invenktion.android.whoisthefastestpainter.lite.view.GlassPaneDrawableView;
import com.invenktion.android.whoisthefastestpainter.lite.view.GommaImageView;
import com.invenktion.android.whoisthefastestpainter.lite.R;

import android.app.Activity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;

import android.graphics.Bitmap;

import android.graphics.Color;

import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.BitmapDrawable;

import android.os.Bundle;
import android.os.Handler;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import android.widget.FrameLayout;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DrawChallengeActivity extends Activity {
    
	private static final String TAG = "DrawChallengeActivity";
	
	static final int DIALOG_RESULT_LEVEL = 0;
	static final int DIALOG_START_LEVEL = 1;
	static final int DIALOG_PAUSE = 2;
	static final int DIALOG_SECTION_UNLOCKED = 3;
	static final int DIALOG_BONUS_UNLOCKED = 4;
	static final int DIALOG_AMMO_UNLOCKED = 5;
	static final int DIALOG_FINISH_GAME = 6;
	static final int DIALOG_INSTRUCTION = 7;
	
	//facebook
	private UiLifecycleHelper uiHelper;
	private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
	private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
	private boolean pendingPublishReauthorization = false;
	//boolean waiting = false;
	
	float DENSITY = 1.0f;
	int dashboardSize = 1;
	//Typeface font; 
	//Typeface fontPaintSize; 
	//Typeface fontTime; 
	//DynamicBackgroundDrawableView backgroundDrawableView;
	FingerPaintDrawableView fingerPaintDrawableView;
	public ImageView contourImage;
	GlassPaneDrawableView glassPane;
	RelativeLayout relativeLayoutFingerDrawable;
	RelativeLayout ammoRelativeContainer;
	
	//Button nextButton;
	TextView timeText;
	boolean playingTime = false;
	Bitmap resultBitmap = null;
	String resultPercentage = null;
	public FrameLayout frameLayout;
	LinearLayout pennelloLayout;
	LinearLayout ammoLayout;
	LinearLayout colorLayout;
	ImageView countDownText;
	
	ImageView atelierCheckBut = null;
	
	//AnimationDrawable ammoAnimation;
	
	String gamemode;
	private boolean newrecord = false;
	private boolean atleastonestar = false;
	private boolean unlockednextsection = false;
	private boolean unlockedbonus  = false;
	private boolean finishGame  = false;
	private AmmoBean unlockedammo  = null;
	private int oldtotalcredits = 0;
	private int newtotalscredits = 0;
	
	protected int tutorialStep = 0;//per mostrare le immagini di tutorial
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    uiHelper.onSaveInstanceState(outState);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
	    super.onActivityResult(requestCode, resultCode, data);
	    uiHelper.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		playingTime = false;
		uiHelper.onDestroy();
		//Rilascio le risorse Bitmap
		if(fingerPaintDrawableView != null) {
			fingerPaintDrawableView.recycleBitmaps();
		}
		AnimationFactory.releaseAllAnimation();
		//backgroundDrawableView.recycleBitmaps();
		//SoundManager.playBackgroundMusic(getApplicationContext());
		/*
		ApplicationManager.setGOMMA_TEXT(null);
		ApplicationManager.setGOMMA_ICON(null);
		ApplicationManager.setPENNELLO_ICON(null);
		ApplicationManager.setPENNELLO_TEXT(null);
		*/
		//if(ApplicationManager.getGLASS_PANE() != null){
			//ApplicationManager.getGLASS_PANE().recycleBitmaps();
		//}
		ApplicationManager.setGLASS_PANE(null);
		System.gc();
	}

	@Override
	protected void onPause() {
		uiHelper.onPause();
		handlePausingGame();
		super.onPause();
		//Spengo la musica solo se un'altra applicazione è davanti alla nostra (VOICE CALL, HOME Button, etc..)
		/*
		if(ActivityHelper.isApplicationBroughtToBackground(this)) {
			SoundManager.pauseBackgroundMusic();
		}
		*/
	}
	
	@Override
	protected void onResume() {
		uiHelper.onResume();
		//Aggiorno la view, per ovviare al bug del dialog che non si vede più dopo la pausa
		if(playingTime && TimeManager.isPaused()){
    		showDialog(DIALOG_PAUSE);
    	}
		
		//Rilancio la musica se e solo se non è già attiva
		//Questo ci permette di utilizzare la stessa traccia musicale tra Activity differenti, oltre
		//al metodo presente nel onPause che controlla se siamo o no in background
		/*
		KeyguardManager keyguardManager = (KeyguardManager)getApplicationContext().getSystemService(Activity.KEYGUARD_SERVICE);  
    	boolean bloccoSchermoAttivo = keyguardManager.inKeyguardRestrictedInputMode();
		if(!bloccoSchermoAttivo && !SoundManager.isBackgroundMusicPlaying()) {
			SoundManager.playBackgroundMusic(getApplicationContext());
		}
		*/
		super.onResume();
	}
	
	private void handlePausingGame() {
		//Quando il calcolo del risultato è iniziato, non è possibile
    	//mettere in pausa.(playingTime)
		if(playingTime && !TimeManager.isPaused()){
    		TimeManager.setPause(true);
    		showDialog(DIALOG_PAUSE);
    	}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	handlePausingGame();
	        return true;
	    }
	    return super.onKeyDown(keyCode, event);
	}

	private boolean checkApplicationKill() {
		if(ApplicationManager.APPLICATION_KILLED == null) {
			finish();
			return true;
		}
		return false;
	}
	
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i(TAG, "Logged in...");
	        
	    } else if (state.isClosed()) {
	        Log.i(TAG, "Logged out...");
	        
	    }
	}
	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean killed = checkApplicationKill();
        if(killed) return;
        //Metto in pausa la musica di background
        SoundManager.pauseBackgroundMusic();
        //Salvo la modalità di gioco che mi è stata passata
        Bundle extras = getIntent().getExtras();
        if(extras !=null){
        	gamemode = extras.getString("gamemode");
        	////Log.d("GAMEMODE ######",gamemode);
        }
        
        //FACEBOOK
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
        
        this.DENSITY = getApplicationContext().getResources().getDisplayMetrics().density;
        //backgroundDrawableView = new DynamicBackgroundDrawableView(this,R.drawable.desktop3,true);
        glassPane = new GlassPaneDrawableView(this);
        ApplicationManager.setGLASS_PANE(glassPane);

        double aspectRatio = (double)ApplicationManager.SCREEN_W / (double)ApplicationManager.SCREEN_H;
        //Log.d("ASPECT RATION",""+aspectRatio);
        
        //LayoutParams myparams = new RelativeLayout.LayoutParams(ApplicationManager.SCREEN_H/2, ApplicationManager.SCREEN_H/2);
        //fingerPaintDrawableView.setLayoutParams(myparams);
        relativeLayoutFingerDrawable = new RelativeLayout(getApplicationContext());
        relativeLayoutFingerDrawable.setLayoutParams(new FrameLayout.LayoutParams(ApplicationManager.SCREEN_H,ApplicationManager.SCREEN_H,Gravity.CENTER_HORIZONTAL));
        	
        //A seconda se lo schermo è allungato o più quadrato rimpicciolisco la lavagna per non accavallare gli strumenti.
        double fractionH;
        if(aspectRatio < 1.4) {
        	fractionH = 1.3;//1.3
        }else {
        	fractionH = 1;
        }
        
        int pixelCorrispondenti = (int)(ApplicationManager.LAVAGNA_MAX_APPARENT_SIZE_DP*DENSITY+0.5f);
        dashboardSize = (int)(ApplicationManager.SCREEN_H/(fractionH));//in pixel
        
        //Qui posso limitare la dimensione fisica (e quindi anche quella in pixel della bitmap) 
        //della lavagna (per dispositivi con schermi xlarge)
        //Va bene perchè tanto la fingerPaint si adatta sulla sua stessa dimensione e i calcoli poi sono corretti.
        if(dashboardSize > pixelCorrispondenti) dashboardSize = pixelCorrispondenti;
        
		RelativeLayout.LayoutParams fingerRelativeParams = new RelativeLayout.LayoutParams(dashboardSize, dashboardSize);
		fingerRelativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		fingerPaintDrawableView = new FingerPaintDrawableView(getApplicationContext(), this);
		contourImage = new ImageView(getApplicationContext());
		
		relativeLayoutFingerDrawable.addView(fingerPaintDrawableView,fingerRelativeParams);
		relativeLayoutFingerDrawable.addView(contourImage,fingerRelativeParams);
        ammoRelativeContainer = new RelativeLayout(getApplicationContext());
        relativeLayoutFingerDrawable.addView(ammoRelativeContainer,fingerRelativeParams);
        /*
        font = Typeface.createFromAsset(getAssets(), FontFactory.FONT1); 
        fontPaintSize = Typeface.createFromAsset(getAssets(), FontFactory.FONT1); 
        fontTime = Typeface.createFromAsset(getAssets(), FontFactory.FONT1); 
        */
        //LIVELLO 3,2,1
        LinearLayout countDownLayout = new LinearLayout(getApplicationContext());
        LayoutParams countdownparams = new FrameLayout.LayoutParams(ApplicationManager.SCREEN_H, ApplicationManager.SCREEN_H, Gravity.CENTER_HORIZONTAL);
        countDownLayout.setLayoutParams(countdownparams);
        countDownLayout.setGravity(Gravity.CENTER_HORIZONTAL);

        int pixelCorrispondentiCountdown = (int)(ApplicationManager.DIALOG_MAX_APPARENT_SIZE_DP*DENSITY+0.5f);
    	int Hcount = (int)(ApplicationManager.SCREEN_H*0.6);
        if(Hcount> pixelCorrispondentiCountdown) {
        	Hcount = pixelCorrispondentiCountdown;
        }
        
        RelativeLayout relativeLayoutImage = new RelativeLayout(getApplicationContext());
        relativeLayoutImage.setLayoutParams(new FrameLayout.LayoutParams(ApplicationManager.SCREEN_H,ApplicationManager.SCREEN_H,Gravity.CENTER_HORIZONTAL));
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(Hcount,Hcount);
        relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
      
        countDownText = new ImageView(getApplicationContext());
        countDownText.setImageResource(R.drawable.tre);
        //countDownText.setText("3");
        //countDownText.setTextAppearance(getApplicationContext(), R.style.CountdownFont_Black);
        //countDownText.setTypeface(fontTime);
        countDownText.setVisibility(View.INVISIBLE);
        
        relativeLayoutImage.addView(countDownText,relativeParams);
        countDownLayout.addView(relativeLayoutImage);

        //LIVELLO COLORI
        LinearLayout contentColorLayout = new LinearLayout(getApplicationContext());
        contentColorLayout.setOrientation(LinearLayout.HORIZONTAL);	
        contentColorLayout.setGravity(Gravity.LEFT);
        //contentColorLayout.setBackgroundColor(Color.GREEN);
        colorLayout = new LinearLayout(getApplicationContext());
			colorLayout.setOrientation(LinearLayout.VERTICAL);
			colorLayout.setGravity(Gravity.CENTER_VERTICAL);
			//colorLayout.setBackgroundColor(Color.RED);
			LayoutParams colorLayoutparams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT, Gravity.BOTTOM);
			colorLayout.setLayoutParams(colorLayoutparams);
		contentColorLayout.addView(colorLayout);

		//FINE LIVELLO COLORI
		
		//LIVELLO STRUMENTI
		LinearLayout contentAmmoLayout = new LinearLayout(getApplicationContext());
		contentAmmoLayout.setOrientation(LinearLayout.HORIZONTAL);	
		contentAmmoLayout.setGravity(Gravity.RIGHT);
		
		LinearLayout ammoBottomLayout = new LinearLayout(getApplicationContext());
			ammoBottomLayout.setOrientation(LinearLayout.VERTICAL);	
			ammoBottomLayout.setGravity(Gravity.BOTTOM);
			LayoutParams ammoBottomLayoutparams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT, Gravity.BOTTOM);
			ammoBottomLayout.setLayoutParams(ammoBottomLayoutparams);
        //contentColorLayout.setBackgroundColor(Color.GREEN);
        ammoLayout = new LinearLayout(getApplicationContext());
        	ammoLayout.setOrientation(LinearLayout.VERTICAL);
        	ammoLayout.setGravity(Gravity.RIGHT);
			//colorLayout.setBackgroundColor(Color.RED);
			//LayoutParams ammoLayoutparams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.FILL_PARENT, Gravity.BOTTOM);
			//ammoLayout.setLayoutParams(ammoLayoutparams);
    		ammoBottomLayout.addView(ammoLayout);
		contentAmmoLayout.addView(ammoBottomLayout);
		//FINE LIVELLO STRUMENTI
	
        frameLayout = new FrameLayout(getApplicationContext());
        BitmapDrawable drawableBg = (BitmapDrawable)getApplicationContext().getResources().getDrawable(LevelManager.getCurrentSection().getSfondoImage());
    	frameLayout.setBackgroundDrawable(drawableBg);
    	frameLayout.setPadding(0,0,0,0);
    	
    	LinearLayout contentDataLayout = new LinearLayout(getApplicationContext());
    	contentDataLayout.setOrientation(LinearLayout.HORIZONTAL);	
    	contentDataLayout.setGravity(Gravity.RIGHT);
 	    	LinearLayout dataLayout = new LinearLayout(getApplicationContext());
	    	dataLayout.setOrientation(LinearLayout.HORIZONTAL);	
	    	
		    	//Nella modalità atelier c'è un bottone per verificare il risultato quando si vuole al posto del tempo
	    		if(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode)) {
	    			atelierCheckBut = new ImageView(getApplicationContext());
	    			atelierCheckBut.setScaleType(ImageView.ScaleType.FIT_XY);
	    			atelierCheckBut.setImageResource(R.drawable.check);
	    			atelierCheckBut.setOnTouchListener(new OnTouchListener() {
	    				public boolean onTouch(View v, MotionEvent event) {
	    					switch (event.getAction()) {
	    				        case MotionEvent.ACTION_UP:
	    				        	if(!TimeManager.isPaused()) {//se non siamo in pausa
	    				        		atelierCheckBut.setEnabled(false);
    									playingTime = false;
    									fingerPaintDrawableView.startResultElaboration();
	    				        	}
	    				            break;
	    					}
	    					return true;
	    				}
	    			});
	    			int pixelCorrispondentiCheck = (int)(ApplicationManager.STRUMENTO_MAX_APPARENT_HEIGHT_DP*DENSITY+0.5f);
	    			int checkH = (int)(ApplicationManager.SCREEN_H*0.20);
	                if(checkH > pixelCorrispondentiCheck) {
	                	checkH = pixelCorrispondentiCheck;
	                }
	    			atelierCheckBut.setLayoutParams(new LinearLayout.LayoutParams(checkH,checkH));
	    			dataLayout.addView(atelierCheckBut);
	    		}else {
		    		timeText = new TextView(getApplicationContext());
		    		timeText.setTextAppearance(getApplicationContext(), R.style.TimeFont_Black);
		    		
		    		//Metto anche una piccola immagine trasparente per spaziare dal bordo
		    		//ImageView traspTimeImage = new ImageView(getApplicationContext());
		    		//traspTimeImage.setImageResource(R.drawable.transparent);
		    		//traspTimeImage.setLayoutParams(new LinearLayout.LayoutParams((int)(5*DENSITY+0.5f),(int)(5*DENSITY+0.5f)));
		    		//Uso un font personalizzato
		    	    timeText.setTypeface(FontFactory.getFont1(getApplicationContext()));  
		    	    
		    	    dataLayout.addView(timeText);
		    	    //dataLayout.addView(traspTimeImage);
	    		}
	    		//dataLayout.addView(nextButton);
	    	contentDataLayout.addView(dataLayout);
    	
    	frameLayout.addView(relativeLayoutFingerDrawable);
    	frameLayout.addView(glassPane);
	    frameLayout.addView(contentDataLayout);
    	frameLayout.addView(contentColorLayout);
    	frameLayout.addView(contentAmmoLayout);
    	frameLayout.addView(countDownLayout);
    
    	
	    initNextLevel(true,true);

     	setContentView(frameLayout);

    }
   
 
    private void initNextLevel(boolean firstInvocation, boolean showDialog) {
    	AnimationFactory.releaseAllAnimation();
    	//Azzero le dimensioni dei pennelli
    	ApplicationManager.setPaintSize((int)(35*DENSITY +0.5f));
    	//ApplicationManager.setGommaPaintSize((int)(35*DENSITY +0.5f));
    	
    	AmmoManager.initializeUnlockedAmmo(getApplicationContext());
    	
    	//Pulisco il livello con le armi animate
    	if(ammoRelativeContainer!= null) {
    		ammoRelativeContainer.removeAllViews();
    	}
    	
		PictureBean picture = LevelManager.getCurrentLevel();
		if(picture != null) {
			playingTime = false;
			fingerPaintDrawableView.setShowResult(true);
		    fingerPaintDrawableView.setPicture(picture);
		    if(!firstInvocation) {
		    	fingerPaintDrawableView.initializeBitmaps();
		    }
		    
		    //Creo la tavolozza dei colori
	    	colorLayout.removeAllViews();
	    	int[] colori = picture.getColors();
	    	//Metto per primo un bottone fittizio per fixare il bug della tinta che non viene messa sul primo bottone
	    	ImageButton tempBut = new ImageButton(getApplicationContext());
	    	tempBut.setImageResource(R.drawable.stella_black);//immagine a caso
	    	tempBut.setColorFilter(Color.BLACK, Mode.SRC_ATOP);
	    	colorLayout.addView(tempBut);
	    	colorLayout.removeView(tempBut);
	    	//Fine fix bug

    		FrameLayout tavolozzaFrameLayout = new FrameLayout(getApplicationContext());
    		
	    		ImageView tavolozza = new ImageView(getApplicationContext());
	    		tavolozza.setImageResource(R.drawable.tavolozzavuota);
	    		tavolozza.setScaleType(ImageView.ScaleType.FIT_XY);
	    		//La tavolozza la facciamo 80% dell'altezza dello schermo, e fissiamo un limite massimo per gli schermi XLARGE
	    		double tavProportion = 260 / 60;
	    		int pixelCorrispondenti = (int)(ApplicationManager.TAVOLOZZA_MAX_APPARENT_HEIGHT_DP*DENSITY+0.5f);
	    		int tavH = (int)(ApplicationManager.SCREEN_H * 0.8);
	            if(tavH > pixelCorrispondenti) {
	            	tavH = pixelCorrispondenti;
	            }
	    		
	    		int tavW = (int) (tavH / tavProportion);
	    		double scaleFactor = (double)tavW / (double)(60*DENSITY+0.5f);
	    		//Log.d("","scale factor = "+scaleFactor);
	    		tavolozza.setLayoutParams(new LinearLayout.LayoutParams(tavW,tavH));
    		
	    		tavolozzaFrameLayout.addView(tavolozza);
	    		
	    		LinearLayout macchieLayout = new LinearLayout(getApplicationContext());
	    		macchieLayout.setOrientation(LinearLayout.VERTICAL);
	    		macchieLayout.setGravity(Gravity.CENTER_VERTICAL);
	    		for(int i=0; i<colori.length; i++) {
	    			//MASSIMO 5 COLORI
	    			if(i>4)break;
		    		final int colore = colori[i];
		    		//int alphaColor = Color.argb(150, Color.red(colore), Color.green(colore), Color.blue(colore));
		    		ColorImageView imBut = new ColorImageView(getApplicationContext(),fingerPaintDrawableView,colore,true,i,colori.length,scaleFactor);
		    		macchieLayout.addView(imBut);
		    	}
	    		tavolozzaFrameLayout.addView(macchieLayout);
    		
    		colorLayout.addView(tavolozzaFrameLayout);
	    	
	    	/*
	    	else {
		    	for(int i=0; i<colori.length; i++) {
		    		//MASSIMO 5 COLORI
	    			if(i>4)break;
		    		final int colore = colori[i];
		    		//int alphaColor = Color.argb(150, Color.red(colore), Color.green(colore), Color.blue(colore));
		    		ColorImageView imBut = new ColorImageView(getApplicationContext(),fingerPaintDrawableView,colore,true,0,colori.length);
		    		colorLayout.addView(imBut);
		    	}
	    	}
	    	*/
	    	//Fine tavolozza colori
	    	
	    	//Creo gli strumenti disponibili
	    	ammoLayout.removeAllViews();
	    	
	    	double proportion = (double)80/(double)66;
			
			int pixelCorrispondentiStrumenti = (int)(ApplicationManager.STRUMENTO_MAX_APPARENT_HEIGHT_DP*DENSITY+0.5f);
			int H = (int)(ApplicationManager.SCREEN_H*0.20);
            if(H > pixelCorrispondentiStrumenti) {
            	H = pixelCorrispondentiStrumenti;
            }
			int W = (int)((double)H*(double)proportion);
			
			//int H = (int)(66*DENSITY+ 0.5f);
			//int W = (int)(80*DENSITY+ 0.5f);

			//int Hs = (int)(30*DENSITY+ 0.5f);
			//int Ws = (int)(37*DENSITY+ 0.5f);
			int Hs = (int)(H * 0.45);
			int Ws = (int)(W * 0.45);

    		
	    	//pennello (c'è sempre)
			/*
	    	FrameLayout pennelloFrame = new FrameLayout(getApplicationContext());
    			PennelloImageView pennelloBut = new PennelloImageView(getApplicationContext(),fingerPaintDrawableView,false);
    			
    			pennelloFrame.addView(pennelloBut);
    			ImageView pennelloText = new ImageView(getApplicationContext());
    			pennelloText.setImageResource(R.drawable.gommasize4);
    			pennelloText.setLayoutParams(new FrameLayout.LayoutParams(Ws,Hs,Gravity.RIGHT));
    			pennelloBut.setLayoutParams(new FrameLayout.LayoutParams(W,H,Gravity.RIGHT));
    			//pennelloText.setGravity(Gravity.RIGHT);
    			//pennelloText.setTextAppearance(getApplicationContext(), R.style.PaintSizeFont_Red);
	    		//pennelloText.setTypeface(font);  
    			pennelloFrame.addView(pennelloText);
    			ApplicationManager.setPENNELLO_ICON(pennelloBut);
    			ApplicationManager.setPENNELLO_TEXT(pennelloText);
    			//Inizialmente invisibile
    			pennelloBut.setVisibility(View.GONE);
    			pennelloText.setVisibility(View.GONE);
    		//pennelloFrame.setLayoutParams(new FrameLayout.LayoutParams(W,H));
    			
    		ammoLayout.addView(pennelloFrame);
    		*/
    		
			//DIMENSIONI PENNELLO (c'è sempre)
	    	FrameLayout dimensioniFrame = new FrameLayout(getApplicationContext());
	    		final ImageView dimBut = new ImageView(getApplicationContext());
	    		dimBut.setImageResource(R.drawable.gommasize3);
	    		dimBut.setLayoutParams(new FrameLayout.LayoutParams((int)(W/1.2),(int)(W/1.2),Gravity.RIGHT));
	    		ImageView increase = new ImageView(getApplicationContext());
		    		increase.setImageResource(R.drawable.increase);
		    		increase.setLayoutParams(new FrameLayout.LayoutParams(Hs,Hs,Gravity.RIGHT));
	    		ImageView decrease = new ImageView(getApplicationContext());
	    			decrease.setImageResource(R.drawable.decrease);
	    			decrease.setLayoutParams(new FrameLayout.LayoutParams(Hs,Hs,Gravity.BOTTOM));
	    		increase.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						int currentPaintSize = ApplicationManager.getPaintSize();
						if(currentPaintSize == (int)(10*DENSITY +0.5f)) {
							ApplicationManager.setPaintSize((int)(20*DENSITY +0.5f));
							dimBut.setImageResource(R.drawable.gommasize2);
						}else if(currentPaintSize == (int)(20*DENSITY +0.5f)) {
							ApplicationManager.setPaintSize((int)(35*DENSITY +0.5f));
							dimBut.setImageResource(R.drawable.gommasize3);
						}else if(currentPaintSize == (int)(35*DENSITY +0.5f)) {
							ApplicationManager.setPaintSize((int)(45*DENSITY +0.5f));
							dimBut.setImageResource(R.drawable.gommasize4);
						}else if(currentPaintSize == (int)(45*DENSITY +0.5f)) {
							ApplicationManager.setPaintSize((int)(55*DENSITY +0.5f));
							dimBut.setImageResource(R.drawable.gommasize5);
						}else if(currentPaintSize == (int)(55*DENSITY +0.5f)) {
							//Non fa nulla
						}
					}
				});
	    		decrease.setOnClickListener(new OnClickListener() {
					
					public void onClick(View v) {
						int currentPaintSize = ApplicationManager.getPaintSize();
						if(currentPaintSize == (int)(10*DENSITY +0.5f)) {
							//niente
						}else if(currentPaintSize == (int)(20*DENSITY +0.5f)) {
							ApplicationManager.setPaintSize((int)(10*DENSITY +0.5f));
							dimBut.setImageResource(R.drawable.gommasize1);
						}else if(currentPaintSize == (int)(35*DENSITY +0.5f)) {
							ApplicationManager.setPaintSize((int)(20*DENSITY +0.5f));
							dimBut.setImageResource(R.drawable.gommasize2);
						}else if(currentPaintSize == (int)(45*DENSITY +0.5f)) {
							ApplicationManager.setPaintSize((int)(35*DENSITY +0.5f));
							dimBut.setImageResource(R.drawable.gommasize3);
						}else if(currentPaintSize == (int)(55*DENSITY +0.5f)) {
							ApplicationManager.setPaintSize((int)(45*DENSITY +0.5f));
							dimBut.setImageResource(R.drawable.gommasize4);
						}
					}
				});
			dimensioniFrame.addView(dimBut);
			dimensioniFrame.addView(increase);
			dimensioniFrame.addView(decrease);
			//dimensioniFrame.addView(gommaText);
			dimensioniFrame.setLayoutParams(new LinearLayout.LayoutParams((int)(W/1.2),(int)(W/1.2)));
	    	//gommaFrame.setLayoutParams(new FrameLayout.LayoutParams(W,H));
    		ammoLayout.addView(dimensioniFrame);
			
    		//Metto uno spazio
    		ImageView trasp = new ImageView(getApplicationContext());
    		trasp.setImageResource(R.drawable.transparent);
    		trasp.setLayoutParams(new LinearLayout.LayoutParams((int)(H/4),(int)(H/4)));
    		ammoLayout.addView(trasp);
    		
	    	//gomma (c'è sempre)
	    	final int colore = ApplicationManager.TRANSPARENT_COLOR;
	    	FrameLayout gommaFrame = new FrameLayout(getApplicationContext());
	    		GommaImageView imBut = new GommaImageView(getApplicationContext(),fingerPaintDrawableView,colore,false);
	    		//ImageView gommaText = new ImageView(getApplicationContext());
	    		//gommaText.setImageResource(R.drawable.gommasize4);
	    		//gommaText.setLayoutParams(new FrameLayout.LayoutParams(Ws,Hs,Gravity.RIGHT));
	    		imBut.setLayoutParams(new FrameLayout.LayoutParams(W,H,Gravity.RIGHT));
	    		//gommaText.setGravity(Gravity.RIGHT);
	    		//gommaText.setText("2");
	    		//gommaText.setTextAppearance(getApplicationContext(), R.style.PaintSizeFont_Red);
	    		//gommaText.setTypeface(font);  
	    		//ApplicationManager.setGOMMA_ICON(imBut);
    			//ApplicationManager.setGOMMA_TEXT(gommaText);
	    	gommaFrame.addView(imBut);
	    	//gommaFrame.addView(gommaText);
	    	
	    	gommaFrame.setLayoutParams(new FrameLayout.LayoutParams(W,H));
    		ammoLayout.addView(gommaFrame);

    		//Fine strumenti
		    
		    //TimeManager.setStartTime(System.currentTimeMillis());
		    TimeManager.setTotalTime((long)picture.getTimeToComplete());
		    //playingTime = true;
		
		    //Prima di iniziare si vede sempre il quadro ben pitturato, poi scompare
			fingerPaintDrawableView.setmBitmpaPaintAlpha(FingerPaintDrawableView.FULL_ALPHA);//mostro la figura originale che poi scomparirà in alpha
			
		    //SE è' il primo quadro del gioco mostro le istruzioni
		    //Mostro al primo livello arcade e al primo quadro dell'atelier (Apple)
		    if(firstInvocation && mostroTutorial(picture,gamemode)) {//solo al primo avvio mostro le istruzioni
		    	showDialog(DIALOG_INSTRUCTION);
		    }else {
			    try {
			    	if(showDialog) {
			    		showDialog(DIALOG_START_LEVEL);
			    	}else{
			    		startAnimation321GO();
			    	}
				}catch (Exception e) {
					e.printStackTrace();
				}
		    }
			/*
		    if(!(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode))) {
		    	startCountDownThread();
		    }
		    */
		}
	}

    private boolean mostroTutorial(PictureBean picture,String gamemode) {
    	if(picture.getTitle().equalsIgnoreCase("Apple") ||
    			(LevelManager.getCurrentSectionIndex() == 0 && LevelManager.getCurrentLevelIndex() == 0) &&
    			 !(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode))){
    		return true;
    	}
    	return false;
    }
    
	//Need handler for callbacks to the UI thread
    final Handler mHandler = new Handler();
    //Create runnable for posting
    final Runnable mUpdateResults = new Runnable() {
			public void run() {
	            updateUI();
	        }
    };
 
  //Da la possibilità di riconfigurare un dialog
    protected void onPrepareDialog(int id, final Dialog dialog) {
    	switch(id) {
        case DIALOG_RESULT_LEVEL:
        	INTENTIONALLY_CLOSED_RESULT_LEVEL = false;
			//dialog.setTitle("Custom Dialog "+System.currentTimeMillis());
        	if(resultBitmap != null) {
        		final TextView textCredits= (TextView) dialog.findViewById(R.id.dialogcredits);
        		textCredits.setTypeface(FontFactory.getFont1(getApplicationContext()));  
        		
        		TextView textBestResult= (TextView) dialog.findViewById(R.id.dialogbesttext);
        		textBestResult.setTypeface(FontFactory.getFont1(getApplicationContext()));  
        		int currentBest = fingerPaintDrawableView.getPicture().getBestResultEver(getApplicationContext(), gamemode);
        		textBestResult.setText("RECORD: "+currentBest+"%");
        		
        		TextView textPercentage = (TextView) dialog.findViewById(R.id.dialogpercentage);
        		//textPercentage.setTextAppearance(getApplicationContext(), R.style.TimeFont_Black_Dialog_Percentage);
        		textPercentage.setTypeface(FontFactory.getFont1(getApplicationContext()));  
        		textPercentage.setText(resultPercentage+" %");
        		
        		int perc = Integer.parseInt(resultPercentage);
        		
        		//Se sono al record mostro la targhetta di record
        		ImageView recordImage = (ImageView) dialog.findViewById(R.id.recordimage);
        		if(newrecord) {
        			recordImage.setVisibility(View.VISIBLE);
        		}else {
        			recordImage.setAnimation(null);
        			recordImage.setVisibility(View.INVISIBLE);
        		}
        		
        		//Se ho sbloccato una sezione nuova mostro il messaggio
        		/*
        		final FrameLayout framebase = (FrameLayout) dialog.findViewById(R.id.layout_dialog_base);
        		ImageView overImage = (ImageView) dialog.findViewById(R.id.overimage);
        		if(unlockednextsection){
	        		framebase.setVisibility(View.INVISIBLE);
	        		overImage.setImageResource(LevelManager.getNextSection().getBossResourceNormal());
	        		overImage.setVisibility(View.VISIBLE);
        		}else {
        			framebase.setVisibility(View.VISIBLE);
	        		overImage.setImageResource(LevelManager.getCurrentSection().getBossResourceNormal());
	        		overImage.setVisibility(View.INVISIBLE);
        		}
        		*/
        		
        		ImageView image = (ImageView) dialog.findViewById(R.id.dialogimage);
        		//Bitmap scaled = Bitmap.createScaledBitmap(resultBitmap, 150, 150, true);
        		image.setImageBitmap(resultBitmap);
        		
        		ImageView imageBoss = (ImageView) dialog.findViewById(R.id.dialogimageboss);
        		if(perc >= ApplicationManager.ONE_STAR_PERCENTAGE) {
        			SoundManager.playSound(SoundManager.SOUND_POSITIVE, getApplicationContext(), false);
        			imageBoss.setImageResource(LevelManager.getCurrentSection().getBossResourceSuccess());
        		}else {
        			SoundManager.playSound(SoundManager.SOUND_NEGATIVE, getApplicationContext(), false);
        			imageBoss.setImageResource(LevelManager.getCurrentSection().getBossResourceFailure());
        		}
        		
        		ImageView nextButton = (ImageView) dialog.findViewById(R.id.nextButton);
    			//Se è l'ultimo livello della sezione non mostro il bottone next
    			//Anche se il prossimo livello è bloccato non mostro il bottone next (solo in modalità arcade)
    			if(LevelManager.getNextLevel() == null) {
    				nextButton.setAnimation(null);
    				nextButton.setVisibility(View.GONE);
    				//nextButton.setLayoutParams(new LinearLayout.LayoutParams(1, 1));
    			}
    			else if(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode)) {//in atelier non c'è il NEXT button
    				nextButton.setAnimation(null);
					nextButton.setVisibility(View.GONE);
					//nextButton.setLayoutParams(new LinearLayout.LayoutParams(1, 1));
    			}
    			else {
    					if(LevelManager.getNextLevel().isBlocked(getApplicationContext(),gamemode)) {
    						nextButton.setAnimation(null);
    						nextButton.setVisibility(View.GONE);
    						//nextButton.setLayoutParams(new LinearLayout.LayoutParams(1, 1));
    					}else {
    						nextButton.setVisibility(View.VISIBLE);
    						//int pixel = (int)(DENSITY*50 + 0.5f);
    						//nextButton.setLayoutParams(new LinearLayout.LayoutParams(pixel, pixel));
    						//Se il next botton è visibile, lo animo rendendolo evidenziato rispetto al resto
    		    			nextButton.setAnimation(AnimationFactory.getButtonDialogAnimation(getApplicationContext()));
    					}
    			}
    			
    			//Crediti
    			if(!(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode))) {
    				textCredits.setText(getApplicationContext().getString(R.string.experience)+": "+CreditsManager.getUserCredits(getApplicationContext()));
    			}
    			
    			//Solo nella modalità ATELIER esiste questo bottone
    			ImageView continueButton = (ImageView) dialog.findViewById(R.id.continueButton);
    			if(!(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode))) {
    				continueButton.setVisibility(View.GONE);
    				//continueButton.setLayoutParams(new LinearLayout.LayoutParams(1,1));//rimpicciolisco per centrare gli altri bottoni
    			}
    			//In modalità atelier invece non mostro i crediti guadagnati
    			//Non mostro nemmeno l'immagine del boss.
    			else{
    				textCredits.setVisibility(View.GONE);
    				//textCredits.setLayoutParams(new LinearLayout.LayoutParams(1,1));//rimpicciolisco per centrare gli altri elementi
    				
    				imageBoss.setVisibility(View.GONE);
    				//imageBoss.setLayoutParams(new LinearLayout.LayoutParams(1,1));//rimpicciolisco per centrare gli altri elementi
    			}
    		
        		//Metto le stelline
        		ImageView imageStars1 = (ImageView) dialog.findViewById(R.id.dialogstarsimage1);
        		ImageView imageStars2 = (ImageView) dialog.findViewById(R.id.dialogstarsimage2);
        		ImageView imageStars3 = (ImageView) dialog.findViewById(R.id.dialogstarsimage3);
        		//A seconda della percentuale metto le stelline opportune
        		if(perc >= ApplicationManager.THREE_STAR_PERCENTAGE) {
        			imageStars1.setImageResource(R.drawable.stella);
        			imageStars2.setImageResource(R.drawable.stella);
        			imageStars3.setImageResource(R.drawable.stella);
	            }else if(perc >= ApplicationManager.TWO_STAR_PERCENTAGE) {
	            	imageStars1.setImageResource(R.drawable.stella);
        			imageStars2.setImageResource(R.drawable.stella);
        			imageStars3.setImageResource(R.drawable.stella_tr);
	            }else if(perc >= ApplicationManager.ONE_STAR_PERCENTAGE) {
	            	imageStars1.setImageResource(R.drawable.stella);
        			imageStars2.setImageResource(R.drawable.stella_tr);
        			imageStars3.setImageResource(R.drawable.stella_tr);
	            }else {
	            	imageStars1.setImageResource(R.drawable.stella_tr);
        			imageStars2.setImageResource(R.drawable.stella_tr);
        			imageStars3.setImageResource(R.drawable.stella_tr);
	            }
        		
        		//Se ho il new record faccio partire l'animazione
        		if(newrecord){
        			Animation recordAnim = AnimationFactory.getNewRecordAnimation(getApplicationContext());
        				recordAnim.setFillAfter(true);
        			recordImage.startAnimation(recordAnim);
        			recordAnim.setAnimationListener(new AnimationListener() {
						
						public void onAnimationStart(Animation animation) {	}
						
						public void onAnimationRepeat(Animation animation) {}
						
						public void onAnimationEnd(Animation animation) {
							SoundManager.playSound(SoundManager.SOUND_STAMP, getApplicationContext(), false);
						}
					});
        		}
        		
        		
        		//Animazione incremento dei crediti
        		if(!(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode))) {
        			if(atleastonestar){//i crediti incrementano solo se si è raggiunta almeno una STELLA
		        		new Thread() {
		        			public void run() {
		        				int streamID =SoundManager.playSound(SoundManager.SOUND_COINS, getApplicationContext(), true);
		        				for(int i=1; i<=(newtotalscredits - oldtotalcredits); i++) {
		        					final int cont = i;
		        					try {
										Thread.sleep(1);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									runOnUiThread(new Runnable(){
										public void run() {
											try {
												textCredits.setText(getApplicationContext().getString(R.string.experience)+": "+ (oldtotalcredits + cont));
											}catch (Exception e) {
												e.printStackTrace();
											}
										}
										
									});
		        				}
		        				SoundManager.stopSound(streamID);
		        			};
		        		}.start();
	        		}
        		}
        		
        		//FACEBOOK pubblico su bacheca utente i risultati di gioco
        		try{
        			Session session = Session.getActiveSession();
        			//pubblico solo ai nuovi record (ossia almeno una volta per quadro nuovo durante il gioco arcade)
        		    if (newrecord && session != null && !session.isClosed()){

        		        // Check for publish permissions    
        		        List<String> permissions = session.getPermissions();
        		        if (!isSubsetOf(PERMISSIONS, permissions)) {
        		            pendingPublishReauthorization = true;
        		            Session.NewPermissionsRequest newPermissionsRequest = new Session
        		                    .NewPermissionsRequest(this, PERMISSIONS);
        		        session.requestNewPublishPermissions(newPermissionsRequest);
        		            return;
        		        }
        		        String nomeQuadro = fingerPaintDrawableView.getPicture().getTitle();
        		        String nomeImmagine = fingerPaintDrawableView.getPicture().getImmagineNome();
        		        Bundle postParams = new Bundle();
        		        postParams.putString("name", "Fastest Painter");
        		        postParams.putString("caption", "New record!");
        		        postParams.putString("description", "Passed "+nomeQuadro+" painting with a new record of "+perc+"%");
        		        postParams.putString("link", "https://play.google.com/store/apps/details?id=com.invenktion.android.whoisthefastestpainter.lite");
        		        postParams.putString("picture", "http://www.invenktion.com/FastestPainterweb/"+nomeImmagine);

        		        Request.Callback callback= new Request.Callback() {
        		            public void onCompleted(Response response) {
        		            	try {
	        		                JSONObject graphResponse = response
	        		                                           .getGraphObject()
	        		                                           .getInnerJSONObject();
	        		                String postId = null;
	        		                try {
	        		                    postId = graphResponse.getString("id");
	        		                } catch (JSONException e) {
	        		                    Log.i(TAG,
	        		                        "JSON error "+ e.getMessage());
	        		                }
	        		                FacebookRequestError error = response.getError();
	        		                if (error != null) {
	        		                    Toast.makeText(DrawChallengeActivity.this.getApplicationContext(),
	        		                         error.getErrorMessage(),
	        		                         Toast.LENGTH_SHORT).show();
	        		                    } else {
	        		                    	/*
	        		                        Toast.makeText(DrawChallengeActivity.this
	        		                             .getApplicationContext(), 
	        		                             "Facebook post send with success",
	        		                             Toast.LENGTH_SHORT).show();
	        		                             */
        		                }
        		            	}catch(Exception e){e.printStackTrace();}
        		            }
        		        };

        		        Request request = new Request(session, "me/feed", postParams, 
        		                              HttpMethod.POST, callback);

        		        RequestAsyncTask task = new RequestAsyncTask(request);
        		        task.execute();
        		    }
        	    }catch(Exception e){
        	    	e.printStackTrace();
        	    }
        	}
            break;
        case DIALOG_SECTION_UNLOCKED:
        	INTENTIONALLY_CLOSED_SECTION_UNLOCKED = false;
        	ImageView nextSectionUnlockedImage = (ImageView) dialog.findViewById(R.id.unlockedimage);
        	SectionArrayList<PictureBean> newSection = LevelManager.getNextSection();
        	if(newSection != null) {//Non dovrebbe mai esserlo in condizioni normali
        		nextSectionUnlockedImage.setImageResource(newSection.getPresentationImage());
        	}else {
        		nextSectionUnlockedImage.setImageResource(R.drawable.logo);
        	}
        	SoundManager.playSound(SoundManager.SOUND_POSITIVE, getApplicationContext(), false);
        	break;
        case DIALOG_BONUS_UNLOCKED:
        	INTENTIONALLY_CLOSED_BONUS_UNLOCKED = false;
        	ImageView bonusUnlockedImage = (ImageView) dialog.findViewById(R.id.unlockedimage);
        	SectionArrayList<PictureBean> bonusSection = LevelManager.getBonusSection();
        	int bonusSize = LevelManager.getBonusSection().size();
        	if(LevelManager.getCurrentSectionIndex() < bonusSize) {
	        	PictureBean bonus = bonusSection.get(LevelManager.getCurrentSectionIndex());
	        	
	        	if(bonus != null) {//Non dovrebbe mai esserlo in condizioni normali
	        		bonusUnlockedImage.setImageResource(bonus.getColoredPicture());
	        	}else {
	        		bonusUnlockedImage.setImageResource(R.drawable.logo);
	        	}
        	}else {
        		bonusUnlockedImage.setImageResource(R.drawable.logo);
        	}
        	SoundManager.playSound(SoundManager.SOUND_POSITIVE, getApplicationContext(), false);
        	break;
        case DIALOG_AMMO_UNLOCKED:
        	INTENTIONALLY_CLOSED_AMMO_UNLOCKED = false;
        	TextView unlockedText = (TextView)dialog.findViewById(R.id.unlockedtext);
        	if(unlockedammo != null) {//Non dovrebbe mai esserlo in condizioni normali
        			unlockedText.setText(getApplicationContext().getString(R.string.ammounlocked));
        	}
        	
        	ImageView ammoUnlockedImage = (ImageView) dialog.findViewById(R.id.unlockedimage);
        	if(unlockedammo != null) {//Non dovrebbe mai esserlo in condizioni normali
        		ammoUnlockedImage.setImageResource(unlockedammo.getGalleryImage());
        	}else {
        		ammoUnlockedImage.setImageResource(R.drawable.logo);
        	}
        	SoundManager.playSound(SoundManager.SOUND_POSITIVE, getApplicationContext(), false);
        	break;
        case DIALOG_START_LEVEL:
        	INTENTIONALLY_CLOSED_START = false;
        	// prepare the custom dialog
			ImageView startImage = (ImageView) dialog.findViewById(R.id.dialogimageboss);
			startImage.setImageResource(LevelManager.getCurrentSection().getBossResourceNormal());
			
			// prepare the custom dialog
			ImageView pictureImage = (ImageView) dialog.findViewById(R.id.dialogimage);
			pictureImage.setImageResource(LevelManager.getCurrentLevel().getColoredPicture());
			
			TextView titleTextView = (TextView) dialog.findViewById(R.id.dialogtitle);
			titleTextView.setText(fingerPaintDrawableView.getPicture().getTitle().toUpperCase());
			titleTextView.setTypeface(FontFactory.getFont1(getApplicationContext()));
			
			TextView bestTextView = (TextView) dialog.findViewById(R.id.dialogbesttext);
			bestTextView.setText("RECORD: "+fingerPaintDrawableView.getPicture().getBestResultEver(getApplicationContext(), gamemode)+"%");
			bestTextView.setTypeface(FontFactory.getFont1(getApplicationContext()));
			
			//In modalità atelier invece non mostro i crediti guadagnati
			//Non mostro nemmeno l'immagine del boss.
			if((ApplicationManager.ATELIER.equalsIgnoreCase(gamemode))) {
				startImage.setVisibility(View.GONE);
				//startImage.setLayoutParams(new LinearLayout.LayoutParams(1,1));//rimpicciolisco per centrare gli altri elementi
			}
			
            break;
        case DIALOG_PAUSE:
        	INTENTIONALLY_CLOSED_PAUSE = false;
        	// prepare the custom dialog
			ImageView pause_startImage = (ImageView) dialog.findViewById(R.id.dialogimageboss);
			pause_startImage.setImageResource(LevelManager.getCurrentSection().getBossResourceNormal());
			
			// prepare the custom dialog
			ImageView pause_pictureImage = (ImageView) dialog.findViewById(R.id.dialogimage);
			pause_pictureImage.setImageResource(LevelManager.getCurrentLevel().getColoredPicture());
			
			TextView pause_titleTextView = (TextView) dialog.findViewById(R.id.dialogtitle);
			pause_titleTextView.setText(fingerPaintDrawableView.getPicture().getTitle().toUpperCase());
			pause_titleTextView.setTypeface(FontFactory.getFont1(getApplicationContext()));
			
			TextView pause_bestTextView = (TextView) dialog.findViewById(R.id.dialogbesttext);
			pause_bestTextView.setText("RECORD: "+fingerPaintDrawableView.getPicture().getBestResultEver(getApplicationContext(), gamemode)+"%");
			pause_bestTextView.setTypeface(FontFactory.getFont1(getApplicationContext()));
			
			//In modalità atelier invece non mostro i crediti guadagnati
			//Non mostro nemmeno l'immagine del boss.
			if((ApplicationManager.ATELIER.equalsIgnoreCase(gamemode))) {
				pause_startImage.setVisibility(View.GONE);
				//pause_startImage.setLayoutParams(new LinearLayout.LayoutParams(1,1));//rimpicciolisco per centrare gli altri elementi
			}
			
            break;
        case DIALOG_INSTRUCTION:
        	INTENTIONALLY_CLOSED_INSTRUCTION = false;
        	tutorialStep = 0;
        	
        	TextView skip = (TextView)dialog.findViewById(R.id.skiptext);
        	skip.setSoundEffectsEnabled(false);
        	skip.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					INTENTIONALLY_CLOSED_INSTRUCTION = true;
					dialog.dismiss();
					showDialog(DIALOG_START_LEVEL);
				}
			});
        	
        	final ImageView instructionImage = (ImageView) dialog.findViewById(R.id.instructionImage);
			int pixelCorrispondenti = (int)(ApplicationManager.DIALOG_MAX_APPARENT_SIZE_DP*DENSITY+0.5f);
			int H = (int)(ApplicationManager.SCREEN_H*0.9);
            if(H > pixelCorrispondenti) {
            	H = pixelCorrispondenti;
            }
			instructionImage.setLayoutParams(new LinearLayout.LayoutParams(H, H));
			instructionImage.setSoundEffectsEnabled(false);
			instructionImage.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(tutorialStep < 0) tutorialStep = 0;
					
					if(tutorialStep == 0) {
						instructionImage.setImageResource(R.drawable.tutorial2);
					}else if(tutorialStep == 1) {
						instructionImage.setImageResource(R.drawable.tutorial3);
					}else if(tutorialStep == 2) {
						instructionImage.setImageResource(R.drawable.tutorial4);
					}else if(tutorialStep == 3) {
						instructionImage.setImageResource(R.drawable.tutorial5);
					}else {
						INTENTIONALLY_CLOSED_INSTRUCTION = true;
						dialog.dismiss();
						showDialog(DIALOG_START_LEVEL);
					}
					tutorialStep++;
				}
			});
        	/*
        	final ImageView prev = (ImageView) dialog.findViewById(R.id.prevtutorial);
        	prev.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					tutorialStep--;
					if(tutorialStep <0) tutorialStep = -1;
				
					if(tutorialStep <= 0) {
						instructionImage.setImageResource(R.drawable.tutorial1);
					}else if(tutorialStep == 1) {
						instructionImage.setImageResource(R.drawable.tutorial2);
					}else if(tutorialStep == 2) {
						instructionImage.setImageResource(R.drawable.tutorial3);
					}else if(tutorialStep == 3) {
						instructionImage.setImageResource(R.drawable.tutorial4);
					}else {
						dialog.dismiss();
						showDialog(DIALOG_START_LEVEL);
					}
				}
			});
        	final ImageView next = (ImageView) dialog.findViewById(R.id.nexttutorial);
        	next.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					
					if(tutorialStep < 0) tutorialStep = 0;
					
					if(tutorialStep == 0) {
						instructionImage.setImageResource(R.drawable.tutorial2);
					}else if(tutorialStep == 1) {
						instructionImage.setImageResource(R.drawable.tutorial3);
					}else if(tutorialStep == 2) {
						instructionImage.setImageResource(R.drawable.tutorial4);
					}else if(tutorialStep == 3) {
						instructionImage.setImageResource(R.drawable.tutorial5);
					}else {
						dialog.dismiss();
						showDialog(DIALOG_START_LEVEL);
					}
					tutorialStep++;
				}
			});
			*/
        	
            break;
        default:
            //dialog = null;
        }
    };
    
	//Crea il particolare dialog una volta sola
    //Per riconfigurarlo usare onPrepareDialog
    //Una veriabile di gestione chiusure per ogni tipo di dialog
    boolean INTENTIONALLY_CLOSED_INSTRUCTION = false;
    boolean INTENTIONALLY_CLOSED_RESULT_LEVEL = false;
    boolean INTENTIONALLY_CLOSED_SECTION_UNLOCKED = false;
    boolean INTENTIONALLY_CLOSED_BONUS_UNLOCKED = false;
    boolean INTENTIONALLY_CLOSED_AMMO_UNLOCKED = false;
    boolean INTENTIONALLY_CLOSED_GAME_FINISH = false;
    boolean INTENTIONALLY_CLOSED_START = false;
    boolean INTENTIONALLY_CLOSED_PAUSE = false;
    
    protected Dialog onCreateDialog(int id) {
        final Dialog dialog;
        switch(id) {
        case DIALOG_RESULT_LEVEL:
        	// prepare the custom dialog
			dialog = new Dialog(this);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					//Come se avesse cliccato MENU BUTTON
					if(!INTENTIONALLY_CLOSED_RESULT_LEVEL) {
						DrawChallengeActivity.this.finish();
						DrawChallengeActivity.this.overridePendingTransition(0,0);
					}
				}
			});
			
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_dialog);
			//dialog.setTitle("Custom Dialog");
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogbg);
			//Solo nella modalità ATELIER esiste questo bottone
			ImageView continueButton = (ImageView) dialog.findViewById(R.id.continueButton);
			ImageView nextButton = (ImageView) dialog.findViewById(R.id.nextButton);
			ImageView retryButton = (ImageView) dialog.findViewById(R.id.retryButton);	
			ImageView levelMenuButton = (ImageView) dialog.findViewById(R.id.levelMenuButton);
			final Vector<ImageView> tasti = new Vector<ImageView>();
			tasti.add(continueButton);
			tasti.add(nextButton);
			tasti.add(retryButton);
			tasti.add(levelMenuButton);
			
			continueButton.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	INTENTIONALLY_CLOSED_RESULT_LEVEL = true;
				        	bloccaITasti(tasti);
				        	dialog.dismiss();
							sbloccaITasti(tasti);
							playingTime = true;
						    fingerPaintDrawableView.setShowResult(false);//abilito il disegno
				        	
				            break;
					}
					return true;
				}
			});
			
			nextButton.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	INTENTIONALLY_CLOSED_RESULT_LEVEL = true;
				        	bloccaITasti(tasti);
				        	LevelManager.setCurrentLevelIndex(LevelManager.getCurrentLevelIndex() + 1);
							
							initNextLevel(false,true);
							dialog.dismiss();
							sbloccaITasti(tasti);
				        	
				            break;
					}
					return true;
				}
			});
			
			retryButton.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	INTENTIONALLY_CLOSED_RESULT_LEVEL = true;
				        	bloccaITasti(tasti);
				        	if(timeText != null){
								timeText.setText("");
							}
							initNextLevel(false,false);
							dialog.dismiss();
							sbloccaITasti(tasti);
				            break;
					}
					return true;
				}
			});
			
			levelMenuButton.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	INTENTIONALLY_CLOSED_RESULT_LEVEL = true;
				        	bloccaITasti(tasti);
				        	dialog.dismiss();
				        	sbloccaITasti(tasti);
							DrawChallengeActivity.this.finish();
							DrawChallengeActivity.this.overridePendingTransition(0,0);
				            break;
					}
					return true;
				}
			});
            break;
        case DIALOG_SECTION_UNLOCKED:
        	dialog = new Dialog(this);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					//Come se avesse cliccato MENU BUTTON
					if(!INTENTIONALLY_CLOSED_SECTION_UNLOCKED) {
						showDialog(DIALOG_RESULT_LEVEL);
					}
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.unlockedsection);
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogbg);
			
			TextView textUnlocked = (TextView)dialog.findViewById(R.id.unlockedtext);
			textUnlocked.setTypeface(FontFactory.getFont1(getApplicationContext()));
			
			FrameLayout flsec = (FrameLayout)dialog.findViewById(R.id.frameunlockedsection);
			flsec.setSoundEffectsEnabled(false);
			flsec.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					INTENTIONALLY_CLOSED_SECTION_UNLOCKED = true;
					dialog.dismiss();
					showDialog(DIALOG_RESULT_LEVEL);
				}
			});
        	break;
        case DIALOG_BONUS_UNLOCKED:
        	dialog = new Dialog(this);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					//Come se avesse cliccato MENU BUTTON
					if(!INTENTIONALLY_CLOSED_BONUS_UNLOCKED) {
						if(unlockednextsection) {
							showDialog(DIALOG_SECTION_UNLOCKED);
						}else {
							showDialog(DIALOG_RESULT_LEVEL);
						}
					}
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.unlockedbonus);
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogbg);
			
			TextView textUnlockedBonus = (TextView)dialog.findViewById(R.id.unlockedtext);
			textUnlockedBonus.setTypeface(FontFactory.getFont1(getApplicationContext()));
			
			FrameLayout flbonus = (FrameLayout)dialog.findViewById(R.id.frameunlockedbonus);
			flbonus.setSoundEffectsEnabled(false);
			flbonus.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					INTENTIONALLY_CLOSED_BONUS_UNLOCKED = true;
					dialog.dismiss();
					if(unlockednextsection) {
						showDialog(DIALOG_SECTION_UNLOCKED);
					}else {
						showDialog(DIALOG_RESULT_LEVEL);
					}
				}
			});
			break;
        case DIALOG_AMMO_UNLOCKED:
        	dialog = new Dialog(this);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					//Come se avesse cliccato MENU BUTTON
					if(!INTENTIONALLY_CLOSED_AMMO_UNLOCKED) {
						if(unlockedbonus) {
							showDialog(DIALOG_BONUS_UNLOCKED);
						}else if(unlockednextsection) {
							showDialog(DIALOG_SECTION_UNLOCKED);
						}else {
							showDialog(DIALOG_RESULT_LEVEL);
						}
					}
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.unlockedammo);
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogbg);
			
			TextView textUnlockedAmmo = (TextView)dialog.findViewById(R.id.unlockedtext);
			textUnlockedAmmo.setTypeface(FontFactory.getFont1(getApplicationContext()));
			TextView textUnlocked2 = (TextView)dialog.findViewById(R.id.unlockedtext2);
			textUnlocked2.setTypeface(FontFactory.getFont1(getApplicationContext()));
			
			FrameLayout flammo = (FrameLayout)dialog.findViewById(R.id.frameunlockedammo);
			flammo.setSoundEffectsEnabled(false);
			flammo.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					INTENTIONALLY_CLOSED_AMMO_UNLOCKED = true;
					dialog.dismiss();
					if(unlockedbonus) {
						showDialog(DIALOG_BONUS_UNLOCKED);
					}else if(unlockednextsection) {
						showDialog(DIALOG_SECTION_UNLOCKED);
					}else {
						showDialog(DIALOG_RESULT_LEVEL);
					}
				}
			});
			break;
        case DIALOG_FINISH_GAME:
        	dialog = new Dialog(this);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					//Come se avesse cliccato MENU BUTTON
					if(!INTENTIONALLY_CLOSED_GAME_FINISH) {
						if(unlockedammo != null){
							showDialog(DIALOG_AMMO_UNLOCKED);
						}else if(unlockedbonus) {
							showDialog(DIALOG_BONUS_UNLOCKED);
						}else if(unlockednextsection) {
							showDialog(DIALOG_SECTION_UNLOCKED);
						}else {
							showDialog(DIALOG_RESULT_LEVEL);
						}
					}
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.unlockedgame);
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogbg);
			
			TextView textUnlockedGame = (TextView)dialog.findViewById(R.id.unlockedtext);
			textUnlockedGame.setTextAppearance(getApplicationContext(), R.style.DialogPaintTitle_White);
			textUnlockedGame.setTypeface(FontFactory.getFont1(getApplicationContext()));
			
			FrameLayout fl = (FrameLayout)dialog.findViewById(R.id.frameunlockedgame);
			fl.setSoundEffectsEnabled(false);
			fl.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					INTENTIONALLY_CLOSED_GAME_FINISH = true;
					dialog.dismiss();
					if(unlockedammo != null){
						showDialog(DIALOG_AMMO_UNLOCKED);
					}else if(unlockedbonus) {
						showDialog(DIALOG_BONUS_UNLOCKED);
					}else if(unlockednextsection) {
						showDialog(DIALOG_SECTION_UNLOCKED);
					}else {
						showDialog(DIALOG_RESULT_LEVEL);
					}
				}
			});
			SoundManager.playSound(SoundManager.SOUND_POSITIVE, getApplicationContext(), false);
			break;
        case DIALOG_START_LEVEL:
        	// prepare the custom dialog
			dialog = new Dialog(this);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					//Come se avesse cliccato MENU BUTTON
					if(!INTENTIONALLY_CLOSED_START) {
						Log.d("NOT INTENTIONALLY_CLOSED_START","NOT INTENTIONALLY_CLOSED_START");
						DrawChallengeActivity.this.finish();
						DrawChallengeActivity.this.overridePendingTransition(0,0);
					}else {
						Log.d("INTENTIONALLY_CLOSED_START","INTENTIONALLY_CLOSED_START");
					}
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_start_dialog);
			//dialog.setTitle("Custom Dialog");
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogbg);
			
			ImageView startMenuButton = (ImageView) dialog.findViewById(R.id.menuButton);
			ImageView startButton = (ImageView) dialog.findViewById(R.id.startButton);
			
			final Vector<ImageView> tastiStartDialog = new Vector<ImageView>();
			tastiStartDialog.add(startMenuButton);
			tastiStartDialog.add(startButton);
			
			startMenuButton.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	INTENTIONALLY_CLOSED_START = true;
				        	bloccaITasti(tastiStartDialog);
				        	dialog.dismiss();
				        	DrawChallengeActivity.this.finish();
							DrawChallengeActivity.this.overridePendingTransition(0,0);
				        	sbloccaITasti(tastiStartDialog);
				            break;
					}
					return true;
				}
			});
			
			//startButton.setImageResource(R.drawable.btn_start);
			startButton.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	INTENTIONALLY_CLOSED_START = true;
				        	bloccaITasti(tastiStartDialog);
				        	dialog.dismiss();
				        	startAnimation321GO();
				        	sbloccaITasti(tastiStartDialog);
				            break;
					}
					return true;
				}
			});
            break;
        case DIALOG_PAUSE:
        	// prepare the custom dialog
			dialog = new Dialog(this);
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					//Come se avesse cliccato MENU BUTTON
					if(!INTENTIONALLY_CLOSED_PAUSE) {
						DrawChallengeActivity.this.finish();
						DrawChallengeActivity.this.overridePendingTransition(0,0);
					}
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.custom_pause_dialog);
			//dialog.setTitle("Custom Dialog");
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogbg);
			ImageView pauseButton = (ImageView) dialog.findViewById(R.id.pauseButton);
			ImageView menuButton = (ImageView) dialog.findViewById(R.id.menuButton);
			ImageView retryButtonPause = (ImageView) dialog.findViewById(R.id.retryButton);	
			
			final Vector<ImageView> tastiPauseDialog = new Vector<ImageView>();
			tastiPauseDialog.add(pauseButton);
			tastiPauseDialog.add(menuButton);
			tastiPauseDialog.add(retryButtonPause);
			
			pauseButton.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	INTENTIONALLY_CLOSED_PAUSE = true;
				        	bloccaITasti(tastiPauseDialog);
				        	TimeManager.setPause(false);
							dialog.dismiss();
							sbloccaITasti(tastiPauseDialog);
				            break;
					}
					return true;
				}
			});
			
			menuButton.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	INTENTIONALLY_CLOSED_PAUSE = true;
				        	bloccaITasti(tastiPauseDialog);
							dialog.dismiss();
							sbloccaITasti(tastiPauseDialog);
							DrawChallengeActivity.this.finish();
							DrawChallengeActivity.this.overridePendingTransition(0,0);
				            break;
					}
					return true;
				}
			});
				
			retryButtonPause.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	INTENTIONALLY_CLOSED_PAUSE = true;
				        	bloccaITasti(tastiPauseDialog);
				        	if(timeText != null){
								timeText.setText("");
							}
							initNextLevel(false,false);
							dialog.dismiss();
							sbloccaITasti(tastiPauseDialog);
				            break;
					}
					return true;
				}
			});
			/*
			ImageView pauseImage = (ImageView) dialog.findViewById(R.id.pausedialogimage);
			pauseImage.setImageResource(LevelManager.getCurrentSection().getBossResourceNormal());
			
			
			((TextView) dialog.findViewById(R.id.startlabel)).setTypeface(fontPaintSize);
			((TextView) dialog.findViewById(R.id.starttitlelabel)).setTypeface(font);
			((TextView) dialog.findViewById(R.id.startrecordlabel)).setTypeface(font);
			*/
			/*
			TextView titlePauseTextView = (TextView) dialog.findViewById(R.id.starttitlepicture);
			titlePauseTextView.setText(fingerPaintDrawableView.getPicture().getTitle());
			titlePauseTextView.setTypeface(font);
			TextView recordPauseTextView = (TextView) dialog.findViewById(R.id.startrecordpicture);
			recordPauseTextView.setText(fingerPaintDrawableView.getPicture().getBestResultEver(getApplicationContext(), gamemode)+"%");
			recordPauseTextView.setTypeface(font);
			*/
			
			//dialog.show();
            break;
        case DIALOG_INSTRUCTION:
        	// prepare the custom dialog
			dialog = new Dialog(this);//con l'app context non si aprono
			dialog.setCancelable(true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(DialogInterface dialog) {
					//Come se avesse cliccato MENU BUTTON
					if(!INTENTIONALLY_CLOSED_INSTRUCTION) {
						DrawChallengeActivity.this.finish();
						DrawChallengeActivity.this.overridePendingTransition(0,0);
					}
				}
			});
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.tutorial_dialog);
			//dialog.setTitle("Custom Dialog");
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent);
			TextView skip = (TextView)dialog.findViewById(R.id.skiptext);
			skip.setTypeface(FontFactory.getFont1(getApplicationContext()));
            break;
        default:
            dialog = null;
        }
        return dialog;
    }
    
    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for (String string : subset) {
            if (!superset.contains(string)) {
                return false;
            }
        }
        return true;
    }
    
    protected void bloccaITasti(Vector<ImageView> tasti) {
		for(final ImageView iv:tasti) {
			iv.setEnabled(false);
		}
	}
    protected void sbloccaITasti(Vector<ImageView> tasti) {
    	for(final ImageView iv:tasti) {
			iv.setEnabled(true);
		}
	}

	protected void startAnimation321GO() {
    	if(!(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode))) {
			//Lancio l'animazione del 3,2,1 e al termine faccio partire il gioco
			//Carico l'animazioncina
			final Animation countDownAnimation = AnimationFactory.getCountDownAnimation3(getApplicationContext());
			final Animation countDownAnimation2 = AnimationFactory.getCountDownAnimation2(getApplicationContext());
			final Animation countDownAnimation3 = AnimationFactory.getCountDownAnimation1(getApplicationContext());
			//final Animation countDownAnimation4 = AnimationFactory.getCountDownAnimationGO(getApplicationContext());
			//countDownAnimation.setFillAfter(true);
			countDownAnimation.setFillBefore(true);
			//countDownAnimation2.setFillAfter(true);
			countDownAnimation2.setFillBefore(true);
			//countDownAnimation3.setFillAfter(true);
			countDownAnimation3.setFillBefore(true);
			//countDownAnimation4.setFillAfter(true);
			//countDownAnimation4.setFillBefore(true);
			//Al termine dell'animazione
			countDownAnimation.setAnimationListener(new AnimationListener() {
				
				public void onAnimationStart(Animation animation) {
					SoundManager.playSound(SoundManager.SOUND_THREE, getApplicationContext(), false);
				}
				
				public void onAnimationRepeat(Animation animation) {}
				
				public void onAnimationEnd(Animation animation) {
					//Eseguo l'animazione
					
					countDownText.startAnimation(countDownAnimation2);
					
				}
			});
			countDownAnimation2.setAnimationListener(new AnimationListener() {
				
				public void onAnimationStart(Animation animation) {
					SoundManager.playSound(SoundManager.SOUND_TWO, getApplicationContext(), false);
					countDownText.setImageResource(R.drawable.due);
				}
				
				public void onAnimationRepeat(Animation animation) {}
				
				public void onAnimationEnd(Animation animation) {
					//Eseguo l'animazione
					
					countDownText.startAnimation(countDownAnimation3);
					
				}
			});
			countDownAnimation3.setAnimationListener(new AnimationListener() {
				
				public void onAnimationStart(Animation animation) {
					SoundManager.playSound(SoundManager.SOUND_ONE, getApplicationContext(), false);
					countDownText.setImageResource(R.drawable.uno);
				}
				
				public void onAnimationRepeat(Animation animation) {}
				
				public void onAnimationEnd(Animation animation) {
					SoundManager.playSound(SoundManager.SOUND_GO, getApplicationContext(), false);
					countDownText.setVisibility(View.INVISIBLE);
					TimeManager.setStartTime(System.currentTimeMillis());
				    playingTime = true;
				    fingerPaintDrawableView.setShowResult(false);//abilito il disegno
				    TimeManager.setPause(false);
				    TimeManager.resetPausingTime();
				    fingerPaintDrawableView.setmBitmpaPaintAlpha(FingerPaintDrawableView.TRANSPARENCY_ALPHA);
			    	startCountDownThread();
				}
			});
			
			//Eseguo l'animazione
			countDownText.startAnimation(countDownAnimation);
			//Inizializzo il countdown
			countDownText.setImageResource(R.drawable.tre);
			countDownText.setVisibility(View.VISIBLE);
	    }else {//Se sono in atelier non c'è il countdown, e abilito subito il disegno
	    	TimeManager.setPause(false);//serve per abilitare il bottone di check all'inizio
	    	playingTime = true;
	    	fingerPaintDrawableView.setmBitmpaPaintAlpha(FingerPaintDrawableView.TRANSPARENCY_ALPHA);
	    	fingerPaintDrawableView.setShowResult(false);//posso disegnare
	    }
	}

	/*
    protected void retryCurrentLevel() {

		PictureBean picture = fingerPaintDrawableView.getPicture();
		if(picture != null) {
			playingTime = false;
			fingerPaintDrawableView.setShowResult(false);
		    fingerPaintDrawableView.setPicture(picture);
		    fingerPaintDrawableView.initializeBitmaps();
		    
		    fingerPaintDrawableView.invalidate();
		    TimeManager.setStartTime(System.currentTimeMillis());
		    TimeManager.setTotalTime((long)picture.getTimeToComplete());
		    playingTime = true;
		
		    startCountDownThread();
		}
	}*/

	long delta;
	
	private void updateUI() {
		if(!TimeManager.isPaused()) {
			synchronized (this) {
				long remainingTime = TimeManager.getRemainingTime();
				////Log.d("","REMAINING = "+remainingTime);
				if(remainingTime <= 0 && playingTime) {
					////Log.e("################# remainingTime <= 0 ","remainingTime <= 0");
					playingTime = false;
					if(timeText != null) {
						timeText.setTextAppearance(getApplicationContext(), R.style.TimeFont_Black);
						timeText.setTypeface(FontFactory.getFont1(getApplicationContext()));
						timeText.setText("");
						fingerPaintDrawableView.setShowResult(true);
						fingerPaintDrawableView.startResultElaboration();
						
						//showDialog(DIALOG_RESULT_LEVEL);
					}
				}
				else {
					if(timeText != null && playingTime) {
						//Ottengo i secondi totali
						int timeleft = (int)(remainingTime/1000);
						//Se i secondi sono > 60 allora conto i minuti
						int minutes = timeleft/60;
						//Conto i secondi rimanenti
						int seconds = timeleft-(minutes*60);
						String secondString = seconds+"";
						if(seconds < 10) {
							secondString = "0"+secondString;
						}
	
						if(timeleft <= 5) {
							timeText.setTextAppearance(getApplicationContext(), R.style.TimeFont_Red);
							timeText.setTypeface(FontFactory.getFont1(getApplicationContext()));
						}else {
							timeText.setTextAppearance(getApplicationContext(), R.style.TimeFont_Black);
							timeText.setTypeface(FontFactory.getFont1(getApplicationContext()));
						}
						timeText.setText(minutes+":"+secondString+" ");
						
						//Se ci sono i disturbi , li eseguo random, (solo in modalità ARCADE)
						/*
						if("arcade".equalsIgnoreCase(gamemode)) {
							int randomNumber = (int)(Math.random()*ApplicationManager.DISTURB_FREQUENCE);
							if(randomNumber == 2) {//casual number 1 possibility of 20
								fingerPaintDrawableView.executeDisturb();
							}
						}
						*/
						//###test animation
						int randomNumber = (int)(Math.random()*ApplicationManager.DISTURB_FREQUENCE);
						if(randomNumber == 2) {//casual number 1 possibility of DISTURB_FREQUENCE
						//if(true) {//casual number 1 possibility of DISTURB_FREQUENCE
							//final int pixelSize = (int)(70*DENSITY+0.5f);
							//Le dimensioni delle armi sono proporzionali alla lavagna
							final int pixelSize = (int)((double)dashboardSize / (double)5);

							final int randomX = (int)(Math.random()*(ammoRelativeContainer.getWidth() - pixelSize));
							final int randomY = (int)(Math.random()*(ammoRelativeContainer.getHeight() - pixelSize));
					    	Animation rotAnim = AnimationFactory.getAmmoRotationAnimation_1(getApplicationContext());
					    	//rotAnim.setFillAfter(true);
					    	rotAnim.setFillBefore(true);
					    	
					    	final AmmoBean ammo = AmmoManager.getRandomAmmo();
					    	
					    	final ImageView testImage = new ImageView(getApplicationContext());
					    	testImage.setOnTouchListener(new OnTouchListener() {
								
								public boolean onTouch(View v, MotionEvent event) {
									switch (event.getAction()) {
							        case MotionEvent.ACTION_UP:
							        	testImage.setEnabled(false);
							        	if(ammo.isPositiveAmmo()) {
							        		SoundManager.playSound(SoundManager.SOUND_POSITIVE, getApplicationContext(), false);
							        	}else{
							        		SoundManager.playSound(SoundManager.SOUND_NEGATIVE, getApplicationContext(), false);
							        	}
							        	fingerPaintDrawableView.executeAmmo(randomX,randomY,getApplicationContext(),pixelSize,ammo);
							            break;
									}
									return true;
								}
							});
					    	rotAnim.setAnimationListener(new AnimationListener() {
								
								public void onAnimationStart(Animation animation) {
									SoundManager.playSound(SoundManager.SOUND_WHOOSH, getApplicationContext(), false);
								}
								
								public void onAnimationRepeat(Animation animation) {}
								
								public void onAnimationEnd(Animation animation) {
									testImage.setVisibility(View.INVISIBLE);
								}
							});
					    	
					    	RelativeLayout.LayoutParams params5 = new RelativeLayout.LayoutParams(pixelSize, pixelSize);
					        params5.leftMargin = randomX;
					        params5.topMargin = randomY;					        
					        ////Log.d("coord "+pixelSize+" -- "+relativeLayoutFingerDrawable.getWidth() +" x "+relativeLayoutFingerDrawable.getHeight(),""+params5.leftMargin+" x "+params5.topMargin);
					        
					        testImage.setLayoutParams(params5);
					        testImage.startAnimation(rotAnim);
					        testImage.setImageResource(ammo.getPresentationImage());
					        ammoRelativeContainer.addView(testImage);
					    	
						}
						//###fine test animation
					}
				}
			}
		}
	}
    
	public void setResultBitmap(Bitmap b,String percentage) {
		this.resultBitmap = b;
		this.resultPercentage = percentage;
		int MULTIPLICATION_FACTOR = 10;
		
		newrecord = false;
        unlockednextsection = false;
        unlockedbonus = false;
        unlockedammo = null;
        atleastonestar = false;
        finishGame = false;
		
		//Se ho almeno una stella aggiorno i crediti
		int numericPercentage = Integer.parseInt(percentage);
		if(!(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode)) && numericPercentage >= ApplicationManager.ONE_STAR_PERCENTAGE) {
			atleastonestar = true;
			oldtotalcredits = CreditsManager.getUserCredits(getApplicationContext());
			newtotalscredits = oldtotalcredits + (numericPercentage*MULTIPLICATION_FACTOR);
			CreditsManager.addToUserCredits(numericPercentage*MULTIPLICATION_FACTOR, getApplicationContext());
		}

		//Salvo nelle shared preferences l'ultimo risultato
		fingerPaintDrawableView.getPicture().setLastResult(getApplicationContext(),gamemode,Integer.parseInt(percentage));

        //Se questo risultato è il migliore di sempre lo salvo
        //Leggo il risultato migliore ottenuto fino ad ora
        int bestResult = fingerPaintDrawableView.getPicture().getBestResultEver(getApplicationContext(), gamemode);
        
        if(bestResult < Integer.parseInt(percentage)) {
        	newrecord = true;
        	fingerPaintDrawableView.getPicture().setBestResult(getApplicationContext(),gamemode,Integer.parseInt(percentage));
        }
        //Controllo se ho sbloccato il livello successivo (percentuale superiore del X%)
        if(!(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode)) && !("Bonus".equalsIgnoreCase(LevelManager.getCurrentSection().getSectionName()))) {
	        if(Integer.parseInt(percentage) >= ApplicationManager.ONE_STAR_PERCENTAGE) {
	        	//Prendo il prossimo livello
	        	PictureBean pBean = LevelManager.getNextLevel();
	        	if(pBean != null && pBean.isBlocked(getApplicationContext(), gamemode)) {
	        		pBean.unlockLevel(getApplicationContext(), gamemode);
	        	}
	        	else if(pBean != null && !(pBean.isBlocked(getApplicationContext(), gamemode))) {
	        		//Non fare niente, stiamo rigiocando ad un livello, il cui livello successivo è già stato sbloccato!
	        	}
	        	else {
	        		//Sono all'ultimo livello della sezione,controllo se c'è una sezione successiva ancora BLOCCATA
	        		//Se si sblocco il primo livello
	        		SectionArrayList<PictureBean> nextSec = LevelManager.getNextSection();
	        		if(nextSec != null && nextSec.size() > 0 &&
	        				!("Bonus".equalsIgnoreCase(nextSec.getSectionName())) && 
	        				!(nextSec.isUnlocked(getApplicationContext(), gamemode))) {
	        			nextSec.unlockSection(getApplicationContext(), gamemode);
	        			unlockednextsection = true;
	        			//Prelevo il primo livello
	        			PictureBean firstLevel = nextSec.get(0);
	        			if(firstLevel!= null) {
	        				firstLevel.unlockLevel(getApplicationContext(), gamemode);
	        			}
	        		}
	        		//Se ho finito anche l'ultima sezione faccio un complimento e invito a sbloccare tutti i Bonus.
	        		else if(nextSec != null && 
	        				("Bonus".equalsIgnoreCase(nextSec.getSectionName()))) {
	        			finishGame = true;
	        		}
	        		else if(nextSec != null && nextSec.isUnlocked(getApplicationContext(), gamemode)) {
	        			//Non fare niente, stiamo rigiocando ad una sezione, la cui sezione successiva è già stata sbloccata!
	        		}else if(nextSec != null && nextSec.size() == 0){//versione LITE
	        			finishGame = true;
	        		}
	        	}
	        	
	        	//Controllo se ho ottenuto 3 STELLE per tutti i quadri della sezione, se si sblocco il quadro bonus di questa sezione.
	        	//Se non è già sbloccato!
	        	int bonusSize = LevelManager.getBonusSection().size();
	        	if(LevelManager.getCurrentSectionIndex() < bonusSize) {
		        	PictureBean bonusCorrispondente = LevelManager.getBonusSection().get(LevelManager.getCurrentSectionIndex());
		        	if(bonusCorrispondente!= null) {
		        		if(bonusCorrispondente.isBlocked(getApplicationContext(), gamemode)) {
		        			//OK verifico se l'ho sbloccata
		        			int gainedStar = Integer.parseInt(LevelManager.getCurrentSection().getGainedStars(getApplicationContext(), gamemode));
		        			int totalStar = LevelManager.getCurrentSection().size() * 3;
		        			if(gainedStar == totalStar) {
		        				//Sblocco il bonus
		        				bonusCorrispondente.unlockLevel(getApplicationContext(), gamemode);
		        				unlockedbonus = true;
		        			}
		        		}
		        	}
	        	}
	        	
	        	//Controllo se ho sbloccato un'arma
	        	AmmoBean ammoUnlocked = AmmoManager.thereIsAnUnlockedAmmo(getApplicationContext(), newtotalscredits);
	        	if(ammoUnlocked != null) {
	        		unlockedammo = ammoUnlocked;
	        	}
	        }
        }
		try {
			//TODO se sono state bloccate armi o sezioni prima mostro quelle che quando svaniscono aprono i successivi dialog
			if(finishGame) {
				showDialog(DIALOG_FINISH_GAME);
			}
			else if(unlockedammo !=null) {
				ApplicationManager.THERE_IS_A_NEW_UNLOCKED_AMMO = true;
				showDialog(DIALOG_AMMO_UNLOCKED);
			}
			else if(unlockedbonus) {
				showDialog(DIALOG_BONUS_UNLOCKED);
			}
			else if(unlockednextsection) {
				showDialog(DIALOG_SECTION_UNLOCKED);
			}else {
				showDialog(DIALOG_RESULT_LEVEL);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		//Sblocco i lock su bottoni e thread paralleli
		if(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode)) {//questo bottone c'è solo in Atelier
			if(atelierCheckBut != null) {
				//Attendo qualche istante, per fare apparire il dialog e evitare doppi click sul CHECK.
				new Thread() {
					public void run() {
						try {Thread.sleep(3000);} catch (InterruptedException e) {e.printStackTrace();}
						runOnUiThread(new Runnable() {
							public void run() {
								atelierCheckBut.setEnabled(true);
							}
							
						});
					};
				}.start();
			}
		}
		fingerPaintDrawableView.setResultComputed(false);
	}
	
    protected void startCountDownThread() {
        // Fire off a thread to do some work that we shouldn't do directly in the UI thread
        Thread t = new Thread() {
            public void run() {
            		while(playingTime) {//Finchè il tempo non scade
            			try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mHandler.post(mUpdateResults);
            		}
            		//Log.e("EXIT THREAD","EXIT THREAD ####################");
            }
        };
        t.start();
    }
}