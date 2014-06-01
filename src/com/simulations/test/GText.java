package com.simulations.test;

import java.util.ArrayList;

public interface GText {

	//TODO: Finish implementing GText interface 

	public boolean getUpdatedState();
	
	//
	public ArrayList<Integer> findIndicesOfText(String text);
	
	//
	public boolean hasText(String text);
	
	//
	public int getNumOfChar();
	
	public String getText();	
	public void setText(String text);
	
	public float getFontSize();
	public void setFontSize(float size);
	
	public float getWidth();
	public float getHeight();
	
	public void setPosition(float x, float y, float z);
	public float[] getPosition();
}
