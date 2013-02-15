package com.invenktion.android.whoisthefastestpainter.lite;

import java.util.ArrayList;

import com.invenktion.android.whoisthefastestpainter.lite.SectionChoosingActivity.ImageAdapter;
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
import com.invenktion.android.whoisthefastestpainter.lite.utils.ActivityHelper;
import com.invenktion.android.whoisthefastestpainter.lite.view.GlassPaneDrawableView;
import com.invenktion.android.whoisthefastestpainter.lite.widget.OneStepGallery;
import com.invenktion.android.whoisthefastestpainter.lite.R;

import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.BitmapFactory.Options;

import android.os.Bundle;

import android.util.Log;

import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;

import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.Gallery;
import android.widget.RelativeLayout;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AmmoActivity extends Activity{
	
	static final int DIALOG_AMMO_INSTRUCTION = 0;
	static final int DIALOG_ADD_MORE_PROBABILITY = 1;
	
	float DENSITY = 1.0f;
	//Typeface font; 
	//GlassPaneDrawableView glassPane;
	ImageAdapter adapter;
	AmmoBean selectedAmmo;
	OneStepGallery g;
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		LevelManager.clearAllCachedImage();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	finish();
	    	overridePendingTransition(0,0);
	    }
	    return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//Spengo la musica solo se un'altra applicazione è davanti alla nostra (VOICE CALL, HOME Button, etc..)
		if(ActivityHelper.isApplicationBroughtToBackground(this)) {
			SoundManager.pauseBackgroundMusic();
		}
	}

	//Questo viene chiamato quando l'utente clicca il tasto "back" e dal livello torna qui
	@Override
	protected void onResume() {
		super.onResume();
		//Gallery g = (Gallery) findViewById(R.id.galleryammos);
		
		//Rilancio la musica se e solo se non è già attiva
		//Questo ci permette di utilizzare la stessa traccia musicale tra Activity differenti, oltre
		//al metodo presente nel onPause che controlla se siamo o no in background
		KeyguardManager keyguardManager = (KeyguardManager)getApplicationContext().getSystemService(Activity.KEYGUARD_SERVICE);  
    	boolean bloccoSchermoAttivo = keyguardManager.inKeyguardRestrictedInputMode();
		if(!bloccoSchermoAttivo && !SoundManager.isBackgroundMusicPlaying()) {
			SoundManager.playBackgroundMusic(getApplicationContext());
		}
		//Una volta entrati tolgo il flag di nuova arma sbloccata esistente
		ApplicationManager.THERE_IS_A_NEW_UNLOCKED_AMMO = false;
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
        this.DENSITY = getApplicationContext().getResources().getDisplayMetrics().density;
        setContentView(R.layout.ammolist);
        adapter = new ImageAdapter(getApplicationContext());
        ImageView mascotteImage = (ImageView)findViewById(R.id.mascotteimage);
        mascotteImage.setLayoutParams(new LinearLayout.LayoutParams((int)(ApplicationManager.SCREEN_H/2.5), (int)(ApplicationManager.SCREEN_H/2.5)));
        
        //font = Typeface.createFromAsset(getAssets(), FontFactory.FONT1); 
        
        //Inserisco lo sfondo
        //glassPane = new GlassPaneDrawableView(this,-1,R.drawable.br3,-1,-1,0.40f);
        LinearLayout galleryLayout = (LinearLayout)findViewById(R.id.ammolayout);
        //frameLayout.addView(glassPane);
        
        g = new OneStepGallery(getApplicationContext());
        galleryLayout.addView(g);
        g.setAdapter(adapter);
        g.setSoundEffectsEnabled(false);
        float scale = AmmoActivity.this.DENSITY;
        g.setSpacing((int)(0*scale+0.5f));//spazio tra le immagini della gallery
        g.setAnimationDuration(500);
        g.setAnimation(null);
        g.setAnimationCacheEnabled(true);
        g.setDrawingCacheEnabled(true);
        g.setHorizontalFadingEdgeEnabled(false);
        g.setUnselectedAlpha(255);
        
        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, final int position, long id) {
            	
            	final AmmoBean ammo = AmmoManager.getAllAmmo().get(position);
            	
            	//Carico l'animazioncina
				Animation selectionAnimation = AnimationFactory.getLevelSelectionAnimation(getApplicationContext());
				//Al termine dell'animazione lancio il livello (con un listener)
				selectionAnimation.setAnimationListener(new AnimationListener() {
					
					public void onAnimationStart(Animation animation) {	}
					
					public void onAnimationRepeat(Animation animation) {}
					
					public void onAnimationEnd(Animation animation) {
						selectedAmmo = ammo;
						showDialog(DIALOG_AMMO_INSTRUCTION);
					}
				});
				if(ammo.isUnlocked(getApplicationContext())) {
					//Eseguo l'animazione
					v.startAnimation(selectionAnimation);
				}
            }
        });
        g.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				AmmoBean ammo = AmmoManager.getAllAmmo().get(position);
				updateInfoText(ammo);
			}

			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
        
        //Mi posiziono sull'ultima arma sbloccata
        SharedPreferences settings = getApplicationContext().getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
		int lastUsed =  settings.getInt("last_ammo_unlocked", 0);
		if(lastUsed < g.getCount()) {
			g.setSelection(lastUsed);
		}
        
        TextView skillText = (TextView) findViewById(R.id.skilltext);
        skillText.setTextAppearance(getApplicationContext(), R.style.TimeFont_Black_NumberScroll);
        skillText.setTypeface(FontFactory.getFont1(getApplicationContext()));
        /*
        TextView ammoNameText = (TextView) findViewById(R.id.ammonametext);
        ammoNameText.setTextAppearance(getApplicationContext(), R.style.AmmoGallery_Black_Title);
        ammoNameText.setTypeface(FontFactory.getFont1(getApplicationContext()));
        */
        
        TextView ammoPriceText = (TextView) findViewById(R.id.ammocoinstext);
        ammoPriceText.setTextAppearance(getApplicationContext(), R.style.AmmoGallery_Black_Skill);
        ammoPriceText.setTypeface(FontFactory.getFont1(getApplicationContext()));
        
        updateCreditsText();
   }
    
   private void updateCreditsText() {
	   TextView skillText = (TextView) findViewById(R.id.skilltext);
       skillText.setText(" "+CreditsManager.getUserCredits(getApplicationContext())+" ");
   }
   
   private void updateInfoText(AmmoBean ammo) {
	   //TextView ammoNameText = (TextView) findViewById(R.id.ammonametext);
	   //ammoNameText.setText(ammo.getName()+"/"+ammo.getAntiWeapon().getName());
       TextView ammoPriceText = (TextView) findViewById(R.id.ammocoinstext);
       ammoPriceText.setText(getApplicationContext().getString(R.string.price)+": "+ammo.getEnableCreditTrigger());
   }

