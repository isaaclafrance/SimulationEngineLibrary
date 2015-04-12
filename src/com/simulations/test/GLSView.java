package com.simulations.test;
import android.opengl.GLSurfaceView;
import android.content.*;

public abstract class GLSView extends GLSurfaceView
{
     public GLRenderer glRenderer;
     protected int renderedWorldIndex;
     private Context context;
	 private World[] worlds;

     public GLSView(Context context){
		 super(context);
		 
		 this.context = context;

		 setEGLContextClientVersion(2); //Use Opengl ES 2.0
		 setEGLConfigChooser(8 , 8, 8, 8, 16, 0);
		 
		 ////
		 setWorld(new BlankWorld(context));
		 //setRenderedWorldIndex(0);
	 }
    
     public World setWorld(World world){
    	 world.setContext(context);
    	 this.worlds = new World[]{world};

    	 return world;
     }
     public void setWorlds(World[] worlds){
    	 this.worlds = worlds;
    	 for(World world:worlds){
    		 world.setContext(context);
    	 }
     }
     
     public void setRenderedWorldIndex(int index){ //Selected which one of the worlds array is to be rendered
    	 if(glRenderer == null){
    		 setGLRenderer(new GLRenderer());
    	 }
    	 glRenderer.setWorldRef(worlds[index]);
    	 renderedWorldIndex = index;
     }    
     protected void setGLRenderer(GLRenderer glRenderer){
    	 //Create and set renderer
		 this.glRenderer = glRenderer;
    	 setRenderer(glRenderer);
		 setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
     }  
     
     public int getRenderedWorldIndex(){
    	 return renderedWorldIndex;
     }
     public World getRenderedWorld(){
    	 return worlds[renderedWorldIndex];
     }
     public GLRenderer getGLRenderer(){
    	 return glRenderer;
     }
}
