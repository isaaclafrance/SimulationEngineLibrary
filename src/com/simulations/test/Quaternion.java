package com.simulations.test;

public final class Quaternion {
	public static final float[] STANDARD_FORWARD_VECTOR = new float[]{0.0f, 0.0f, -1.0f};
	public static final float[] STANDARD_UP_VECTOR = new float[]{0.0f, 1.0f, 0.0f};
	
	public static final float[] scratchVec1 = new float[3], scratchVec2 = new float[3]; //Stores temporary vectors used in calculations in order prevent too much GC cause by frequent array initializations
	private final float[] crossProductVec; //calculateCrossProductVec method must be called each time this vector needs to be updated with a new cross product	
	
	private float x,y,z,w;
	private final float[] axis;
	private float angle;

	////
 	public Quaternion(){
 		axis = new float[3];
		angle = 0.0f;
		crossProductVec = new float[3];		
 		
 		fromAxis_Angle_SelfEquate(new float[]{0.0f, 1.0f, 0.0f}, 0.001f);
	}
	public Quaternion(float[] axis, float angle){
		this();
		fromAxis_Angle_SelfEquate(axis, angle);
	}
	public Quaternion(float x, float y, float z, float w){
		this();
		this.x = x;
		this.y = y; 
		this.z = z; 
		this.w = w;
	
		normalizeQuat();
		calculateAxis();
		calculateAngle();
	}
	
	////
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
	    
	    calculateAxis();
	    calculateAngle();
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
		this.axis[0] = axis[0]; this.axis[1] = axis[1]; this.axis[2] = axis[2];
		
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
				
		calculateCrossProductAxis(fromVec, toVec);		
		if(!reverseAngle){
			fromAxis_Angle_SelfEquate(crossProductVec, dotProductAngle(fromVec, toVec));
		}
		else{
			fromAxis_Angle_SelfEquate(crossProductVec, -dotProductAngle(fromVec, toVec));
		}
	}
	public void orientAboutCurrentAxis(float angle){
		fromAxis_Angle_SelfEquate(getAxis(), angle);
	}
	
	public void rotateVector(float[] vec){
		//Reference: http://molecularmusings.wordpress.com/2013/05/24/a-faster-quaternion-vector-multiplication/

		scratchVec1[0] = x;
		scratchVec1[1] = y;
		scratchVec1[2] = z;
		
		calculateCrossProductAxis(scratchVec1, vec);
		
		scratchVec2[0] = crossProductVec[0]*2.0f*w;
		scratchVec2[1] = crossProductVec[1]*2.0f*w; 
		scratchVec2[2] = crossProductVec[2]*2.0f*w;
		
		calculateCrossProductAxis(scratchVec1, scratchVec2);
		
		vec[0] = vec[0] + scratchVec1[0] + crossProductVec[0];
		vec[1] = vec[1] + scratchVec1[1] + crossProductVec[1];
		vec[2] = vec[2] + scratchVec1[2] + crossProductVec[2];
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

	////
	private void calculateAxis(){
		if(!(x==0 && y==0 && z==0 && (Math.abs(w)==1)) || Math.abs(w)!=1){
			axis[0] = (float) (x/Math.sqrt(1-w*w));
			axis[1] = (float) (y/Math.sqrt(1-w*w));
			axis[2] = (float) (z/Math.sqrt(1-w*w));
			normalizeVec(axis);
		}		
	}
	private void calculateAngle(){ //in radians
		angle = (float) (Math.acos(w)*2.0f);
	}
	private void calculateCrossProductAxis(float[] fromVec, float[] toVec){
		crossProductVec[0] = fromVec[1]*toVec[2] - fromVec[2]*toVec[1];
		crossProductVec[1] = fromVec[2]*toVec[0] - fromVec[0]*toVec[2];
		crossProductVec[2] = fromVec[0]*toVec[1] - fromVec[1]*toVec[0];
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
	public float getX(){
		return x;
	}
	public float getY(){
		return y;
	}
	public float getZ(){
		return z;
	}
	public float getW(){
		return w;
	}
	public float[] getAxis(){
		return axis;
	}
	public float getAngle(){ 
		return angle;
	}
 	public Quaternion getConjugate(){
		return new Quaternion(-x, -y, -z, w);
	}	
	public static Quaternion getIdentityQuaternion(){
		return new Quaternion(0.0f, 0.0f, 0.0f, 1.0f);
	}	
	
	public void storeDirectionVector(float[] targetVector){
		targetVector[0] = targetVector[1] = 0.0f;
		targetVector[2] = -1.0f;
		
		rotateVector(targetVector);
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
