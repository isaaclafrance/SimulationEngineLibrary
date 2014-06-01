package com.simulations.test;

import java.util.ArrayList;

public class PhysicalObject extends Transformable{
	//Fields
	public float[] forceAccum;
	public float[] acceleration;
	public float mass;
		
	private float[] oldPosition;
	private float posDisp[];
	public float direcDisp; //stores angular velocity
		
	final ArrayList<Float[]> boundaryPlanes = new ArrayList<Float[]>(); //This stores parameters used to define planes. The planes will be used collision detection module to limit an object' motion. Instance of a physical object has it own set of boundary planes.
	public final BoundingManager bManager;
	
	//Constructors
	public PhysicalObject(float x, float y, float z, float mass, Float[][] boundaryPlanes, BoundingManager.BoundingBoxTypes bbType){
		super(x,y,z);
		
		this.forceAccum = new float[3];
		this.acceleration = new float[]{0.0f, 0.0f, 0.0f};
		this.mass = (mass == 0.0f)?1.0f:mass;
	
		oldPosition = new float[]{x,y,z};
		
		posDisp = new float[]{0.0f, 0.0f, 0.0f};
		direcDisp = 0.0f;
		
		for(int i=0; i<boundaryPlanes.length; i++){
			addBoundaryPlane(boundaryPlanes[i]);
		}
		
		bManager = new BoundingManager(bbType, getPosition());		
	}

	////
	public void addBoundaryPlane(Float[] plane){
		boundaryPlanes.add(plane);
	}
	public void removeBoundaryPlane(int index){
		boundaryPlanes.remove(index);
	}
	
	////
	public void setLinearVelocity(float xv, float yv, float zv){
		oldPosition[0] = getPosition()[0] - xv; 
		oldPosition[1] = getPosition()[1] - yv; 
		oldPosition[2] = getPosition()[2] - zv; 
	}	
	private void setAcceleration(){
		acceleration[0] = forceAccum[0]/mass;
		acceleration[1] = forceAccum[1]/mass;
		acceleration[2] = forceAccum[2]/mass;
	}
	public void setOldPosition(float x, float y, float z){
		oldPosition[0] = x;
		oldPosition[1] = y;
		oldPosition[2] = z;
	}
	public void setForceAccum(float x, float y, float z){
		forceAccum[0] = x;
		forceAccum[1] = y;
		forceAccum[2] = z;
	}
	public void setAcceleration(float x, float y, float z){
		acceleration[0] = x;
		acceleration[1] = y;
		acceleration[2] = z;
	}
	public void setMass(float mass){
		this.mass = mass;
	}
	
	public float[] getLinearVelocity(){
		return posDisp;
	}
	public float[] getOldPosition(){
		return oldPosition;
	}
	public float getMass(){
		return mass;
	}
	public float[] getForceAccum(){
		return forceAccum;
	}
	public float[] getAcceleration(){
		return acceleration;
	}
	
	////
	public void update(){
		posDisp[0] = getPosition()[0] - oldPosition[0];
		posDisp[1] = getPosition()[1] - oldPosition[1];
		posDisp[2] = getPosition()[2] - oldPosition[2];
		
		bManager.updateBoundingBox(getOrientationQuaternion(), getScale());
	}
	
	@Override
	public void animate(){
		super.animate();
		setAcceleration();
	}

	////
	public ArrayList<Float[]> getBoundaryPlanes(){
		return boundaryPlanes;
	}
}
