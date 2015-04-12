package com.simulations.test;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public abstract class GDrawablePhysicalObject extends PhysicalObject implements GDrawable
{ 
	 protected int mProgram;
	 protected Scene scene;
	
     public final VertexManager vManager;
     public final ColorManager cManager; 
     public final TextureManager tManager;
 	 
     private int vertexShadLocID;
     private int fragmentShadLocID;
 
	 protected boolean isLighted;
	 protected boolean isTextured;
	 protected boolean isShaderChanged;
	 
	 protected boolean isBillboard; //If true, then the texture rotates such that it is always facing the camera
	 
	 public GDrawablePhysicalObject(float x, float y, float z, float r, float g, float b, float transparency, 
			 				 float[] shapeCoords, short[] indices, float mass, Float[][] boundaryPlanes, BoundingManager.BoundingBoxTypes bbType){
		 super(x,y,z, mass, boundaryPlanes, bbType);
		 
		 isLighted = false;			 
		 
		 vManager = new VertexManager(shapeCoords, indices, isLighted);
		 cManager = new ColorManager(r, g, b, transparency, vManager.getNumOfDrawnVertices());
		 tManager = null;
	 
		 vertexShadLocID = -1;
		 fragmentShadLocID = -1;
		 
		 setInitialPositionOffsetNBounding();	 
		 
		 mMVMatrix = new float[16];
		 mMVPMatrix = new float[16];
		 
		 isTextured = false;
		 isShaderChanged = true;
		 
		 isBillboard = false;
	 }
	 public GDrawablePhysicalObject(float x, float y, float z, int[] textureResourceIDs, float[] textureCoords,
			 				 float[] shapeCoords, short[] indices, float mass, Float[][] boundaryPlanes, BoundingManager.BoundingBoxTypes bbType){
		 super(x,y,z, mass, boundaryPlanes, bbType);
		  
		 isLighted = false;			 
		 
		 vManager = new VertexManager(shapeCoords, indices, isLighted);
		 tManager = new TextureManager(textureCoords);
		 for(int i=0; i<((textureResourceIDs.length<tManager.MAX_TEXTURES[0])?textureResourceIDs.length:tManager.MAX_TEXTURES[0]); i++){
			 tManager.setTextureResource(i, textureResourceIDs[i]);
		 }
		 cManager = null;
	 
		 vertexShadLocID = -1;
		 fragmentShadLocID = -1;
		 
		 setInitialPositionOffsetNBounding();	 
		 
		 mMVMatrix = new float[16];
		 mMVPMatrix = new float[16];
		 
		 isTextured = true;
		 isShaderChanged = true;	
		 
		 isBillboard = false;
	 }
	 
	 private void setInitialPositionOffsetNBounding(){
		 //Determines initial position before any transformations.
		 //Does this by finding the average position using the maximum and minimum value
		 //for each axis i.e. x,y,z. Also sets bounding dimensions based on size.
		 float[] offset = vManager.calculatePositionOffset(0);
		 Matrix.translateM(mTransOffsetMatrix, 0, offset[0], offset[1], offset[2]);
		 
		 bManager.setBounds(vManager.getShapeCoordsArray());
	 }	 
		
     @Override
     public void initBuffers(){
    	 vManager.setServerBuffers();
    	 
    	 if(!isTextured){
    		 cManager.setServerBuffer();
    	 }
    	 else{
    		 tManager.setServerBuffer();
    	 }
     }
     
     protected void updateBuffers(){
    	 vManager.updateClientBuffers();
    	 vManager.updateServerBuffers();
    	 
    	 if(!isTextured){
    		 cManager.updateClientBuffer();
    		 cManager.updateServerBuffer();
    	 }
    	 else{
    		 tManager.updateClientBuffer();
    		 tManager.updateServerBuffer();
    	 }
     }
     @Override
     public void updateShadersNTextures(Context context){
    	 if(isShaderChanged){
			 //Set shaders
			 mProgram = GLShaders.setShaderProgram(context, vertexShadLocID, fragmentShadLocID, isTextured, isLighted, scene.getLightManagerRef().getLightQuality());
	     
			 //Load texture
			 if(isTextured){
				 tManager.loadTexture(context);
			 }
			 
			 isShaderChanged = false;
    	 }
     }
     
     @Override
     public void clearAll(){
    	 vManager.clearAll();
    	 cManager.clearAll();
    	 tManager.clearAll();
     }
     
	 protected void setupForDraw(){
	  // Add program to OpenGL environment
		 GLES20.glUseProgram(mProgram);
		 
	  // Update client and server buffer objects if necessary
		 updateBuffers();
		 
	  //**VERTEX POSITION**//
		 vManager.setVertexPositionAttribute_Server(mProgram);
		 		 
	  //**COLOR or TEXTURE**//
		 if(isTextured){
			 tManager.setTextureHandles(mProgram);
			 tManager.setNBindTextureAttribute(mProgram);
		 }
		 else{
			 cManager.setColorAttribute_Server(mProgram);
		 }

	  //**LIGHTING**//
		 if(isLighted){
			scene.getLightManagerRef().setUniforms(mProgram);
			  
		    //**NORMAL**//
			vManager.setNormalAttribute_Server(mProgram);		
			  
			mMVMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVMatrix");
			GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVMatrix, 0);		  
		 }
		 
	  //**TRANSFORMATION
		 //Get handle to shape's transformation matrix
		 mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
		 
		 // Apply the projection and view transformation	 
		 GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);
	  }
	 @Override
	 public void draw(float[] mVMatrix, float[] mPMatrix){
		 resetMatrcies();
		 setMVPTransfMatrix(mVMatrix, mPMatrix);		 
		 setupForDraw();
		 
		 //OpenGL Primitive Drawing Instruction 
		 //....
		 
		 // //Disable handle to the vertices
	   	 // GLES20.glDisableVertexAttribArray(mPositionHandle);
		 
	     // //UNBIND IBO
	     // GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
	 }
	 	 
	 @Override
	 public void animate(){
		super.animate();
	 }	 
	 
	 @Override
	 public void setSceneRef(Scene scene){
		 this.scene = scene;
	 }
	 @Override
	 public void setLightState(boolean state){
		 isLighted = state;
		 vManager.setNormalizedState(state);
	 }
	 @Override
	 public void setVertexShaderLocID(int locID){
		 vertexShadLocID = locID;
		 isShaderChanged = true;
	 }
	 @Override
	 public void setFragmentShaderLocID(int locID){
		 fragmentShadLocID = locID;
		 isShaderChanged = true;
	 }
	 @Override
	 public void setBillboardState(boolean state){
		 isBillboard = state;
	 }	 
	 @Override
	 public void setTransparency(float value){
		 if(!isTextured){
			 cManager.setTransparencyAll(value);			 
		 }
	 }
	 public void setColor(float r, float g, float b){
		 cManager.setColorAll(r, g, b);
	 }

	 @Override
	 public float getTransparency(){
		 float val;
		 
		 if(!isTextured){
			val = cManager.getTransparency();
		 }
		 else{
			val = 1.0f;
		 }

		 return val;
	 }
	 @Override
	 public boolean getBillboardState(){
		 return isBillboard;
	 }
	 @Override
	 public boolean getLightState(){
		 return isLighted;
	 }
	 @Override
	 public boolean getTextureState(){
		 return isTextured;
	 }
	 public float[] getColor(){
		 return cManager.getColor();
	 }
}
