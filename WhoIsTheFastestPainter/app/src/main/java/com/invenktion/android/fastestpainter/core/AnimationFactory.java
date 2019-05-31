package com.invenktion.android.fastestpainter.core;

import com.invenktion.android.fastestpainter.R;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;

public class AnimationFactory {

	static Animation colorChooseAnimation;
	static Animation strumentiAnimation;
	//static Animation levelPresentationAnimation1,levelPresentationAnimation2,levelPresentationAnimation3;
	static Animation buttonDialogAnimation;
	static Animation levelSelectionAnimation;
	static Animation countDownAnimation3,countDownAnimation2,countDownAnimation1,countDownAnimationGO;
	static Animation rotationAnimation1,rotationAnimation2,rotationAnimation3,rotationAnimation4,rotationAnimation5;
	static Animation newRecordAnimation;
	static Animation ammoRotationAnimation1;
	static Animation terremotoAnimation;
	static Animation vorticeAnimation;
	
	
	public static void releaseAllAnimation() {
		colorChooseAnimation = null;
		strumentiAnimation = null;
		//levelPresentationAnimation1= null;
		//levelPresentationAnimation2= null;
		//levelPresentationAnimation3= null;
		buttonDialogAnimation= null;
		levelSelectionAnimation= null;
		countDownAnimation3= null;
		countDownAnimation2= null;
		countDownAnimation1= null;
		countDownAnimationGO= null;
		rotationAnimation1= null;
		rotationAnimation2= null;
		rotationAnimation3= null;
		rotationAnimation4= null;
		rotationAnimation5= null;
		newRecordAnimation= null;
		ammoRotationAnimation1= null;
		terremotoAnimation= null;
		vorticeAnimation= null;
	}
	
	public static Animation getTerremotoAnimation(Context context) {
		if(terremotoAnimation == null) {
			terremotoAnimation = AnimationUtils.loadAnimation(context, R.anim.terremotoanimation);
		}
		return terremotoAnimation;
	}
	
	public static Animation getVorticeAnimation(Context context) {
		if(vorticeAnimation == null) {
			vorticeAnimation = AnimationUtils.loadAnimation(context, R.anim.vorticeanimation);
		}
		return vorticeAnimation;
	}
	
	public static Animation getButtonDialogAnimation(Context context) {
		if(buttonDialogAnimation == null) {
			buttonDialogAnimation = new ScaleAnimation(1.0f,1.2f,1.0f,1.2f,ScaleAnimation.RELATIVE_TO_SELF,0.5f,ScaleAnimation.RELATIVE_TO_SELF,0.5f);
			buttonDialogAnimation.setInterpolator(new BounceInterpolator());
			buttonDialogAnimation.setRepeatMode(Animation.REVERSE); 
			buttonDialogAnimation.setRepeatCount(Animation.INFINITE);
			buttonDialogAnimation.setDuration(200);
		}
		return buttonDialogAnimation;
	}
	
	public static Animation getNewRecordAnimation(Context context) {
		if(newRecordAnimation == null) {
			newRecordAnimation = AnimationUtils.loadAnimation(context, R.anim.newrecord);
		}
		return newRecordAnimation;
	}
	
	public static Animation getColorChooseAnimation(Context context) {
		if(colorChooseAnimation == null) {
			colorChooseAnimation = AnimationUtils.loadAnimation(context, R.anim.colorchooseanimation2);
		}
		return colorChooseAnimation;
	}
	
