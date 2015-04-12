package com.simulations.test;

import java.util.ArrayList;

public class AnimatedObject implements Animated
{
	 private final ArrayList<Animation> animationsList = new ArrayList<Animation>();
	
	 public AnimatedObject(){
		 
	 }
	 
	 //Runs all animations in the order they had been added to animationsList
	 private void processAnimationsList(){
		 for(Animation animation:animationsList){
			 animation.animate();
		 }
	 }
	 
     public void animate(){
    	 processAnimationsList();
     }
     
     public void addAnimation(Animation animation){
    	animation.setAnimatedObjectRef(this);
    	animationsList.add(animation);
     }
     public Animation removeAnimation(int index){
    	 return animationsList.remove(index);
     }
     public void clearAllAnimations(){
    	animationsList.clear();
     }	
}
