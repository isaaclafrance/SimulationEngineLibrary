package com.simulations.test;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;

import android.content.Context;

public abstract class World
{	
	 //Fields
	 private Context context;    
	 private ArrayList<Scene> scenes;

	 private final CameraManager cameraManager;
	 private final LightManager lightManager;
	 private PhysicsEngine physicsEngine;
	 
	 private final Scene mainScene;	
	 
	 //Constructors
	 public World(){
		 this.mainScene = new Scene();			 
		 this.scenes = new ArrayList<Scene>();
		 this.cameraManager = new CameraManager(this);
		 this.lightManager = new LightManager(this);
 	
    	 setupPhysicsEngine();	 
    	 setupScenes();
       	 //setupLights();		 
	 }
	 public World(Context context){
		 this.mainScene = new Scene();
		 this.scenes = new ArrayList<Scene>();
		 this.cameraManager = new CameraManager(this);
		 this.lightManager = new LightManager(this); 
		
    	 setupPhysicsEngine();	 
    	 setupScenes();
       	 //setupLights();
	 }
	 
	 ////
	 private void update_ObjShadersNBuffers(){
		 for (Scene scene:scenes){
			 scene.update_ObjShadersNBuffers(context);
		 }
	 } 
	 private void runNDraw(){
		for (Scene scene:scenes){
			if(scene.getRunningState()){
				scene.runNDraw();				
			}
		}
		 
		lightManager.run();
		
		if(physicsEngine != null){
			physicsEngine.run(3);
		}
	 }
	 
	 public void onSurfaceCreated(EGLConfig p2){
		update_ObjShadersNBuffers();	
	 }
	 public void onSurfaceChanged(int width, int height){
		float ratio = (float) width/height;
		
		cameraManager.getSelectedCamera().setProjectionMatrix(ratio);	
	 }
	 public void onDrawFrame(){
		update_ObjShadersNBuffers();			
		
		//Run and/or draw objects
		runNDraw();
	 }
	 
	 ////
	 protected abstract void setupPhysicsEngine();
	 protected abstract void setupScenes();      	//Initializes scenes and setups up global cameras if necessary
	 //protected abstract void setupLights();	 

	 ////
	 public void addScene(Scene scene){
		 scene.setLightManagerRef(getLightManager());
		 scene.setPhysicsEngineRef(getPhysicsEngine());
		 scene.setCameraManagerRef(getCameraManager());
		 scenes.add(scene);
	 }
	 public void addScenes(Scene[] scenes){
		 for(Scene scene:scenes){
			 addScene(scene);
		 }
	 }
 	 public void removeScene(int sceneIndex){
		 scenes.get(sceneIndex).prepareForDeletion();
		 scenes.remove(sceneIndex);
	 }
	 public void setPhysicsEngine(PhysicsEngine physicsEngine){
		 this.physicsEngine = physicsEngine;
	 }
	 
	 ////
	 public PhysicsEngine getPhysicsEngine(){
		 return physicsEngine;
	 }
	 
	 public ArrayList<Scene> getScenes(){
		 return scenes;
	 }
	 public Scene getMainScene(){
		 return mainScene;
	 }

	 public CameraManager getCameraManager(){
		 return cameraManager;
	 }
	 public LightManager getLightManager(){
		 return lightManager;
	 }

	 public void setContext(Context context){
		 this.context = context;
	 }
}
