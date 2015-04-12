package com.simulations.test;

import java.util.ArrayList;

import android.opengl.GLES20;
import android.opengl.Matrix;

public final class GTextLines extends GT_Line implements GText{	
	private final int MAX_TEXT_LENGTH = 125; //Specifies the number characters allowed in a text for efficiency purposes
	
	private String text;	
	private int prevTotNumOfLines;	
	private boolean isUpdated;	
		
	private float charSep = StandardCharacters.charSeparation; 
	private float charStandardLengthWidth = StandardCharacters.squareCharRegionLengthWidth;
	private float whiteSpaceSep = StandardCharacters.whiteSpaceSeparation;

	//Used to store temporary data when parsing text
	private final float[] transCharVerts = new float[StandardCharacters.maxVertices()*3];
	private final float[] vertex;
	private final float[] transMatrix;
	
	////
	public GTextLines(String text, float fontSize){
		super(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, new float[]{}, new short[]{});	
		
		prevTotNumOfLines = 0;
		isUpdated = true;
		
		//
		vertex = new float[4];
		transMatrix = new float[16];
		//
				
		this.text = text;
		this.setBillboardState(false);
		this.setFontSize(fontSize);	
	}
	public GTextLines(String text, float fontSize, float[] pos){
		this(text, fontSize);
		setPosition(pos[0], pos[1], pos[2]);
	}
	public GTextLines(String text, float fontSize, float[] pos, float[] color, boolean isBillboard){
		this(text, fontSize, pos);
		
		setColor(color[0], color[1], color[2]);
		setBillboardState(isBillboard);
	}
		
	////
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
	public ArrayList<Integer> findIndicesOfText(String word){
		return findIndicesOfWord(word, 0);
	}	
	public void replaceWords(String word, String replacement){
		text.replaceAll(""+word.toString()+"", replacement);
		
		isUpdated = true;
	}	
	public void removeWords(String word){
		text.replaceAll(word.toString(), "");
	    
		isUpdated = true;
	}
	
	private void parseText(){ //Allow the addition of vertices to existing space in vertex array. Only increase size of array when the require # of total character vertices is less than the total number of vertices.
		Matrix.setIdentityM(transMatrix, 0);	
		int numOfDrawnLines = 0;
		int charVertLength = 0;
		float charactersRead = 0.0f;
		
		//Extract extract and position character vertices 
		for(int index = 0; index<text.length(); index++){
			if(text.charAt(index) == ' '){
				charactersRead += (whiteSpaceSep + charSep)/(charStandardLengthWidth + charSep);
			}	
			else{
				//Translate characters so they are positioned adjacent to one another based on alignment
				charVertLength = StandardCharacters.loadCharacterVertex(text.charAt(index), transCharVerts);
				
				Matrix.translateM(transMatrix, 0, charStandardLengthWidth*0.5f, charStandardLengthWidth*0.5f, 0.0f);
				Matrix.translateM(transMatrix, 0, (float)(charStandardLengthWidth + charSep)*charactersRead , 0.0f, 0.0f);
				
				for(int i=0; i<=charVertLength-3; i+=3){
					vertex[0] = transCharVerts[i];
					vertex[1] = transCharVerts[i+1];
					vertex[2] = transCharVerts[i+2];
					vertex[3] = 1.0f;
					Matrix.multiplyMV(vertex, 0, transMatrix, 0, vertex, 0);
					
					transCharVerts[i] = vertex[0];
					transCharVerts[i+1] = vertex[1];
					transCharVerts[i+2] = vertex[2];
				}
				
				//Add each translated character line by line into vertex array. In order to prevent unnecessary memory allocations, previously allocated memory is replaced with new vertex information when possible.		
				for(int i=0; i<charVertLength-1; i+=6){
					if(numOfDrawnLines < prevTotNumOfLines){
						replaceLineVertices_noOffset(transCharVerts[i], transCharVerts[i+1], transCharVerts[i+2],
												   transCharVerts[i+3], transCharVerts[i+4], transCharVerts[i+5], (i/3));
					}
					else
					{
						addLine_noOffset(transCharVerts[i], transCharVerts[i+1], transCharVerts[i+2],
								   transCharVerts[i+3], transCharVerts[i+4], transCharVerts[i+5]);
					}
					numOfDrawnLines++;
				}
				
				Matrix.setIdentityM(transMatrix, 0);
				charactersRead++;					
			}
		}
			
		//If necessary, remove excess line vertex indices data from the previous parsing.
		for(int i = numOfDrawnLines; i<prevTotNumOfLines; i++){
			removeLine_noOffset(i);
		}
		prevTotNumOfLines = numOfDrawnLines;		
	}
	
	////
	@Override
	public void animate(){
		if(isUpdated){
			parseText();
			isUpdated = false;
		}
		super.animate();		
	}
	
	////
	@Override
	public boolean hasText(String text){
		return text.contains(text);
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
		
		isUpdated = true;
	}
	
	////
	@Override
	public float getWidth(){
		float width;
		
		if(charSep == whiteSpaceSep){
			width = scale[0] * (text.length() - 1) * (charStandardLengthWidth + charSep);
		}else{
			float textLengthWithoutWhiteSpace = (text.split(" ")).length;
			width = scale[0] * (charStandardLengthWidth + charSep)*( (text.charAt(0) != ' ')?(textLengthWithoutWhiteSpace - 1):(textLengthWithoutWhiteSpace) );
			width += scale[0] * (whiteSpaceSep + charSep) * ( (text.charAt(0) == ' ')?(text.length() - textLengthWithoutWhiteSpace - 1):(text.length() - textLengthWithoutWhiteSpace) );
		}
		
		return width;
	}
	@Override
	public float getHeight(){
		return scale[1]*StandardCharacters.squareCharRegionLengthWidth;
	}	
	
	@Override
	public float getFontSize(){
		return scale[0]; //It does not matter which scale is used since all scale dimensions(x and y) are suppose to be identical 
	}
	@Override
	public void setFontSize(float size){
		scale[0] = size;
		scale[1] = size;
		
		isUpdated = true;
	}	
		
	@Override
 	public boolean getUpdateState() {
		return isUpdated;
	}
	@Override
	public void setUpdateState(boolean state){
		isUpdated = state;
	}
	
	@Override
	public int getNumOfChar() {
		return text.length();
	}
}
