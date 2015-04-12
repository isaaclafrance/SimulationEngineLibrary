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
		vManager.setNormalizedState(false);
	}
	public GT_Point(float x, float y, float z, float r, float g, float b, float transparency,
			float pointSize, float[] pointsData) {
		super(x, y, z, r, g, b, transparency, pointsData, new short[]{0});
		
		this.pointSize = pointSize;
		
		vManager.removeIndex_noOffset(0);
		setupInitialPointIndices(pointsData);
		
		vManager.setNormalizedState(false);
	}
	
	////
	protected void setupInitialPointIndices(float[] pointsData){
		//Create index array.
		int pointsNum = (int) Math.floor(pointsData.length/3.0f);
		short[] indices = new short[pointsNum];
		for(short i=0; i<pointsNum; i++){
			indices[i] = i;
		}	
		
		//Add vertices and indices to vertex manager
		float[]offsetArray = vManager.addIndices_withOffset(indices, 0);
		
		Matrix.setIdentityM(mTransOffsetMatrix, 0);
		Matrix.translateM(mTransOffsetMatrix, 0, offsetArray[0], offsetArray[1], offsetArray[2]);
		
		cManager.updateNumOfColorVertices(vManager.getNumOfDrawnVertices());
	}
	
	@Override
	public void updateShadersNTextures(Context context){
		if(isShaderChanged){
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
	}
	
	public void addPoint_noOffset(float x, float y, float z){
		vManager.addVertex_noOffset(x, y, z);
		vManager.addIndex_noOffset((short) (vManager.getNumOfTotalVertices()-1), vManager.getNumOfDrawnVertices()-1);
		
		//vManager.removeObsoleteVertices();
		
		cManager.updateNumOfColorVertices(cManager.getNumColorVertex()+1);
	}
	public void addPoint_withOffset(float x, float y, float z){
		vManager.addVertex_noOffset(x, y, z);
		float[] offsetArray = vManager.addIndex_withOffset((short) (vManager.getNumOfTotalVertices()-1), vManager.getNumOfDrawnVertices()-1);
		
		Matrix.setIdentityM(mTransOffsetMatrix, 0);
		Matrix.translateM(mTransOffsetMatrix, 0, offsetArray[0], offsetArray[1], offsetArray[2]);
		
		//vManager.removeObsoleteVertices();

		cManager.updateNumOfColorVertices(cManager.getNumColorVertex()+1);
	}
	public void addPoint_noOffset(float x, float y, float z, float r, float g, float b){
		addPoint_noOffset(x, y, z);
		cManager.replaceVertexColor(r, g, b, cManager.getNumColorVertex()-1);
	}
	public void addPoint_wihtOffset(float x, float y, float z, float r, float g, float b){
		addPoint_withOffset(x, y, z);
		cManager.replaceVertexColor(r, g, b, cManager.getNumColorVertex());
	}
	
	public void removePoint_noOffset(int locationIndex){
		vManager.removeIndex_noOffset(locationIndex);

		//vManager.removeObsoleteVertices();
		
		cManager.updateNumOfColorVertices(cManager.getNumColorVertex()-1);
	}
	public void removePoint_withOffset(int locationIndex){
		float[] offsetArray = vManager.removeIndex_withOffset(locationIndex);
		
		Matrix.setIdentityM(mTransOffsetMatrix, 0);
		Matrix.translateM(mTransOffsetMatrix, 0, offsetArray[0], offsetArray[1], offsetArray[2]);

		//vManager.removeObsoleteVertices();
		
		cManager.updateNumOfColorVertices(cManager.getNumColorVertex()-1);
	}	
	
	public int getNumOfPoints(){
		return vManager.getNumOfDrawnVertices();
	}
	
	@Override
	protected void setupForDraw(){
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
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vManager.getIndexServerBufferObjectID()[0]);
        GLES20.glDrawElements(GLES20.GL_POINTS, vManager.getNumOfDrawnVertices(), GLES20.GL_UNSIGNED_SHORT, 0);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(vManager.get_mVertexPositionHandle());	
        
        //UNBIND IBO
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
	}
}
