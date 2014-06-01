package com.simulations.test;

import java.nio.FloatBuffer;
import java.util.ArrayList;

import android.opengl.GLES20;

public final class ColorManager {
	//Fields
    public static final int COORDS_PER_VERTEX = 4;	
    public static final int vertexStride = COORDS_PER_VERTEX * GLBuffers.BYTES_PER_FLOAT; // 4 bytes per vertex
	
    private boolean isUpdated;
    private boolean allVerticesIdentical;
    
	private float[] color;  
	private float transparency;	
	private ArrayList<Float> color_array;

    //[0] = smallest index changed (-1->reallocation, -2->do nothing); [1] = # of vertex changes
    private int[] cboUpdateData;
	
	private FloatBuffer colorClientBuffer;
	private  int[] colorServerBufferObjectID;
	
    private int mColorHandle; //Used in shader	 
	
    //Constructors
    public ColorManager(float r, float g, float b, float transparency, int numColorIndex){
    	this.colorServerBufferObjectID = new int[3];
    	
    	cboUpdateData = new int[2];
    	
    	isUpdated = false;
    	allVerticesIdentical = false;
    	
    	this.color = new float[3];
    	this.color[0] = r;
    	this.color[1] = g;
    	this.color[2] = b;
    	this.transparency = transparency; 

 		color_array = new ArrayList<Float>(numColorIndex*4);   	
    	
    	//Create color array
    	setColor(r, g, b, transparency, numColorIndex);
    }
    
	private void setColor(float r, float g, float b, float transparency, int numColorVertex){
		if(!allVerticesIdentical){
			for(int i=0; i< ((numColorVertex<color_array.size())?numColorVertex:color_array.size()); i++){
				color_array.set(i*4, r);
				color_array.set(i*4+1, g);
				color_array.set(i*4+2, b);
				color_array.set(i*4+3, transparency);
			}
			
			allVerticesIdentical = true;
			cboUpdateData[0] = 0;
			cboUpdateData[1]++;
			
			if(color_array.size() != numColorVertex){
				setColor(r, g, b, transparency, numColorVertex);
			}
		}
		else if(color_array.size() < numColorVertex){
			for(int i=color_array.size()-1; i>=numColorVertex; i--){
				color_array.remove(i*4);
				color_array.remove(i*4+1);
				color_array.remove(i*4+2);
				color_array.remove(i*4+3);
			}
			
			cboUpdateData[0] = -1;
		}
		else if(color_array.size() > numColorVertex){
			for(int i=color_array.size(); i<numColorVertex; i++){
				color_array.add(color[0]);
				color_array.add(color[1]);
				color_array.add(color[2]);
				color_array.add(transparency);
			}
			
			cboUpdateData[0] = -1;
		}
	}
	
