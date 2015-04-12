package com.simulations.test;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.sax.StartElementListener;

//TODO: Implement Text Manager. Allow for the ability to arrange any group of GText objects into sentences, paragraphs, or any positioned arrangement .

public class TextGroup extends SceneGraphNode implements GText {
	
	//Fields
	public int MAX_LINE_CHAR_LENGTH = 125; //Maximum number of characters that can be displayed in one line	
	public float MAX_LINE_WIDTH = 20.0f;
	
	protected Scene sceneRef;
	
	public static enum ALIGNMENT_STATE{LEFT, RIGHT, CENTER};
	public ALIGNMENT_STATE alignment; 
	
	private ArrayList<GText> gTextArrayList; 
	private String text;
	private int avgNumOfLines;
	private int totNumOfChar;
	private float width;
	private float height;
	private float[] position; //Position relative to top left corner
		
	//Constructor
	public TextGroup(){
		text = "";
		alignment = ALIGNMENT_STATE.LEFT;
		avgNumOfLines = 0;
		totNumOfChar = 0;
		width = 0.0f;
		height = 0.0f;
		position = new float[3];
		gTextArrayList = new ArrayList<GText>();
	}
	public TextGroup(GText[] gTextArray){
		this();
		addGText(gTextArray);
	}
	
	//Content Modifier Methods
	public void addGText(GText gText){
		gTextArrayList.add(gText);
		
		isUpdated = true;
	}
	public void addGText(int index, GText gText){
		gTextArrayList.add(index, gText);
		
		isUpdated = true;
	}
	public void addGText(GText[] gTexts){
		for(int i=0; i<gTexts.length; i++){
			addGText(gTexts[i]);
		}
	}
	public void addGText(int index, GText[] gTexts){
		for(int i=0; i<gTexts.length; i++){
			addGText(index++, gTexts[i]);
		}
	}
 
	public ArrayList<GText> getGText(String text){
		ArrayList<GText> gTextArray = new ArrayList<GText>();	
		for(GText gText:gTextArrayList){
			if(gText.hasText(text)){
				gTextArray.add(gText);
			}
		}	
		return gTextArray;
	}
	
	//Formating Methods
	//TODO: Implement different alignments when formating
	//TODO: Implement formating that take into account MAX_LINE_WIDTH (instead of)/(along with) MAX_LINE_CHAR_LENGTH when deciding when to create a new line
	public void format(){ //Properly positions the GText based on size and locations when a GText is updated
		boolean needsUpdating = false;
		int leastNonUpdatedIndex = gTextArrayList.size();  //For efficiency purposes below this index no gText's are updated
		int lineStartIndex = leastNonUpdatedIndex;
		int numCharProcessed = 0;
		float prevLineWidth = 0.0f;
		float maxCurrentLineHeight = 0.0f; 
		
		GText[] gTextArray = new GText[gTextArrayList.size()];
		gTextArrayList.toArray(gTextArray);				
				
		for(int i = 0; i<gTextArray.length; i++){
			needsUpdating = needsUpdating || gTextArray[i].getUpdateState();
			
			if(gTextArray[i].getUpdateState() && i<leastNonUpdatedIndex){
				leastNonUpdatedIndex = i;
			}
			
			//Animate when update state is not longer relevant
			gTextArray[i].animate();
		}
		
		if(needsUpdating){

			if(leastNonUpdatedIndex == 0){
				gTextArray[leastNonUpdatedIndex].setPosition(position[0], position[1], position[2]); 
				numCharProcessed += gTextArray[leastNonUpdatedIndex++].getNumOfChar();
				lineStartIndex = leastNonUpdatedIndex;				
			}
			
			for(int i=leastNonUpdatedIndex; i<gTextArrayList.size(); i++){
				//Iterates through GText and places them side by side until on a line until the number of GText processed for a single line is 
				//greater 
				if(numCharProcessed != MAX_LINE_CHAR_LENGTH){
					gTextArray[i].setPosition(gTextArray[i-1].getPosition()[0] + gTextArray[i-1].getWidth(), 
							  gTextArray[i-1].getPosition()[1], 
							  gTextArray[i-1].getPosition()[2]); 
						
					//Calculate width of current line by adding up the individual width of GText in line.
					this.width += gTextArray[i].getWidth();
					
					//Determines the largest height of attained by a GText in the this current line. This height will be the 
					if(maxCurrentLineHeight < gTextArray[i].getHeight()){
						maxCurrentLineHeight = gTextArray[i].getHeight();
					}
				}
				else{
					setLine(lineStartIndex, i, maxCurrentLineHeight, gTextArray);
					
					//Set up initial vertical for new line
					gTextArray[i].setPosition(position[0], 
											  gTextArray[i-1].getPosition()[1] - maxCurrentLineHeight, 
											  position[2]);
					lineStartIndex = i;				
					
					//Only stores current line width if it is larger than previous line widths
					if(prevLineWidth < this.width){ 
						prevLineWidth = this.width; 
					}
					this.height += maxCurrentLineHeight; //Tallies up height

					//Reset width and height for next line 
					maxCurrentLineHeight = 0;					
					this.width = 0;  
				}
				numCharProcessed += gTextArray[i].getNumOfChar();
			}
			
			updateProperties(maxCurrentLineHeight, prevLineWidth);
		}
	}
	//Sets position of a group of gText within a particular index range that form a line. 
	private void setLine(int startIndex, int endIndex, float maxCurrentLineHeight, GText[] gTextArray){
		for(int i=++startIndex; i<endIndex; i++  ){
			gTextArray[i].setPosition(gTextArray[i-1].getPosition()[0] + gTextArray[i-1].getWidth(), 
					  gTextArray[i-1].getPosition()[1], 
					  gTextArray[i-1].getPosition()[2]); 	
		}
	}
	//Updates group text, totNumOfChar, avgNumOfLine, height, and width
	private void updateProperties(float maxCurrentLineHeight, float prevLineWidth){
		height = maxCurrentLineHeight;		
		width = prevLineWidth;
		totNumOfChar = 0;
		text = "";
		
		for(GText gText:gTextArrayList){
			totNumOfChar += gText.getText().length();
			this.text += gText.getText();
		}	
		avgNumOfLines = totNumOfChar/MAX_LINE_CHAR_LENGTH;
	}
	
