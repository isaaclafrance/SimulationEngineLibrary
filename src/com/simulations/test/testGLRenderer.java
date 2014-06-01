package com.simulations.test;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class testGLRenderer extends GLRenderer {

	public testGLRenderer(World world) {
		super(world);
	}
	public testGLRenderer(){
		super();
	}

	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig p2)
	{
		super.onSurfaceCreated(unused, p2);
		
		//
		getWorldRef().onSurfaceCreated(p2);
	}

	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height)
	{
		super.onSurfaceChanged(unused, width, height);
		
		//
		getWorldRef().onSurfaceChanged(width, height);
	}

	@Override
	public void onDrawFrame(GL10 unused)
	{
		super.onDrawFrame(unused);
		
		//
		getWorldRef().onDrawFrame();
	}	
}
