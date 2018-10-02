package com.invenktion.android.whoisthefastestpainter.lite.core;

import android.content.Context;
import android.content.SharedPreferences;

public class CreditsManager {
	
	/**
	 * @param context
	 * @return un intero che indica il numero totale di crediti guadagnati dall'utente.
	 */
	public static int getUserCredits(Context context) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
		return settings.getInt("total_user_credits", 0);
	}
	
	/**
	 * @param increment,il numero di crediti da aggiungere a quelli attuali dell'utente
	 * @param context
	 */
	public static void addToUserCredits(int increment, Context context) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        int currentTotalCredits = CreditsManager.getUserCredits(context);
        editor.putInt("total_user_credits", currentTotalCredits+increment);
        //Commit the edits!
        editor.commit();
	}
	
	/**
	 * @param decrement,il numero di crediti da aggiungere a quelli attuali dell'utente
	 * @param context
	 */
	public static void decrementToUserCredits(int decrement, Context context) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        int currentTotalCredits = CreditsManager.getUserCredits(context);
        editor.putInt("total_user_credits", currentTotalCredits-decrement);
        //Commit the edits!
        editor.commit();
	}
}
