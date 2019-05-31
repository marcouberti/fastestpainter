package com.invenktion.android.fastestpainter.view;



import com.invenktion.android.fastestpainter.core.ApplicationManager;
import com.invenktion.android.fastestpainter.core.SoundManager;
import com.invenktion.android.fastestpainter.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Paint;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class AmmoImageView extends ImageView implements OnTouchListener{

	private int paintSize = 15;

	private FingerPaintDrawableView fingerPaintView;

	float DENSITY = 1.0f;
	private Paint  mBitmapPaint;
	
	public AmmoImageView(Context context, FingerPaintDrawableView fingerPaintView) {
		super(context);

		this.DENSITY = context.getResources().getDisplayMetrics().density;
		this.fingerPaintView = fingerPaintView;

		mBitmapPaint = new Paint();
		mBitmapPaint.setAntiAlias(true);
		mBitmapPaint.setFilterBitmap(true);
		
		//this.setImageResource(R.drawable.pennello);
		//Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.bombacolore);  

        //this.setImageBitmap(bmp);
		
		this.setScaleType(ImageView.ScaleType.FIT_XY);
		//this.setColorFilter(alphaColor, Mode.SRC_ATOP);
		//this.setLayoutParams(new LinearLayout.LayoutParams((int)(80*DENSITY+0.5f),(int)(66*DENSITY+0.5f)));
		
		setOnTouchListener(this);
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
        case MotionEvent.ACTION_UP:
        	ApplicationManager.setShowPaintSize(false);
        	ApplicationManager.refreshGlashPane();
        
        	SoundManager.playSound(SoundManager.SOUND_PLAF, getContext().getApplicationContext(),false);
        	this.setVisibility(View.INVISIBLE);
            break;
		}
		//fingerPaintView.executeAmmo((int)event.getX(),(int)event.getY(),getContext());
		return true;
	}
}
