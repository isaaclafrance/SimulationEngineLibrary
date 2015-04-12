package com.simulations.test;

import java.util.ArrayList;
import android.content.Context;

//This class is a container class that initializes links into an a array of GT_Line objects. It will use the connections list to determine how to transform and rotate each GT_Line object based on the position of the objects linked. This container class will implement the GDrawable interface although will not itself be technically drawable, rather it will use its draw function to call the draw functions of each individual GT_Line object.     

public final class GT_DrawableMultiLink extends MultiLink implements GDrawable{
	private Quaternion orientationQuaternion;
	public ArrayList<GT_Line> linesList;
	public float[] color;
	
	public GT_DrawableMultiLink( PhysicalObject linkedObjects[], float elasticityFactor,
					float r, float g, float b, float transparency) {
		super(linkedObjects, elasticityFactor);

		this.linesList = new ArrayList<GT_Line>(linkedObjects.length-1);
		this.color = new float[]{r, g, b, transparency};
		
		orientationQuaternion = new Quaternion();
		
		setInitialTargetLengths();
	}

	//Helper functions
	private void addConnectionLine(float[] pos1, float[] pos2){
		linesList.add(new GT_Line((pos1[0]+pos2[0])/2, (pos1[1]+pos2[1])/2, (pos1[2]+pos2[2])/2,
													   color[0], color[1], color[2], color[3],
													   new float[]{pos1[0], pos1[1], pos1[2], pos2[0], pos2[1], pos2[2]}, new short[]{0,1}));
	}

	////
	@Override
	public void setInitialTargetLengths(){
		//Connections are established in the order by which the objects appear in the linkedObjects list.
		//In other words every, object is linked to its adjacent object in the list.
		float x1,y1,z1,x2,y2,z2;
		float x,y,z;

		for(int i=0; i<linkedObjects.size()-1; i++){
			x1 = linkedObjects.get(i).getPosition()[0];
			y1 = linkedObjects.get(i).getPosition()[1];
			z1 = linkedObjects.get(i).getPosition()[2];
			x2 = linkedObjects.get(i+1).getPosition()[0];
			y2 = linkedObjects.get(i+1).getPosition()[1];
			z2 = linkedObjects.get(i+1).getPosition()[2];
			
			x = x1-x2;
			y = y1-y2;
			z = z1-z2;
			
			connections.get(i)[0] = i;
			connections.get(i)[1] = i+1;
			connections.get(i)[2] = (float)Math.sqrt(x*x + y*y + z*z);
			linesList.set( i, new GT_Line((x1+x2)/2.0f, (y1+y2)/2.0f, (z1+z2)/2.0f, 
											color[0], color[1], color[2], color[3], 
											new float[]{x1, y1, z1, x2, y2, z2}, new short[]{0, 1}));
		}
	}	
	@Override
	public void setNewTargetLengths(PhysicalObject obj1,
			PhysicalObject obj2, float newLength) {
		if(hasObject(obj1) && hasObject(obj2)){
			int oI1 = findObjectIndex(obj1);
			int oI2 = findObjectIndex(obj2);
			int connectionIndex = findConnectionIndex(oI1, oI2);
			float scaleFactor = newLength/connections.get(connectionIndex)[2];
			
			connections.get(connectionIndex)[2] = newLength;	
			
			//Scale the corresponding drawable line
			linesList.get(connectionIndex).getScale()[0] *= scaleFactor;
			linesList.get(connectionIndex).getScale()[1] *= scaleFactor;
			linesList.get(connectionIndex).getScale()[2] *= scaleFactor;
		}
	}
	
	////
	@Override
	public void addConnection(int obj1Index, int obj2Index) {
		PhysicalObject obj1 = linkedObjects.get(obj1Index);
		PhysicalObject obj2 = linkedObjects.get(obj2Index);
		
		float x1 = obj1.getPosition()[0];
		float y1 = obj1.getPosition()[1];
		float z1 = obj1.getPosition()[2];
		
		float x2 = obj2.getPosition()[0];
		float y2 = obj2.getPosition()[1];
		float z2 = obj2.getPosition()[2];
		
		float x = x1-x2;
		float y = y1-y2;
		float z = z1-z2;
		
		float targetLength = (float)Math.sqrt(x*x + y*y + z*z);
		
		addMemberConnection(obj1Index, obj1Index, targetLength);
		addConnectionLine(new float[]{x1,y1,z1}, new float[]{x2,y2,z2});	
	}
	@Override
	public void removeConnection(int oI1, int oI2){
		int cIndex = findConnectionIndex(oI1, oI2);
		
		//Delete line object and  resources used by object
		linesList.get(cIndex).clearAll();
		linesList.remove(cIndex);
	}
	@Override
	public void removeObject(PhysicalObject obj) {
		PhysicalObject connObj1, connObj2;
		for(int i=0; i<connections.size(); i++){
			connObj1 = linkedObjects.get((int) connections.get(i)[0]);
			connObj2 = linkedObjects.get((int) connections.get(i)[1]);
			if(connObj1.equals(obj)){			
				removeConnection(connObj1, connObj2);
			}
			else if(connObj2.equals(obj)){		
				removeConnection(connObj2, connObj1);
			}
		}	
	}		
	
