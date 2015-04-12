package com.simulations.test;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class GT_Line extends GDrawableTransformable{ // implements VertexChangable {
	public enum LineDrawMode{GL_LINES, GL_LINE_STRIP, GL_LINE_LOOP}
	public float lineThickness;
	public LineDrawMode lineDrawMode;
	
	public GT_Line(float x, float y, float z, float r, float g, float b, float transparency,
			float[] linesData, short[] indices) {
		super(x, y, z, r, g, b, transparency, linesData, indices);
		
		vManager.setNormalizedState(false);
		lineThickness = 2.0f;
		
		lineDrawMode = LineDrawMode.GL_LINES;
	}
	public GT_Line(float x, float y, float z, float r, float g, float b, float transparency){
		this(x,y,z,r,g,b,transparency,new float[0], new short[0]);
	}
	
	////	
	public void addLines_noOffset(float[] linesData){
		for(int i=0; i<linesData.length-3; i+=3){
			vManager.addVertex_noOffset(linesData[i], linesData[i+1], linesData[i+2]);
			vManager.addIndex_noOffset((short) (vManager.getNumOfTotalVertices()-1), vManager.getNumOfDrawnVertices());
		}
		//vManager.removeObsoleteVertices();
		cManager.updateNumOfColorVertices(cManager.getNumColorVertex()+linesData.length/3);
	}
	public void addLines_withOffset(float[] linesData){
		int startIndex = vManager.getNumOfDrawnVertices()-1;
		
		addLines_noOffset(linesData);

		float[]offsetArray = vManager.calculatePositionOffset(startIndex);
		
		Matrix.setIdentityM(mTransOffsetMatrix, 0);
		Matrix.translateM(mTransOffsetMatrix, 0, offsetArray[0], offsetArray[1], offsetArray[2]);	
	}
	public void addLine_noOffset(float x1, float y1, float z1, float x2, float y2, float z2){
		vManager.addVertex_noOffset(x1, y1, z1);
		vManager.addVertex_noOffset(x2, y2, z2);	
		
		vManager.addIndex_noOffset((short) (vManager.getNumOfTotalVertices()-2), vManager.getNumOfDrawnVertices());
		vManager.addIndex_noOffset((short) (vManager.getNumOfTotalVertices()-1), vManager.getNumOfDrawnVertices());
		
		//vManager.removeObsoleteVertices();

		cManager.updateNumOfColorVertices(cManager.getNumColorVertex()+2);
	}
	public void addLine_withOffset(float x1, float y1, float z1, float x2, float y2, float z2){
		vManager.addVertex_noOffset(x1, y1, z1); 
		vManager.addVertex_noOffset(x2, y2, z2);
	
		vManager.addIndex_noOffset((short) (vManager.getNumOfTotalVertices()-2), vManager.getNumOfDrawnVertices());
		float[]offsetArray = vManager.addIndex_withOffset((short) (vManager.getNumOfTotalVertices()-1), vManager.getNumOfDrawnVertices());
		
		Matrix.setIdentityM(mTransOffsetMatrix, 0);
		Matrix.translateM(mTransOffsetMatrix, 0, offsetArray[0], offsetArray[1], offsetArray[2]);		
		
		//vManager.removeObsoleteVertices();
		
		cManager.updateNumOfColorVertices(cManager.getNumColorVertex()+2);
	}
	public void addLine_noOffset(float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b){
		addLine_noOffset(x1, y1, z1, x2, y2, z2);
		cManager.replaceVertexColor(r, g, b, cManager.getNumColorVertex()-1);
	}
	public void addLine_withOffset(float x1, float y1, float z1, float x2, float y2, float z2, float r, float g, float b){
		addLine_withOffset(x1, y1, z1, x2, y2, z2);
		cManager.replaceVertexColor(r, g, b, cManager.getNumColorVertex()-1);
	}
	public void addLine_noOffset(float[] vertexOne, float[] vertexTwo, float[] colorOne, float[] colorTwo){
		addLine_noOffset(vertexOne[0], vertexOne[1], vertexOne[2], vertexTwo[0], vertexTwo[1], vertexTwo[2]);
		cManager.replaceVertexColor(colorOne[0], colorOne[1], colorOne[2], cManager.getNumColorVertex()-2);
		cManager.replaceVertexColor(colorTwo[0], colorTwo[1], colorTwo[2], cManager.getNumColorVertex()-1);
	}
	public void addLine_withOffset(float[] vertexOne, float[] vertexTwo, float[] colorOne, float[] colorTwo){
		addLine_withOffset(vertexOne[0], vertexOne[1], vertexOne[2], vertexTwo[0], vertexTwo[1], vertexTwo[2]);
		cManager.replaceVertexColor(colorOne[0], colorOne[1], colorOne[2], cManager.getNumColorVertex()-2);
		cManager.replaceVertexColor(colorTwo[0], colorTwo[1], colorTwo[2], cManager.getNumColorVertex()-1);
	}
	
	public void removeLine_noOffset(int lineIndex){
		//First converts index into line location in vertex array. Then, iterates through line vertices.
		for(int i=0; i<2; i++){
			vManager.removeIndex_noOffset(lineIndex*2);
		}
		
		//vManager.removeObsoleteVertices();		
		
		cManager.updateNumOfColorVertices(cManager.getNumColorVertex()-2);
	}
	public void removeLine_withOffset(int lineIndex){
		vManager.removeIndex_noOffset(lineIndex*2);
		float[] offsetArray = vManager.removeIndex_withOffset(lineIndex*2);
			
		Matrix.setIdentityM(mTransOffsetMatrix, 0);
		Matrix.translateM(mTransOffsetMatrix, 0, offsetArray[0], offsetArray[1], offsetArray[2]);
		
		//vManager.removeObsoleteVertices();	
		
		cManager.updateNumOfColorVertices(cManager.getNumColorVertex()-2);
	}
	public void removeAllLines(){
		vManager.clearAll();
	}
	
	public void replaceLineVertices_noOffset(float x1, float y1, float z1, float x2, float y2, float z2, int lineLocationIndex){
		vManager.replaceIndex_noOffset(x1, y1, z1, lineLocationIndex);
		vManager.replaceIndex_noOffset(x2, y2, z2, lineLocationIndex++);			
	}
	public void replaceLineVertices_withOffset(float x1, float y1, float z1, float x2, float y2, float z2, int lineLocationIndex){
			vManager.replaceIndex_noOffset(x1, y1, z1, lineLocationIndex);
			float[] offsetArray = vManager.replaceIndex_withOffset(x2, y2, z2, lineLocationIndex++);	
			
			Matrix.setIdentityM(mTransOffsetMatrix, 0);
			Matrix.translateM(mTransOffsetMatrix, 0, offsetArray[0], offsetArray[1], offsetArray[2]);
	}
	public void replaceLineColor(float r, float g, float b, int lineLocationIndex){
		cManager.replaceVertexColor(r, g, b, lineLocationIndex);
		cManager.replaceVertexColor(r, g, b, lineLocationIndex++);
	}
	public void replaceLineColor(float r1, float g1, float b1, float r2, float g2, float b2, int lineLocationIndex){
		cManager.replaceVertexColor(r1, g1, b1, lineLocationIndex);
		cManager.replaceVertexColor(r2, g2, b2, lineLocationIndex++);
	}
		
 	public int getNumOfLines(){
		return vManager.getNumOfDrawnVertices()/6;
	}
	public LineDrawMode getLineDrawMode(){
		return lineDrawMode;
	}
	public void setLineDrawMode(LineDrawMode lineDrawMode){
		this.lineDrawMode = lineDrawMode;
	}
	
	////
	@Override
	public void draw(float[] mVMatrix, float[] mPMatrix){
		super.draw(mVMatrix, mPMatrix);

		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vManager.getIndexServerBufferObjectID()[0]);
		GLES20.glLineWidth(lineThickness);
		
		switch(lineDrawMode){
		case GL_LINES:
		    GLES20.glDrawElements(GLES20.GL_LINES, vManager.getNumOfDrawnVertices(), GLES20.GL_UNSIGNED_SHORT, 0);
		    break;
		case GL_LINE_LOOP:
		    GLES20.glDrawElements(GLES20.GL_LINE_LOOP, vManager.getNumOfDrawnVertices(), GLES20.GL_UNSIGNED_SHORT, 0);
			break;
		case GL_LINE_STRIP:
		    GLES20.glDrawElements(GLES20.GL_LINE_STRIP, vManager.getNumOfDrawnVertices(), GLES20.GL_UNSIGNED_SHORT, 0);
			break;
		default:
			break;
		}
	
        // Disable vertex array
        GLES20.glDisableVertexAttribArray(vManager.get_mVertexPositionHandle());	
        
        //UNBIND IBO
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
		
	}
}
