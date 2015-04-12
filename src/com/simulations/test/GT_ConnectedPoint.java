package com.simulations.test;

import android.opengl.GLES20;

public class GT_ConnectedPoint extends GT_Point {
	public float lineThickness;
	public boolean areLinesDrawn;
	private final ColorManager cManager_Lines;
	
	////
	public GT_ConnectedPoint(float x, float y, float z, float r, float g,
			float b, float transparency, float pointSize, float lineWidth) {
		super(x, y, z, r, g, b, transparency, pointSize);
		
		this.lineThickness = lineWidth;
		this.areLinesDrawn = true;
		
		this.cManager_Lines = new ColorManager(r, g, b, transparency, getNumOfPoints());
	}
	public GT_ConnectedPoint(float x, float y, float z, float r, float g,
			float b, float transparency, float pointSize, float lineWidth, float[] pointsData) {
	
		super(x, y, z, r, g, b, transparency, pointSize, pointsData);
		
		this.lineThickness = lineWidth;
		this.areLinesDrawn = true;
		
		this.cManager_Lines = new ColorManager(r, g, b, transparency, getNumOfPoints());	
	}

	////
	@Override
	public void addPoint_noOffset(float x, float y, float z){
		super.addPoint_noOffset(x, y, z);
		
		cManager_Lines.updateNumOfColorVertices(cManager.getNumColorVertex());
	}
	@Override
	public void addPoint_withOffset(float x, float y, float z){
		super.addPoint_withOffset(x, y, z);
		
		cManager_Lines.updateNumOfColorVertices(cManager.getNumColorVertex());
	}
	@Override
	public void removePoint_noOffset(int locationIndex){
		super.removePoint_noOffset(locationIndex);
		
		cManager_Lines.updateNumOfColorVertices(cManager.getNumColorVertex());
	}
	@Override
	public void removePoint_withOffset(int locationIndex){
		super.removePoint_withOffset(locationIndex);
		
		cManager_Lines.updateNumOfColorVertices(cManager.getNumColorVertex());
	}
	
	////	
	@Override
	public void initBuffers(){
		super.initBuffers();
		
		cManager_Lines.setServerBuffer();
	}
	@Override
	protected void updateBuffers(){
		super.updateBuffers();
		
		cManager_Lines.updateClientBuffer();
		cManager_Lines.updateServerBuffer();
	}
	
	@Override
	public void clearAll(){
		super.clearAll();
		
		cManager_Lines.clearAll();
	}
	
	////
	@Override
	public void draw(float[] mVMatrix, float[] mPMatrix){
		super.draw(mVMatrix, mPMatrix);
		
		if(areLinesDrawn){
			// Reenable position vertex array
			GLES20.glEnableVertexAttribArray(vManager.get_mVertexPositionHandle());
			
			//Disable point color array 
			GLES20.glDisableVertexAttribArray(cManager.get_mColorArrayHandle());
			
			// Set different color for lines
			cManager_Lines.setColorAttribute_Server(mProgram);
			
			// Draw the lines using indices
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vManager.getIndexServerBufferObjectID()[0]);
			GLES20.glLineWidth(lineThickness);
	        GLES20.glDrawElements(GLES20.GL_LINE_STRIP, vManager.getNumOfDrawnVertices(), GLES20.GL_UNSIGNED_SHORT, 0);

	        // Disable vertex array
	        GLES20.glDisableVertexAttribArray(vManager.get_mVertexPositionHandle());	
	        
	        //UNBIND IBO
	        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
		}
	}
	
	////
	@Override
	public void setColor(float r, float g, float b){
		super.setColor(r, g, b);
		
		setLineColor(r, g, b);
	}
	public void setLineColor(float r, float g, float b){
		cManager_Lines.setColorAll(r, g, b);
	}
	public void setPointColor(float r, float g, float b){
		super.setColor(r, g, b);
	}

	public float[] getLineColor(){
		return cManager_Lines.getColor();
	}
}
