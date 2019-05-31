package com.invenktion.android.fastestpainter.view;

import com.invenktion.android.fastestpainter.core.ApplicationManager;
import com.invenktion.android.fastestpainter.core.FontFactory;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class GlassPaneDrawableView  extends View {
    
	private int SCREEN_HEIGHT,SCREEN_WIDTH = 0;
	private int CX,CY =0;
	//float DENSITY = 1.0f;
	Paint paint = new Paint();
	//private Bitmap lt,lb,rt,rb = null;
	float DENSITY = 1.0f;
	//private int CORNER_DIM;
	

	//-1 per non mettere l'immagine
	//fraction = la dimensione delle immagini ai bordi relativamente all'altezza dello schermo
	//public GlassPaneDrawableView(Context context, int lt, int lb, int rt, int rb,float fraction) {
	public GlassPaneDrawableView(Context context) {
        super(context);
        this.DENSITY = context.getResources().getDisplayMetrics().density;
        /*
        CORNER_DIM = (int)(ApplicationManager.SCREEN_H*fraction);
        paint.setAntiAlias(true);
    	paint.setAlpha(100);
    	paint.setColor(Color.BLACK);
    	
    	if(lt != -1) {
    		Bitmap b = BitmapFactory.decodeResource(getResources(), lt);
    		this.lt = Bitmap.createScaledBitmap(b, CORNER_DIM, CORNER_DIM, true);
    		b.recycle();
    		b=null;
    	}
		if(lb != -1) {
			Bitmap b = BitmapFactory.decodeResource(getResources(), lb);
			this.lb = Bitmap.createScaledBitmap(b, CORNER_DIM, CORNER_DIM, true);
    		b.recycle();
    		b=null;
		}
		if(rt != -1) {
			Bitmap b = BitmapFactory.decodeResource(getResources(), rt);
			this.rt = Bitmap.createScaledBitmap(b, CORNER_DIM,CORNER_DIM, true);
    		b.recycle();
    		b=null;
		}
		if(rb != -1) {
			Bitmap b = BitmapFactory.decodeResource(getResources(), rb);
			this.rb = Bitmap.createScaledBitmap(b, CORNER_DIM, CORNER_DIM, true);
    		b.recycle();
    		b=null;
		}
    	*/
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);
    	SCREEN_WIDTH = getWidth();
    	SCREEN_HEIGHT = getHeight();
    	CY = SCREEN_HEIGHT/2;
    	CX = SCREEN_WIDTH/2;
    }

    protected void onDraw(Canvas canvas) {
    	int paintSize = ApplicationManager.getPaintSize();
    	boolean showPaintSize = ApplicationManager.isShowPaintSize();
    	if(paintSize > 0 && showPaintSize) {	
    		paint.setAlpha(100);
	    	canvas.drawCircle(CX,CY, paintSize/2, paint);
	    	paint.setAlpha(50);
	    	canvas.drawCircle(CX,CY, (int)(paintSize/(1.5)), paint);
    	}
    	
    	/*
    	paint.setAlpha(255);
    	paint.setTypeface(FontFactory.getFont1(getContext()));
    	paint.setTextSize(20f);
    	canvas.drawText("Choose your color here", 10, 20,paint);
    	*/
    	
    	//Immagini ai bordi
    	/*
    	if(lt != null) {
    		canvas.drawBitmap(lt, 0,0, paint); 
    	}
    	if(lb != null) {
    		canvas.drawBitmap(lb, 0,SCREEN_HEIGHT-CORNER_DIM, paint); 
    	}
    	if(rt != null) {
    		canvas.drawBitmap(rt, SCREEN_WIDTH-CORNER_DIM,0, paint); 
    	}
    	if(rb != null) {
    		canvas.drawBitmap(rb, SCREEN_WIDTH-CORNER_DIM,SCREEN_HEIGHT-CORNER_DIM, paint); 
    	}
    	*/
    }

    /*
	public void recycleBitmaps() {
		if(lt!=null) {
			lt.recycle();
			lt=null;
		}
		if(lb!=null) {
			lb.recycle();
			lb=null;
		}
		if(rt!=null) {
			rt.recycle();
			rt=null;
		}
		if(rb!=null) {
			rb.recycle();
			rb=null;
		}
		System.gc();
	}
	*/

}

