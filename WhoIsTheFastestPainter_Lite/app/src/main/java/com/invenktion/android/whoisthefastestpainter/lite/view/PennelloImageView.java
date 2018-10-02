package com.invenktion.android.whoisthefastestpainter.lite.view;

import com.invenktion.android.whoisthefastestpainter.lite.core.AnimationFactory;
import com.invenktion.android.whoisthefastestpainter.lite.core.ApplicationManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.BitmapManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.SoundManager;
import com.invenktion.android.whoisthefastestpainter.lite.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

import android.graphics.Paint;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class PennelloImageView extends ImageView implements OnTouchListener{

	private int paintSize = 35;//in DP

	private FingerPaintDrawableView fingerPaintView;
	private boolean dxIncreaseDirection;
	float DENSITY = 1.0f;
	private Paint  mBitmapPaint;
	//Bitmap gomma1,gomma2,gomma3,gomma4,gomma5;
	Bitmap bmpMask;
	Bitmap colorPennelloBitmap;

	public PennelloImageView(Context context, FingerPaintDrawableView fingerPaintView,boolean dxIncreaseDirection) {
		super(context);

		this.DENSITY = context.getResources().getDisplayMetrics().density;
		this.fingerPaintView = fingerPaintView;
		this.dxIncreaseDirection = dxIncreaseDirection;
		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setFilterBitmap(true);
		
		//this.setImageResource(R.drawable.pennello);
		/*
		bmpMask = BitmapManager.getPennelloMask(context);  
		gomma1 = BitmapManager.getPennello1(context); 
		gomma2 = BitmapManager.getPennello2(context);  
		gomma3 = BitmapManager.getPennello3(context);  
		gomma4 = BitmapManager.getPennello4(context);   
		gomma5 = BitmapManager.getPennello5(context);   
		*/

		setColorePennello(ApplicationManager.getCurrentColor());
    
		this.setScaleType(ImageView.ScaleType.FIT_XY);
		//this.setColorFilter(alphaColor, Mode.SRC_ATOP);
		/*
		double proportion = (double)80/(double)66;
		int H = (int)(ApplicationManager.getSCREEN_H()*0.20);
		int W = (int)((double)H*(double)proportion);
		//Log.d("WXH 1 = ",""+W+"x"+H);
		this.setLayoutParams(new FrameLayout.LayoutParams(W,H));
		*/
		
		setOnTouchListener(this);
	}

	public int getPaintSize() {
		return paintSize;
	}

	public void setPaintSize(int paintSize) {
		this.paintSize = paintSize;
	}

	public void setColorePennello(int colore) {
		int w = BitmapManager.getPennello1(getContext().getApplicationContext()).getWidth();
		int h = BitmapManager.getPennello1(getContext().getApplicationContext()).getHeight();
		colorPennelloBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas userResultCanvas = new Canvas(colorPennelloBitmap);
        userResultCanvas.drawBitmap(BitmapManager.getPennelloMask(getContext().getApplicationContext()), 0, 0, mBitmapPaint);
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mBitmapPaint.setColor(colore);
        userResultCanvas.drawRect(0, 0, w, h, mBitmapPaint);
        mBitmapPaint.setXfermode(null);
        userResultCanvas.drawBitmap(BitmapManager.getPennello1(getContext().getApplicationContext()), 0, 0, mBitmapPaint);
        this.setImageBitmap(colorPennelloBitmap);
	}
	
	float historicalX = 0;
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		/*
        case MotionEvent.ACTION_MOVE:

        	if(dxIncreaseDirection) {
	        	if(historicalX > event.getX()) {
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
        	
        	//Applico l'immagine giusta al pennello
        	if(paintSize >=6 && paintSize<=10) {
        		ApplicationManager.getPENNELLO_TEXT().setImageResource(R.drawable.pensize1);
        	}else if(paintSize >10 && paintSize<=20){
        		ApplicationManager.getPENNELLO_TEXT().setImageResource(R.drawable.pensize2);
        	}
			else if(paintSize >20 && paintSize<=30){
				ApplicationManager.getPENNELLO_TEXT().setImageResource(R.drawable.pensize3);
			}
			else if(paintSize >30 && paintSize<=45){
				ApplicationManager.getPENNELLO_TEXT().setImageResource(R.drawable.pensize4);
			}else {
				ApplicationManager.getPENNELLO_TEXT().setImageResource(R.drawable.pensize5);
			}
			
        	ApplicationManager.setTOOL(ApplicationManager.TOOL_PENNELLO);
        	//La dimensione reale del pennello  in pixel,quindi trasformo i DP in pixel
        	ApplicationManager.setPaintSize((int)(paintSize*DENSITY +0.5f));
        	ApplicationManager.setShowPaintSize(true);
        	//showPaintSize = true;
        	ApplicationManager.refreshGlashPane();
            break;
        */
        case MotionEvent.ACTION_UP:
        	ApplicationManager.setShowPaintSize(false);
        	ApplicationManager.refreshGlashPane();
        
        	//Cambio lo strumento
        	ApplicationManager.setTOOL(ApplicationManager.TOOL_PENNELLO);
        	
        	SoundManager.playSound(SoundManager.SOUND_PLAF, getContext().getApplicationContext(),false);
        	
        	//Animazione
        	PennelloImageView me = PennelloImageView.this;
        	//Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.colorchooseanimation);
        	//me.startAnimation(AnimationFactory.getStrumentiAnimation(getContext().getApplicationContext()));
        	
        	
        	break;
		}
		
		int currentColor = ApplicationManager.getCurrentColor();
		if(currentColor != -1) {
			fingerPaintView.setColor(currentColor);
		}
		return true;
	}
}
