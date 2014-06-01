package com.simulations.test;

import android.opengl.GLSurfaceView;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.*;
import android.opengl.*;

public abstract class GLRenderer implements GLSurfaceView.Renderer
{
    private World world;

	public GLRenderer(World world){
		super();
		this.world = world;
	}
	public GLRenderer(){
		super();
	}
	
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig p2)
	{
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		
		// Use culling to remove back faces.
		//GLES20.glEnable(GLES20.GL_CULL_FACE);
		
		// Enable depth testing
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height)
	{
		//Adjusts surface based on geometry changes 
		//such as screen rotation.
		GLES20.glViewport(0, 0, width, height);
	}

	@Override
	public void onDrawFrame(GL10 unused)
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
		GLES20.glEnable(GLES20.GL_DEPTH_TEST);
	}	
	
	public void setWorldRef(World world){
		this.world = world;
	}
	public World getWorldRef(){
		return world;
	}
}