	public void updateLines(){
		float x1,y1,z1,x2,y2,z2;
		float dx1,dy1,dz1,dx2,dy2,dz2;
		float dot, angle;
		
		for(int i=0; i<linesList.size(); i++){
			x1 = linkedObjects.get((int) connections.get(i)[0]).getPosition()[0];
			y1 = linkedObjects.get((int) connections.get(i)[0]).getPosition()[1];
			z1 = linkedObjects.get((int) connections.get(i)[0]).getPosition()[2];
			
			x2 = linkedObjects.get((int) connections.get(i)[1]).getPosition()[0];
			y2 = linkedObjects.get((int) connections.get(i)[1]).getPosition()[1];
			z2 = linkedObjects.get((int) connections.get(i)[1]).getPosition()[2];
			
			//*Update Position
			linesList.get(i).getPosition()[0] = (x1+x2)/2;
			linesList.get(i).getPosition()[1] = (y1+y2)/2;
			linesList.get(i).getPosition()[2] = (z1+z2)/2;
			
			//*Update Orientation
				//calculate rotation angle
				dx1 = x1-x2;
				dy1 = y1-y2;
				dz1 = z1-z2;
				
				dx2 = linesList.get(i).vManager.getShapeCoords().get(0) - linesList.get(i).vManager.getShapeCoords().get(3);
				dy2 = linesList.get(i).vManager.getShapeCoords().get(1) - linesList.get(i).vManager.getShapeCoords().get(4);
				dz2 = linesList.get(i).vManager.getShapeCoords().get(2) - linesList.get(i).vManager.getShapeCoords().get(5);
				
				dot = dx1*dx2 + dy1*dy2 + dz1*dz2;
				angle = (float) Math.acos(dot/(Math.sqrt(dx1*dx1+dy1*dy1+dz1*dz1)*Math.sqrt(dx2*dx2+dy2*dy2+dz2*dz2)));
				
				//calculate axis of rotation
				Quaternion.scratchVec1[0] = dy1*dz2 - dz1*dy2;
				Quaternion.scratchVec1[1] = dz1*dx2 - dx1*dz2;
				Quaternion.scratchVec1[2] = dx1*dy2 - dy1*dx2;
				
				//Update the orientation of line
				linesList.get(i).getOrientation().fromAxis_Angle_SelfMultiply(Quaternion.scratchVec1, angle);
		}
	}
	@Override
	public void updateShadersNTextures(Context context){
		for(GT_Line line :linesList){
			line.updateShadersNTextures(context);
		}	
	}
	@Override
	public void clearAll(){
		for(GT_Line gTL:linesList){
			gTL.clearAll();
		}	
	}
	
	@Override
	public void initBuffers(){
		for(GT_Line line:linesList){
			line.initBuffers();
		}	
	}
	
	@Override
	public void draw(float[] mVMatrix, float[] mPMatrix){
		for(GT_Line line:linesList){
			line.draw(mVMatrix, mPMatrix);
		}
	}

	@Override
	public void animate(){
		for(GT_Line line:linesList){
			line.animate();
		}
		super.animate();
		
		updateLines();
	}	
	
	////
	@Override
	public void setSceneRef(Scene scene){
		for(GT_Line line:linesList){
			line.setSceneRef(scene);
		}
	}
	@Override
	public void setLightState(boolean state){
		for(GT_Line line:linesList){
			line.setLightState(state);
		}	
	}
	@Override
	public void setBillboardState(boolean state) {
		for(GT_Line gTL:linesList){
			gTL.setBillboardState(state);
		}		
	}	
	public void setTransparency(float value){
		for(GT_Line gtLine:linesList){
			gtLine.setTransparency(value);
		}
	}
	
	@Override
	public float getTransparency(){
		float value = 0.0f;
		int count = 0;
		
		for(GT_Line gtLine:linesList){
			value += gtLine.getTransparency();
			count++;
		}
		
		return value/count;		
	}
	@Override
	public boolean getBillboardState(){
		boolean state = true;
		
		for(GT_Line gtLine:linesList){
			if(!gtLine.getBillboardState()){
				state = false;
			}
			break;
		}
		
		return state;
	}
	@Override
	public boolean getLightState() {
		boolean state = true;
		
		for(GT_Line gtLine:linesList){
			if(!gtLine.getLightState()){
				state = false;
			}
			break;
		}
		
		return state;
	}

	////
	@Override
	public void setVertexShaderLocID(int locID) {
		
	}
	@Override
	public void setFragmentShaderLocID(int locID) {
		
	}
	@Override
	public void setPosition(float x, float y, float z) {
		
	}	
		
	@Override
	public float[] getPosition(){ //Returns the average postions of all the lines
		float posX = 0.0f, posY = 0.0f, posZ = 0.0f;
		int count = 0;
		
		for(GT_Line gtLine:linesList){
			posX += gtLine.getPosition()[0];
			posY += gtLine.getPosition()[1];
			posZ += gtLine.getPosition()[2];
			count++;
		}
		
		return new float[]{posX/count, posY/count, posZ/count};
	}
	@Override
	public boolean getTextureState() {
		return false;
	}
	@Override
	public Quaternion getOrientation(){
		return orientationQuaternion;
	}


}





















