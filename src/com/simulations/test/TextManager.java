package com.simulations.test;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

//TODO: Implement Text Manager. Allow for the ability to combine GText objects in sentences and paragraphs with proper formating

public final class TextManager {
	//Fields
	public int MAX_LINE_CHAR_LENGTH = 125; //Maximum number of characters that can be displayed in one line	
	
	public Map<String, GText> gTextMap; 
	private int avgNumOfLine;
	private float width;
	private float height;
	private float[] position; //Position relative to top left corner
		
	//Constructor
	public TextManager(){
		gTextMap = new LinkedHashMap<String, GText>();
	}
	public TextManager(GText[] gTextArray){
		this();
		for(GText gT:gTextArray){
			gTextMap.put(gT.getText(), gT);
		}
	}
	
	//Content Methods
	public void addGText(GText gText){
		gTextMap.put(gText.getText(), gText);
	}
	public void addGText(GText[] gTexts){
		for(int i=0; i<gTexts.length; i++){
			addGText(gTexts[i]);
		}
	}
	public  void removeGText(String textOfGText){
		gTextMap.remove(textOfGText);
	}

	//Formating Methods
	public void format(){ //TODO: Properly positions the GText based on size and locations
		boolean needsUpdating = false;
		int leastNonUpdatedIndex = gTextMap.size();  //Below this index no gText's are updated
		
		for(int i = 0; i<gTextMap.size(); i++){
			needsUpdating = needsUpdating || gTextMap.get(i).getUpdatedState();
			
			if(gTextMap.get(i).getUpdatedState() && i<leastNonUpdatedIndex){
				leastNonUpdatedIndex = i;
			}
		}
		
		if(needsUpdating){
			int numCharProcessed = 0;
			GText[] gTextArray= new GText[gTextMap.size()];
			gTextMap.values().toArray(gTextArray);	
			
			if(leastNonUpdatedIndex == 0){
				gTextArray[0].setPosition(position[0], position[1], position[2]); 
				leastNonUpdatedIndex++;
			}
			
			for(int i=leastNonUpdatedIndex; i<gTextMap.size(); i++){
				//TODO:  finish this portion of Text Manager formating
				if(numCharProcessed != MAX_LINE_CHAR_LENGTH){
					gTextArray[i].setPosition(gTextArray[i-1].getPosition()[0]+gTextArray[i-1].getWidth()/2, 
							  gTextArray[i-1].getPosition()[1]+gTextArray[i-1].getHeight()/2, 
							  gTextArray[i-1].getPosition()[2]); 
				}
				else{
					gTextArray[i].setPosition(position[0], 
											  gTextArray[i].getPosition()[1]-StandardCharacters.squareCharRegionLengthWidth/2, 
											  position[2]);
				}
			}
			calculateAvgNumOfLines();
		}
	}
	
	//Helper methods
	private void calculateAvgNumOfLines(){
		int avgTextLength = 0;	
		for(int i=0; i<gTextMap.size(); i++){
			avgTextLength += gTextMap.keySet().size();
		}
		avgTextLength /= gTextMap.keySet().size();
		
		avgNumOfLine = MAX_LINE_CHAR_LENGTH/avgTextLength;
	}
	
	//Accessors and Mutators
	public String getText(){
		String text = "";
		for(String t:gTextMap.keySet()){
			text += t;
		}	
		return text;
	}
	public float getWidth(){
		return width;
	}
	public float getHeight(){
		return height;
	}
	public float[] getPosition(){
		return position;
	}
	public int getAvgNumOfLines(){
		return avgNumOfLine;
	}
	public ArrayList<GText> getGText(String text){
		ArrayList<GText> gTextArray = new ArrayList<GText>();	
		for(GText gText:gTextMap.values()){
			if(gText.hasText(text)){
				gTextArray.add(gText);
			}
		}	
		return gTextArray;
	}
	public boolean hasText(String text){
		for(GText gText:gTextMap.values()){
			if(gText.hasText(text)){
				return true;
			}
		}	
		
		return false;
	}
	
	public void setPosition(float x, float y){
		this.position[0] = x;
		this.position[1] = y;
	}
	public void setAllToFontSize(int fontSize){
		for(GText gT:gTextMap.values()){
			gT.setFontSize(fontSize);
		}
	}
	public void setMaxWidth(int maxWidth){
		this.MAX_LINE_CHAR_LENGTH = maxWidth;
	}
}