//Crea il particolare dialog una volta sola
    //Per riconfigurarlo usare onPrepareDialog
    protected Dialog onCreateDialog(int id) {
        final Dialog dialog;
        switch(id) {
        case DIALOG_AMMO_INSTRUCTION:
        	// prepare the custom dialog
			dialog = new Dialog(this);//con l'app context non si aprono
			dialog.setCancelable(true);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.ammo_instruction_dialog);
			//dialog.setTitle("Custom Dialog");
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.transparent);
			ImageView instructionImage = (ImageView) dialog.findViewById(R.id.instructionImage);
			int pixelCorrispondenti = (int)(ApplicationManager.DIALOG_MAX_APPARENT_SIZE_DP*DENSITY+0.5f);
        	int H = (int)(ApplicationManager.SCREEN_H*0.8);
            if(H > pixelCorrispondenti) {
            	H = pixelCorrispondenti;
            }
			instructionImage.setLayoutParams(new RelativeLayout.LayoutParams(H, H));
			
			ImageView instructionBGImage = (ImageView) dialog.findViewById(R.id.instructionBgImage);
			int Hbg = (int)(ApplicationManager.SCREEN_H*0.8);
			if(Hbg > pixelCorrispondenti) {
				Hbg = pixelCorrispondenti;
            }
			instructionBGImage.setLayoutParams(new RelativeLayout.LayoutParams(Hbg, Hbg));
            break;
        case DIALOG_ADD_MORE_PROBABILITY:
        	// prepare the custom dialog
			dialog = new Dialog(this);//con l'app context non si aprono
			dialog.setCancelable(true);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.add_weapon_dialog);
			
			TextView textAddAmmo = (TextView)dialog.findViewById(R.id.textaddammo);
			textAddAmmo.setTypeface(FontFactory.getFont1(getApplicationContext()));
			//dialog.setTitle("Custom Dialog");
			dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialogbg);
			break;
        default:
            dialog = null;
        }
        return dialog;
    }
    private boolean weaponImage = true;//per mostrare l'arma o l'antiarma
    //Da la possibilità di riconfigurare un dialog
    protected void onPrepareDialog(int id, final Dialog dialog) {
    	switch(id) {
        case DIALOG_AMMO_INSTRUCTION:
        	weaponImage = true;
        	
        	int userCredits = CreditsManager.getUserCredits(getApplicationContext());
        	final boolean buyable = userCredits >= selectedAmmo.getEnableCreditTrigger();
        	
        	final ImageView instructionBgImage = (ImageView) dialog.findViewById(R.id.instructionBgImage);
    		final ImageView instructionImage = (ImageView) dialog.findViewById(R.id.instructionImage);
    		instructionImage.setImageResource(selectedAmmo.getInstructionImage());
    		if(buyable) {
    			instructionBgImage.setImageResource(R.drawable.trickbg);
    		}else {
    			instructionBgImage.setImageResource(R.drawable.tricktooexpensive);
    		}
    		instructionImage.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	//Cambio la variabile che indica se mostrare l'arma o l'antiarma
							weaponImage =!weaponImage;
							if(weaponImage) {
								instructionImage.setImageResource(selectedAmmo.getInstructionImage());
								if(buyable) {
									instructionBgImage.setImageResource(R.drawable.trickbg);
								}else {
									instructionBgImage.setImageResource(R.drawable.tricktooexpensive);
								}
							}else {
								instructionImage.setImageResource(selectedAmmo.getAntiWeapon().getInstructionImage());
								if(buyable) {
									instructionBgImage.setImageResource(R.drawable.trapbg);
								}else {
									instructionBgImage.setImageResource(R.drawable.traptooexpensive);
								}
							}
				            break;
					}
					return true;
				}
			});
    		final ImageView addProbabilityImage = (ImageView) dialog.findViewById(R.id.addMoreProbability);
    		addProbabilityImage.setEnabled(true);
    		addProbabilityImage.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	addProbabilityImage.setEnabled(false);
							//TODO faccio partire l'altro dialog di conferma acquisto e scalo crediti
							dialog.dismiss();
							showDialog(DIALOG_ADD_MORE_PROBABILITY);
				            break;
					}
					return true;
				}
			});
    		//Se i crediti dell'utente non sono necessari per comprare l'arma/antiarma disabilito
    		
    		if(buyable) {
    			addProbabilityImage.setEnabled(true);
    			addProbabilityImage.setVisibility(View.VISIBLE);
    		}else {
    			addProbabilityImage.setEnabled(false);
    			addProbabilityImage.setVisibility(View.INVISIBLE);
    		}
            break; 
        case DIALOG_ADD_MORE_PROBABILITY:
        	final ImageView yesButton = (ImageView)dialog.findViewById(R.id.yesButton);
        	final ImageView noButton = (ImageView)dialog.findViewById(R.id.noButton);
        	
        	yesButton.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	yesButton.setEnabled(false);
				        	dialog.dismiss();
							yesButton.setEnabled(true);
							//Incremento le probabilità dell'arma
							selectedAmmo.incrementNumberOfProbability(getApplicationContext());
							//Tolgo i crediti all'user
							CreditsManager.decrementToUserCredits(selectedAmmo.getEnableCreditTrigger(), getApplicationContext());
							updateCreditsText();
							//Aggiorno l'UI
							adapter.notifyDataSetChanged();
				            break;
					}
					return true;
				}
			});
        	noButton.setOnTouchListener(new OnTouchListener() {
				public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
				        case MotionEvent.ACTION_UP:
				        	noButton.setEnabled(false);
				        	dialog.dismiss();
							noButton.setEnabled(true);
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
    
    //Adapter per la Gallery
    class ImageAdapter extends BaseAdapter {
        int mGalleryItemBackground;
        private Context mContext;

        private ArrayList<AmmoBean> allAmmos = AmmoManager.getAllAmmo();

        public ImageAdapter(Context c) {
            mContext = c;
            /*
            TypedArray a = obtainStyledAttributes(R.styleable.HelloGallery);
            mGalleryItemBackground = a.getResourceId(
                    R.styleable.HelloGallery_android_galleryItemBackground, 0);
            a.recycle();
            */
        }

        public int getCount() {
            return allAmmos.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        
        public View getView(int position, View convertView, ViewGroup parent) {
        	
        	//if (convertView == null) {
        		//convertView = new LinearLayout(mContext);
        	//}
        	AmmoBean ammo = allAmmos.get(position);
        	return buildView(ammo);
        }
    }
    
    private View buildView(AmmoBean ammo) {
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

        	final LinearLayout ll = new LinearLayout(getApplicationContext());
        	ll.setOrientation(LinearLayout.VERTICAL);
        	ll.setGravity(Gravity.TOP);
        	//ll.setBackgroundColor(Color.WHITE);
        	//ll.setBackgroundResource(R.drawable.dialogbg);
        	
        	//ImageView i = new ImageView(getApplicationContext());
        	ImageView iBg = new ImageView(getApplicationContext());

        	//Controllo se il livello è stato già sbloccato o meno
            boolean isUnlocked = (ammo.isUnlocked(getApplicationContext()));
            //boolean isUnlocked = true;
            
            float scale = AmmoActivity.this.DENSITY;

            int pixelCorrispondenti = (int)(ApplicationManager.AMMO_GALLERY_MAX_APPARENT_SIZE_DP*DENSITY+0.5f);
        	//L'immagine visiva al massimo può essere di 450 dp
            int H = (int)(ApplicationManager.SCREEN_H*0.8);
            if(H > pixelCorrispondenti) {
            	H = pixelCorrispondenti;
            }
 
        	if(isUnlocked) {//La prima è sbloccata di default
        		iBg.setImageBitmap(ammo.getGalleryPicture(getApplicationContext()));
        	}else{
        		iBg.setImageResource(R.drawable.tricktrapblocked);
        	}
            iBg.setLayoutParams(new FrameLayout.LayoutParams(H,H));
            iBg.setScaleType(ImageView.ScaleType.FIT_XY);
         
            LinearLayout imagell = new LinearLayout(getApplicationContext());
            imagell.setGravity(Gravity.CENTER_HORIZONTAL);
	            RelativeLayout relativeLayoutImage = new RelativeLayout(getApplicationContext());
	            relativeLayoutImage.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,Gravity.CENTER_HORIZONTAL));
	            	relativeLayoutImage.addView(iBg);
	            TextView changeText = new TextView(getApplicationContext());
	            if(isUnlocked) {
	            	
	            	//TRUCCO PER CENTRARE DEL TESTO DINAMICAMENTE: metterlo al centro di un layout fisso più grande,sia in verticale che orizzontale
	            	RelativeLayout textContainer = new RelativeLayout(getApplicationContext());
	            	//textContainer.setBackgroundColor(Color.GREEN);
	            	
		            changeText.setText(ammo.getNumberOfProbability(getApplicationContext()));
		            changeText.setTextAppearance(getApplicationContext(), R.style.AmmoHowManyProbability_Text);
		            changeText.setTypeface(FontFactory.getFont1(getApplicationContext()));
		            
		            //Il container del testo non deve sforare l'immagine dell'arma altrimenti la gallery si sbarella
		            //quindi calcolo la sua dimensione massima, cioè dal centro del testo fino al bordo dell'immagine
		            float relX = 0.8f;
		            float relY = 0.75f;
		            int gap = (int)(H - (H*relX) -2); 
		            final int CONTAINER_SIZE = gap*2;
		            
		            RelativeLayout.LayoutParams paramsText = new RelativeLayout.LayoutParams(CONTAINER_SIZE,CONTAINER_SIZE);
		            paramsText.leftMargin = (int)((H*relX) - (CONTAINER_SIZE/2));
		            paramsText.topMargin = (int)((H*relY) - (CONTAINER_SIZE/2));
		            textContainer.setLayoutParams(paramsText);
		            
		            changeText.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,Gravity.CENTER_VERTICAL));
		            
		            RelativeLayout.LayoutParams centerParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		            centerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		            textContainer.addView(changeText,centerParams);
	            	relativeLayoutImage.addView(textContainer);
	            }
            imagell.addView(relativeLayoutImage);

            ll.addView(imagell);
            primariga.addView(ll);

        return container;
	}
}
