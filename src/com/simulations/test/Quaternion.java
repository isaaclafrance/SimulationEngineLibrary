package com.simulations.test;

public final class Quaternion {
	public float x,y,z,w;
	
	public Quaternion(){
		x = z = 0.0f;
		y = 0.0f;
		w = 1.0f;
	}
	
	public Quaternion(float[] axis, float angle){
		fromAxis_Angle_SelfEquate(axis, angle);
	}
	
	public Quaternion(float x, float y, float z, float w){
		this.x = x;
		this.y = y; 
		this.z = z; 
		this.w = w;
		
		normalizeQuat();
	}
	
	public Quaternion multiplyQuat(Quaternion quat){
		return new Quaternion(w*quat.x + x*quat.w + y*quat.z - z*quat.y,
							  w*quat.y + y*quat.w + z*quat.x - x*quat.z,
							  w*quat.z + z*quat.w + x*quat.y - y*quat.x,
							  w*quat.w - x*quat.x - y*quat.y - z*quat.z);
	}
	public void multiplyQuat_Self(float quatx, float quaty, float quatz, float quatw){
		x = w*quatx + x*quatw + y*quatz - z*quaty;
		y = w*quaty + y*quatw + z*quatx - x*quatz;
	    z = w*quatz + z*quatw + x*quaty - y*quatx;
	    w = w*quatw - x*quatx - y*quaty - z*quatz;
	}
	
	static public Quaternion fromAxis_Angle(float[] axis, float angle){
		float sinAngle;
		normalizeVec(axis);	 
		
		angle *= 0.5f;
		sinAngle = (float) Math.sin(angle);
	
		return (new Quaternion((axis[0] * sinAngle), (axis[1] * sinAngle), (axis[2] * sinAngle), (float) Math.cos(angle)));
	}
	public void fromAxis_Angle_SelfEquate(float[] axis, float angle){
		float sinAngle;
		normalizeVec(axis);	 
		
		angle *= 0.5f;
		sinAngle = (float) Math.sin(angle);
	
		this.x = (axis[0] * sinAngle);
		this.y = (axis[1] * sinAngle);
		this.z = (axis[2] * sinAngle);
		this.w = (float) Math.cos(angle);
	}
	public void fromAxis_Angle_SelfMultiply(float[] axis, float angle){
		float sinAngle;
		normalizeVec(axis);	 
		
		angle *= 0.5f;
		sinAngle = (float) Math.sin(angle);
	
		this.multiplyQuat_Self((axis[0] * sinAngle),
							   (axis[1] * sinAngle), 
							   (axis[2] * sinAngle), 
							   (float) Math.cos(angle));
	}
		
	public void orientFromTo(float[] fromVec, float[] toVec, boolean reverseAngle){
		normalizeVec(toVec);
		normalizeVec(fromVec);
				
		if(!reverseAngle){
			fromAxis_Angle_SelfEquate(crossProductAxis(fromVec, toVec), dotProductAngle(fromVec, toVec));
		}
		else{
			fromAxis_Angle_SelfEquate(crossProductAxis(fromVec, toVec), -dotProductAngle(fromVec, toVec));
		}
	}
	public void orientAboutCurrentAxis(float angle){
		fromAxis_Angle_SelfEquate(getAxis(), angle);
	}
	
	public void rotateVector(float[] vec){
		//Reference: http://molecularmusings.wordpress.com/2013/05/24/a-faster-quaternion-vector-multiplication/
		
		float[] t = crossProductAxis(new float[]{x,y,z}, vec);
		t[0] = t[0]*2.0f*w;
		t[1] = t[1]*2.0f*w; 
		t[2] = t[2]*2.0f*w;
		
		float[] t2 = crossProductAxis(new float[]{x, y, z}, t);
	    
		vec[0] = vec[0] + t[0] + t2[0];
		vec[1] = vec[1] + t[1] + t2[1];
		vec[2] = vec[2] + t[2] + t2[2];
		
/*		System.out.println("rotate vec 1: "+vec[0]);
		System.out.println("rotate vec 2: "+vec[1]);
		System.out.println("rotate vec 3: "+vec[2]);*/
	}
	
 	public Quaternion getConjugate(){
		return new Quaternion(-x, -y, -z, w);
	}
	public void normalizeQuat(){
		float mag = (float) Math.sqrt( w*w + x*x + y*y + z*z );
		
		if(mag != 0){
			w/=mag;
			x/=mag;
			y/=mag;
			z/=mag;
		}
	}
	public static Quaternion getIdentityQuaternion(){
		return new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
	}
	
