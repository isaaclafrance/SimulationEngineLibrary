package com.simulations.test;

import android.content.Context;

public interface GDrawable extends Animated{	 
	public void updateShadersNTextures(Context context);
	public void initBuffers();
	
	public void clearAll();
	
	public void draw(float[] mVMatrix, float[] mPMatrix);

	public void setSceneRef(Scene scene);
	public void setLightState(boolean state);
	public void setVertexShaderLocID(int locID);
	public void setFragmentShaderLocID(int locID);
	public void setBillboardState(boolean state);
	public void setPosition(float x, float y, float z);	
	public void setTransparency(float value);
	
	public float[] getPosition();
	
	public Quaternion getOrientation();
	public boolean getBillboardState();
	public boolean getTextureState();
	public boolean getLightState();
	public float getTransparency();
}
