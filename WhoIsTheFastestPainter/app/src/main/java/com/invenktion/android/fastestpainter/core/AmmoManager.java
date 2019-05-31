package com.invenktion.android.fastestpainter.core;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;

import com.invenktion.android.fastestpainter.R;
import com.invenktion.android.fastestpainter.bean.AmmoBean;

public class AmmoManager {

	public static final String COLOR_BOMB_SMALL = "SMALL BOMB";
	public static final String COLOR_BOMB_BIG = "BIG BOMB";
	public static final String JOLLY = "JOKER";
	public static final String MOSCA = "FLY";
	public static final String INK = "INK";
	public static final String SKULL = "SKULL";
	public static final String CLUSTER_BOMB = "CLUSTER BOMB";
	public static final String X_BOMB = "X BOMB";
	public static final String TRIANGLE_BOMB = "T BOMB";
	public static final String SPRAY = "SPRAY";
	public static final String CRAZY_COLOR = "CRAZY COLOR";
	public static final String MORETIME_X_SECONDS = "+ 10 SEC";
	public static final String TERREMOTO = "EARTHQUAKE";
	public static final String VORTICE = "VORTEX";
	public static final String SHANGHAI_BOMB = "SHANGHAI BOMB";
	public static final String CANDEGGINA = "WHITENER";
	public static final String MAGIC_EDGES = "MAGIC EDGES";
	public static final String MAGIC_EDGES_INVERSE = "BAD EDGES";
	public static final String BULLET_HOLE = "UZI";
	public static final String COLOR_BULLET_HOLE = "COLOR UZI";
	
	private static ArrayList<AmmoBean> ammos = new ArrayList<AmmoBean>();
	private static ArrayList<AmmoBean> unlockedAmmos = new ArrayList<AmmoBean>();
	
