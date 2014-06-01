package com.simulations.test;

import android.content.Context;

public final class testWorld extends World
{
	public testWorld(){
		
	}
    public testWorld(Context context){
    	super(context); 
	}
     
	@Override
	protected void setupPhysicsEngine() {
		 //Initialize a particular physical engine
		 float[] const_accel = new float[]{0.0f, -0.1f, 0.0f};
		 setPhysicsEngine(new VerletEngine(const_accel));
		 getPhysicsEngine().getCollisionHandler().addBoundaryPlane(new Float[]{0.0f, 1.0f, 0.0f, 2.0f});
	}
    @Override
    protected void setupScenes() {
     	getCameraManager().setMaxNumOfCameras(1);   	
    	addScene(new testScene());
    }	
}
