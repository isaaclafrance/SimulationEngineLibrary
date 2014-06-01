package com.simulations.test;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public final class TextureManager {
    public static final int COORDS_PER_VERTEX = 2;	
    public static final int VERTEX_STRIDE = COORDS_PER_VERTEX * GLBuffers.BYTES_PER_FLOAT; // 4 bytes per vertex
    public final int[] MAX_TEXTURES = new int[1];
	
    private boolean isUpdated;
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
		GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_IMAGE_UNITS, MAX_TEXTURES, 0);
		
		this.texCoordServerBufferObjectID = new int[3];
		this.textureResourceLocIDs = new int[MAX_TEXTURES[0]];
		this.currentTextureIndex = 0;
		
		isUpdated = false;
		tboUpdateData = new int[2];
		
		for(int i=0; i < mTextureCoordinateData_a.length; i++){
			this.textureCoordinateData.add(mTextureCoordinateData_a[i]);
		}	
	}
	
	public void setTextureHandles(int mProgram){
		mTextureUniformHandle = GLES20.glGetUniformLocation(mProgram, "uTexture");
		texCoordHandle = GLES20.glGetAttribLocation(mProgram, "aTexCoordinate");
	}
	public void setNBindTextureAttribute(){
		//Bind TBO
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, texCoordServerBufferObjectID[0]);
		
		//Set the active texture unit to texture unit 0,1,2..etc.
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0+currentTextureIndex); //TODO: Implement a way to load texture data to texture units other than 0;
		
		//Bind the texture to this unit.
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureDataHandles[currentTextureIndex]);
		
		//Tell the texture uniform sampler to use this texture in the shader by binding to texture unit 0,1,2..etc.
		GLES20.glUniform1f(mTextureUniformHandle, currentTextureIndex);
		
		//Pass in the texture coordinate information
		GLES20.glVertexAttribPointer(texCoordHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, TextureManager.VERTEX_STRIDE, 0);
	
		//Enable handle to texture coordinate data
		GLES20.glEnableVertexAttribArray(texCoordHandle);
		
		//UNBIND TBO
	    GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);	
	}	
	public void loadTexture(final Context context){
		mTextureDataHandles = new int[textureResourceLocIDs.length];
			
		GLES20.glGenTextures(mTextureDataHandles.length, mTextureDataHandles, 0);		
		for(int i=0; i<mTextureDataHandles.length; i++){
			if(mTextureDataHandles[i] != 0){
				final BitmapFactory.Options options = new BitmapFactory.Options();
				options.inScaled = true;  //No pre-scaling
	
				//Read in the resource
				final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), textureResourceLocIDs[i], options);
	
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
			mTextureDataHandles[i] = mTextureDataHandles[i];
		}
	}
	public void releaseTextureData(int textureIndex){
		GLES20.glDeleteTextures(textureIndex, mTextureDataHandles, 0);
	}
	
	public void setClientBuffer(){
		float[] tA = new float[textureCoordinateData.size()];
		for(int i=0; i<textureCoordinateData.size(); i++){
			tA[i] = textureCoordinateData.get(i);
		}
		
		texCoordClientBuffer = GLBuffers.getFloatBuffer(textureCoordinateData.size(), tA);	
	}
	public void setServerBuffer(){
		 GLES20.glGenBuffers(1, texCoordServerBufferObjectID, 0);
		 GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, texCoordServerBufferObjectID[0]);
		 GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, texCoordClientBuffer.capacity()*GLBuffers.BYTES_PER_FLOAT, texCoordClientBuffer, GLES20.GL_DYNAMIC_DRAW);
		 GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}
	
	public void updateClientBuffer(){
		if (isUpdated) {
			float[] tA = new float[textureCoordinateData.size()];
			for (int i = tboUpdateData[0]; i < tA.length; i++) {
				tA[i] = textureCoordinateData.get(i);
			}		
			texCoordClientBuffer.position(tboUpdateData[0]);
			texCoordClientBuffer.put(tA).position(0);
		}	
	}
	public void updateServerBuffer(){
		 if (isUpdated) {
			//Set CBO
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,
					texCoordServerBufferObjectID[0]);
			if (tboUpdateData[0] == -1) {
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
						texCoordClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT,
						texCoordClientBuffer, GLES20.GL_DYNAMIC_DRAW);
			} else if (tboUpdateData[1] == 0) {
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						tboUpdateData[0], 1 * GLBuffers.BYTES_PER_FLOAT,
						texCoordClientBuffer);
			} else if (tboUpdateData[1] > 0) {
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						tboUpdateData[0], texCoordClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, texCoordClientBuffer);
			}
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);

			//Reset tboUpdateData objects
			tboUpdateData[0] = -2; tboUpdateData[1] = 0;
			
			isUpdated = false;
		}
	}
	
	private void clearClientBuffer(){
		texCoordClientBuffer = null;
	}
	public void clearServerBuffer(){
		 GLES20.glDeleteBuffers(1, texCoordServerBufferObjectID, 0);
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
	
	public void addTexCoord(float x, float y){
		textureCoordinateData.add(x);
		textureCoordinateData.add(y);
		
		tboUpdateData[0] = -1;
		tboUpdateData[1]++;
		
		isUpdated = true;
	}
	public void addTexCoords(float[] coords){
		for(int i=0; i<coords.length;i++){
			textureCoordinateData.add(coords[i]);
		}
		
		tboUpdateData[0] = -1;
		tboUpdateData[1]++;
		
		isUpdated = true;
	}
	public void replaceTexCoord(float x, float y, int coordIndex){
		textureCoordinateData.set(coordIndex, x);
		textureCoordinateData.set(coordIndex, y);
		
		//Store smallest index location
		if(coordIndex < tboUpdateData[0]){
			tboUpdateData[0] = coordIndex;	
		}
		tboUpdateData[1]++;
		
		isUpdated = true;
	}

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
	public float[] getTexCoordsArray(){
		float[] tA = new float[textureCoordinateData.size()];
		for(int i=0; i<textureCoordinateData.size(); i++){
			tA[i] = textureCoordinateData.get(i);
		}
		
		return tA;
	}
	
 	public void setTextureResource(int index, int resourceID){
		textureResourceLocIDs[index] = resourceID; 
	}
	public void setCurrentTextureIndex(int index){
		currentTextureIndex = index;
	}
}
