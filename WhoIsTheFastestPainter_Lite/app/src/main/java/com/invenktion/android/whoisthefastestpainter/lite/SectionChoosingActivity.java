package com.invenktion.android.whoisthefastestpainter.lite;

import java.util.ArrayList;

import com.invenktion.android.whoisthefastestpainter.lite.bean.PictureBean;
import com.invenktion.android.whoisthefastestpainter.lite.bean.SectionArrayList;
import com.invenktion.android.whoisthefastestpainter.lite.core.AnimationFactory;
import com.invenktion.android.whoisthefastestpainter.lite.core.ApplicationManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.FontFactory;
import com.invenktion.android.whoisthefastestpainter.lite.core.LevelManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.SoundManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.TimeManager;
import com.invenktion.android.whoisthefastestpainter.lite.utils.ActivityHelper;
import com.invenktion.android.whoisthefastestpainter.lite.utils.LogUtils;
import com.invenktion.android.whoisthefastestpainter.lite.view.GlassPaneDrawableView;
import com.invenktion.android.whoisthefastestpainter.lite.widget.OneStepGallery;
import com.invenktion.android.whoisthefastestpainter.lite.R;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;


import android.graphics.Typeface;

import android.os.Bundle;
import android.os.Debug;

import android.util.Log;

