package com.andyridge.minutetimerlite.lib;

public class Lib implements Constants
{

	/**
	 * Works out how big the screen is, and hence which xml layout needs to be loaded.
	 * 
	 * @param width
	 * @param height
	 * @return
	 */
	public static RES getResourceSize(int width, int height)
	{
		int[] resolution; 
		if(width < height)
		{
			resolution = new int[]{ width, height };
		}
		else
		{
			resolution = new int[]{ height, width };
		}
		
		if((resolution[0] == RES_QVGA[0] && resolution[1] == RES_QVGA[1]) || 
		   (resolution[0] == RES_WQVGA[0] && resolution[1] == RES_WQVGA[1]))
		{
			return RES.QVGA;
		}
		else if((resolution[0] == RES_HVGA[0] && resolution[1] == RES_HVGA[1]) || 
				(resolution[0] == RES_NHD[0]) && resolution[1] == RES_NHD[1])
		{
			return RES.HVGA;
		}
		else
		{
			return RES.WVGA;
		}
	}
}
