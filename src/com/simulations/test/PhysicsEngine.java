package com.simulations.test;

import java.util.ArrayList;

public abstract class PhysicsEngine{
	private CollisionHandler cHandler;
	private ArrayList<Linkable> links; 
	private ArrayList<PhysicalObject> phyObjects; 
	private float[] const_accel;
	
	public PhysicsEngine(float[] const_accel){
		this.phyObjects = new ArrayList<PhysicalObject>();
		this.links = new ArrayList<Linkable>();
		this.const_accel = const_accel.clone();
	}
	
	/////
	abstract public void run(float steps);
	abstract public void inertia(float delta);
	abstract public void acceleration(float delta);
	
	public void addPhysicalObject(PhysicalObject phyObj){ 
		phyObjects.add(phyObj);
	}
	public void addPhysicalObjects(PhysicalObject[] phyObjs){ 
		for(PhysicalObject phyObj:phyObjects){
			addPhysicalObject(phyObj);
		}
	}
	public void removePhysicalObject(PhysicalObject phyObj){
		phyObjects.remove(phyObj);
	}
	
	public void addLink(Linkable link){ 
		links.add(link);
	}
	public void addLinks(Linkable[] links){ 
		for(Linkable link:links){
			addLink(link);
		}
	}
	public void removeLink(Linkable link){
		links.remove(link);
	}

	public CollisionHandler getCollisionHandler() {
		return cHandler;
	}
	public void setConstantAccerlation(float x, float y, float z){
		const_accel[0] = x;
		const_accel[1] = y;
		const_accel[2] = z;
	}
	public ArrayList<Linkable> getLinks() {
		return links;
	}
	public ArrayList<PhysicalObject> getPhyObjects() {
		return phyObjects;
	}
		
	public void setCollisionHandler(CollisionHandler cHandler) {
		this.cHandler = cHandler;
		cHandler.setPhysicsEngineRef(this);
	}
	public float[] getConstAcceleration() {
		return const_accel;
	}
	
	/////
	
}
