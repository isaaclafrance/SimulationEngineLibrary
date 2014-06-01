package com.simulations.test;

public class Light extends GT_Point{
	private float ambientLevel;
	private boolean isUpdated;
	private LightManager.LightingTypes lighting_type;
	
	public Light(float x, float y, float z, LightManager.LightingTypes lighting_type) {
		super(x, y, z, 1.0f, 1.0f, 1.0f, 0.75f, 10.0f);
		
		this.lighting_type = lighting_type;
		this.isUpdated = true;
	}
	
	public short getLightTypeNumber(){
		switch (lighting_type){
		case POINT_LIGHTING:
			return 0;
		case SPOT_LIGHTING:
			return 1;
		default:
			return 0;
		}
	}
	
	////
	public void setLightType(LightManager.LightingTypes lighting_type){
		this.lighting_type = lighting_type;
		this.isUpdated = true;
	}
	public void setAmbientLevel(float ambientLevel){
		this.ambientLevel = ambientLevel;
		this.isUpdated = true;
	}
	@Override
	public void setPosition(float x, float y, float z){
		super.setPosition(x, y, z);
		isUpdated = true;
	}
	public void setUpdatedState(boolean state){
		this.isUpdated = state;
	}
	
	public LightManager.LightingTypes getLightType(){
		return lighting_type;
	}
	public float getAmbientLevel(){
		return ambientLevel;
	}
	public boolean getUpdatedState(){
		return isUpdated;
	}
	
	@Override
	public void animate(){
		super.animate();
	}
}
