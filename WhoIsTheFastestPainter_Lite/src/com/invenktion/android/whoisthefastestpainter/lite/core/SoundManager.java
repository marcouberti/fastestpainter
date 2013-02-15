package com.invenktion.android.whoisthefastestpainter.lite.core;

import java.util.HashMap;

import com.invenktion.android.whoisthefastestpainter.lite.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.util.Log;
import android.util.SparseIntArray;

/**
 * @author Marco Uberti
 * SoundManager
 */
public class SoundManager {
	//COSTANTI
	public static final String SOUND_DISABLED = "SOUND_DISABLED";
	public static final String SOUND_ENABLED = "SOUND_ENABLED";
	
	//Preferenza utente
	public static boolean SOUND_ON = true;

	//Lista dei miei suoni
	public static final int SOUND_FLUSH = 1;
	public static final int SOUND_PLAF = 2;
	public static final int SOUND_STAMP = 3;
	public static final int SOUND_THREE = 4;
	public static final int SOUND_TWO = 5;
	public static final int SOUND_ONE = 6;
	public static final int SOUND_GO = 7;
	public static final int SOUND_COINS = 8;
	public static final int SOUND_POSITIVE = 9;
	public static final int SOUND_NEGATIVE = 10;
	public static final int SOUND_WHOOSH = 11;

	private static SoundPool soundPool;
	private static SparseIntArray soundPoolMap;
	private static MediaPlayer music;
	
	
	public static void initSounds(Context context) {
		try {
			
			String soundState = SoundManager.getSoundPreference(context);
	        if(SoundManager.SOUND_ENABLED.equalsIgnoreCase(soundState)) {
	        	SOUND_ON = true;
	        }else {
	        	SOUND_ON = false;
	        }
	        
	        /*
	        if(SOUND_ON = true) {
		        //Controllo in che modalità è il dispositivo (silenzioso/vibrazione/normale)
		        AudioManager am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
	
		        switch (am.getRingerMode()) {
		            case AudioManager.RINGER_MODE_SILENT:
		            	SOUND_ON = false;
		            	SoundManager.saveSoundPreference(SoundManager.SOUND_DISABLED, context);
		                break;
		            case AudioManager.RINGER_MODE_VIBRATE:
		            	SOUND_ON = false;
		            	SoundManager.saveSoundPreference(SoundManager.SOUND_DISABLED, context);
		                break;
		            case AudioManager.RINGER_MODE_NORMAL:
		            	SOUND_ON = true;
		                break;
		        }
	        }
	        */
			
			soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
			soundPoolMap = new SparseIntArray();
			soundPoolMap.put(SOUND_FLUSH, soundPool.load(context, R.raw.flush, 1));
			soundPoolMap.put(SOUND_PLAF, soundPool.load(context, R.raw.plaf, 1));
			soundPoolMap.put(SOUND_STAMP, soundPool.load(context, R.raw.stamp, 1));
			soundPoolMap.put(SOUND_THREE, soundPool.load(context, R.raw.three, 1));
			soundPoolMap.put(SOUND_TWO, soundPool.load(context, R.raw.two, 1));
			soundPoolMap.put(SOUND_ONE, soundPool.load(context, R.raw.one, 1));
			soundPoolMap.put(SOUND_GO, soundPool.load(context, R.raw.go, 1));
			soundPoolMap.put(SOUND_COINS, soundPool.load(context, R.raw.coins, 1));
			soundPoolMap.put(SOUND_POSITIVE, soundPool.load(context, R.raw.positive, 1));
			soundPoolMap.put(SOUND_NEGATIVE, soundPool.load(context, R.raw.negative, 1));
			soundPoolMap.put(SOUND_WHOOSH, soundPool.load(context, R.raw.whoosh, 1));
		}catch (Exception e) {
			 e.printStackTrace();
		}
	}
	
