package com.invenktion.android.whoisthefastestpainter.lite.view;


import com.invenktion.android.whoisthefastestpainter.lite.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class MessageView extends View {
    
	private int SCREEN_HEIGHT,SCREEN_WIDTH = 0;
	private int CX,CY =0;
	private Bitmap bgScaled = null;
	
	public MessageView(Context context) {
        super(context);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
    	super.onSizeChanged(w, h, oldw, oldh);
    	SCREEN_WIDTH = getWidth();
    	SCREEN_HEIGHT = getHeight();
    	CY = SCREEN_HEIGHT/2;
    	CX = SCREEN_WIDTH/2;
    	
    	initializeBitmaps();
    }
   
    protected void onDraw(Canvas canvas) {
    	
    }
    
    private void initializeBitmaps() {
    	
    }
}
