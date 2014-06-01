package com.simulations.test;

public class BoundingManager {
	public static enum BoundingBoxTypes {AABB, AOBB, NONE};
	public final BoundingBoxTypes bbType;
	public float[] cornerPositions;
	public float boundRadius;
	public float[] oldScale;
	public float boundBoxLength[];	
	private float[] old_QuaterionComponents;
	
	public BoundingManager(BoundingBoxTypes bbType, float[] position){
		boundRadius = 0.0f;
		oldScale = new float[]{1.0f, 1.0f, 1.0f};
		boundBoxLength = new float[3];	
		old_QuaterionComponents = new float[4];
		cornerPositions = new float[24];
		this.bbType = bbType;
	}
	
	private void setInitialCornerVertices(){
		cornerPositions[0] = cornerPositions[3] = cornerPositions[6] = cornerPositions[9] = boundBoxLength[0]/2; //x Right
			cornerPositions[1] = cornerPositions[4] = boundBoxLength[1]/2; //y top
				cornerPositions[2] = -boundBoxLength[2]/2;//z back
				cornerPositions[5] = boundBoxLength[2]/2; //z front
			cornerPositions[7] = cornerPositions[10] = -boundBoxLength[1]/2;//y	bottom		
				cornerPositions[8] = -boundBoxLength[2]/2;//z back
				cornerPositions[11] = boundBoxLength[2]/2; //z front
			
		cornerPositions[12] = cornerPositions[15] = cornerPositions[18] = cornerPositions[21] = -boundBoxLength[0]/2; //x Left
			cornerPositions[13] = cornerPositions[16] = boundBoxLength[1]/2; //y top
				cornerPositions[14] = -boundBoxLength[2]/2;//z back
				cornerPositions[17] = boundBoxLength[2]/2; //z front
			cornerPositions[19] = cornerPositions[22] = -boundBoxLength[1]/2;//y	bottom		
				cornerPositions[20] = -boundBoxLength[2]/2;//z back
				cornerPositions[23] = boundBoxLength[2]/2; //z front
	}
	
	public void updateBoundingBox(Quaternion orientationQuaternion, float[] scale){
		if(oldScale != scale){
			resizeBB(scale);
		}
		
		//Rotate and reconfigure bounding boxes only if there is a change in orientation of object
		if(	old_QuaterionComponents[0] != orientationQuaternion.x &&
			old_QuaterionComponents[1] != orientationQuaternion.y &&
			old_QuaterionComponents[2] != orientationQuaternion.z &&
			old_QuaterionComponents[3] != orientationQuaternion.w){
			switch (bbType) {
				case AABB:
					updateAABB(orientationQuaternion);
					break;
				case AOBB:
					updateAOBB(orientationQuaternion);
					break;
				case NONE:
					break;
			}
		}
		
		//Record current quaternion components to be used later
		old_QuaterionComponents[0] = orientationQuaternion.x;
		old_QuaterionComponents[1] = orientationQuaternion.y;
		old_QuaterionComponents[2] = orientationQuaternion.z;
		old_QuaterionComponents[3] = orientationQuaternion.w;
	}
	
	private void resizeBB(float[] scale){
		//Resize bounding box local axis lengths
		boundBoxLength[0] /= oldScale[0];
		boundBoxLength[1] /= oldScale[1];
		boundBoxLength[2] /= oldScale[2];
		boundBoxLength[0] *= scale[0];
		boundBoxLength[1] *= scale[1];
		boundBoxLength[2] *= scale[2];
		
		//Resize bounding radius lengths based on scaled local axis of greatest length
		int maxLengthIndex = 0;
		for(int i=0; i<3; i++){
			if(boundBoxLength[i]>boundBoxLength[maxLengthIndex]){
				maxLengthIndex = i;
			}
		}
		
		boundRadius = boundBoxLength[maxLengthIndex]/2;
		
		oldScale = scale;
	}
	private void updateAABB(Quaternion orientationQuaternion){
		float[] vec = new float[4];
		
		for(int i=0; i<cornerPositions.length-3; i+=3){
			vec[0] = cornerPositions[i];
			vec[1] = cornerPositions[i+1];
			vec[2] = cornerPositions[i+2];
			vec[3] = 1.0f;
			//Matrix.multiplyMV(vec, 0, orientationQuaternion.toRotationMatrix(), 0, vec, 0);
			orientationQuaternion.rotateVector(vec);
			
			cornerPositions[i] = vec[0];
			cornerPositions[i+1] = vec[1];
			cornerPositions[i+2] = vec[2];
		}
		setBounds(cornerPositions);
	}
	private void updateAOBB(Quaternion orientationQuaternion){
		//TODO: Implement Axis Oriented Bounding Box that is rotationally transformed using rotation matrix representation of orientationQuaternion
	}
	
	public void setBounds(float[] shapeCoords){
		if(bbType != BoundingBoxTypes.NONE){
			calculateBoxBoundsNSphereRadius(shapeCoords);
			
			//Use calculated bound to set four corner vertices
			setInitialCornerVertices();
		}
	}
	private void calculateBoxBoundsNSphereRadius(float[] shapeCoords){
		 //Also sets bounding dimensions based on size.
		 boundRadius = (shapeCoords[0]-shapeCoords[3])/2;
		 float max, min, lengthMax;
		 
		 for(int axis = 0; axis < 3; axis++){
			 max = shapeCoords[axis];
			 min = shapeCoords[axis];
			 for(int coord = 0; coord < shapeCoords.length; coord += 3){
				if( shapeCoords[axis+coord] > max){
			   	   max = shapeCoords[axis+coord];}
				if( shapeCoords[axis+coord] < min){
				   min = shapeCoords[axis+coord]; }
			
			 }
			 
			 lengthMax = max-min;
			 boundBoxLength[axis] = (lengthMax != 0)? max-min : 0.001f; // Set a lower limit for coordinate maximum range
			 
			 if( boundBoxLength[axis]/2 > boundRadius){
			    boundRadius = boundBoxLength[axis]/2;}	
		 }
	}
}
