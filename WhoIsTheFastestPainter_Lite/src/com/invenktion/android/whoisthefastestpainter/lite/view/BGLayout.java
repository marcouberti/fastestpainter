package com.invenktion.android.whoisthefastestpainter.lite.view;

import android.content.Context;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * @author Marco Uberti
 * Questo elemento consente di creare uno sfondo che si adatta automaticamente a tutti i tipi di schermi.
 * L'immagine top e bottom, attenzione che devono essere in formato altamente landscape, molto lunghe e strette, altrimenti
 * il gioco non regge.
 * Quella centrale invece è un quadrello di colore uniforme, va bene anche piccolissimo.
 */
public class BGLayout extends FrameLayout{

	public BGLayout(Context context, int imgTop, int imgCenter, int imgBottom) {
		super(context);
		ImageView top = new ImageView(context);
	
		top.setImageResource(imgTop);
		top.setScaleType(ImageView.ScaleType.FIT_START);
		
		ImageView bottom = new ImageView(context);
		bottom.setImageResource(imgBottom);
		bottom.setScaleType(ImageView.ScaleType.FIT_END);
		
		ImageView center = new ImageView(context);
		center.setImageResource(imgCenter);
		center.setScaleType(ImageView.ScaleType.FIT_XY);
		
		this.addView(center);
		this.addView(top);
		this.addView(bottom);
	}

}
