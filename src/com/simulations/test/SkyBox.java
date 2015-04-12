package com.simulations.test;

//TODO: Implement skybox. Remember to disable depth testing. Checkout: http://iphonedevelopment.blogspot.com/2009/05/opengl-es-from-ground-up-part-6_25.html

public class SkyBox extends GT_Sprite {
	public SkyBox(int[] textureResourceIDs){
		super(0.0f, 0.0f, 0.0f, textureResourceIDs);
	}
}
