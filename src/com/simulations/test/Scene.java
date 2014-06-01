package com.simulations.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;

//Manages and Coordinates Interacting Objects.

public class Scene {
     //Fields
	 private LightManager lightManager;
	 private CameraManager cameraManager;
	 private PhysicsEngine physicsEngine;
	 
	 private ArrayList<GDrawable> drawable_objects;
	 private ArrayList<Animated> nondrawable_objects;

	 private Map<String, GDrawable> drawableObjectsMap; 
	 private Map<String, Animated> nonDrawableObjectsMap;
	 	 
	 private float[] sceneTranslation;
	 private Quaternion sceneOrientation;
	 
	 private boolean isLighted;
	 private boolean isRunning;
	 
	 //Constructors
	 public Scene(){
		drawable_objects = new ArrayList<GDrawable>();
		nondrawable_objects = new ArrayList<Animated>();
		
		drawableObjectsMap = new HashMap<String, GDrawable>();
		nonDrawableObjectsMap = new HashMap<String, Animated>();
		
		isLighted = false;
		isRunning = true;
		
		sceneTranslation = new float[]{0.0f, 0.0f, 0.0f};
		sceneOrientation = new Quaternion();
		
		////
		setupDrawableObjects();
		setupNonDrawableObjects();
		setupLights();
		setupCameras();
		setupLinks();
	 }
	
	 ////	
	 public void update_ObjShadersNBuffers(Context context){
		 for (GDrawable object : drawable_objects){
			 ((GDrawable)object).updateShadersNTextures(context);
			 ((GDrawable)object).initBuffers();
		 }
	 }
	 
	 public void prepareForDeletion(){
		 if(drawable_objects != null){
		     for (GDrawable object : drawable_objects){
		    	 removeDrawableObject(object);
			 }
		 }
	 }
	 
 	 private void runDrawableObjects(){
		 if(drawable_objects != null){
		     for (GDrawable object : drawable_objects){
		    	 object.animate();
				 object.draw(cameraManager.getSelectedCamera().getViewMatrix(), cameraManager.getSelectedCamera().getProjMatrix());
			 }
		 }
	 }
	 private void runNonDrawableObjects(){
		 if(nondrawable_objects != null){
			 for (Animated object : nondrawable_objects){
				 object.animate();
			 }
		 }
	 }
	 private void runManagers(){
		cameraManager.run();	
	 }
	 public void runNDraw(){
		 runDrawableObjects();
		 runNonDrawableObjects();
		 runManagers();
	 }
	
	 ////
	 protected void setupDrawableObjects(){
		 
	 }
	 protected void setupNonDrawableObjects(){
		 
	 }
	 protected void setupLights(){
		 
	 }
	 protected void setupCameras(){
		 
	 }
	 protected void setupLinks(){
		 
	 }
	 
	 //// 
	 public GDrawable addDrawableObject(String objName, GDrawable obj){
		drawableObjectsMap.put(objName, obj);
		return addDrawableObject(obj);
	 }
	 public GDrawable addDrawableObject(GDrawable obj){
		drawable_objects.add(obj); 
		
		//Give object a reference of the scene
		obj.setSceneRef(this);		
		
		return obj;
	 }
	 public void removeDrawableObject(GDrawable obj){
		obj.clearAll();
		drawable_objects.remove(obj);
		drawableObjectsMap.values().remove(obj);
	 }
	 public void removeDrawableObject(int objIndex){
		 removeDrawableObject(drawable_objects.get(objIndex));
	 }
	 
	 public Animated addNonDrawableObject(String objName, Animated obj){
		nonDrawableObjectsMap.put(objName, obj);	
		return addNonDrawableObject(obj);
	 }
	 public Animated addNonDrawableObject(Animated obj){
		nondrawable_objects.add(obj);		
		return obj;		
	 }
	 
	 public void removeNonDrawableObject(Animated obj){
		nondrawable_objects.remove(obj);
		drawableObjectsMap.values().remove(obj);
	 }
	 public void removeNonDrawableObject(int objIndex){
		removeNonDrawableObject(nondrawable_objects.get(objIndex)); 
	 }
	 	 
	 ////
	 public ArrayList<GDrawable> getDrawableObjects(){
		 return drawable_objects;
	 }
	 public ArrayList<Animated> getNonDrawableObjects(){
		 return nondrawable_objects;
	 }
	 
	 public Map<String, GDrawable> getDrawableObjectsMap(){
		 return drawableObjectsMap;
	 }
	 public Map<String, Animated> getNonDrawableObjectsMap(){
		 return nonDrawableObjectsMap;
	 }
	 
	 public boolean getLightedState(){
		 return isLighted;
	 }
	 public void setLightedState(boolean state){
		 isLighted = state;
	 }
	 
	 public boolean getRunningState(){
		 return isRunning;
	 }
	 public void setRunningState(boolean state){
		 isRunning = state;
	 } 
	 
	 public PhysicsEngine getPhysicsEngineRef(){
		 return physicsEngine;
	 }
	 public LightManager getLightManagerRef(){
		 return lightManager;
	 }
	 public CameraManager getCameraManagerRef(){
		 return cameraManager;
	 }
	 
	 public void setPhysicsEngineRef(PhysicsEngine physicsEngine){
		 this.physicsEngine = physicsEngine;
	 }
	 public void setLightManagerRef(LightManager lightManager){
		 this.lightManager = lightManager;
	 }
	 public void setCameraManagerRef(CameraManager cameraManager){
		 this.cameraManager = cameraManager;
	 }

	 public float[] getSceneTranslation(){
		 return sceneTranslation;
	 }
	 public Quaternion getSceneOrientation(){
		 return sceneOrientation;
	 }
	 public void setTranslateNOrientation(float[] trans, Quaternion orientationQuaternion){
		 for(GDrawable dObject:drawable_objects){
			 ((Transformable)dObject).setSceneMatrix(trans, orientationQuaternion);
		 }
		 
		 for(Animated aObject:drawable_objects){
			 if(aObject.getClass() == Transformable.class){
				 ((Transformable)aObject).setSceneMatrix(trans, orientationQuaternion);
			 }
		 }
		 
		 sceneTranslation = trans;
		 sceneOrientation = orientationQuaternion;
	 }
}
