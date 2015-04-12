package com.simulations.test;

import java.util.ArrayList;

public interface GText extends GDrawable {
	public ArrayList<Integer>  findIndicesOfWord(String word, int startIndex);
	
	//
	public boolean hasText(String text);
	public String getText();	
	public void setText(String text);	
	
	//	
	public float getFontSize();
	public void setFontSize(float size);
	
	public float[] getColor();
	public void setColor(float r, float g, float b);
		
	public float getWidth();
	public float getHeight();
	
	public void setPosition(float x, float y, float z);
	public float[] getPosition();
	
	public boolean getUpdateState();
	public void setUpdateState(boolean state);
	
	public int getNumOfChar();	
}
