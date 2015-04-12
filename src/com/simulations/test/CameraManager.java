package com.simulations.test;

import java.util.ArrayList;
import java.util.Collections;

public final class CameraManager {
	//Fields
	public int MAX_NUM_OF_CAMERAS; //0 -> no max #; 
	public boolean isNumOfCamerasFixed;
    public static enum Mode {ORBIT, STATIC, STATIC_TRACK};	
    public final Camera DEFAULT_CAMERA(){
    	return (new Camera(0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, Mode.STATIC, new Float[0][0]));
    }	
    
	private World world;
    public ArrayList<Camera> cameras;
    private int selectedCamera;

    //Constructors
    public CameraManager(World world){
    	this.world = world;
    	cameras = new ArrayList<Camera>();
    	selectedCamera = 0;
    	MAX_NUM_OF_CAMERAS = 0;
    	isNumOfCamerasFixed = false;
    }
    public CameraManager(Camera[] cameras, float selectedCamera, World world){
    	this(world);
    	Collections.addAll(this.cameras, cameras);
    }
    
    ////
    public ArrayList<Camera> getCamera(){
    	return cameras;
    }
    public Camera getSelectedCamera(){
    	return cameras.get(selectedCamera);
    }
    public void setSelectedCamera(int camIndex){
    	if((camIndex+1) > getNumOfCameras()){
    		selectedCamera = camIndex;
    	}
    	else{
    		selectedCamera = 0;
    	}
    }
    public int getNumOfCameras(){
    	return cameras.size();
    }

    ////
	public void addCamera(Camera camera){
		if(getNumOfCameras() != getMaxNumOfCameras() || isNumOfCamerasFixed == false){
			cameras.add(camera);
		}
	 }
	public void removeCamera(int cameraIndex){
		if(isNumOfCamerasFixed != true){
			cameras.remove(cameraIndex);
		}
	 }
	
	////
	private void processBillboards(){
		for(Scene scene:world.getScenes()){
			scene.processBillboard(getSelectedCamera().getPosition()[0], getSelectedCamera().getPosition()[1], getSelectedCamera().getPosition()[2]);
		}
	}
	private void processMode(Camera camera){
		switch (camera.camMod) {
		case STATIC_TRACK:
			camera.performStaticTrack();
		case ORBIT:
			camera.performOrbit();
			break;
		default:
			break;
		}
		
	}		
	public void run(){
		getSelectedCamera().animate();
		
		if(cameras.size() != 0){
			processMode(getSelectedCamera());	
		}
		if(getSelectedCamera().getShowBillboardsState()){
			processBillboards();
		}	
	}
	
	////
	public void setMaxNumOfCameras(int num){
		MAX_NUM_OF_CAMERAS = num;
	}
	public int getMaxNumOfCameras(){
		return MAX_NUM_OF_CAMERAS;
	}
	
	public void setFixedCameraNumState(boolean state){
		isNumOfCamerasFixed = state;
	}
	public boolean getFixedCameraNumState(){
		return isNumOfCamerasFixed;
	}
}
