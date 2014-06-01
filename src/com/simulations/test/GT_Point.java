package com.simulations.test;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.Matrix;

public class GT_Point extends GDrawableTransformable{ // implements VertexChangable{
	public int mPointSizeHandle;
	public final float pointSize;
	
	public GT_Point(float x, float y, float z, float r, float g, float b, float transparency,
			float pointSize) {
		super(x, y, z, r, g, b, transparency, new float[]{0.0f, 0.0f, 0.0f}, new short[]{0});
		
		this.pointSize = pointSize;		
		vManager.normalized = false;
	}
	public GT_Point(float x, float y, float z, float r, float g, float b, float transparency,
			float pointSize, float[] pointsData) {
		super(x, y, z, r, g, b, transparency, pointsData, new short[]{0});
		
		this.pointSize = pointSize;
		
		setupInitialPointVerticesNIndices(pointsData);
		
		vManager.normalized = false;
	}
	
	private void setupInitialPointVerticesNIndices(float[] pointsData){
		//Create index array.
		int pointsNum = 0;
		for(int i=0; i<pointsData.length; i+=3){
			pointsNum++;
		}
		short[] indices = new short[pointsNum];
		for(short i=0; i<pointsNum; i++){
			indices[i] = i;
		}	
		
		//Add vertices and indices to vertex manager
		vManager.addVertices_noOffset(pointsData);
		float[]offsetArray = vManager.addIndices_withOffset(indices, 0);
		
		Matrix.setIdentityM(mTransOffsetMatrix, 0);
		Matrix.translateM(mTransOffsetMatrix, 0, offsetArray[0], offsetArray[1], offsetArray[2]);
	}
	
	@Override
	public void updateShadersNTextures(Context context){
		String vertexShaderCode = "uniform mat4 uMVPMatrix;"+
								  "uniform float uPointSize;"+
								  "attribute vec4 aPosition;"+
								  "attribute vec4 aColor;"+
								  "varying vec4 vColor;"+
								  "void main(){"+
								  "     vColor = aColor;"+
								  "		gl_Position = uMVPMatrix*aPosition;"+
								  "		gl_PointSize = uPointSize;}";
		String fragmentShaderCode = "precision mediump float;"+
								  	"varying vec4 vColor;"+
								  	"void main(){"+
								  	"	gl_FragColor = vColor;}";
		
		// Prepare OpenGL shaders program
        int vertexShader = GLShaders.loadShader(GLES20.GL_VERTEX_SHADER,
									  vertexShaderCode);
        int fragmentShader = GLShaders.loadShader(GLES20.GL_FRAGMENT_SHADER,
										fragmentShaderCode);

        int mProgram = GLES20.glCreateProgram();
        GLES20.glAttachShader(mProgram, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(mProgram, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(mProgram);                  // create OpenGL program executables
        
        this.mProgram = mProgram;
	}
	
	public void addPoint_noOffset(float x, float y, float z){
		vManager.addVertex_noOffset(x, y, z);
		vManager.addIndex_noOffset((short) (vManager.getNumOfTotVertices()-1), vManager.getIndicesArray().length-1);
		
		//vManager.removeObsoleteVertices();
	}
	public void addPoint_withOffset(float x, float y, float z){
		vManager.addVertex_noOffset(x, y, z);
		float[] offsetArray = vManager.addIndex_withOffset((short) (vManager.getNumOfTotVertices()-1), vManager.getIndicesArray().length-1);
		
		Matrix.setIdentityM(mTransOffsetMatrix, 0);
		Matrix.translateM(mTransOffsetMatrix, 0, offsetArray[0], offsetArray[1], offsetArray[2]);
		
		//vManager.removeObsoleteVertices();
	}
	public void removePoint_noOffset(int locationIndex){
		vManager.removeIndex_noOffset(locationIndex);

		//vManager.removeObsoleteVertices();
	}
	public void removePoint_withOffset(int locationIndex){
		float[] offsetArray = vManager.removeIndex_withOffset(locationIndex);
		
		Matrix.setIdentityM(mTransOffsetMatrix, 0);
		Matrix.translateM(mTransOffsetMatrix, 0, offsetArray[0], offsetArray[1], offsetArray[2]);

		//vManager.removeObsoleteVertices();
	}	
	
	public int getNumOfPoints(){
		return vManager.getNumOfDrawnVertices()/3;
	}
	
	@Override
	public void setupForDraw(){
		super.setupForDraw();
			 
		//**POINT SIZE
			// get handle to vertex shader's vPosition member
			mPointSizeHandle = GLES20.glGetUniformLocation(mProgram, "uPointSize");
	
			// Prepare the triangle coordinate data
			GLES20.glUniform1f(mPointSizeHandle, pointSize);	
	}
	
	@Override
	public void draw(float[] mVMatrix, float[] mPMatrix){
		super.draw(mVMatrix, mPMatrix);
		
		// Draw the points using indices
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vManager.indexServerBufferObjectID[0]);
        GLES20.glDrawElements(GLES20.GL_POINTS, vManager.getIndicesArray().length, GLES20.GL_UNSIGNED_SHORT, 0);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(vManager.mVertexPositionHandle);	
        
        //UNBIND IBO
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
}
