package com.simulations.test;

import java.util.ArrayList;
import android.opengl.GLES20;

public final class LightManager {
	//Fields
	 private final World world;	
     public final int MAX_LIGHTS = 3; 
     
	 public enum LightingTypes{POINT_LIGHTING, SPOT_LIGHTING};
	 public enum LightingQuality{PER_VERTEX, PER_FRAGMENT};	
	 private ArrayList<Light> lights;
	 private LightingQuality lighQuality;	
	 
	 private ArrayList<Float> lightPositions;
	 private ArrayList<Short> lightTypeNums;
	 
	 private int lightPositionsHandle;
	 private int lightTypeNumsHandle;
	 private int numOfLightsHandle;
	 
	 //Constructors
	 public LightManager(World world){
		 this.world = world;
		 this.lighQuality = LightingQuality.PER_FRAGMENT;
		 this.lightPositions = new ArrayList<Float>();
		 this.lightTypeNums = new ArrayList<Short>();
	 }
	 
	 private void setLightTypesUniform(int mProgram){
		 lightTypeNumsHandle = GLES20.glGetUniformLocation(mProgram, "uLightTypeNums");
		 GLES20.glUniform3f(lightTypeNumsHandle, (float)lightTypeNums.get(0), (float)lightTypeNums.get(1), (float)lightTypeNums.get(2));
	 }
	 private void setLightPositionsUniform(int mProgram){
		lightPositionsHandle = GLES20.glGetUniformLocation(mProgram, "uLightPositions");
		
		float[] lPosArray = new float[lightPositions.size()];	
		for(int i=0; i<lightPositions.size(); i++){
			lPosArray[i] = lightPositions.get(i);
		}
		
		GLES20.glUniformMatrix3fv(lightPositionsHandle, 1, false, lPosArray, 0);
	 }
	 private void setNumOfLightsUniform(int mProgram){
		 numOfLightsHandle = GLES20.glGetUniformLocation(mProgram, "uNumOfLights");
		 GLES20.glUniform1i(numOfLightsHandle, lights.size());
	 }
	 public void setUniforms(int mProgram){
		 setNumOfLightsUniform(mProgram);
		 setLightPositionsUniform(mProgram);
		 setLightTypesUniform(mProgram);
	 }
	
	 public Light getLight(int index){
		 return lights.get(index);
	 }
	 public int getNumOfLights(){
		 return lights.size();
	 }
	 public LightingQuality getLightQuality(){
		 return lighQuality;
	 }
	 
	 public void setLightQuality(LightingQuality lightQuality){
		 this.lighQuality = lightQuality;
	 }
	 
 	 public void addLight(Light light){
	    //Add light and light types to corresponding arrays 
		if(lights.size() != MAX_LIGHTS){			
			lights.add(light);
			lightTypeNums.add(light.getLightTypeNumber());
		}
		else{
			removeLight(lights.size()-1);
			addLight(light);
		}
		
		//Add light position to lights positions array
		addLightPosition(light.getPosition()[0], light.getPosition()[1], light.getPosition()[2]);
		
		//Add light to main scene
		world.getMainScene().addDrawableObject(light);		
		
		//Set as lighted all drawable in all lighted scenes objects in world
		for(Scene scene:world.getScenes()){
			if(scene.getLightedState()){
				for(GDrawable drawableObj: scene.getDrawableObjects()){
					if(drawableObj.getClass()!= Light.class){
						drawableObj.setLightState(true);
					}
				}	
			}
		}

	 }
	 public void removeLight(int lightIndex){
		 	if(lights.size() == 1){ //If no light present then set drawable objects in all lighted to non lighted state
				for(Scene scene:world.getScenes()){
					if(scene.getLightedState()){
				 		for(GDrawable drawableObj: scene.getDrawableObjects()){
							if(drawableObj.getClass()!= Light.class){
								if(drawableObj.getClass() == GDrawableTransformable.class)
									((GDrawableTransformable)drawableObj).setLightState(false);
								if(drawableObj.getClass() == GDrawablePhysicalObject.class)
									((GDrawablePhysicalObject)drawableObj).setLightState(false);
							}
						}
					}
				}
		 	}
		 	
			//Remove light object
			world.getMainScene().removeDrawableObject(lights.get(lightIndex));
			lights.remove(lightIndex);
			lightTypeNums.remove(lightIndex);
			
			//Remove light object position
			removeLightPosition(lightIndex);
	 }
	
	 private void addLightPosition(float x, float y, float z){
		lightPositions.add(x);
		lightPositions.add(y);
		lightPositions.add(z);
	}
	 private void removeLightPosition(int lightIndex){
		lightPositions.remove(lightIndex*3);
		lightPositions.remove(lightIndex*3+1);
		lightPositions.remove(lightIndex*3+2);
	}
	
	 public void run(){
		//Updates all light properties if necessary
		for(int i=0;i<getNumOfLights();i++){	
			if(lights.get(i).getUpdatedState()){
				lightTypeNums.set(i, lights.get(i).getLightTypeNumber());
				
				lightPositions.set(i*3, lights.get(i).getPosition()[0]);
				lightPositions.set(i*3+1, lights.get(i).getPosition()[1]);
				lightPositions.set(i*3+2, lights.get(i).getPosition()[2]);
				
				lights.get(i).setUpdatedState(false);
			}
		}
	}
	
}
