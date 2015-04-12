package com.simulations.test;
import com.simulations.test.BoundingManager.BoundingBoxTypes;

import android.opengl.GLES20;

public class GP_Triangle extends GDrawablePhysicalObject
{
    static final float[] S_COORDS = {0.0f, 0.622008f, 0.0f,
	                           -0.5f, -0.31100424f, 0.0f,
		                       0.5f, -0.31100424f, 0.0f};
    static final short[] S_INDICIES = {0, 1, 2};
	
    public GP_Triangle(float x, float y, float z, float r, float g, float b, float transparency, float mass, Float[][] boundaryPlanes, BoundingBoxTypes bbType){
		super(x,y,z,r,g,b, transparency, S_COORDS, S_INDICIES, mass, boundaryPlanes, bbType);
		
	}
    public GP_Triangle(float x, float y, float z, int[] textureResourceIDs, float[] textureCoords, float mass, Float[][] boundaryPlanes, BoundingBoxTypes bbType){
		super(x,y,z, textureResourceIDs, textureCoords, S_COORDS, S_INDICIES, mass, boundaryPlanes, bbType);
		
	}

	public void draw(float[] mVMatrix, float[] mPMatrix)
	{
		super.draw(mVMatrix, mPMatrix);
		
		// Draw the triangle using indices
		GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, vManager.getIndexServerBufferObjectID()[0]);
        GLES20.glDrawElements(GLES20.GL_TRIANGLE_STRIP, vManager.getNumOfDrawnVertices(), GLES20.GL_UNSIGNED_SHORT, 0);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(vManager.get_mVertexPositionHandle());	
       
        //TODO:Also texture vertex array attribute if necessary
        
        
        //UNBIND IBO
        GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
	}

}
