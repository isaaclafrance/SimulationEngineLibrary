package com.simulations.test;

import java.util.ArrayList;
import java.util.Collections;

public final class CameraManager {
	//Fields
	public static int MAX_NUM_OF_CAMERAS = 0; //-1->current cameras cannot be changed; 0->no max #; 
	public static final float[] REFERENCE_LOOKDIREC_UNIT_VEC = new float[]{0.0f,0.0f,-1.0f}; 
    public static enum Mode {ORBIT, STATIC, STATIC_TRACK};	
    public final Camera DEFAULT_CAMERA(){
    	return (new Camera(0.0f, 0.0f, 5.0f, 0.0f, 0.0f, 0.0f, Mode.STATIC, null));
    }	
    
	private World world;
    public ArrayList<Camera> cameras;
    private int selectedCamera;

    //Constructors
    public CameraManager(World world){
    	this.world = world;
    	selectedCamera = 0;
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
		if(getNumOfCameras() != getMaxNumOfCameras() || getMaxNumOfCameras() == -1){
			cameras.add(camera);
		}
	 }
	public void removeCamera(int cameraIndex){
		if(getMaxNumOfCameras() != -1){
			cameras.remove(cameraIndex);
		}
	 }
	
	////
	private void processBillboard(){
		 //TODO: Implement billboard behavior
		 float[] cam_to_obj_Vec = new float[3];
		
		 for(Scene scene:world.getScenes()){
		 for(int i = 0; i<scene.getDrawableObjects().size(); i++){
			 //account for objects that return null as position
			 if(scene.getDrawableObjects().get(i).getPosition() != null){
				 cam_to_obj_Vec[0] = scene.getDrawableObjects().get(i).getPosition()[0] - getSelectedCamera().getPosition()[0];
				 cam_to_obj_Vec[1] = scene.getDrawableObjects().get(i).getPosition()[1] - getSelectedCamera().getPosition()[1];
				 cam_to_obj_Vec[2] = scene.getDrawableObjects().get(i).getPosition()[2] - getSelectedCamera().getPosition()[2];
				 
				 scene.getDrawableObjects().get(i).getOrientationQuaternion().orientFromTo(REFERENCE_LOOKDIREC_UNIT_VEC, cam_to_obj_Vec, true);
			 }
		 }
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
			processBillboard();
		}	
	}
	
	////
	public void setMaxNumOfCameras(int num){
		MAX_NUM_OF_CAMERAS = num;
	}
	public int getMaxNumOfCameras(){
		return MAX_NUM_OF_CAMERAS;
	}
}
