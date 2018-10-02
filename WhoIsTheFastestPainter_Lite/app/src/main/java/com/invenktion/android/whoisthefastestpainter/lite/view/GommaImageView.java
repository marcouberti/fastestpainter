package com.invenktion.android.whoisthefastestpainter.lite.view;

import com.invenktion.android.whoisthefastestpainter.lite.core.AnimationFactory;
import com.invenktion.android.whoisthefastestpainter.lite.core.ApplicationManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.SoundManager;
import com.invenktion.android.whoisthefastestpainter.lite.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Paint;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class GommaImageView extends ImageView implements OnTouchListener{

	private int paintSize = 35;

	private FingerPaintDrawableView fingerPaintView;
	private boolean dxIncreaseDirection;
	float DENSITY = 1.0f;
	private Paint  mBitmapPaint;
	private int colore;
	//Bitmap gomma1,gomma2,gomma3,gomma4,gomma5;
	Bitmap gomma;
	
	public GommaImageView(Context context, FingerPaintDrawableView fingerPaintView, int colore,boolean dxIncreaseDirection) {
		super(context);

		this.DENSITY = context.getResources().getDisplayMetrics().density;
		this.colore = colore;
		this.fingerPaintView = fingerPaintView;
		this.dxIncreaseDirection = dxIncreaseDirection;
		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setFilterBitmap(true);
		
		//this.setImageResource(R.drawable.pennello);
		gomma = BitmapFactory.decodeResource(getResources(), R.drawable.gomma_size_1);  
		/*
		gomma2 = BitmapFactory.decodeResource(getResources(), R.drawable.gomma_size_2);  
		gomma3 = BitmapFactory.decodeResource(getResources(), R.drawable.gomma_size_3);  
		gomma4 = BitmapFactory.decodeResource(getResources(), R.drawable.gomma_size_4);  
		gomma5 = BitmapFactory.decodeResource(getResources(), R.drawable.gomma_size_5);  
		*/

        this.setImageBitmap(gomma);
		
		this.setScaleType(ImageView.ScaleType.FIT_XY);
		//this.setColorFilter(alphaColor, Mode.SRC_ATOP);
		/*
		double proportion = 80/66;
		int H = (int)(ApplicationManager.getSCREEN_H()*0.20);
		int W = (int)(H*proportion);
		
		this.setLayoutParams(new LinearLayout.LayoutParams(W,H));
		*/
		setOnTouchListener(this);
	}

	public int getPaintSize() {
		return paintSize;
	}

	public void setPaintSize(int paintSize) {
		this.paintSize = paintSize;
	}

	float historicalX = 0;
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		/*
        case MotionEvent.ACTION_MOVE:

        	if(dxIncreaseDirection) {
	        	if(historicalX > event.getX()) {
	        		paintSize -= 2;
	        		if(paintSize <= 6) {
	        			paintSize = 6;
	        		}
	        	}else {
	        		paintSize += 2;
	        		if(paintSize >= 60) {
	        			paintSize = 60;
	        		}
	        	}
        	}else {
        		if(historicalX < event.getX()) {
	        		paintSize -=2;
	        		if(paintSize <= 6) {
	        			paintSize = 6;
	        		}
	        	}else {
	        		paintSize +=2;
	        		if(paintSize >= 60) {
	        			paintSize = 60;
	        		}
	        	}
        	}
        	historicalX = event.getX();
        	
        	//Applico l'immagine giusta alla gomma
        	if(paintSize >=6 && paintSize<=10) {
        		ApplicationManager.getGOMMA_TEXT().setImageResource(R.drawable.gommasize1);
        	}else if(paintSize >10 && paintSize<=20){
        		ApplicationManager.getGOMMA_TEXT().setImageResource(R.drawable.gommasize2);
        	}
			else if(paintSize >20 && paintSize<=30){
				ApplicationManager.getGOMMA_TEXT().setImageResource(R.drawable.gommasize3);
			}
			else if(paintSize >30 && paintSize<=45){
				ApplicationManager.getGOMMA_TEXT().setImageResource(R.drawable.gommasize4);
			}else {
				ApplicationManager.getGOMMA_TEXT().setImageResource(R.drawable.gommasize5);
			}
			
        	ApplicationManager.setTOOL(ApplicationManager.TOOL_GOMMA);
        	//La dimensione reale del pennello  in pixel,quindi trasformo i DP in pixel
        	ApplicationManager.setGommaPaintSize((int)(paintSize*DENSITY +0.5f));
        	ApplicationManager.setShowPaintSize(true);
        	//showPaintSize = true;
        	ApplicationManager.refreshGlashPane();
            break;
        */
        case MotionEvent.ACTION_UP:
        	ApplicationManager.setShowPaintSize(false);
        	ApplicationManager.refreshGlashPane();
        	
        	//Cambio lo strumento
        	ApplicationManager.setTOOL(ApplicationManager.TOOL_GOMMA);
        
        	SoundManager.playSound(SoundManager.SOUND_PLAF, getContext().getApplicationContext(),false);
        	
        	//Animazione
        	GommaImageView me = GommaImageView.this;
        	Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.colorchooseanimation);
        	me.startAnimation(AnimationFactory.getStrumentiAnimation(getContext().getApplicationContext()));
        	/*
        	ApplicationManager.getGOMMA_ICON().setVisibility(View.GONE);
        	ApplicationManager.getGOMMA_TEXT().setVisibility(View.GONE);
        	ApplicationManager.getPENNELLO_ICON().setVisibility(View.VISIBLE);
        	ApplicationManager.getPENNELLO_TEXT().setVisibility(View.VISIBLE);
        	*/
            break;
		}
		fingerPaintView.setColor(colore);
		return true;
	}
}
