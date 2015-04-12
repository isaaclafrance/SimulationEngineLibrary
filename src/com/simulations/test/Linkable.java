package com.simulations.test;

public interface Linkable{
	public void setInitialTargetLengths();	
	public void setNewTargetLengths(PhysicalObject obj1, PhysicalObject obj2, float newLength);
	
	public float getTargetLength(PhysicalObject obj1, PhysicalObject obj2);
	public Boolean hasObject(PhysicalObject obj);
	
	public void addConnection(PhysicalObject obj1, PhysicalObject obj2);
	public void addConnection(int obj1Index, int obj2Index);
	
	public void removeConnection(PhysicalObject obj1, PhysicalObject obj2);
	public void removeConnection(int obj1Index, int obj2Index);
	
    //Deletes object in link and returns separated links resulting from removal	
	public void removeObject(PhysicalObject obj); 
	public void removeObject(int objIndex); 
	
	public void resolveLinksLength();
	
	public void animate();
}
