package com.invenktion.android.whoisthefastestpainter.lite.utils;

public class FilterUtils {

	public static int[][] intorno(int[][] mask) {
		int KERNEL_SIZE = 3;
		int W = mask.length;
		int H = mask[0].length;
		int[][] result = new int[W][H];
		
		for(int w=0; w<W; w++) {
			for(int h=0; h<H; h++){
				if(mask[w][h] == 0) {
					result[w][h] = 0;
				}else {//Controllo se nell'intorno c'è uno 0
					boolean trovato = false;
					for(int i=w-(int)((KERNEL_SIZE)/2); i<w+(int)((KERNEL_SIZE)/2); i++) {
						for(int j=h-(int)((KERNEL_SIZE)/2); j<h+(int)((KERNEL_SIZE)/2); j++) {
							//Controllo se non sono in out of bound
							if(i >= 0 && i<W && j >= 0 && j<H) {
								if(mask[i][j] == 0) {
									trovato = true;
									break;
								}
							}
						}
						if(trovato) break;
					}
					if(trovato) {
						result[w][h] = 0;
					}else {
						result[w][h] = 1;
					}
				}
			}
		}
		
		return result;
	}

}
