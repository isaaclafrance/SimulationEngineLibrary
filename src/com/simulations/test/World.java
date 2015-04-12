package com.simulations.test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.microedition.khronos.egl.EGLConfig;

import android.content.Context;

public abstract class World
{	
	 //Fields
	 private Context context;    
	 private Map<String, Scene> scenesMap;

	 private final CameraManager cameraManager;
	 private final LightManager lightManager;
	 private PhysicsEngine physicsEngine;
	 
	 private final Scene mainScene;	
	 
	 public final float[] backgroundColor;
	 
	 //Constructors
	 public World(){
		 this.mainScene = new Scene();			 
		 this.scenesMap = new HashMap<String, Scene>();
		 this.cameraManager = new CameraManager(this);
		 this.lightManager = new LightManager(this);
		 
		 this.backgroundColor = new float[]{0.0f, 0.0f, 0.0f, 0.0f};
 	
    	 setupPhysicsEngine();	 
    	 setupScenes();
       	 //setupLights();		 
	 }
	 public World(Context context){
		 this();
		 this.context = context;
	 }
	 
	 ////
 	 private void initialize_ObjShadersNBuffers(){
		 for (Scene scene:scenesMap.values()){
			 scene.initialize_ObjShadersNBuffers(context);
		 }
	 } 
 	 private void update_ObjShaders(){
		 for (Scene scene:scenesMap.values()){
			 scene.update_ObjShaders(context);
		 } 		 
 	 }
	 private void runNDraw(){
		for (Scene scene:scenesMap.values()){
			if(scene.getRunningState()){
				scene.animate();				
			}
		}
		 
		lightManager.run();
		
		if(physicsEngine != null){
			physicsEngine.run(3);
		}
	 }
	 
	 public void onSurfaceCreated(EGLConfig p2){
		initialize_ObjShadersNBuffers();
	 }
	 public void onSurfaceChanged(int width, int height){
		float ratio = (float) width/height;
		
		cameraManager.getSelectedCamera().setProjectionMatrix(ratio);	
	 }
	 public void onDrawFrame(){
		update_ObjShaders();			
		
		//Run and/or draw objects
		runNDraw();
	 }
	 
	 ////
	 protected abstract void setupPhysicsEngine();
	 protected abstract void setupScenes();      	//Initializes scenes and setups up global cameras if necessary
	 //protected abstract void setupLights();	 

	 ////
	 public void addScene(Scene scene){
		 addScene(scene.getNodeName(), scene);
	 }
	 public void addScene(String sceneName, Scene scene){
		if(!scenesMap.containsValue(scene)){
			scene.setLightManagerRef(getLightManager());
			scene.setPhysicsEngineRef(getPhysicsEngine());
			scene.setCameraManagerRef(getCameraManager());
			scene.setup();
			scenesMap.put(scene.getNodeName(), scene);
			 
			//Adds the scene nodes associated with this scene if not done already
			for(SceneGraphNode sceneNode:scene.getChildrenNodes()){
				if(sceneNode.getClass() == Scene.class){
					addScene(((Scene)sceneNode));				 
				}
			}
		}		 
	 }
	 public void addScenes(ArrayList<Scene> scenes){
		 for(Scene scene:scenes){
			 addScene(scene);
		 }
	 }
	 
 	 public void removeScene(String sceneName){
		 scenesMap.get(sceneName).prepareForDeletion();
		 scenesMap.remove(sceneName);
	 }
 	 public void removeScene(Scene scene){
 		 removeScene(scene.getNodeName());
 	 }
 	 public void removeScenes(ArrayList<Scene> scenes){
 		 for(Scene scene:scenes){
 			 removeScene(scene);
 		 }
 	 }
 	 
 	 ////
	 public void setPhysicsEngine(PhysicsEngine physicsEngine){
		 this.physicsEngine = physicsEngine;
	 }
	 public PhysicsEngine getPhysicsEngine(){
		 return physicsEngine;
	 }
	 
	 public Scene getScene(String sceneName){
		 return  scenesMap.get(sceneName);
	 }
	 public Collection<Scene> getScenes(){
		 return scenesMap.values();
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

	 ////
	 public void setBackgroundColor(float r, float g, float b, float transparency){
		 backgroundColor[0] = r;
		 backgroundColor[1] = g;
		 backgroundColor[2] = b;
		 backgroundColor[3] = transparency;
	 }
	 public void setSkyBoxBackgroundImage(int[] textureResourceIDs){
		 mainScene.addDrawableObject("skybox", new SkyBox(textureResourceIDs));
	 }
	 public void disableSkybox(){
		 mainScene.removeDrawableObject("skybox");
	 }
}