	/////
	@Override
	public ArrayList<Integer> findIndicesOfWord(String word, int startIndex) {
		// TODO: find Indices Of Word
		return null;
	}
	
	public int getNumOfGTextObjects(){
		return gTextArrayList.size();
	}
	public void setMaxWidth(int maxWidth){
		this.MAX_LINE_CHAR_LENGTH = maxWidth;
		
		isUpdated = true;
	}	
	public int getAvgNumOfLines(){
		return avgNumOfLines;
	}
	public void addLine(GText gTexts){ //TODO: Appends an extra line of GText's after the current last line
		
	}
	
	//GDrawable implemented methods. 
	public void updateShadersNTextures(Context context){
		for(GText gText:gTextArrayList){
			gText.updateShadersNTextures(context);
		}
	}
	public void initBuffers(){
		for(GText gText:gTextArrayList){
			gText.initBuffers();
		}
	}	
	public void clearAll(){
		gTextArrayList.clear();
	}	
	public void draw(float[] mVMatrix, float[] mPMatrix){
		for(GText gText:gTextArrayList){
			gText.draw(mVMatrix, mPMatrix);
		}
	}
	public void animate(){		
		format(); //Only formats when necessary
		
		if(isUpdated){
			for(GDrawable gText:gTextArrayList){
				((Transformable)gText).setSceneTransformMatrix(worldTransformationMatrix);
			}			
		}
		
		super.animate();		
	}
	
	public boolean getTextureState(){
		return false;
	}
	public void setVertexShaderLocID(int locID){
	}
	public void setFragmentShaderLocID(int locID){
	}
	
	public boolean getLightState(){
		boolean state = true;
		
		for(GText gText:gTextArrayList){
			if(!gText.getLightState()){
				state = false;
			}
			break;
		}
		
		return state;
	}
	public boolean getBillboardState(){
		boolean state = true;
		
		for(GText gText:gTextArrayList){
			if(!gText.getBillboardState()){
				state = false;
			}
			break;
		}
		
		return state;
	}
	
	public void setSceneRef(Scene scene){
		sceneRef = scene;
		for(GText gText:gTextArrayList){
			gText.setSceneRef(scene);
		}
	}
	public void setLightState(boolean state){
		for(GText gText:gTextArrayList){
			gText.setLightState(state);
		}
	}
	public void setBillboardState(boolean state){
		for(GText gText:gTextArrayList){
			gText.setBillboardState(state);
		}
	}
	
	@Override
	public void setPosition(float x, float y, float z) {
		setTranslation(x, y, z);
	}
	@Override
	public float[] getPosition() {
		return getTranslation();
	}	
	
	//GText implemented methods
	public boolean getUpdateState(){
		boolean state = true;
		
		for(GText gText:gTextArrayList){
			if(gText.getUpdateState()){
				state = true;;
			}
			break;
		}
		
		return (state || isUpdated);
	}
	public String getText(){
		return text;
	}
	@Override
	public int getNumOfChar() {
		int numOfChar = 0;
		for(GText gText:gTextArrayList){
			numOfChar += gText.getNumOfChar();
		}
		return numOfChar;
	}
	public float getFontSize(){
		float totalFontSize = 0;
		int count = 0;
		
		for(GText gText:gTextArrayList){
			totalFontSize += gText.getFontSize();
			count++;
		}
		
		return (count == 0)?0.0f:totalFontSize/count;
	}
	public float getWidth(){
		//TODO: Calculate width of TextGroup in 'updateProperties' method
		return width;
	}
	public float getHeight(){
		//TODO: Calculate height of TextGroup in 'updateProperties' method
		return height;
	}
	
	public boolean hasText(String text){
		for(GText gText:gTextArrayList){
			if(gText.hasText(text)){
				return true;
			}
		}	
		
		return false;
	}
	
	public void setText(String text) { //Splits text into substrings places each substring into a separate GText object
		int interval = text.length()/getNumOfGTextObjects();
		int currentPosition = 0;
		
		for(GText gText:gTextArrayList){
			if((currentPosition+interval) <= text.length()){
				gText.setText(text.substring(currentPosition, currentPosition+interval));	
				currentPosition += interval;
			}
			else{
				gText.setText("");
			}
		}
		this.text = text;
	}
	public void setFontSize(float fontSize){
		for(GText gT:gTextArrayList){
			gT.setFontSize(fontSize);
		}
	}
	@Override
	public void setTransparency(float value) {
		for(GText gText:gTextArrayList){
			gText.setTransparency(value);
		}
	}
	@Override
	public void setColor(float r, float g, float b) {
		for(GText gText:gTextArrayList){
			gText.setColor(r, g, b);
		}	
	}
	
	///Unimplemented methods
	@Override
	public float[] getColor() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public float getTransparency() {
		// TODO Auto-generated method stub
		return 1.0f;
	}

}
