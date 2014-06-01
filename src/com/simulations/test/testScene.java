package com.simulations.test;

import com.simulations.test.LightManager.LightingTypes;

public class testScene extends Scene {

	public testScene(){
		super();
	}
	
	////
	@Override
	protected void setupDrawableObjects(){
		 //Create GDrawable Objects
		 getPhysicsEngineRef().addPhysicalObject( (PhysicalObject) addDrawableObject("T1", new GP_Triangle(0.7f, 2.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.5f, 1.0f, new Float[][]{new Float[]{0.0f, 1.0f, 0.0f, 4.0f}}, BoundingManager.BoundingBoxTypes.AABB)) );
		 getPhysicsEngineRef().addPhysicalObject( (PhysicalObject) addDrawableObject("T2", new GP_Triangle(0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, null, BoundingManager.BoundingBoxTypes.AABB)) );
		 //addDrawableObject(new GT_Point(0.0f, 0.0f, 0.0f, 1.0f, 0.7f, 1.0f, 1.0f, 5.0f, new float[]{0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 2.0f, 2.0f, 0.0f}));
		 //addDrawableObject(new GT_Point(0.0f, 0.0f, 0.0f, 0.5f, 0.7f, 1.0f, 1.0f, 4.0f));
		 addDrawableObject(new GT_Line(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 1.0f, new float[]{9.0f, 9.0f, 0.0f, 10.0f, 9.0f, 0.0f, 9.0f, 8.0f, 0.0f, 10.0f, 8.0f, 0.0f}, new short[]{0,1,2,3}));
		 addDrawableObject(new GP_Triangle(-0.7f, -2.0f, 0.0f, new int[]{R.drawable.goldenbridge}, new float[]{1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f}, 1.0f, null ,BoundingManager.BoundingBoxTypes.AABB));
		 addDrawableObject(new GTextLines("ABCDEFG HIJKLMN OPQRSTU VWXYZ: #123-456789.", 1.0f, new float[]{0.0f, 0.0f, 0.0f}, new float[]{1.0f, 0.0f, 1.0f}, false));	 
	}
	@Override
	protected void setupNonDrawableObjects(){
	 
	}
	@Override
	protected void setupLights(){
		getLightManagerRef().addLight(new Light(0.0f, 5.0f, 1.5f, LightingTypes.POINT_LIGHTING));
	}
	@Override
	protected void setupCameras(){
		 //Creates and setup Cameras
		 getCameraManagerRef().addCamera(getCameraManagerRef().DEFAULT_CAMERA());
		 getCameraManagerRef().getSelectedCamera().setupOrbit(getDrawableObjectsMap().get("T1"), new float[]{0.0f, 1.0f, 0.0f}, 0.1f);
	}
	@Override
	protected void setupLinks(){
	 
	}
	
	/////
	@Override
	public void runNDraw() {
		super.runNDraw();
	}
}
