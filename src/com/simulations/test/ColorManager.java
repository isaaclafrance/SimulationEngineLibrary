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

    //[0] = smallest index changed (-1->reallocation, -2->do nothing); [1] = # of vertex changes; [2] = largest index changed; [3] = 0->do not reallocate, 1->reallocate; [4] = capacity of server buffer
    private int[] cboUpdateData;
	
	private FloatBuffer colorClientBuffer;
	private  int[] colorServerBufferObjectID;
	
    private int mColorArrayHandle; //Used in shader	 
	
    //Constructors
    public ColorManager(float r, float g, float b, float transparency, int numColorVertex){
    	this.colorServerBufferObjectID = new int[3];
    	
    	cboUpdateData = new int[]{0, 0, numColorVertex, 1, 0};
 
    	isUpdated = true;
    	allVerticesIdentical = false;
    	
    	this.color = new float[3];
    	this.color[0] = r;
    	this.color[1] = g;
    	this.color[2] = b;
    	this.transparency = transparency; 

 		color_array = new ArrayList<Float>();
    	
    	//Create color array
    	setColor(r, g, b, transparency, numColorVertex);
    }
    
	private void setColor(float r, float g, float b, float transparency, int numColorVertex){
		if (numColorVertex != 0) {
			if (!allVerticesIdentical) {
				for (int i = 0; i < ((numColorVertex < color_array.size() / 4) ? numColorVertex
						: color_array.size() / 4); i++) {
					color_array.set(i * 4, r);
					color_array.set(i * 4 + 1, g);
					color_array.set(i * 4 + 2, b);
					color_array.set(i * 4 + 3, transparency);
				}
				allVerticesIdentical = true;

				if (color_array.size() / 4 != numColorVertex) {
					setColor(r, g, b, transparency, numColorVertex);
				}
			} else if (color_array.size() / 4 > numColorVertex) {
				for (int i = (color_array.size() - 1) / 4; i >= numColorVertex - 1; i--) {
					color_array.remove(i * 4);
					color_array.remove(i * 4 + 1);
					color_array.remove(i * 4 + 2);
					color_array.remove(i * 4 + 3);
				}
			} else if (color_array.size() / 4 < numColorVertex) {
				for (int i = color_array.size() / 4; i < numColorVertex; i++) {
					color_array.add(color[0]);
					color_array.add(color[1]);
					color_array.add(color[2]);
					color_array.add(transparency);
				}
			}
			cboUpdateData[3] = 1;
			cboUpdateData[2] = color_array.size() - 1;
			cboUpdateData[1]++;
			cboUpdateData[0] = 0;
			isUpdated = true;
		}
	}
	
	public void setServerBuffer(){
		 GLES20.glGenBuffers(1, colorServerBufferObjectID, 0);
		 
	     cboUpdateData[0] = cboUpdateData[1] = cboUpdateData[4] = 0;
	     cboUpdateData[2] = (color_array.size() == 0)?0:color_array.size()-1;
	     cboUpdateData[3] = 1;
	     
	     isUpdated = true;
	}
	
	public void updateClientBuffer(){
		if (isUpdated) {
			if(cboUpdateData[3] == 1){
				colorClientBuffer = GLBuffers.getFloatBuffer(color_array);
			}else{
				colorClientBuffer.position(cboUpdateData[0]);				
				for (int i = cboUpdateData[0]; i < cboUpdateData[2]; i++) {
					colorClientBuffer.put(color_array.get(i));
				}
				colorClientBuffer.position(0);
			}	
		}	
	}
	public void updateServerBuffer(){
		 if (isUpdated) {
			//Set CBO
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,
					colorServerBufferObjectID[0]);
			if (cboUpdateData[3] == 1 && colorClientBuffer.capacity() > cboUpdateData[4]) {
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
						colorClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT,
						colorClientBuffer, GLES20.GL_DYNAMIC_DRAW);
			} else if(cboUpdateData[3] == 1 && colorClientBuffer.capacity() < cboUpdateData[4]){
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						0*GLBuffers.BYTES_PER_FLOAT, colorClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, colorClientBuffer);
				
				//Invalidate unused portion of server buffer
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						cboUpdateData[2]*GLBuffers.BYTES_PER_FLOAT, cboUpdateData[4]-colorClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, null);	
			}else{
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						cboUpdateData[0]*GLBuffers.BYTES_PER_FLOAT, colorClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, colorClientBuffer);				
			}
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

			//Store current sizes of server buffer objects
			cboUpdateData[4] = colorClientBuffer.capacity();
			
			resetBOUpdateDataObjects();
		}		
	}
	
	private void clearClientBuffer(){
		colorClientBuffer = null;
	}
	public void clearServerBuffer(){
		 GLES20.glDeleteBuffers(1, colorServerBufferObjectID, 0);
		 resetBOUpdateDataObjects();
	}
	public void clearAll(){
		color_array.clear();
		
		clearClientBuffer();
		clearServerBuffer();
	}
	
	private void resetBOUpdateDataObjects(){
		//Reset ""boUpdateData objects
		cboUpdateData[0] = cboUpdateData[1] = cboUpdateData[2] = cboUpdateData[3] = 0;
		
		isUpdated = false;
	}
	
	public void setColorAttribute_Server(int mProgram){
		 //BIND CBO
		 GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, colorServerBufferObjectID[0]);		 
		 
		 // get handle to vertex shader's vPosition member
		 mColorArrayHandle = GLES20.glGetAttribLocation(mProgram, "aColor");

		 // Prepare the triangle coordinate data
		 GLES20.glVertexAttribPointer(mColorArrayHandle, COORDS_PER_VERTEX,
				 						GLES20.GL_FLOAT, false,
				 						ColorManager.vertexStride, 0); 
		 
		 // Enable a handle to the triangle vertices		 
		 GLES20.glEnableVertexAttribArray(mColorArrayHandle);		 
		 
		 //UNBIND CBO
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);	
	}
	
	public void setColorAll(float r, float g, float b){
		allVerticesIdentical = false;
		setColor(r,g,b, this.transparency,  getNumColorVertex());
	}
	public void setTransparencyAll(float transparency){
		setColor(getColor()[0], getColor()[1], getColor()[2], transparency, getNumColorVertex());
	}
	
	public void addColorVertex(float r, float g, float b, float transparency){
		color_array.add(r);
		color_array.add(g);
		color_array.add(b);
		color_array.add(transparency);
		
		cboUpdateData[3] = 1; //Set cbo to allocate larger memory
		cboUpdateData[2] = color_array.size()-1;
		cboUpdateData[0] = 0;
		cboUpdateData[1]++;
		
		postArrayDataChangeUpdate(0);
	}
	public void addColorVertices(float[] colorVertices){
		for(int i=0; i<colorVertices.length; i++){
			color_array.add(colorVertices[i]);
		}
		
		cboUpdateData[3] = 1; //Set cbo to allocate larger memory
		cboUpdateData[2] = color_array.size()-1;
		cboUpdateData[0] = 0;
		cboUpdateData[1]++;
		
		postArrayDataChangeUpdate(0);
	}
	public void replaceVertexColor(float r, float g, float b, int vertIndex){
		color_array.set(vertIndex, r);
		color_array.set(vertIndex+1, g);
		color_array.set(vertIndex+2, b);
		color_array.set(vertIndex+3, transparency);
		
		postArrayDataChangeUpdate(vertIndex);
	}
	public void replaceTransparencyVertex(float transparency, int vertLocationIndex){
		color_array.set(vertLocationIndex*4 + 3, transparency);
		postArrayDataChangeUpdate(vertLocationIndex);
	}
	
	public void updateNumOfColorVertices(int numColorVertex){
		setColor(getColor()[0], getColor()[1], getColor()[2], getTransparency(), numColorVertex);
	}	
	
	private void postArrayDataChangeUpdate(int changeLocationIndex){
		if(cboUpdateData[3] != 1){
			//Store smallest index location
			if(changeLocationIndex < cboUpdateData[0]){
				cboUpdateData[0] = changeLocationIndex;	
			}
			//Store largest index location
			if(changeLocationIndex > cboUpdateData[2]){
				cboUpdateData[2] = changeLocationIndex;	
			}
			cboUpdateData[1]++;
		}
		
		isUpdated = true;
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

	public int get_mColorArrayHandle(){
		return mColorArrayHandle;
	}
}
