package com.invenktion.android.whoisthefastestpainter.lite.core;

public class TimeManager {
	private static boolean PAUSED = false;
	private static long startTime = -1;
	private static long totalTime = -1;
	private static long pausingTimeTotal = 0;
	private static long intermediatePauseStart = -1;
	public static long getStartTime() {
		return startTime;
	}
	public static void setStartTime(long startTime) {
		TimeManager.startTime = startTime;
	}
	public static long getTotalTime() {
		return totalTime;
	}
	public static void setTotalTime(long totalTime) {
		if(!ApplicationManager.DEBUG_MODE) {
			TimeManager.totalTime = totalTime;
		}else {
			TimeManager.totalTime = 4000;
		}
	}
	public static long getRemainingTime() {
		if(startTime != -1 && totalTime != -1) {
			long delta = System.currentTimeMillis() - pausingTimeTotal - startTime;
			if(delta > totalTime) return 0;
			else {
				return totalTime - delta;
			}
		}
		return -1;
	}
	public static void setPause(boolean b) {
		if(PAUSED == b) return;
		if(b == true) {
			intermediatePauseStart = System.currentTimeMillis();
		}else {
			long delta = System.currentTimeMillis() - intermediatePauseStart;
			pausingTimeTotal += delta;
		}
		PAUSED = b;
	}
	public static boolean isPaused() {
		return PAUSED;
	}
	public static void resetPausingTime() {
		pausingTimeTotal = 0;
	}
}