import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import android.view.KeyEvent;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SectionChoosingActivity extends Activity{
	
	float DENSITY = 1.0f;
	//Typeface font; 
	//DynamicBackgroundDrawableView backgroundDrawableView;
	//GlassPaneDrawableView glassPane;
	private boolean waiting = false;
	String gamemode;
	ImageAdapter adapter;
	OneStepGallery g;
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//if(glassPane != null) {
			//glassPane.recycleBitmaps();
		//}
		//Log.e("SectionChoosingActivity","DESTROY SECTION CHOOSING ####################");
		
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
	
	@Override
	protected void onPause() {
		super.onPause();
		//LevelManager.clearAllCachedImage();
		
	}

	//Questo viene chiamato quando l'utente clicca il tasto "back" e dal livello torna qui
	@Override
	protected void onResume() {
		super.onResume();
		//Aggiorno la gallery
		adapter.notifyDataSetChanged();
		updateStars(g.getSelectedItemPosition());
		/*
        //Mi posiziono sull'ultimo livello usato
        int pos = LevelManager.getCurrentSectionIndex();
        if(pos >= 0 && pos< LevelManager.getSectionCount()) {
        	g.setSelection(LevelManager.getCurrentSectionIndex());
        }
        */
        
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
        setContentView(R.layout.sectionschoose);
        
        //font = Typeface.createFromAsset(getAssets(), FontFactory.FONT1); 
        
        //Inserisco lo sfondo
        //backgroundDrawableView = new DynamicBackgroundDrawableView(this,R.drawable.desktop,true);
        //glassPane = new GlassPaneDrawableView(this);
        FrameLayout frameLayout = (FrameLayout)findViewById(R.id.sectionschooselevelframelayout);
        //frameLayout.setBackgroundResource(R.drawable.desktop3);
        //frameLayout.addView(backgroundDrawableView, 0);
        //frameLayout.addView(glassPane);
        
        ImageView bottomImage1 = (ImageView)findViewById(R.id.secimage1);
        bottomImage1.setLayoutParams(new LinearLayout.LayoutParams((int)(ApplicationManager.SCREEN_H/2.5), (int)(ApplicationManager.SCREEN_H/2.5)));
        //ImageView bottomImage2 = (ImageView)findViewById(R.id.secimage2);
        //bottomImage2.setLayoutParams(new LinearLayout.LayoutParams((int)(ApplicationManager.SCREEN_H/3.5), (int)(ApplicationManager.SCREEN_H/3.5)));
        
        TextView gainedStarsText = (TextView) findViewById(R.id.starimagetext);
		gainedStarsText.setTextAppearance(getApplicationContext(), R.style.SectionChoose_StarText);
		gainedStarsText.setTypeface(FontFactory.getFont1(getApplicationContext())); 
        
        //Gallery g = (Gallery) findViewById(R.id.gallerysections);
		g = new OneStepGallery(getApplicationContext());
        adapter = new ImageAdapter(getApplicationContext());
        g.setAdapter(adapter);
        g.setSoundEffectsEnabled(false);
        float scale = SectionChoosingActivity.this.DENSITY;
        g.setSpacing((int)(50*scale+0.5f));//spazio tra le immagini della gallery
        g.setAnimationDuration(500);
        g.setAnimation(null);
        g.setAnimationCacheEnabled(true);
        g.setDrawingCacheEnabled(true);
        g.setHorizontalFadingEdgeEnabled(false);
        g.setUnselectedAlpha(255);
        
        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, final int position, long id) {
            	if(waiting) return;
            	waiting = true;
            	
            	//Qui setto la sezione
				final SectionArrayList selected =adapter.getItemBean(position);
            	LevelManager.setCurrentSection(selected.getNumber());
            	final boolean allowed = selected.isUnlocked(getApplicationContext(), gamemode) || "Bonus".equalsIgnoreCase(selected.getSectionName());
            	//Carico l'animazioncina
				Animation selectionAnimation = AnimationFactory.getLevelSelectionAnimation(getApplicationContext());
				//Al termine dell'animazione lancio il livello (con un listener)
				selectionAnimation.setAnimationListener(new AnimationListener() {
					
					public void onAnimationStart(Animation animation) {	}
					
					public void onAnimationRepeat(Animation animation) {}
					
					public void onAnimationEnd(Animation animation) {
		            	//SectionArrayList<PictureBean> selected = LevelManager.getCurrentSection();
		            	if(allowed) {
			        		Intent myIntent = new Intent(SectionChoosingActivity.this, ArcadeModeChooseLevelActivity.class);
			        		myIntent.putExtra("gamemode", gamemode);
			        		SectionChoosingActivity.this.startActivity(myIntent);
			        		overridePendingTransition(0,0);
		            	}
					}
				});
				
				if(allowed) {
					//Eseguo l'animazione
					v.startAnimation(selectionAnimation);
				}else {
					waiting = false;
				}
            }
        });
        
        g.setOnItemSelectedListener(new OnItemSelectedListener() {
			
			public void onItemSelected(AdapterView<?> arg0, View arg1,
										final int position, long arg3) {
					LogUtils.logHeap();
					updateStars(position);
			}
			
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
        
        LinearLayout galleryLayout = (LinearLayout)findViewById(R.id.sectionschooselayout);
        galleryLayout.addView(g);
        //g.setSelection(1);//mi posiziono sul primo boss, lasciando il bonus dietro
        
        //Mi posiziono sull'ultima sezione usata, leggendo dalle shared preferences
        SharedPreferences settings = getApplicationContext().getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
		int lastUsed =  settings.getInt("last_section_used", 0);
		if(lastUsed < g.getCount()) {
			g.setSelection(lastUsed);
		}
    }
    
    private void updateStars(final int position) {
    	new Thread() {
			public void run() {
				try {
					final TextView text = (TextView) findViewById(R.id.starimagetext);
					final ImageView image = (ImageView)findViewById(R.id.starimagelabel);
					//ArrayList<SectionArrayList> allSections = LevelManager.getAllSections();
					//SectionArrayList section = allSections.get(position);
					SectionArrayList section =adapter.getItemBean(position);
					
					//Controllo se la sezione  gi stata sbloccata o meno
		        	boolean sbloccata = section.isUnlocked(getApplicationContext(),gamemode);
					
		        	if(sbloccata || "Bonus".equalsIgnoreCase(section.getSectionName())) {
						final String gainedStars = section.getGainedStars(getApplicationContext(),gamemode);
						final String totalStars = ""+section.size()*3;
							runOnUiThread(new Runnable() {
								public void run() {
									image.setImageResource(R.drawable.stella_black);
									text.setText(gainedStars+"/"+totalStars);
								}
							});
		        	}else {
		        		runOnUiThread(new Runnable() {
							public void run() {
								try {
									image.setImageResource(R.drawable.lucchetto);
									text.setText(R.string.blocked);
								}catch (Exception e) {
									//Log.e("",e.getMessage());
								}
							}
						});
		        	}
				}catch (Exception e) {
					//Log.e("",e.getMessage());
				}
			};
		}.start();
    }
    
    //Adapter per la Gallery
    class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;

        private ArrayList<SectionArrayList> allSections;

        public ImageAdapter(Context c) {
            mContext = c;
            allSections = new ArrayList<SectionArrayList>();
            
            for(SectionArrayList<PictureBean> sec:LevelManager.getAllSections()) {
            	if(!("Bonus".equalsIgnoreCase(sec.getSectionName())) && !("Atelier".equalsIgnoreCase(sec.getSectionName()))) {
            		allSections.add(sec);
            	}
            }
            allSections.add(LevelManager.getBonusSection());
            /*
            TypedArray a = obtainStyledAttributes(R.styleable.HelloGallery);
            mGalleryItemBackground = a.getResourceId(
                    R.styleable.HelloGallery_android_galleryItemBackground, 0);
            a.recycle();
            */
        }

        public int getCount() {
            return allSections.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public SectionArrayList<PictureBean> getItemBean(int position) {
        	return allSections.get(position);
        }
        
        public View getView(int position, View convertView, ViewGroup parent) {
        	
        	//if (convertView == null) {
        		convertView = new LinearLayout(mContext);
        	//}
        	SectionArrayList section = allSections.get(position);
        	
        	((LinearLayout)convertView).setOrientation(LinearLayout.VERTICAL);
        	ImageView bossImage = new ImageView(mContext);
        	
        	//L'immagine visiva al massimo pu essere di 450 dp
        	int pixelCorrispondenti = (int)(ApplicationManager.SECTION_GALLERY_MAX_APPARENT_SIZE_DP*DENSITY+0.5f);
            int H = (int)(ApplicationManager.SCREEN_H*0.6);
            if(H > pixelCorrispondenti) {
            	H = pixelCorrispondenti;
            }
        	//Controllo se la sezione  gi stata sbloccata o meno
        	boolean sbloccata = section.isUnlocked(getApplicationContext(),gamemode);
        	if(sbloccata || "Bonus".equalsIgnoreCase(section.getSectionName())) {
        		//bossImage.setImageResource(R.drawable.stella);
        		bossImage.setImageBitmap(section.getPresentaionImage(getApplicationContext()));
        	}else {
        		//bossImage.setImageResource(R.drawable.stella_black);
        		bossImage.setImageBitmap(section.getLockedImage(getApplicationContext()));
        	}

            bossImage.setLayoutParams(new Gallery.LayoutParams(H,H));
            bossImage.setScaleType(ImageView.ScaleType.FIT_XY);
            
	            
            ((LinearLayout)convertView).addView(bossImage);
            
            return convertView;
        }
    }
}
