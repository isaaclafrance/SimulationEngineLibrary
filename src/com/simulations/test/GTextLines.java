package com.simulations.test;

import java.util.ArrayList;

import android.opengl.Matrix;

public final class GTextLines extends GT_Line implements GText{	
	private String text;
	private final int MAX_TEXT_LENGTH = 125; //Specifies the number characters allowed in a text for efficiency purposes
	
	private final float charSep = StandardCharacters.charSparation;
	private final float whiteSpaceSep = StandardCharacters.whiteSpaceSeparation;
	private final float[] charVerts = new float[StandardCharacters.maxVertices()*3];
	private final float[] transCharVerts = new float[StandardCharacters.maxVertices()*3];	
	
	private int prevTotNumOfLines; 
	
	private float charactersRead;	
	private float[] vertex;
	private float[] transMatrix;
	
	private boolean isUpdated;

	////
	public GTextLines(String text, float fontSize, float[] pos, float[] color, boolean isBillboard){
		super(pos[0],pos[1], pos[2], color[0], color[1], color[2], 1.0f, new float[]{}, new short[]{});
		
		prevTotNumOfLines = 0;
		
		//
		charactersRead = 0;
		vertex = new float[4];
		transMatrix = new float[16];
		//
		
		this.text = text;
		this.setBillboardState(isBillboard);
		this.setFontSize(fontSize);
		parseText();
	}
		
	////
	public ArrayList<Integer> findIndicesOfText(String word){
		return findIndicesOfWord(word, 0);
	}
	public ArrayList<Integer>  findIndicesOfWord(String word, int startIndex){
		//Use recursion to find an array of indices		
		ArrayList<Integer> intArray = new ArrayList<Integer>();
		int nextFoundIndex = text.indexOf(word, startIndex);
		
		if(startIndex == 0 && nextFoundIndex == -1){
			return intArray;
		}
		else if(startIndex != text.length()-1){
			intArray.add(nextFoundIndex);
			intArray.addAll(findIndicesOfWord(word, nextFoundIndex+1));
		}
		
		return intArray;
	}
	public void replaceWords(String word, String replacement){
		text.replaceAll(""+word.toString()+"", replacement);
		parseText();
	}	
	public void removeWords(String word){
		text.replaceAll(""+word.toString()+"", "");
	    parseText();
	}
	
	private void parseText(){
		Matrix.setIdentityM(transMatrix, 0);
		
		//removeAllLines();
		//TODO: Allow the addition of vertices to existing space in vertex array. Only increase size of array when the require # of total character vertices is less than the total numer of vertices.
		
		int numOfLinesProcessed = 0;
		
		for(int index = 0; index<text.length(); index++){
			if(text.charAt(index) != ' '){
				//Translate characters so they are positioned adjacent to one another
				StandardCharacters.loadCharacterVertex(text.charAt(index), charVerts);
				
				Matrix.translateM(transMatrix, 0, (float)charSep*charactersRead, 0.0f, 0.0f);				
				
				for(int i=0; i<=charVerts.length-3; i+=3){
					vertex[0] = charVerts[i];
					vertex[1] = charVerts[i+1];
					vertex[2] = charVerts[i+2];
					vertex[3] = 1.0f;
					Matrix.multiplyMV(vertex, 0, transMatrix, 0, vertex, 0);
					
					transCharVerts[i] = vertex[0];
					transCharVerts[i+1] = vertex[1];
					transCharVerts[i+2] = vertex[2];
				}
				
				//Add each translated character line by line into vertex array				
				for(int i=0; i<transCharVerts.length-1; i+=6){
					if(numOfLinesProcessed < prevTotNumOfLines){
						replaceLineVertex_noOffset(transCharVerts[i], transCharVerts[i+1], transCharVerts[i+2],
												   transCharVerts[i+3], transCharVerts[i+4], transCharVerts[i+5], (i/3));
					}
					else
					{
						addLine_noOffset(transCharVerts[i], transCharVerts[i+1], transCharVerts[i+2],
								         transCharVerts[i+3], transCharVerts[i+4], transCharVerts[i+5]);
					}
					numOfLinesProcessed++;
				}
				
				Matrix.setIdentityM(transMatrix, 0);
				charactersRead++;				
			}
			else{
				charactersRead += whiteSpaceSep/charSep;
			}
		}
			
		//Trim line array if necessary
		for(int i = numOfLinesProcessed; i<prevTotNumOfLines; i++){
			removeLine_noOffset(i);
		}

		prevTotNumOfLines = numOfLinesProcessed;		
		
		isUpdated = true;
	}
	////
	@Override
	public boolean hasText(String text){
		return this.text.equals(text);
	}
	
	////
	@Override
	public float getWidth(){
		return scale[0]*((float)(text.length()*(StandardCharacters.squareCharRegionLengthWidth) + (float)(text.length()-1)*StandardCharacters.charSparation));
	}
	@Override
	public float getHeight(){
		return scale[1]*StandardCharacters.squareCharRegionLengthWidth;
	}
	
	@Override
	public String getText(){
		return text;
	}
	@Override
	public void setText(String text){
		this.text = text;
		this.text.trim();
		this.text = (this.text.length() < MAX_TEXT_LENGTH) ? this.text:this.text.substring(0, MAX_TEXT_LENGTH-1);
		
		parseText();
	}
	
	@Override
	public void setFontSize(float size){
		scale[0] = size;
		scale[1] = size;
	}
	@Override
	public float getFontSize(){
		return scale[0]; //It does not matter which scale is used since all scale dimensions(x and y) are suppose to be identical 
	}
	
	@Override
	public void setPosition(float x, float y, float z){
		super.setPosition(x, y, z);
	}
	
	@Override 
	public float[] getPosition(){
		return super.getPosition();
	}
	
	@Override
	public boolean getUpdatedState() {
		return isUpdated;
	}

	@Override
	public int getNumOfChar() {
		return text.length();
	}
}
