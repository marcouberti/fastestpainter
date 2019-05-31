package com.invenktion.android.fastestpainter.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.widget.Gallery;

/**
 * @author Marco Uberti
 * Questa Gallery non permette di scrollare pi immagini alla volta,ma
 * fa solo un passo alla volta.
 */
public class OneStepGallery extends Gallery{

	public OneStepGallery(Context context) {
		super(context);
	}
	
	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		return super.onFling(e1, e2, 0, velocityY);
	}

}