	public static Animation getStrumentiAnimation(Context context) {
		if(strumentiAnimation == null) {
			strumentiAnimation = AnimationUtils.loadAnimation(context, R.anim.colorchooseanimation);
		}
		return strumentiAnimation;
	}
/*
	public static Animation getLevelPresentationAnimation1(Context context) {
		if(levelPresentationAnimation1 == null) {
			levelPresentationAnimation1 = AnimationUtils.loadAnimation(context, R.anim.appearbouncing);
		}
		return levelPresentationAnimation1;
	}
	
	public static Animation getLevelPresentationAnimation2(Context context) {
		if(levelPresentationAnimation2 == null) {
			levelPresentationAnimation2 = AnimationUtils.loadAnimation(context, R.anim.appearbouncing2);
		}
		return levelPresentationAnimation2;
	}
	
	public static Animation getLevelPresentationAnimation3(Context context) {
		if(levelPresentationAnimation3 == null) {
			levelPresentationAnimation3 = AnimationUtils.loadAnimation(context, R.anim.appearbouncing3);
		}
		return levelPresentationAnimation3;
	}
*/
	public static Animation getLevelSelectionAnimation(
			Context context) {
		if(levelSelectionAnimation == null) {
			levelSelectionAnimation = AnimationUtils.loadAnimation(context, R.anim.levelselectionanimation);
		}
		return levelSelectionAnimation;	
	}

	public static Animation getRotationAnimation_1(
			Context context) {
		if(rotationAnimation1 == null) {
			rotationAnimation1 = AnimationUtils.loadAnimation(context, R.anim.rotationset);
		}
		return rotationAnimation1;	
	}
	
	public static Animation getRotationAnimation_2(
			Context context) {
		if(rotationAnimation2 == null) {
			rotationAnimation2 = AnimationUtils.loadAnimation(context, R.anim.rotationset2);
		}
		return rotationAnimation2;	
	}
	public static Animation getRotationAnimation_3(
			Context context) {
		if(rotationAnimation3 == null) {
			rotationAnimation3 = AnimationUtils.loadAnimation(context, R.anim.rotationset3);
		}
		return rotationAnimation3;	
	}
	public static Animation getRotationAnimation_4(
			Context context) {
		if(rotationAnimation4 == null) {
			rotationAnimation4 = AnimationUtils.loadAnimation(context, R.anim.rotationset4);
		}
		return rotationAnimation4;	
	}
	public static Animation getRotationAnimation_5(
			Context context) {
		if(rotationAnimation5 == null) {
			rotationAnimation5 = AnimationUtils.loadAnimation(context, R.anim.rotationset5);
		}
		return rotationAnimation5;	
	}

	public static Animation getCountDownAnimation3(Context context) {
		if(countDownAnimation3 == null) {
			countDownAnimation3 = AnimationUtils.loadAnimation(context, R.anim.countdownanimation);
		}
		return countDownAnimation3;	
	}
	public static Animation getCountDownAnimation2(Context context) {
		if(countDownAnimation2 == null) {
			countDownAnimation2 = AnimationUtils.loadAnimation(context, R.anim.countdownanimation);
		}
		return countDownAnimation2;	
	}
	public static Animation getCountDownAnimation1(Context context) {
		if(countDownAnimation1 == null) {
			countDownAnimation1 = AnimationUtils.loadAnimation(context, R.anim.countdownanimation);
		}
		return countDownAnimation1;	
	}
	public static Animation getCountDownAnimationGO(Context context) {
		if(countDownAnimationGO == null) {
			countDownAnimationGO = AnimationUtils.loadAnimation(context, R.anim.countdownanimation);
		}
		return countDownAnimationGO;	
	}

	public static Animation getAmmoRotationAnimation_1(
			Context context) {
		/*
		if(ammoRotationAnimation1 == null) {
			ammoRotationAnimation1 = AnimationUtils.loadAnimation(context, R.anim.ammorotationset);
		}
		return ammoRotationAnimation1;	
		*/
		//Una nuova per ogni arma
		return AnimationUtils.loadAnimation(context, R.anim.ammorotationset);
	}

	public static void initializeAnimation(Context context) {
		getAmmoRotationAnimation_1(context);
		getButtonDialogAnimation(context);
		getColorChooseAnimation(context);
		getCountDownAnimation1(context);
		getCountDownAnimation2(context);
		getCountDownAnimation3(context);
		getCountDownAnimationGO(context);
		getLevelSelectionAnimation(context);
		getNewRecordAnimation(context);
		getRotationAnimation_1(context);
		getRotationAnimation_2(context);
		getRotationAnimation_3(context);
		getRotationAnimation_4(context);
		getRotationAnimation_5(context);
		getStrumentiAnimation(context);
		getTerremotoAnimation(context);
		getVorticeAnimation(context);
	}
}
