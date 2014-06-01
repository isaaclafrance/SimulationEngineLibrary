package com.simulations.test;

import java.util.ArrayList;

//TODO: Implement this class to use texture of characters instead of lines

public class GTextTexture implements GText{
	public boolean isUpdated;
	
	////
	public GTextTexture(){
		isUpdated = false;
	}
	
	////
	
	////
	@Override
	public ArrayList<Integer> findIndicesOfText(String text) {
		// TODO Auto-generated method stub
		return null;
	}
	
	////
	public boolean hasText(String text){
		
	}
	
	////
	@Override
	public int getNumOfChar() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setText(String text) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPosition(float x, float y, float z) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public float[] getPosition() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public float getFontSize() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void setFontSize(float size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public float getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public float getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean getUpdatedState() {
		return isUpdated;
	}
}
