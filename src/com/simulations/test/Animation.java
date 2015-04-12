package com.simulations.test;

public abstract class Animation {
	private AnimatedObject animatedObjectRef;
	
	public abstract void animate();
	
	public void setAnimatedObjectRef(AnimatedObject animatedObject){
		this.animatedObjectRef = animatedObject;
	}
}
