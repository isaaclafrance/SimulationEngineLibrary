package com.simulations.test;

import java.util.ArrayList;

public abstract class CollisionHandler {
	//Fields
	protected PhysicsEngine physicsEngineRef;
	final protected ArrayList<Float[]> boundaryPlanes = new ArrayList<Float[]>(); //The first index identifies the boundary. The second index identifies plane parameters associated with boundary. ie. Ax+By+Cz+D = 0
	
	//Constructors
	public CollisionHandler(){
	}
	public CollisionHandler(Float[][] boundaryPlanes){
		for(int i=0; i<boundaryPlanes.length; i++){
			addBoundaryPlane(boundaryPlanes[i]);
		}
	}
	
	//**//Helper functions
	abstract public Boolean areObjectsCloseEnough(PhysicalObject o1, PhysicalObject o2);
	private float pointToPlane_Dist(float pointX, float pointY, float pointZ, float planeCoeffsA, float planeCoeffsB, float planeCoeffsC, float planeCoeffsD){
		float dist = (float) (( planeCoeffsA*pointX+planeCoeffsB*pointY+planeCoeffsC*pointZ+ planeCoeffsD)
						/
						Math.sqrt(planeCoeffsA*planeCoeffsA + planeCoeffsB*planeCoeffsB + planeCoeffsC*planeCoeffsC)) ;
		return dist;
	}
	
	private void findPenLengthNTarget(PhysicalObject obj, int vertexNum, Float[] planeCoeffs, float[] len){
		float length, target;
		
		//Determines which corner on the bounding box is likely to collide with plane
		//float xBLen = obj.bManager.boundBoxLength[0+vertexNum*3]/2;
		//float yBLen = obj.bManager.boundBoxLength[1+vertexNum*3]/2;
		//float zBLen = obj.bManager.boundBoxLength[2+vertexNum*3]/2;
		float xBLen = obj.bManager.boundBoxLength[0]/2;
		float yBLen = obj.bManager.boundBoxLength[1]/2;
		float zBLen = obj.bManager.boundBoxLength[2]/2;
		
		xBLen = (planeCoeffs[0]>0)? -xBLen:xBLen;
		yBLen = (planeCoeffs[1]>0)? -yBLen:yBLen;
		zBLen = (planeCoeffs[2]>0)? -zBLen:zBLen;

		//target = (float) Math.sqrt(xBLen*xBLen + yBLen*yBLen + zBLen*zBLen);	
		target = planeCoeffs[0]*xBLen + planeCoeffs[1]*yBLen + planeCoeffs[2]*zBLen;
		
		xBLen += obj.getPosition()[0];
		yBLen += obj.getPosition()[1];
		zBLen += obj.getPosition()[2];

		length = target + pointToPlane_Dist(xBLen, yBLen, zBLen, planeCoeffs[0], planeCoeffs[1], planeCoeffs[2], planeCoeffs[3]);
			
		if( length < 0){	
			len[0] = length;
			len[1] = target;
		}
		else{
			len[0] = -1;
		}
	}
	//**//
	
	public void resolve_BorderObject_Collision(float w, float h){
		//Determine collision between object and border planes as determined by length and width of screen
		float length, target;
		
		for(PhysicalObject object:physicsEngineRef.getPhyObjects()){
			if(object.getPosition()[0]-object.bManager.boundBoxLength[0]/2  < -w/2.0f){
				length = Math.abs(object.getPosition()[0]+w/2.0f);
				target = object.bManager.boundBoxLength[0]/2;
				object.getPosition()[0] += (length - target)/length*object.getPosition()[0]*0.5f;
				//object.position[0] = -w/2;
			}
			if(object.getPosition()[0]+object.bManager.boundBoxLength[0]/2 > w/2.0f){
				length = Math.abs(object.getPosition()[0]-w/2.0f);
				target = object.bManager.boundBoxLength[0]/2;
				object.getPosition()[0] += (length - target)/length*object.getPosition()[0]*0.5f;
				//object.position[0] = w/2;
			}			
			if(object.getPosition()[1]-object.bManager.boundBoxLength[1]/2  < -h/2.0f){
				length = Math.abs(object.getPosition()[1]+h/2.0f);
				target = object.bManager.boundBoxLength[1]/2;
				object.getPosition()[1] += (length - target)/length*object.getPosition()[1]*0.5f;
				//object.position[1] = -h/2;
			}
			if(object.getPosition()[1]+object.bManager.boundBoxLength[1]/2 > h/2.0f){
				length = Math.abs(object.getPosition()[1]-h/2.0f);
				target = object.bManager.boundBoxLength[1]/2;
				object.getPosition()[1] += (length - target)/length*object.getPosition()[1]*0.5f;
				//object.position[1] = h/2;
			}
		}
	}
	public void resolve_BorderObject_Collision(){
		if(boundaryPlanes.size() != 0){
			//Determines collision between object and border planes through plane parameters.
			float x, y, z, length, target, factor;
			float[] len = Quaternion.scratchVec1;
			
			for(PhysicalObject object:physicsEngineRef.getPhyObjects()){
				
				for(Float[] planeCoeffs:boundaryPlanes){
					for(int i=0;i<8;i++){
						findPenLengthNTarget(object, i, planeCoeffs, len);
						
						if(len[0] != -1){
							length = len[0];
							target = len[1];
							
							if(length < target && len[0] != -1){						
								x = planeCoeffs[0]*length;
								y = planeCoeffs[1]*length;
								z = planeCoeffs[2]*length;
								
								factor = (length - target)/length;
								object.getPosition()[0] -= factor*x*0.5f;
								object.getPosition()[1] -= factor*y*0.5f;
								object.getPosition()[2] -= factor*z*0.5f;
							}
						}
					}
				}
			}
		}
	}
	public void resolve_IndividualBorderObject_Collision(){
		//Determines collision between object and their individual border planes through plane parameters.
		float x,y,z, length, target, factor;
		float[] len = Quaternion.scratchVec1;
		
		for(PhysicalObject object:physicsEngineRef.getPhyObjects()){
			if(object.boundaryPlanes.size() != 0){
				for(Float[] planeCoeffs:object.boundaryPlanes){
					for(int i=0;i<8;i++){
						findPenLengthNTarget(object, i, planeCoeffs, len);					
						
						if(len[0] != -1){
							length = len[0];
							target = len[1];
							
							if(length < target && len[0] != -1){
								x = planeCoeffs[0]*length;
								y = planeCoeffs[1]*length;
								z = planeCoeffs[2]*length;
								
								factor = (length - target)/length;
								object.getPosition()[0] -= factor*x*0.5f;
								object.getPosition()[1] -= factor*y*0.5f;
								object.getPosition()[2] -= factor*z*0.5f;
							}
						}
					}
				}
			}
		}
	}
	
	abstract public void resolve_ObjectObject_Collision();
	
	abstract public void run();
	abstract public void run(int w, int h);
	
	////
	public void addBoundaryPlane(Float[] plane){
		boundaryPlanes.add(plane);
	}
	public void removeBoundaryPlane(int index){
		boundaryPlanes.remove(index);
	}
	
	////
	
	public void setPhysicsEngineRef(PhysicsEngine physicsEngine){
		this.physicsEngineRef = physicsEngine;
	}
}
