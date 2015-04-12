package com.simulations.test;

//TODO: Implement collision handling that conserves momentum and/or kinetic energy

public class SphereAABBCollisionHandler extends CollisionHandler{
	
	public SphereAABBCollisionHandler(){
		super();
	}
	public SphereAABBCollisionHandler(Float[][] boundaryPlanes){
		super(boundaryPlanes);
	}
	
	//**//Helper functions
	private void findLeastPenetratingLengths(PhysicalObject o1, PhysicalObject o2, float[] len){
		float greatestSeparation = 0; //Penetration decreases as the distance between the objects increases.
		float lpa = 0;;
		float targetSeparation = 0; 
		int axisIndexSum = 0;
		
		float sep, pen, target;
		
		target = (o1.bManager.boundBoxLength[0]+o2.bManager.boundBoxLength[0])/2; 
		sep = Math.abs(o1.getPosition()[0]-o2.getPosition()[0]);
		pen = target - sep;
		for(int i=0; i<3; i++){
			target = (o1.bManager.boundBoxLength[i]+o2.bManager.boundBoxLength[i])/2; 
			sep = Math.abs(o1.getPosition()[i]-o2.getPosition()[i]);
			if( pen > (target - sep) && sep<target){
				pen = (target - sep);
				greatestSeparation = sep;
				targetSeparation = target;
				lpa = i;
				axisIndexSum += i;
			}
		}  
			
		if(axisIndexSum == 3){
			len[0] = greatestSeparation; 
			len[1] = targetSeparation; 
			len[2] = lpa;
		}
		else{
			len[0] = -1;
		}
	}
	@Override
	public Boolean areObjectsCloseEnough(PhysicalObject o1, PhysicalObject o2){
		float length = (float) Math.sqrt(Math.pow(o1.getPosition()[0]-o2.getPosition()[0],2) + Math.pow(o1.getPosition()[1]-o2.getPosition()[1],2) + Math.pow(o1.getPosition()[2]-o2.getPosition()[2],2));
		float target = o1.bManager.boundRadius + o2.bManager.boundRadius;
		
		return (Math.abs(length)<target);
	}
	//**//
	
	@Override
	public void resolve_ObjectObject_Collision(){
		float x, y , z , length, target, factor, lpa;
		float[] len = Quaternion.scratchVec1;
		
		for(PhysicalObject object1:physicsEngineRef.getPhyObjects()){
			for(PhysicalObject object2:physicsEngineRef.getPhyObjects()){
				length = (float) Math.sqrt(Math.pow(object1.getPosition()[0]-object2.getPosition()[0],2) + Math.pow(object1.getPosition()[1]-object2.getPosition()[1],2) + Math.pow(object1.getPosition()[2]-object2.getPosition()[2],2));
				target = object1.bManager.boundRadius + object2.bManager.boundRadius;
				
				if(!object1.equals(object2)&& (length<target)){
					findLeastPenetratingLengths(object1, object2, len);
					
					if(len[0] != -1){
						length = len[0];
						target = len[1];
						lpa = len[2];
					    //length = object1.position[lpa]-object2.position[lpa];	
					    //target = object1.bManager.boundBoxLength[lpa]+object2.bManager.boundBoxLength[lpa];
					    
						object1.getPosition()[(int)lpa] -= (length - target)*0.5f;
						object2.getPosition()[(int)lpa] += (length - target)*0.5f;
						
						//TODO: In order to make collision look more realistic , perform resolution that conserves energy.
						//factor = (length - target)/length;
						//x = object1.position[0] - object2.position[0];
						//y = object1.position[1] - object2.position[1];
						//z = object1.position[2] - object2.position[2];
						//object1.position[0] -= x*factor*0.5f;
						//object1.position[1] -= y*factor*0.5f;
						//object1.position[2] -= z*factor*0.5f;
						//object2.position[0] += x*factor*0.5f;
						//object2.position[1] += y*factor*0.5f;
						//object2.position[2] += z*factor*0.5f;
						//object1.position[lpa] -= factor*0.5f;
						//object2.position[lpa] += factor*0.5f;
					}
				}
			}
		}
	}

	@Override
	public void run(int w, int h){
		resolve_ObjectObject_Collision();	
		resolve_BorderObject_Collision(w, h);
		resolve_IndividualBorderObject_Collision();		
	}
	@Override
	public void run(){
		resolve_ObjectObject_Collision();
		resolve_BorderObject_Collision();
		resolve_IndividualBorderObject_Collision();
	}
}
