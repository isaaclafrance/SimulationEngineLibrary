package com.simulations.test;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public final class TextureManager {
    public static final int COORDS_PER_VERTEX = 2;	
    public static final int VERTEX_STRIDE = COORDS_PER_VERTEX * GLBuffers.BYTES_PER_FLOAT; // 4 bytes per vertex
    public final int[] MAX_TEXTURES = new int[]{1};
	
    private boolean isUpdated;
    //[0] = smallest index changed (-1->reallocation, -2->do nothing); [1] = # of vertex changes; [2] = largest index changed; [3] = 0->do not reallocate, 1->reallocate; [4] = capacity of server buffer
    private int[] tboUpdateData;
    
    //Holds textures location information
    private int[] textureResourceLocIDs;
    
    //Holds index of current texture location. Therefore multiple textures can be loaded
    public int currentTextureIndex;
    
	//Stores model texture coordinates a float array
	private ArrayList<Float> textureCoordinateData; //final? 
    
	//This will be used to pass in model texture coordinate information
	private int texCoordHandle;
	
	//Store our model texture coordinate data on client and server side.
	private FloatBuffer texCoordClientBuffer; 
	private int[] texCoordServerBufferObjectID;
	
	//This will be used to pass in the texture.
	private int mTextureUniformHandle;
	
	//This a handle to our texture data.
	private int[] mTextureDataHandles;

	public TextureManager(float[] mTextureCoordinateData_a){
		//GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS, MAX_TEXTURES, 0);
		
		this.texCoordServerBufferObjectID = new int[3];
		this.textureResourceLocIDs = new int[MAX_TEXTURES[0]];
		this.currentTextureIndex = 0;
		
		isUpdated = true;
		tboUpdateData = new int[]{0, 0, mTextureCoordinateData_a.length, 1, 0};

		textureCoordinateData = new ArrayList<Float>();
		for(int i=0; i < mTextureCoordinateData_a.length; i++){
			textureCoordinateData.add(mTextureCoordinateData_a[i]);
		}	
	}
	
	////
	public void setTextureHandles(int mProgram){
		texCoordHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoordinate");
	}
	public void setNBindTextureAttribute(int mProgram){
		//Set the active texture unit to texture unit 0,1,2..etc.
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0+currentTextureIndex); //TODO: Implement a way to load texture data to texture units other than 0;					
		//Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandles[currentTextureIndex]);
		
		//Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0,1,2..etc.
		mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "uTexture");
		GLES20.glUniform1i(mTextureUniformHandle, currentTextureIndex); //TODO: float or integer
		
		//Bind TBO
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, texCoordServerBufferObjectID[0]);		
		
		//Enable handle to texture coordinate data
		GLES20.glEnableVertexAttribArray(texCoordHandle);	
		//Pass in the texture coordinate information
		GLES20.glVertexAttribPointer(texCoordHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, TextureManager.VERTEX_STRIDE, 0);
		
		//UNBIND TBO
	    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);	
	}	
	public void loadTexture(Context context){
		mTextureDataHandles = new int[textureResourceLocIDs.length];
			
		GLES20.glGenTextures(mTextureDataHandles.length, mTextureDataHandles, 0);		
		for(int i=0; i<mTextureDataHandles.length; i++){
			if(mTextureDataHandles[i] != 0){
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inScaled = false;  //No pre-scaling
	
				//Read in the resource
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), textureResourceLocIDs[i], options);
	
				//Bind to the texture in OpenGL
				GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandles[i]);
	
				//Set filtering
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
				
				//Load the bitmap into the bound texture.
				GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
				
				//Recycle the bitmap, since its data has been loaded into OpenGL.
				bitmap.recycle();
			}
		}
	}
	public void releaseTextureData(int textureIndex){
		GLES20.glDeleteTextures(textureIndex, mTextureDataHandles, 0);
	}
	
	////
	public void setServerBuffer(){
		 GLES20.glGenBuffers(1, texCoordServerBufferObjectID, 0);
		 
	     tboUpdateData[0] = tboUpdateData[1] = tboUpdateData[4] = 0;
	     tboUpdateData[2] = (textureCoordinateData.size() == 0)? 0:textureCoordinateData.size()-1;
	     tboUpdateData[3] = 1;
	     
	     isUpdated = true;
	}
	
	public void updateClientBuffer(){
		if (isUpdated) {
			if(tboUpdateData[3] == 1){
				texCoordClientBuffer = GLBuffers.getFloatBuffer(textureCoordinateData);
			}else{
				texCoordClientBuffer.position(tboUpdateData[0]);				
				for (int i = tboUpdateData[0]; i < tboUpdateData[2]; i++) {
					texCoordClientBuffer.put(textureCoordinateData.get(i));
				}
				texCoordClientBuffer.position(0);
			}	
		}
	}
	public void updateServerBuffer(){
		 if (isUpdated) {
			//Set TBO
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,
					texCoordServerBufferObjectID[0]);
			if (tboUpdateData[3] == 1 && texCoordClientBuffer.capacity() > tboUpdateData[4]) {
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
						texCoordClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT,
						texCoordClientBuffer, GLES20.GL_DYNAMIC_DRAW);
			} else if(tboUpdateData[3] == 1 && texCoordClientBuffer.capacity() < tboUpdateData[4]){
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						0*GLBuffers.BYTES_PER_FLOAT, texCoordClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, texCoordClientBuffer);
				
				//Invalidate unused portion of server buffer
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						tboUpdateData[2]*GLBuffers.BYTES_PER_FLOAT, tboUpdateData[4]-texCoordClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, null);	
			}else{
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						tboUpdateData[0]*GLBuffers.BYTES_PER_FLOAT, texCoordClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, texCoordClientBuffer);				
			}
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

			//Store current sizes of server buffer objects
			tboUpdateData[4] = texCoordClientBuffer.capacity();
			
			resetBOUpdateDataObjects();
		}
	}
	
	private void clearClientBuffer(){
		texCoordClientBuffer = null;
	}
	public void clearServerBuffer(){
		 GLES20.glDeleteBuffers(1, texCoordServerBufferObjectID, 0);
		 resetBOUpdateDataObjects();
	}
	public void clearAll(){
		textureCoordinateData.clear();
		
		clearClientBuffer();
		clearServerBuffer();
		
		//Unbind all texture data
		for(int i=0; i<getNumOfTextures(); i++){
			releaseTextureData(i);
		}
	}
	
	private void resetBOUpdateDataObjects(){
		//Reset ""boUpdateData objects
		tboUpdateData[0] = tboUpdateData[1] = tboUpdateData[2] = tboUpdateData[3] = 0;
		
		isUpdated = false;
	}
	
	////
	public void addTexCoord(float x, float y){
		textureCoordinateData.add(x);
		textureCoordinateData.add(y);
		
		tboUpdateData[3] = 1; //Set tbo to allocate larger memory
		tboUpdateData[2] = textureCoordinateData.size()-1;
		tboUpdateData[0] = 0;
		tboUpdateData[1]++;
		
		postArrayDataChangeUpdate(0);
	}
	public void addTexCoords(float[] coords){
		for(int i=0; i<coords.length;i++){
			textureCoordinateData.add(coords[i]);
		}
		
		tboUpdateData[3] = 1; //Set tbo to allocate larger memory
		tboUpdateData[2] = textureCoordinateData.size()-1;
		tboUpdateData[0] = 0;
		tboUpdateData[1]++;
		
		postArrayDataChangeUpdate(0);
	}
	public void replaceTexCoord(float x, float y, int coordIndex){
		textureCoordinateData.set(coordIndex, x);
		textureCoordinateData.set(coordIndex, y);
		
		postArrayDataChangeUpdate(0);
	}

	private void postArrayDataChangeUpdate(int changeLocationIndex){
		if(tboUpdateData[3] != 1){
			//Store smallest index location
			if(changeLocationIndex < tboUpdateData[0]){
				tboUpdateData[0] = changeLocationIndex;	
			}
			//Store largest index location
			if(changeLocationIndex > tboUpdateData[2]){
				tboUpdateData[2] = changeLocationIndex;	
			}
			tboUpdateData[1]++;
		}
		
		isUpdated = true;
	}
	
	////
	public int getNumOfTextures(){
		return textureResourceLocIDs.length;
	}
	public int getNumOfTexCoords(){
		return textureCoordinateData.size()/COORDS_PER_VERTEX;
	}
	public int getCurrentTextureIndex(){
		return currentTextureIndex;
	}
	public ArrayList<Float> getTexCoords(){
		return textureCoordinateData;
	}
	
	////
 	public void setTextureResource(int index, int resourceID){
		textureResourceLocIDs[index] = resourceID; 
	}
	public void setCurrentTextureIndex(int index){
		currentTextureIndex = index;
	}
}
