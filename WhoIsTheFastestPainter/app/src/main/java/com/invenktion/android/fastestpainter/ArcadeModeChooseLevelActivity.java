package com.invenktion.android.fastestpainter;

import java.util.ArrayList;

import com.invenktion.android.fastestpainter.bean.PictureBean;
import com.invenktion.android.fastestpainter.core.AnimationFactory;
import com.invenktion.android.fastestpainter.core.ApplicationManager;
import com.invenktion.android.fastestpainter.core.CreditsManager;
import com.invenktion.android.fastestpainter.core.FontFactory;
import com.invenktion.android.fastestpainter.core.LevelManager;
import com.invenktion.android.fastestpainter.core.SoundManager;
import com.invenktion.android.fastestpainter.utils.ActivityHelper;
import com.invenktion.android.fastestpainter.utils.LogUtils;
import com.invenktion.android.fastestpainter.view.GlassPaneDrawableView;
import com.invenktion.android.fastestpainter.widget.OneStepGallery;
import com.invenktion.android.fastestpainter.R;


import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.drawable.BitmapDrawable;

import android.graphics.Typeface;

import android.os.Bundle;
import android.os.Debug;

import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;

import android.view.View;

import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.RelativeLayout;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ArcadeModeChooseLevelActivity extends Activity{
	
	private int PICTURE_PER_PAGE = 1;
	private boolean waiting = false;
	static final int DIALOG_STORYBOARD = 0;
	
	float DENSITY = 1.0f;
	//Typeface font; 
	//DynamicBackgroundDrawableView backgroundDrawableView;
	//GlassPaneDrawableView glassPane;
	String gamemode;
	OneStepGallery g;
	ImageAdapter adapter;
	
	@Override
	protected void onPause() {
		super.onPause();
		
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//if(glassPane != null) {
			//glassPane.recycleBitmaps();
		//}
		LevelManager.clearAllCachedImage();
		//Log.e("ArcadeModeChooseLevelActivity","DESTROY ArcadeModeChooseLevelActivity ####################");
		//System.gc();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if(waiting) return false;
	    	else {
	    		waiting = true;
	    		finish();
	    		overridePendingTransition(0,0);
	        	return true;
	    	}
	    }
	    return super.onKeyDown(keyCode, event);
	}

	//Questo viene chiamato quando l'utente clicca il tasto "back" e dal livello torna qui
	@Override
	protected void onResume() {
		super.onResume();
		//Aggiorno lo stato dei livelli
		adapter.notifyDataSetChanged();
		 //Mi posiziono sull'ultimo livello usato
        int pos = LevelManager.getCurrentLevelIndex();
        if(pos >= 0 && pos< LevelManager.getAllLevels().size()) {
        	g.setSelection(pos);
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
	}
	
	protected Dialog onCreateDialog(int id) {
        final Dialog dialog;
        switch(id) {
        case DIALOG_STORYBOARD:
        	// prepare the custom dialog
			dialog = new Dialog(this);//con l'app context non si aprono
			dialog.setCancelable(false);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.storyboard_dialog);
			//dialog.setTitle("Custom Dialog");
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent);
			ImageView instructionImage = (ImageView) dialog.findViewById(R.id.storyboardImage);
			
			int pixelCorrispondenti = (int)(ApplicationManager.DIALOG_MAX_APPARENT_SIZE_DP*DENSITY+0.5f);
			int H = (int)(ApplicationManager.SCREEN_H*0.95);
            if(H > pixelCorrispondenti) {
            	H = pixelCorrispondenti;
            }
			instructionImage.setLayoutParams(new LinearLayout.LayoutParams(H, H));
            break;
        default:
            dialog = null;
        }
        return dialog;
    }
	
	protected void onPrepareDialog(int id, final Dialog dialog) {
    	switch(id) {
        case DIALOG_STORYBOARD:
        	final ImageView storyboardImage = (ImageView)dialog.findViewById(R.id.storyboardImage);
        	storyboardImage.setImageResource(LevelManager.getCurrentSection().getStoryboardImage());
        	storyboardImage.setEnabled(true);
        	storyboardImage.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	storyboardImage.setEnabled(false);
				        	dialog.dismiss();
							//Lancio il gioco
							launchGame();
				            break;
					}
					return true;
				}
			});
			break;
        default:
            //dialog = null;
        }
 
    };
	
	private View buildView(int page) {
		//FrameLayout frameLayout = (FrameLayout)findViewById(R.id.arcadechooselevellayout);
		//Facciamo due strisce con 5 livelli ciscuna
        LinearLayout container = new LinearLayout(getApplicationContext());
        container.setOrientation(LinearLayout.VERTICAL);
        container.setGravity(Gravity.CENTER_VERTICAL);
        LinearLayout primariga = new LinearLayout(getApplicationContext());
        primariga.setOrientation(LinearLayout.HORIZONTAL);
        primariga.setGravity(Gravity.CENTER_HORIZONTAL);
        LinearLayout secondariga = new LinearLayout(getApplicationContext());
        secondariga.setOrientation(LinearLayout.HORIZONTAL);
        secondariga.setGravity(Gravity.CENTER_HORIZONTAL);
        container.addView(primariga);
        container.addView(secondariga);
       
        Resources res;
    	Options opts;
    	res = getApplicationContext().getResources();
    	opts = new BitmapFactory.Options();
    	opts.inSampleSize=2;
    	Bitmap.Config conf = Bitmap.Config.ARGB_8888;
    	opts.inPreferredConfig = conf;
        ArrayList<PictureBean> allLevels = LevelManager.getAllLevels();
        int currentSectionTela = LevelManager.getCurrentSection().getTelaImage();
        for(int n=page*PICTURE_PER_PAGE; n<(page*PICTURE_PER_PAGE)+PICTURE_PER_PAGE; n++) {
        	final int position = n;
        	if(position > allLevels.size() -1) break;//massimo
        	final LinearLayout ll = new LinearLayout(getApplicationContext());
        	ll.setOrientation(LinearLayout.VERTICAL);
        	ll.setGravity(Gravity.TOP);
        	//ll.setBackgroundColor(Color.WHITE);
        	//ll.setBackgroundResource(R.drawable.dialogbg);
        	
        	ImageView i = new ImageView(getApplicationContext());
        	ImageView iBg = new ImageView(getApplicationContext());

        	PictureBean pic = allLevels.get(position);
        	
        	//Controllo se il livello  stato gi sbloccato o meno
            boolean isUnlocked = !(pic.isBlocked(getApplicationContext(), gamemode));
            //boolean isUnlocked = true;
            
            float scale = ArcadeModeChooseLevelActivity.this.DENSITY;

        	int pixelCorrispondenti = (int)(ApplicationManager.LEVEL_GALLERY_MAX_APPARENT_SIZE_DP*DENSITY+0.5f);
        	int H = (int)(ApplicationManager.SCREEN_W*0.3);
            if(H > pixelCorrispondenti) {
            	H = pixelCorrispondenti;
            }
            int Hpic = (int)((double)H*0.4);
 
        	//boolean firstSection = (LevelManager.getCurrentSectionIndex() == 0);
        	
        	//opts.inPurgeable=true;
        	if(isUnlocked) {//La prima  sbloccata di default
        		//i.setImageBitmap(BitmapFactory.decodeResource(res, pic.getColoredPicture(),opts));
        		i.setImageBitmap(pic.getColoredPicture(getApplicationContext()));
        		//i.setImageResource(R.drawable.stella);
        		i.setLayoutParams(new LinearLayout.LayoutParams(Hpic,Hpic));
        		i.setScaleType(ImageView.ScaleType.FIT_XY);
        	}
        	if(isUnlocked) {//La prima  sbloccata di default
        		iBg.setImageResource(currentSectionTela);
        	}else{
        		if("Bonus".equalsIgnoreCase(LevelManager.getCurrentSection().getSectionName())){
        			iBg.setImageResource(R.drawable.tela_coperta_bonus);
        		}else{
        			iBg.setImageResource(R.drawable.tela_coperta);
        		}
        	}
            iBg.setLayoutParams(new FrameLayout.LayoutParams(H,H));
            iBg.setScaleType(ImageView.ScaleType.FIT_XY);
         
            LinearLayout imagell = new LinearLayout(getApplicationContext());
            imagell.setGravity(Gravity.CENTER_HORIZONTAL);
	            RelativeLayout relativeLayoutImage = new RelativeLayout(getApplicationContext());
	            relativeLayoutImage.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,Gravity.CENTER_HORIZONTAL));
	            	relativeLayoutImage.addView(iBg);
	            	if(isUnlocked) {//La prima  sbloccata di default
	            		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(Hpic, Hpic);
	            		relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	            		relativeLayoutImage.addView(i,relativeParams);
	            	}
            imagell.addView(relativeLayoutImage);
            
            LinearLayout starsll = new LinearLayout(getApplicationContext());
            ImageView starsImage1 = new ImageView(getApplicationContext());
            ImageView starsImage2 = new ImageView(getApplicationContext());
            ImageView starsImage3 = new ImageView(getApplicationContext());
            starsll.addView(starsImage1);
            starsll.addView(starsImage2);
            starsll.addView(starsImage3);
            // Le stelle le facciamo larghe 1/4 del quadro
            int sizeStar = (int)((double)H/4.5);
            starsImage1.setLayoutParams(new LinearLayout.LayoutParams(sizeStar,sizeStar));
            starsImage2.setLayoutParams(new LinearLayout.LayoutParams(sizeStar,sizeStar));
            starsImage3.setLayoutParams(new LinearLayout.LayoutParams(sizeStar,sizeStar));
            starsll.setGravity(Gravity.CENTER_HORIZONTAL);
            /*
            TextView titleTextView = new TextView(getApplicationContext());
            titleTextView.setTextAppearance(getApplicationContext(), R.style.TimeFont_Black_PictureTitle);
            //Uso un font personalizzato
            titleTextView.setTypeface(font); 
            titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            titleTextView.setText(pic.getTitle());
            
            TextView bestTextView = new TextView(getApplicationContext());
            bestTextView.setTextAppearance(getApplicationContext(), R.style.TimeFont_Black_DrawedLevelChoose);
            //Uso un font personalizzato
            bestTextView.setTypeface(font); 
            bestTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            
            TextView lastTextView = new TextView(getApplicationContext());
            lastTextView.setTextAppearance(getApplicationContext(), R.style.TimeFont_Black_DrawedLevelChoose);
            //Uso un font personalizzato
            lastTextView.setTypeface(font); 
            lastTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            */
            
            if(isUnlocked) {//La prima  sbloccata di default
	            //Leggo il risultato migliore ottenuto fino ad ora
	            int bestResult = pic.getBestResultEver(getApplicationContext(), gamemode);
	            //Leggo l'ultimo risultato ottenuto
	            int lastResult = pic.getLastResult(getApplicationContext(), gamemode);
	            
	            //bestTextView.setText("Best: "+ bestResult+"%");
	            //lastTextView.setText("Last: "+ lastResult+"%");
	            
	            
	            if(bestResult >= ApplicationManager.THREE_STAR_PERCENTAGE) {
	            	starsImage1.setImageResource(R.drawable.stella_black);
	            	starsImage2.setImageResource(R.drawable.stella_black);
	            	starsImage3.setImageResource(R.drawable.stella_black);
	            }else if(bestResult >= ApplicationManager.TWO_STAR_PERCENTAGE) {
	            	starsImage1.setImageResource(R.drawable.stella_black);
	            	starsImage2.setImageResource(R.drawable.stella_black);
	            	starsImage3.setImageResource(R.drawable.stella_black_tr);
	            }else if(bestResult >= ApplicationManager.ONE_STAR_PERCENTAGE) {
	            	starsImage1.setImageResource(R.drawable.stella_black);
	            	starsImage2.setImageResource(R.drawable.stella_black_tr);
	            	starsImage3.setImageResource(R.drawable.stella_black_tr);
	            }else {
	            	starsImage1.setImageResource(R.drawable.stella_black_tr);
	            	starsImage2.setImageResource(R.drawable.stella_black_tr);
	            	starsImage3.setImageResource(R.drawable.stella_black_tr);
	            }
            }else {
            	//bestTextView.setText("Best: 0%");
	            //lastTextView.setText("Last: 0%");
	            starsImage1.setImageResource(R.drawable.stella_black_tr);
            	starsImage2.setImageResource(R.drawable.stella_black_tr);
            	starsImage3.setImageResource(R.drawable.stella_black_tr);
            }
            
            ll.addView(imagell);
            ll.addView(starsll);
            //ll.addView(titleTextView);
            //ll.addView(bestTextView);
            //ll.addView(lastTextView);
            
            /*
            ll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
				}
            });*/
            
            
            
            //if(position <=page*PICTURE_PER_PAGE+(PICTURE_PER_PAGE/2 -1)){
            	primariga.addView(ll);
            //}else{
            	//secondariga.addView(ll);
            //}
        }
        //frameLayout.addView(container);
        //frameLayout.addView(glassPane);
        
        return container;
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
        //Salvo la modalit di gioco che mi  stata passata
        Bundle extras = getIntent().getExtras();
        if(extras !=null){
        	gamemode = extras.getString("gamemode");
        	//Log.d("GAMEMODE ######",gamemode);
        }
        
        this.DENSITY = getApplicationContext().getResources().getDisplayMetrics().density;
        setContentView(R.layout.arcadechooselevel);
        
        ImageView mascotteImage = (ImageView)findViewById(R.id.mascotteimage);
        mascotteImage.setLayoutParams(new LinearLayout.LayoutParams((int)(ApplicationManager.SCREEN_H/2.5), (int)(ApplicationManager.SCREEN_H/2.5)));
        
        //font = Typeface.createFromAsset(getAssets(), FontFactory.FONT1); 
        
        //Inserisco lo sfondo
        //backgroundDrawableView = new DynamicBackgroundDrawableView(this,R.drawable.desktop,true);
        //glassPane = new GlassPaneDrawableView(this,-1,-1,-1,R.drawable.br4,0.45f);
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.arcadechooselevellayoutframe);
        BitmapDrawable drawableBg = (BitmapDrawable)getApplicationContext().getResources().getDrawable(R.drawable.bg1);
    	frameLayout.setBackgroundDrawable(drawableBg);
    	frameLayout.setPadding(0,0,0,0);
        //frameLayout.setBackgroundResource(R.drawable.desktop3);
        //frameLayout.addView(backgroundDrawableView, 0);
        //buildView();
        //Log.d("BUILDVIEW","BUILDVIEW ONCREATE");
        
        //g = (Gallery) findViewById(R.id.galleryarcade);
        g = new OneStepGallery(getApplicationContext());
        adapter = new ImageAdapter(getApplicationContext());
        g.setAdapter(adapter);
        g.setSoundEffectsEnabled(false);
        float scale = ArcadeModeChooseLevelActivity.this.DENSITY;
        g.setSpacing((int)(10*scale+0.5f));//spazio tra le immagini della gallery
        g.setAnimationDuration(500);
        g.setAnimation(null);
        g.setAnimationCacheEnabled(true);
        g.setDrawingCacheEnabled(true);
        g.setHorizontalFadingEdgeEnabled(false);
        g.setUnselectedAlpha(255);
        
        //LinearLayout frameLayout = (LinearLayout)findViewById(R.id.arcadechooselevellayout);
        frameLayout.addView(g,0);
        //frameLayout.addView(glassPane);
        
        Button prevBut = (Button)findViewById(R.id.prevbut);
        Button nextBut = (Button)findViewById(R.id.nextbut);
        prevBut.setSoundEffectsEnabled(false);
        nextBut.setSoundEffectsEnabled(false);
        nextBut.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				int nextpos = g.getSelectedItemPosition()+1;
				if(nextpos >g.getCount()-1) nextpos = g.getCount()-1;
				g.setSelection(nextpos);
			}
		});
        prevBut.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				int nextpos = g.getSelectedItemPosition()-1;
				if(nextpos <0) nextpos = 0;
				g.setSelection(nextpos);
			}
		});
        final TextView scrollText = (TextView)findViewById(R.id.scrolltext);
        
        scrollText.setTextAppearance(getApplicationContext(), R.style.TimeFont_Black_NumberScroll);
        scrollText.setTypeface(FontFactory.getFont1(getApplicationContext())); 
        
        g.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				scrollText.setText(arg2+1 + " / "+ g.getCount());
				LogUtils.logHeap();
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
      
        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, final int position, long id) {
            	if(waiting) return;
            	waiting = true;
            	
            	LevelManager.setCurrentLevelIndex(position);
            	PictureBean pb = LevelManager.getCurrentLevel();
            	final boolean isBlocked = pb.isBlocked(getApplicationContext(), gamemode);
            	
            	//Carico l'animazioncina
				Animation selectionAnimation = AnimationFactory.getLevelSelectionAnimation(getApplicationContext());
				//Al termine dell'animazione lancio il livello (con un listener)
				selectionAnimation.setAnimationListener(new AnimationListener() {
					
					public void onAnimationStart(Animation animation) {	}
					
					public void onAnimationRepeat(Animation animation) {}
					
					public void onAnimationEnd(Animation animation) {
						//Log.d("ANIM END","ANIM END");
						
	                	//boolean isBlocked = false;
	                	
    	            	/*
                		if(pb.isTheFirstOfSection() && !(ApplicationManager.ATELIER.equalsIgnoreCase(gamemode))){
    	            		Intent myIntent = new Intent(ArcadeModeChooseLevelActivity.this, SectionPresentationActivity.class);
    	            		myIntent.putExtra("gamemode", gamemode);
    	            		ArcadeModeChooseLevelActivity.this.startActivity(myIntent);
    	            	}else{
    	            	*/
                		
                		//Salvo nelle shared preferences questo livello e relativa sezione
                		new Thread() {
                			public void run() {
                				SharedPreferences settings = getApplicationContext().getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
		                        SharedPreferences.Editor editor = settings.edit();
		                        editor.putInt("last_section_used", LevelManager.getCurrentSectionIndex());
		                        editor.putInt("last_"+LevelManager.getCurrentSectionIndex()+"_section_level_used", position);
		                        //Commit the edits!
		                        editor.commit();
                			};
                		}.start();
                		
                		String secName = LevelManager.getCurrentSection().getSectionName();
                		if(position == 0 && !("Bonus".equalsIgnoreCase(secName))) {
    	            		showDialog(DIALOG_STORYBOARD);
                		}else {
                			launchGame();
                		}
    	            	//}
	                	
					}
				});
				if(!(isBlocked)) {
					//Eseguo l'animazione
					v.startAnimation(selectionAnimation);
				}else {
					waiting = false;
				}
            }
        });
        
        //Mi posiziono sull'ultimo quadro usato, leggendo dalle shared preferences
        SharedPreferences settings = getApplicationContext().getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
		int lastUsed =  settings.getInt("last_"+LevelManager.getCurrentSectionIndex()+"_section_level_used", 0);
		if(lastUsed < g.getCount()) {
			g.setSelection(lastUsed);
		}
    }
    
    private void launchGame() {
    	Intent myIntent = new Intent(ArcadeModeChooseLevelActivity.this, DrawChallengeActivity.class);
		myIntent.putExtra("gamemode", gamemode);
		ArcadeModeChooseLevelActivity.this.startActivity(myIntent);
		overridePendingTransition(0,0);
    }
    
  //Adapter per la Gallery
    class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;
        Resources res;
    	Options opts;

        private ArrayList<PictureBean> allLevels = LevelManager.getAllLevels();

        public ImageAdapter(Context c) {
            mContext = c;
            /*
            TypedArray a = obtainStyledAttributes(R.styleable.HelloGallery);
            mGalleryItemBackground = a.getResourceId(
                    R.styleable.HelloGallery_android_galleryItemBackground, 0);
            a.recycle();
            */
            res = mContext.getResources();
        	opts = new BitmapFactory.Options();
        	opts.inSampleSize=2;
        	Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        	opts.inPreferredConfig = conf;
        }

        public int getCount() {
        	int div = (int)(allLevels.size() / PICTURE_PER_PAGE);
        	
        	if(allLevels.size() % PICTURE_PER_PAGE != 0) div++;
        	
            return div;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	return buildView(position);
        	/*
        	LinearLayout ll = new LinearLayout(mContext);
        	ll.setOrientation(LinearLayout.VERTICAL);
        	ll.setGravity(Gravity.TOP);
        	//ll.setBackgroundColor(Color.WHITE);
        	//ll.setBackgroundResource(R.drawable.dialogbg);
        	
        	ImageView i = new ImageView(mContext);
        	ImageView iBg = new ImageView(mContext);

        	PictureBean pic = allLevels.get(position);
        	
        	//Controllo se il livello  stato gi sbloccato o meno
            boolean isUnlocked = !(pic.isBlocked(mContext, gamemode));
            //boolean isUnlocked = true;
            
            float scale = ArcadeModeChooseLevelActivity.this.DENSITY;

        	//int Hpic = (int)(70*scale+0.5f);
        	//int H = (int)(150*scale+0.5f);
        	int H = (int)(ApplicationManager.SCREEN_H*0.5);
        	int Hpic = (int)(ApplicationManager.SCREEN_H*0.25);
 
        	boolean firstSection = (LevelManager.getCurrentSectionIndex() == 0);
        	
        	//opts.inPurgeable=true;
        	if(isUnlocked || (firstSection && position == 0)) {//La prima  sbloccata di default
        		i.setImageBitmap(BitmapFactory.decodeResource(res, pic.getColoredPicture(),opts));
        		i.setLayoutParams(new LinearLayout.LayoutParams(Hpic,Hpic));
        		i.setScaleType(ImageView.ScaleType.FIT_XY);
        	}
        	if(isUnlocked || (firstSection && position == 0)) {//La prima  sbloccata di default
        		iBg.setImageResource(R.drawable.tela);
        	}else{
        		iBg.setImageResource(R.drawable.tela_coperta);
        	}
            iBg.setLayoutParams(new FrameLayout.LayoutParams(H,H));
            iBg.setScaleType(ImageView.ScaleType.FIT_XY);
         
            LinearLayout imagell = new LinearLayout(mContext);
            imagell.setGravity(Gravity.CENTER_HORIZONTAL);
	            RelativeLayout relativeLayoutImage = new RelativeLayout(mContext);
	            relativeLayoutImage.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,Gravity.CENTER_HORIZONTAL));
	            	relativeLayoutImage.addView(iBg);
	            	if(isUnlocked || (firstSection && position == 0)) {//La prima  sbloccata di default
	            		RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(Hpic, Hpic);
	            		relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	            		relativeLayoutImage.addView(i,relativeParams);
	            	}
            imagell.addView(relativeLayoutImage);
            
            LinearLayout starsll = new LinearLayout(mContext);
            ImageView starsImage = new ImageView(mContext);
            starsll.addView(starsImage);
            // Convert the dps to pixels
            starsImage.setLayoutParams(new LinearLayout.LayoutParams((int)(100*scale+0.5f),(int)(42*scale+0.5f)));
            starsll.setGravity(Gravity.CENTER_HORIZONTAL);
            
            TextView titleTextView = new TextView(mContext);
            titleTextView.setTextAppearance(getApplicationContext(), R.style.TimeFont_Black_PictureTitle);
            //Uso un font personalizzato
            titleTextView.setTypeface(font); 
            titleTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            titleTextView.setText(pic.getTitle());
            
            TextView bestTextView = new TextView(mContext);
            bestTextView.setTextAppearance(getApplicationContext(), R.style.TimeFont_Black_DrawedLevelChoose);
            //Uso un font personalizzato
            bestTextView.setTypeface(font); 
            bestTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            
            TextView lastTextView = new TextView(mContext);
            lastTextView.setTextAppearance(getApplicationContext(), R.style.TimeFont_Black_DrawedLevelChoose);
            //Uso un font personalizzato
            lastTextView.setTypeface(font); 
            lastTextView.setGravity(Gravity.CENTER_HORIZONTAL);
            
            if(isUnlocked || (firstSection && position == 0)) {//La prima  sbloccata di default
	            //Leggo il risultato migliore ottenuto fino ad ora
	            int bestResult = pic.getBestResultEver(mContext, gamemode);
	            //Leggo l'ultimo risultato ottenuto
	            int lastResult = pic.getLastResult(mContext, gamemode);
	            
	            bestTextView.setText("Best: "+ bestResult+"%");
	            lastTextView.setText("Last: "+ lastResult+"%");
	            
	            
	            if(bestResult >= ApplicationManager.THREE_STAR_PERCENTAGE) {
	            	starsImage.setImageResource(R.drawable.starthree);
	            }else if(bestResult >= ApplicationManager.TWO_STAR_PERCENTAGE) {
	            	starsImage.setImageResource(R.drawable.startwo);
	            }else if(bestResult >= ApplicationManager.ONE_STAR_PERCENTAGE) {
	            	starsImage.setImageResource(R.drawable.starone);
	            }else {
	            	starsImage.setImageResource(R.drawable.starzero);
	            }
            }else {
            	bestTextView.setText("Best: 0%");
	            lastTextView.setText("Last: 0%");
	            starsImage.setImageResource(R.drawable.starzero);
            }
            
            ll.addView(imagell);
            ll.addView(starsll);
            ll.addView(titleTextView);
            ll.addView(bestTextView);
            ll.addView(lastTextView);
      
            return ll;
            */
        }
    }
}
