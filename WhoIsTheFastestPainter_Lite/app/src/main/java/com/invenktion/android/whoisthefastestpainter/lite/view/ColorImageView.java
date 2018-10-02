package com.invenktion.android.whoisthefastestpainter.lite.view;

import com.invenktion.android.whoisthefastestpainter.lite.core.AnimationFactory;
import com.invenktion.android.whoisthefastestpainter.lite.core.ApplicationManager;
import com.invenktion.android.whoisthefastestpainter.lite.core.SoundManager;
import com.invenktion.android.whoisthefastestpainter.lite.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SlidingDrawer;

public class ColorImageView extends ImageView implements OnTouchListener{

	private int paintSize = 15;
	private int colore;
	private FingerPaintDrawableView fingerPaintView;
	private boolean dxIncreaseDirection;
	float DENSITY = 1.0f;
	private Paint  mBitmapPaint;
	private int position;//posizione sulla tavolozza
	private int totalColorNum;//Numero totale di colori sulla tavolozza

	
	public ColorImageView(Context context, FingerPaintDrawableView fingerPaintView, int colore, boolean dxIncreaseDirection, int position, int totalColorNum, double scaleFactor) {
		super(context);

		this.DENSITY = context.getResources().getDisplayMetrics().density;
		this.colore = colore;
		this.position = position;
		this.totalColorNum = totalColorNum;
		this.fingerPaintView = fingerPaintView;
		this.dxIncreaseDirection = dxIncreaseDirection;
		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setFilterBitmap(true);
		
		if(totalColorNum == 4) position = position +0;
		if(totalColorNum == 3) position = position +1;
		if(totalColorNum == 2) position = position +1;
		if(totalColorNum == 1) position = position +2;
		
		//this.setImageResource(R.drawable.pennello);
		int stainResource = 0;
		if(position == 0)  stainResource = R.drawable.col5;
		else if(position == 1)  stainResource = R.drawable.col4;
		else if(position == 2)  stainResource = R.drawable.col1;
		else if(position == 3)  stainResource = R.drawable.col3;
		else if(position == 4)  stainResource = R.drawable.col2;
		
		int stainResourceMask = 0;
		if(position == 0)  stainResourceMask = R.drawable.col5mask;
		else if(position == 1)  stainResourceMask = R.drawable.col4mask;
		else if(position == 2)  stainResourceMask = R.drawable.col1mask;
		else if(position == 3)  stainResourceMask = R.drawable.col3mask;
		else if(position == 4)  stainResourceMask = R.drawable.col2mask;
		
		int dpiH = 0;
		if(position == 0)  dpiH = 63;
		else if(position == 1)  dpiH = 44;
		else if(position == 2)  dpiH = 42;
		else if(position == 3)  dpiH = 43;
		else if(position == 4)  dpiH = 59;
		
		Bitmap bmp = BitmapFactory.decodeResource(getResources(), stainResource);  
		Bitmap bmpMask = BitmapFactory.decodeResource(getResources(), stainResourceMask); 
		Bitmap finalBitmap = Bitmap.createBitmap(bmp.getWidth(), bmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas userResultCanvas = new Canvas(finalBitmap);
        userResultCanvas.drawBitmap(bmp, 0, 0, mBitmapPaint);
        mBitmapPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
        mBitmapPaint.setColor(colore);
        userResultCanvas.drawRect(0, 0, bmp.getWidth(), bmp.getHeight(), mBitmapPaint);
        mBitmapPaint.setXfermode(null);
        userResultCanvas.drawBitmap(bmpMask, 0, 0, mBitmapPaint);
		
        this.setImageBitmap(finalBitmap);
		
        bmp.recycle();
        bmp = null;
        bmpMask.recycle();
        bmpMask = null;
        //NOSystem.gc();
        
		this.setScaleType(ImageView.ScaleType.FIT_XY);
		//this.setColorFilter(alphaColor, Mode.SRC_ATOP);
		this.setLayoutParams(new LinearLayout.LayoutParams((int)(60*scaleFactor*DENSITY+0.5f),(int)(dpiH*scaleFactor*DENSITY+0.5f)));
		
		setOnTouchListener(this);
	}


	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		/*
        case MotionEvent.ACTION_MOVE:

        	if(dxIncreaseDirection) {
	        	if(event.getHistoricalX(0) > event.getX()) {
	        		paintSize --;
	        		if(paintSize <= 6) {
	        			paintSize = 6;
	        		}
	        	}else {
	        		paintSize ++;
	        		if(paintSize >= 60) {
	        			paintSize = 60;
	        		}
	        	}
        	}else {
        		if(event.getHistoricalX(0) < event.getX()) {
	        		paintSize --;
	        		if(paintSize <= 6) {
	        			paintSize = 6;
	        		}
	        	}else {
	        		paintSize ++;
	        		if(paintSize >= 60) {
	        			paintSize = 60;
	        		}
	        	}
        	}
        	ApplicationManager.setPaintSize(paintSize);
        	ApplicationManager.setShowPaintSize(true);
        	//showPaintSize = true;
        	ApplicationManager.refreshGlashPane();
            break;
            */
        case MotionEvent.ACTION_UP:
        	ApplicationManager.setShowPaintSize(false);
        	//ApplicationManager.refreshGlashPane();
        
        	//Cambio lo strumento
        	ApplicationManager.setTOOL(ApplicationManager.TOOL_PENNELLO);
        	
        	SoundManager.playSound(SoundManager.SOUND_PLAF, getContext().getApplicationContext(),false);
        	
        	//Animazione
        	ColorImageView me = ColorImageView.this;
        	//Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(getContext().getApplicationContext(), R.anim.colorchooseanimation2);
        	me.startAnimation(AnimationFactory.getColorChooseAnimation(getContext().getApplicationContext()));
        	
            break;
		}
		ApplicationManager.setCurrentColor(colore);
		fingerPaintView.setColor(colore);
		return true;
	}
}
