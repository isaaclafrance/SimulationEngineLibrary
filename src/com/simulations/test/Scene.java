package com.simulations.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

//Manages interactions among both drawable and nondrawable objects.

public class Scene extends SceneGraphNode{
     //Fields
	 private LightManager lightManager;
	 private CameraManager cameraManager;
	 private PhysicsEngine physicsEngine;
	 
	 private Map<String, GDrawable> drawableObjectsMap; 
	 private Map<String, AnimatedObject> nonDrawableObjectsMap;
	 	 	 
	 private boolean isLighted;
	 private boolean isRunning;
	 
	 //Constructors
	 public Scene(){
		drawableObjectsMap = new HashMap<String, GDrawable>();
		nonDrawableObjectsMap = new HashMap<String, AnimatedObject>();
		
		isLighted = false;
		isRunning = true;
		
		isUpdated = true;
	 }
	
	 ////	
	 public void initialize_ObjShadersNBuffers(Context context){
		 for (GDrawable object : drawableObjectsMap.values()){
			 ((GDrawable)object).updateShadersNTextures(context);
			 ((GDrawable)object).initBuffers();
		 }
	 } 
	 public void update_ObjShaders(Context context){
		 for (GDrawable object : drawableObjectsMap.values()){
			 ((GDrawable)object).updateShadersNTextures(context);
		 }
	 }
	 public void prepareForDeletion(){
		 if(drawableObjectsMap.size() != 0){
		     for (GDrawable object : drawableObjectsMap.values()){
		    	 removeDrawableObject(object);
			 }
		 }
	 }
	 
	 ////
 	 private void runDrawableObjects(){
		 if(drawableObjectsMap.size() != 0){
		     for(GDrawable object : drawableObjectsMap.values()){
		    	 object.animate();
				 object.draw(cameraManager.getSelectedCamera().getViewMatrix(), cameraManager.getSelectedCamera().getProjMatrix());
			 }
		 }
	 }
	 private void runNonDrawableObjects(){
		 if(nonDrawableObjectsMap.size() != 0){
			 for (AnimatedObject object : nonDrawableObjectsMap.values()){
				 object.animate();
			 }
		 }
	 }
	 private void runManagers(){
		cameraManager.run();
	 }
	 private void runNDraw(){ 
		 runDrawableObjects();
		 runNonDrawableObjects();
		 runManagers();
	 }
	 
	 public void processBillboard(float camPosX, float camPosY, float camPosZ){
		 //Implement billboard behavior such that drawable objects are always facing the camera
		 for(GDrawable dObj:drawableObjectsMap.values()){
			 if(dObj.getBillboardState()){
				 Quaternion.scratchVec1[0] = dObj.getPosition()[0] - camPosX;
				 Quaternion.scratchVec1[1] = dObj.getPosition()[1] - camPosY;
				 Quaternion.scratchVec1[2] = dObj.getPosition()[2] - camPosZ;
			 
				 dObj.getOrientation().orientFromTo(Quaternion.STANDARD_FORWARD_VECTOR, Quaternion.scratchVec1, true);
			 }
		 }
	  }
	 
	 @Override
	 public void animate(){
		 super.animate();
		 setTransformationMatrices();			 
		 runNDraw();
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
	 public void setup(){
		////
		setupDrawableObjects();
		setupNonDrawableObjects();
		setupLights();
		setupCameras();
		setupLinks();		 
	 }
	 
	 //// 
	 public GDrawable addDrawableObject(String objName, GDrawable obj){
		drawableObjectsMap.put(objName, obj);
		
		//Give object a reference of the scene
		obj.setSceneRef(this);			
		
		return obj;
	 }
	 public GDrawable addDrawableObject(GDrawable obj){
		return addDrawableObject(obj.toString(), obj);
	 }
	 public void removeDrawableObject(String objName){
		drawableObjectsMap.remove(objName).clearAll();
	 }
	 public void removeDrawableObject(GDrawable obj){
		 for(Entry<String, GDrawable> entry:drawableObjectsMap.entrySet()){
			 if(entry.getValue().equals(obj)){
				 removeDrawableObject(entry.getKey());
				 break;
			 }
		 }
	 }

	 
	 public AnimatedObject addNonDrawableObject(String objName, AnimatedObject obj){
		nonDrawableObjectsMap.put(objName, obj);
		return obj;
	 }
	 public AnimatedObject addNonDrawableObject(AnimatedObject obj){
			return addNonDrawableObject(obj.toString(), obj);	
	 }
	 public void removeNonDrawableObject(String objName){
		nonDrawableObjectsMap.remove(objName);
	 }
	 public void removeNonDrawableObject(AnimatedObject obj){
		 for(Entry<String, AnimatedObject> entry:nonDrawableObjectsMap.entrySet()){
			 if(entry.getValue().equals(obj)){
				 removeNonDrawableObject(entry.getKey());
				 break;
			 }
		 }
	 }

	 ////
	 public GDrawable getDrawableObject(String objName){
		 return drawableObjectsMap.get(objName);
	 }
	 public AnimatedObject getNonDrawableObject(String objName){
		 return nonDrawableObjectsMap.get(objName);
	 }
	 public Collection<GDrawable> getDrawableObjects(){
		 return drawableObjectsMap.values();
	 }
	 public Collection<AnimatedObject> getNonDrawableObjects(){
		 return nonDrawableObjectsMap.values();
	 }
	 public int getNumDrawableObjects(){
		 return drawableObjectsMap.size();
	 }
	 public int getNumNonDrawableObjects(){
		 return nonDrawableObjectsMap.size();
	 }
	 	 
	 public boolean getLightedState(){
		 return isLighted;
	 }
	 public void setLightedState(boolean state){
		isLighted = state;
		
		//Set all drawable objects in scene as lighted if scene is lighted
		if(isLighted){
			for(GDrawable drawableObj: drawableObjectsMap.values()){
				if(drawableObj.getClass()!= Light.class){
					drawableObj.setLightState(true);
				}
			}	
		}
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
	 
	 private void setTransformationMatrices(){
		 if(isUpdated){
			 if(getNumDrawableObjects() != 0){
				 for(GDrawable dObject:drawableObjectsMap.values()){
					 if(dObject.getClass() == Transformable.class){
						 ((Transformable)dObject).setSceneTransformMatrix(worldTransformationMatrix);
					 }
				 }				 
			 }
			 if(getNumNonDrawableObjects() != 0){
				 for(AnimatedObject ndObject:nonDrawableObjectsMap.values()){
					 if(ndObject.getClass() == Transformable.class){
						 ((Transformable)ndObject).setSceneTransformMatrix(worldTransformationMatrix);
					 }
				 }				 
			 }			 
			 isUpdated = false;			 
		 }

	 }
}
