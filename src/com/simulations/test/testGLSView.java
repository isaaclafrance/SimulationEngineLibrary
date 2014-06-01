package com.simulations.test;
import android.content.*;
import android.view.MotionEvent;

public class testGLSView extends GLSView
{
	 private float mPreviousX;
     private float mPreviousY;
	
     public testGLSView(Context context){
	    super(context); 	
	    
		//Attach World(s)
		setWorlds(new World[]{new testWorld()}); // setWorld(new testWorld()); 
		
		//Set and select world to be rendered
		setRenderedWorldIndex(0);
	    setGLRenderer(new testGLRenderer());  
	 }
     
     @Override
     public boolean onTouchEvent(MotionEvent e){
    	 float x = e.getX();
    	 float y = e.getY();
    	 
    	 switch(e.getAction())
    	 {
    	 	case MotionEvent.ACTION_MOVE:
    	 		float dx = x - mPreviousX;
    	 		float dy = y - mPreviousY;
    	 		
    	 		if(y > getHeight() /2){
    	 			//dx = dx * -1;
    	 		}
    	 		if(x < getWidth() / 2){
    	 			//dy = dy * -1;
    	 		}
    	 		
    	 		//world.getCameraManager().getSelectedCamera().position[2] = (float)(Math.sin(angle) * 5.0);
    	 		//world.getCameraManager().getSelectedCamera().lookAtPos[0] += dy*0.1f;
    	 		getRenderedWorld().getCameraManager().getSelectedCamera().position[0] += dx*0.005f;
    	 		//world.getCameraManager().getSelectedCamera().lookAtPos[0] = (float)(Math.cos(angle) * 5.0);

    	 		requestRender();
    	 }
    	 
    	 mPreviousX = x;
    	 mPreviousY = y;
    	 return true;
     }
}
