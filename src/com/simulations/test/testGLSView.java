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
	    setGLRenderer(new testGLRenderer());  		
		setRenderedWorldIndex(0);
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
    	 		
    	 		float[] camPosition = getRenderedWorld().getCameraManager().getSelectedCamera().getPosition();
    	 		getRenderedWorld().getCameraManager().getSelectedCamera().setPosition(camPosition[0]+dx*0.005f, camPosition[1], camPosition[2]+dy*0.005f);

    	 		requestRender();
    	 }
    	 
    	 mPreviousX = x;
    	 mPreviousY = y;
    	 return true;
     }
}
