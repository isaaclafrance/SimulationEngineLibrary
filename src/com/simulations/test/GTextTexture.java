package com.simulations.test;

import java.util.ArrayList;

//TODO: Implement this class to use texture of characters instead of lines

public class GTextTexture extends GT_Sprite implements GText{
	public String text;
	public boolean isUpdated;
	
	////
	public GTextTexture(String text, float fontSize, float[] pos){
		super(pos[0], pos[1], pos[2], new int[1]);
		
		this.text = text;
		isUpdated = false;
	}
	
	////
	
	////
	@Override
	public ArrayList<Integer> findIndicesOfWord(String word, int startIndex) {
		// TODO Auto-generated method stub
		return null;
	}
	
	////
	public boolean hasText(String text){
		return false;
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
	public boolean getUpdateState() {
		return isUpdated;
	}

	@Override
	public float[] getColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setColor(float r, float g, float b) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setUpdateState(boolean state) {
		// TODO Auto-generated method stub
		
	}
}
