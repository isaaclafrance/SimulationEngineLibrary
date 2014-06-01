package com.simulations.test;

import java.util.ArrayList;
import java.util.Collections;

public class MultiLink implements Linkable{
	public ArrayList<PhysicalObject> linkedObjects;
	//Each connection consists of two connected objects and a targeted length
	//[n][0]-->first obj, [n][1]-->second obj, [n][2]-->target length between 1st and 2nd obj
	public ArrayList<float[]> connections;
	public float elasticityFactor; //Range: 0.0-1.0
	
	//Constructor
	public MultiLink(PhysicalObject linkedObjects[], float elasticityFactor){
		Collections.addAll(this.linkedObjects, linkedObjects);
		this.connections = new ArrayList<float[]>(linkedObjects.length);
		this.elasticityFactor = elasticityFactor;
		setInitialTargetLengths();
	}
	
	//Helper functions
	protected int findConnectionIndex(int pO1I, int pO2I){
		for(int i=0; i<connections.size(); i++){
			if((connections.get(i)[0]==pO1I && connections.get(i)[1]==pO2I)){
				return i;
			}
		}
		return -1;
	}
	protected int findObjectIndex(PhysicalObject obj){
		return linkedObjects.lastIndexOf(obj);
	}
	@Override
	public Boolean hasObject(PhysicalObject obj){
		for(PhysicalObject o:linkedObjects){
			if(obj.equals(o)){
				return true;
			}
		}
		return false;
	}
	@Override
	public float getTargetLength(PhysicalObject obj1, PhysicalObject obj2){	
		if(hasObject(obj1) && hasObject(obj2)){
			int oI1 = findObjectIndex(obj1);
			int oI2 = findObjectIndex(obj2);			
			
			return connections.get(findConnectionIndex(oI1, oI2))[2];
		}
		else{
			return -1;
		}
	}
	protected void addObject(PhysicalObject obj){
		linkedObjects.add(obj);
	}
	protected void addMemberConnection(int oI1, int oI2, float targetLength){
		connections.add(new float[]{oI1,oI2,targetLength});
	}
	//

	@Override
	public void setInitialTargetLengths(){
		//Connections are established in the order by which the objects appear in the linkedObjects list.
		//In other words every, object is linked to its adjacent object in the list.
		
		for(int i=0; i<linkedObjects.size()-1; i++){
			float x = linkedObjects.get(i).getPosition()[0]-linkedObjects.get(i+1).getPosition()[0];
			float y = linkedObjects.get(i).getPosition()[1]-linkedObjects.get(i+1).getPosition()[1];
			float z = linkedObjects.get(i).getPosition()[2]-linkedObjects.get(i+1).getPosition()[2];
			
			connections.get(i)[0] = i;
			connections.get(i)[1] = i+1;
			connections.get(i)[2] = (float)Math.sqrt(x*x + y*y + z*z);
		}
	}
	@Override
	public void setNewTargetLengths(PhysicalObject obj1,
			PhysicalObject obj2, float newLength) {
		if(hasObject(obj1) && hasObject(obj2)){
			int oI1 = findObjectIndex(obj1);
			int oI2 = findObjectIndex(obj2);

			connections.get(findConnectionIndex(oI1, oI2))[2] = newLength;			
		}
	}

	@Override
	public void addConnection(PhysicalObject obj1, PhysicalObject obj2) { 
		if(!hasObject(obj1)){
			addObject(obj1);
		}
		if(!hasObject(obj2)){
			addObject(obj2);
		}
		
		int oI1 = findObjectIndex(obj1);
		int oI2 = findObjectIndex(obj2);
		
		addConnection(oI1, oI2);
	}
	@Override
	public void addConnection(int obj1Index, int obj2Index) {
		PhysicalObject obj1 = linkedObjects.get(obj1Index);
		PhysicalObject obj2 = linkedObjects.get(obj2Index);
		
		float x = obj1.getPosition()[0]-obj2.getPosition()[0];
		float y = obj1.getPosition()[1]-obj2.getPosition()[1];
		float z = obj1.getPosition()[2]-obj2.getPosition()[2];
		
		float targetLength = (float)Math.sqrt(x*x + y*y + z*z);
		
		addMemberConnection(obj1Index, obj1Index, targetLength);
	}

	@Override
	public void removeConnection(PhysicalObject obj1,
			PhysicalObject obj2) {
		removeConnection(findObjectIndex(obj1), findObjectIndex(obj2));
	}
	@Override
	public void removeConnection(int oI1, int oI2){
		int cIndex = findConnectionIndex(oI1, oI2);
		
		if(cIndex != -1){
			//Remove connection
			connections.remove(cIndex);
	
		}
	}

	@Override
	public void removeObject(PhysicalObject obj) {
		for(int i=0; i<connections.size(); i++){
			PhysicalObject connObj1 = linkedObjects.get((int) connections.get(i)[0]);
			if(connObj1.equals(obj)){
				PhysicalObject connObj2 = linkedObjects.get((int) connections.get(i)[1]);				
				removeConnection(connObj1, connObj2);
			}
		}
	}	
	@Override
	public void removeObject(int objIndex) {
		removeObject(linkedObjects.get(objIndex));
	}		
	
	@Override
	public void resolveLinksLength() {
		for(int i=0; i<connections.size(); i++){
			PhysicalObject obj1 = linkedObjects.get((int)connections.get(i)[0]);
			PhysicalObject obj2 = linkedObjects.get((int)connections.get(i)[1]);
			float targetLength = connections.get(i)[2];
			
			float x = obj1.getPosition()[0]-obj2.getPosition()[0];
			float y = obj1.getPosition()[1]-obj2.getPosition()[1];
			float z = obj1.getPosition()[2]-obj2.getPosition()[2];
			
			float length = (float)Math.sqrt(x*x + y*y + z*z);
			float factor = (length-targetLength)/length;
			
			obj1.getPosition()[0] -= factor*x*0.5f*elasticityFactor;
			obj1.getPosition()[1] -= factor*y*0.5f*elasticityFactor;
			obj1.getPosition()[2] -= factor*z*0.5f*elasticityFactor;
			
			obj2.getPosition()[0] += factor*x*0.5f*elasticityFactor;
			obj2.getPosition()[1] += factor*y*0.5f*elasticityFactor;
			obj2.getPosition()[2] += factor*z*0.5f*elasticityFactor;
		}
	}
	
	@Override
	public void animate(){
		resolveLinksLength();
	}
}