	public void setClientBuffer(){
		float[] cA = new float[color_array.size()];
		for(int i=0; i<color_array.size(); i++){
			cA[i] = color_array.get(i);
		}
		
		colorClientBuffer = GLBuffers.getFloatBuffer(color_array.size(), cA);	
	}
	public void setServerBuffer(){
		 GLES20.glGenBuffers(1, colorServerBufferObjectID, 0);
		 GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, colorServerBufferObjectID[0]);
		 GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, colorClientBuffer.capacity()*GLBuffers.BYTES_PER_FLOAT, colorClientBuffer, GLES20.GL_DYNAMIC_DRAW);
		 GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}
	
	public void updateClientBuffer(){
		if (isUpdated) {
			float[] cA = new float[color_array.size()];
			for (int i = cboUpdateData[0]; i < cA.length; i++) {
				cA[i] = color_array.get(i);
			}		
			colorClientBuffer.position(cboUpdateData[0]);
			colorClientBuffer.put(cA).position(0);
		}		
	}
	public void updateServerBuffer(){
		 if (isUpdated) {
			//Set CBO
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,
					colorServerBufferObjectID[0]);
			if (cboUpdateData[0] == -1) {
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
						colorClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT,
						colorClientBuffer, GLES20.GL_DYNAMIC_DRAW);
			} else if (cboUpdateData[1] == 0) {
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						cboUpdateData[0], 1 * GLBuffers.BYTES_PER_FLOAT,
						colorClientBuffer);
			} else if (cboUpdateData[1] > 0) {
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						cboUpdateData[0], colorClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, colorClientBuffer);
			}
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

			//Reset cboUpdateData objects
			cboUpdateData[0] = -2; cboUpdateData[1] = 0;
			
			isUpdated = false;
		}		
	}
	
	private void clearClientBuffer(){
		colorClientBuffer = null;
	}
	public void clearServerBuffer(){
		 GLES20.glDeleteBuffers(1, colorServerBufferObjectID, 0);
	}

	public void clearAll(){
		color_array.clear();
		
		clearClientBuffer();
		clearServerBuffer();
	}
	
	public void setColorAttribute_Server(int mProgram){
		 //BIND CBO
		 GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, colorServerBufferObjectID[0]);		 
		 
		 // get handle to vertex shader's vPosition member
		 mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");

		 // Prepare the triangle coordinate data
		 GLES20.glVertexAttribPointer(mColorHandle, COORDS_PER_VERTEX,
				 						GLES20.GL_FLOAT, false,
				 						ColorManager.vertexStride, 0); 
		 
		 // Enable a handle to the triangle vertices		 
		 GLES20.glEnableVertexAttribArray(mColorHandle);		 
		 
		 //UNBIND CBO
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);	
	}
	public void setColorAttribute_Client(int mProgram){
		 // get handle to vertex shader's vPosition member
		 mColorHandle = GLES20.glGetAttribLocation(mProgram, "aColor");

		 // Prepare the triangle coordinate data
		 GLES20.glVertexAttribPointer(mColorHandle, COORDS_PER_VERTEX,
				 						GLES20.GL_FLOAT, false,
				 						ColorManager.vertexStride, colorClientBuffer); 
		 
		 // Enable a handle to the triangle vertices		 
		 GLES20.glEnableVertexAttribArray(mColorHandle);
	}
	
	public void setColorAll(float r, float g, float b){
		setColor(r,g,b, this.transparency,  getNumColorVertex());
	}
	public void setTransparencyAll(float transparency){
		setColor(getColor()[0], getColor()[1], getColor()[2], transparency, getNumColorVertex());
	}
	
	public void setColorVertex(float r, float g, float b, int vertLocationIndex){
		color_array.set(vertLocationIndex*4, r);
		color_array.set(vertLocationIndex*4 + 1, g);
		color_array.set(vertLocationIndex*4 + 2, b);
	}
	public void setTransparencyVertex(float transparency, int vertLocationIndex){
		color_array.set(vertLocationIndex*4 + 3, transparency);
	}
	
	public float[] getColor(){
		return color;
	}
	public float[] getColorArray(){
		float[] cA = new float[color_array.size()];
		for(int i=0; i<color_array.size(); i++){
			cA[i] = color_array.get(i);
		}
		
		return cA;
	}
	public float getTransparency(){
		return transparency;
	}
	public int getNumColorVertex(){
		return color_array.size()/COORDS_PER_VERTEX;
	}
	
	public void addColorVertex(float r, float g, float b, float transparency){
		color_array.add(r);
		color_array.add(g);
		color_array.add(b);
		color_array.add(transparency);
		
		cboUpdateData[0] = -1;
		cboUpdateData[1]++;
		
		isUpdated = true;
	}
	public void addColorVertices(float[] colorVertices){
		for(int i=0; i<colorVertices.length; i++){
			color_array.add(colorVertices[i]);
		}
		
		isUpdated = true;

		//Set cbo to allocate larger memory
		cboUpdateData[0] = -1;
		cboUpdateData[1]++;
	}
	public void changeVertexColor(float r, float g, float b, float transparency, int vertIndex){
		color_array.set(vertIndex, r);
		color_array.set(vertIndex+1, g);
		color_array.set(vertIndex+2, b);
		color_array.set(vertIndex+3, transparency);
		
		//Store smallest index location
		if(vertIndex < cboUpdateData[0]){
			cboUpdateData[0] = vertIndex;	
		}
		cboUpdateData[1]++;
		
		isUpdated = true;
	}
	
	public void updateNumOfColorVertices(int numColorVertex){
		setColor(getColor()[0], getColor()[1], getColor()[2], getTransparency(), numColorVertex);
	}
}