	////
	public static void normalizeVec(float[] vec){
		float length = vectorLength(vec);
		
		for(int i=0; i<vec.length; i++){
			if(length != 0.0f)
				vec[i] /= length;
		}
	}
	public static float vectorLength(float[] vec){
		float length = 0.0f;
		for(float elem:vec){
			length += elem*elem;
		}
		return (float) Math.sqrt(length);
	}
	public static float[] crossProductAxis(float[] fromVec, float[] toVec){
		float axisX = fromVec[1]*toVec[2] - fromVec[2]*toVec[1];
		float axisY = fromVec[2]*toVec[0] - fromVec[0]*toVec[2];
		float axisZ = fromVec[0]*toVec[1] - fromVec[1]*toVec[0];
		
		return new float[]{axisX, axisY, axisZ};
	}
	public static void storeCrossProductAxis(float[] fromVec, float[] toVec, float[] crossProdAxis){
		float axisX = fromVec[1]*toVec[2] - fromVec[2]*toVec[1];
		float axisY = fromVec[2]*toVec[0] - fromVec[0]*toVec[2];
		float axisZ = fromVec[0]*toVec[1] - fromVec[1]*toVec[0];
		
		crossProdAxis[0] = axisX;
		crossProdAxis[1] = axisY;
		crossProdAxis[2] = axisZ;
	}
	
	public static float dotProductAngle(float[] fromVec, float[] toVec){
		return  (float)	Math.acos( (fromVec[0]*toVec[0] + fromVec[1]*toVec[1] + fromVec[2]*toVec[2]) / Math.sqrt(fromVec[0]*fromVec[0] + fromVec[1]*fromVec[1] + fromVec[2]*fromVec[2])/Math.sqrt(toVec[0]*toVec[0] + toVec[1]*toVec[1] + toVec[2]*toVec[2]) );
	}
	
	////
	public float[] getDirecVec(){
		float[] directionVec = new float[]{0.0f, 0.0f, -1.0f};
		
		rotateVector(directionVec);
		
		//System.out.println("getDirecVec 1: "+directionVec[0]);
		
		return directionVec;
	}
	public float[] getAxis(){
		float[] axis = new float[]{0.0f, 0.0f, 0.0f};
		
		if(!(x==0 && y==0 && z==0 && (Math.abs(w)==1)) || Math.abs(w)!=1){
			axis = new float[]{(float) (x/Math.sqrt(1-w*w)), (float) (y/Math.sqrt(1-w*w)), (float) (z/Math.sqrt(1-w*w))};
			normalizeVec(axis);
		}
		
		return new float[]{axis[0], axis[1], axis[2]};
	}
	public float getAngle(){ //in radians
		return (float) (Math.acos(w)*2.0f);
	}
	
	public float[] toRotationMatrix(){
		float x2 = x*x;
		float y2 = y*y;
		float z2 = z*z;
		float xy = x*y;
		float xz = x*z;
		float yz = y*z;
		float wx = w*x;
		float wy = w*y;
		float wz = w*z;
		
		return new float[]{1.0f - 2.0f*(y2+z2), 2.0f*(xy-wz), 2.0f*(xz+wy), 0.0f,
						   2.0f*(xy+wz), 1.0f-2.0f*(x2+z2), 2.0f*(yz-wx), 0.0f,
						   2.0f*(xz-wy), 2.0f*(yz+wx), 1.0f - 2.0f*(x2+y2), 0.0f,
						   0.0f, 0.0f, 0.0f, 1.0f};
	}
	public void storeRotationMatrix(float[] rotMatrix){
		float x2 = x*x;
		float y2 = y*y;
		float z2 = z*z;
		float xy = x*y;
		float xz = x*z;
		float yz = y*z;
		float wx = w*x;
		float wy = w*y;
		float wz = w*z;
		
		rotMatrix[0] = 1.0f - 2.0f*(y2+z2);
		rotMatrix[1] = 2.0f*(xy-wz);
		rotMatrix[2] = 2.0f*(xz+wy);
		rotMatrix[3] = 0.0f;
		rotMatrix[4] = 2.0f*(xy+wz);
		rotMatrix[5] = 1.0f-2.0f*(x2+z2);
		rotMatrix[6] = 2.0f*(yz-wx);
		rotMatrix[7] = 0.0f;
		rotMatrix[8] = 2.0f*(xz-wy);
		rotMatrix[9] = 2.0f*(yz+wx);
		rotMatrix[10] = 1.0f - 2.0f*(x2+y2);
		rotMatrix[11] = 0.0f;
		rotMatrix[12] = 0.0f;
		rotMatrix[13] = 0.0f;
		rotMatrix[14] = 0.0f;
		rotMatrix[15] = 1.0f;
	}
}