	public static void stopSound(int streamID){
		if(SoundManager.SOUND_ON) {
			try {	
				soundPool.stop(streamID);
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static int playSound(int sound, Context context, boolean loop) {
		if(SoundManager.SOUND_ON) {
			try {	
			     AudioManager mgr = (AudioManager)(context.getSystemService(Context.AUDIO_SERVICE));
				 //Come volume uso quello impostato dall'utente sul suo dispositivo come suoneria
				 float streamVolumeCurrent = mgr.getStreamVolume(AudioManager.STREAM_MUSIC);
				 float streamVolumeMax = mgr.getStreamMaxVolume(AudioManager.STREAM_MUSIC); 
				 float volume = streamVolumeCurrent / streamVolumeMax;
				 
				 /* Riproduco il suono */
				 if(loop) {
					 return soundPool.play(soundPoolMap.get(sound), volume, volume, 1, -1, 1f); 
				 }else{
					 return soundPool.play(soundPoolMap.get(sound), volume, volume, 1, 0, 1f); 
				 }
			}catch (Exception e) {
				e.printStackTrace();
			}
		}
		return -1;
	}
	
	public static void finalizeSounds() {
		try {
			if(soundPool != null) {
				soundPool.release();
				soundPool = null;//!Importante lasciare così
			}
			SoundManager.stopBackgroundMusic();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	/*
	public static void muteBackgroundMusic(Context context) {
		//Imposto il volume
		AudioManager audioMan = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		//Abbasso il volume in modo soft
		while(audioMan.getStreamVolume(AudioManager.STREAM_MUSIC) > 0) {
			audioMan.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_LOWER,AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("######### MUTO");
	}
	
    public static void raiseBackgroundMusic(Context context) {
    	if(SoundManager.SOUND_ON) {
	    	//Imposto il volume
			AudioManager audioMan = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
			//int correctVolume = audioMan.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
			//Log.d("MAX VOLUME",""+correctVolume);
			float streamVolumeCurrent = audioMan.getStreamVolume(AudioManager.STREAM_MUSIC);
			 float streamVolumeMax = audioMan.getStreamMaxVolume(AudioManager.STREAM_MUSIC); 
			 float volume = streamVolumeCurrent / streamVolumeMax;
			while(audioMan.getStreamVolume(AudioManager.STREAM_MUSIC) < volume) {
				audioMan.adjustStreamVolume(AudioManager.STREAM_MUSIC,AudioManager.ADJUST_RAISE,0);
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("######### RAISE");
    	}
	}
	*/
	
	public static void playBackgroundMusic(Context context) {
		try {
			if(SoundManager.SOUND_ON) {
				if (music!=null){  
					music.reset();  
					music.release();  
			    }  
				music = MediaPlayer.create(context, R.raw.song);  
				music.setLooping(true);
				music.start();  
				/*
				if(music == null) {
					music = MediaPlayer.create(context, R.raw.orchestrale2);
					music.setLooping(true);
					music.seekTo(0);
					music.start();
				}
				else if (music != null) {
					music.seekTo(0);
					music.start();
				}
				*/
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    private static void stopBackgroundMusic() {
    	try {
	    	if(music != null) {
		    	music.stop();
			    music.release();
			    music = null;//!Importante lasciare così
	    	}
	    }catch (Exception e) {
	    	e.printStackTrace();
		}
	}
 
    public static void pauseBackgroundMusic() {
		try {
	    	if (music != null && music.isPlaying()) {
				music.pause();
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void initVolume(Context context) {
		AudioManager mAudioManager = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        int myVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC) / 2;
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, myVolume, 0);//0 sta per nessun avviso all'utente
	}

	public static boolean isBackgroundMusicPlaying() {
		if(music != null) {
			return music.isPlaying();
		}else return false;
	}

	public static void saveSoundPreference(String soundState,
			Context context) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("soundstate", soundState);
        //Commit the edits!
        editor.commit();
	}

	public static String getSoundPreference(Context context) {
		SharedPreferences settings = context.getSharedPreferences(ApplicationManager.PREFS_NAME, Context.MODE_PRIVATE);
		return settings.getString("soundstate", SoundManager.SOUND_ENABLED);
	}
}
