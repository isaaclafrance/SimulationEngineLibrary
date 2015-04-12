package com.simulations.test;

//TODO: Manages the display of a 2D image
public class GT_Sprite extends GDrawableTransformable {
	
	public GT_Sprite(float x, float y, float z, int[] textureResourceIDs) {
		super(x, y, z, textureResourceIDs, new float[]{0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f},
				new float[]{0.0f, 3.0f, 3.0f, 3.0f, 0.0f, 0.0f, 3.0f, 0.0f}, new short[]{1, 2, 3, 4});
	}
	
	@Override
	public void draw(float[] mVMatrix, float[] mPMatrix){
		super.draw(mVMatrix, mPMatrix);
		
		//TODO: Use triangle strip
	}
}