	public static ArrayList<AmmoBean> getAllAmmo() {
		return ammos;
	}
	public static void initializeAmmo() {
		ammos.clear();
		
		//ANTI ARMI
		AmmoBean ammo5 = new AmmoBean(INK,0,R.drawable.ink,R.drawable.lucchetto,R.drawable.inkinstruction,false,null,-1);
		AmmoBean ammo20 = new AmmoBean(CANDEGGINA,0,R.drawable.candeggina,R.drawable.lucchetto,R.drawable.candegginainstruction,false,null,-1);
		AmmoBean ammo4 = new AmmoBean(MOSCA,0,R.drawable.mosquito,R.drawable.lucchetto,R.drawable.mosquitoinstruction,false,null,-1);
		AmmoBean ammo11 = new AmmoBean(SPRAY,0,R.drawable.spray,R.drawable.lucchetto,R.drawable.sprayinstruction,false,null,-1);
		AmmoBean ammo6 = new AmmoBean(SKULL,0,R.drawable.skull,R.drawable.lucchetto,R.drawable.skullinstruction,false,null,-1);
		AmmoBean ammo18 = new AmmoBean(VORTICE,0,R.drawable.vortice,R.drawable.lucchetto,R.drawable.vortexinstruction,false,null,-1);
		AmmoBean ammo22 = new AmmoBean(MAGIC_EDGES_INVERSE,0,R.drawable.magicedgesinverse,R.drawable.lucchetto,R.drawable.magicedgeinverseinstruction,false,null,-1);
		AmmoBean ammo12 = new AmmoBean(CRAZY_COLOR,0,R.drawable.crazycolors,R.drawable.lucchetto,R.drawable.crazycolorsinstruction,false,null,-1);
		AmmoBean ammo17 = new AmmoBean(TERREMOTO,0,R.drawable.terremoto,R.drawable.lucchetto,R.drawable.terremotoinstruction,false,null,-1);
		AmmoBean ammo23 = new AmmoBean(BULLET_HOLE,0,R.drawable.mitra,R.drawable.lucchetto,R.drawable.mitrainstruction,false,null,-1);
		
		AmmoBean ammo1 = new AmmoBean(COLOR_BOMB_SMALL,ApplicationManager.AMMO_MIN_PRICE_VALUE,R.drawable.bomb3,R.drawable.lucchetto,R.drawable.smallbombinstruction,true,ammo5,R.drawable.arma1);
		ammos.add(ammo1);
		AmmoBean ammo24 = new AmmoBean(COLOR_BULLET_HOLE,1000,R.drawable.mitracolor,R.drawable.lucchetto,R.drawable.mitracolorinstruction,true,ammo23,R.drawable.arma5);
		ammos.add(ammo24);
		AmmoBean ammo2 = new AmmoBean(COLOR_BOMB_BIG,4000,R.drawable.bomb2,R.drawable.lucchetto,R.drawable.bigbombinstruction,true,ammo20,R.drawable.arma2);
		ammos.add(ammo2);
		AmmoBean ammo13 = new AmmoBean(MORETIME_X_SECONDS,8000,R.drawable.plusfivesec,R.drawable.lucchetto,R.drawable.timeinstruction,true,ammo12,R.drawable.arma10);
		ammos.add(ammo13);
		AmmoBean ammo21 = new AmmoBean(MAGIC_EDGES,10000,R.drawable.magicedges,R.drawable.lucchetto,R.drawable.magicedgesinstruction,true,ammo22,R.drawable.arma3);
		ammos.add(ammo21);
		AmmoBean ammo7 = new AmmoBean(CLUSTER_BOMB,15000,R.drawable.clusterbomb,R.drawable.lucchetto,R.drawable.clusterbombinstruction,true,ammo4,R.drawable.arma4);
		ammos.add(ammo7);
		AmmoBean ammo19 = new AmmoBean(SHANGHAI_BOMB,20000,R.drawable.shangaibomb,R.drawable.lucchetto,R.drawable.shanghaibombinstruction,true,ammo11,R.drawable.arma9);
		ammos.add(ammo19);
		AmmoBean ammo10 = new AmmoBean(TRIANGLE_BOMB,25000,R.drawable.trianglebomb,R.drawable.lucchetto,R.drawable.tbombinstruction,true,ammo17,R.drawable.arma7);
		ammos.add(ammo10);
		AmmoBean ammo9 = new AmmoBean(X_BOMB,30000,R.drawable.xbomb,R.drawable.lucchetto,R.drawable.xbombinstruction,true,ammo18,R.drawable.arma8);
		ammos.add(ammo9);
		AmmoBean ammo3 = new AmmoBean(JOLLY,50000,R.drawable.jolly,R.drawable.lucchetto,R.drawable.jollyinstruction,true,ammo6,R.drawable.arma6);
		ammos.add(ammo3);
	}
	
	public static AmmoBean thereIsAnUnlockedAmmo(Context context,int currentSkill) {
		int cont = 0;
		for(AmmoBean ammo:ammos) {
			if(!(ammo.isUnlocked(context))) {//controllo quelle non sbloccate di gi
				if(ammo.getEnableCreditTrigger() <= currentSkill) {
					ammo.unlock(context);

            		//Salvo l'ultima arma sbloccata
            		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putInt("last_ammo_unlocked", cont);
                    //Commit the edits!
                    editor.commit();

					return ammo;
				}
			}
			cont++;
		}
		return null;
	}
	
	public static AmmoBean getRandomAmmo() {
		int random = (int)(Math.random()*(unlockedAmmos.size()));
		if(random >= unlockedAmmos.size()) {
			random = 0;
		}
		return unlockedAmmos.get(random);
	}
	
	public static void initializeUnlockedAmmo(Context context) {
		unlockedAmmos.clear();
		if(!ApplicationManager.DEBUG_MODE) {
			for(AmmoBean ammo: ammos) {
				if(ammo.isUnlocked(context)){
					int numberOfProbability = Integer.parseInt(ammo.getNumberOfProbability(context));
					//Aggiungo sia l'arma che l'antiarma
					for(int i=0; i<numberOfProbability; i++) {
						unlockedAmmos.add(ammo);
						unlockedAmmos.add(ammo.getAntiWeapon());
					}
				}
			}
		}else {
			unlockedAmmos.add(ammos.get(9));
			unlockedAmmos.add(ammos.get(9).getAntiWeapon());
		}
	}
}
