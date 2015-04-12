package com.simulations.test;

import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.*;
import android.opengl.*;

public class GLRenderer implements GLSurfaceView.Renderer
{
    private World worldRef;

 	public GLRenderer(World world){
		super();
		this.worldRef = world;
	}
	public GLRenderer(){
		super();
	}
	
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig p2)
	{
		GLES20.glClearColor(getWorldRef().backgroundColor[0], getWorldRef().backgroundColor[1], getWorldRef().backgroundColor[2], getWorldRef().backgroundColor[3]);
		
		// Use culling to remove back faces.
		//GLES20.glEnable(GLES20.GL_CULL_FACE);
		
		// Enable depth testing
		//GLES20.glEnable(GLES20.GL_DEPTH_TEST);
		
		//Enable blending
	    GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
	    GLES20.glEnable(GLES20.GL_BLEND);		
		
		////
		getWorldRef().onSurfaceCreated(p2);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height)
	{
		//Adjusts surface based on geometry changes 
		//such as screen rotation.
		GLES20.glViewport(0, 0, width, height);
		
		////
		getWorldRef().onSurfaceChanged(width, height);
	}

	@Override
	public void onDrawFrame(GL10 unused)
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		
		////
		getWorldRef().onDrawFrame();
	}	
	
	public void setWorldRef(World world){
		this.worldRef = world;
	}
	public World getWorldRef(){
		return worldRef;
	}
}
