package com.simulations.test;

import com.simulations.test.CameraManager.Mode;
import android.opengl.*;

public final class Camera extends PhysicalObject
{
	 //Fields
	 private boolean showBillboards;
	 private boolean isTargeting;
	
     private final float[] mProjMatrix;
	 private final float[] mViewMatrix;
	 private final float[] mVPMatrix;
	 
	 private float viewWidth;
	 private float viewHeight;
	 
	 private float focus;
	 private final float[] upDirec;
	 private final float[] lookAtDirec;
	 
	 private GDrawable targetObject;
	 private final Quaternion orbitQuaternion;
	 private final float[] orbitAxis;
	 private float currentOrbitAngle;
	 private float orbitAngularSpeed;
	 private float idealFollowDist;

	 private boolean isUpdated;
	 
	 public CameraManager.Mode camMod;
	 
	 //Constructors
	 public Camera(float ePx, float ePy, float ePz, 
	               float lAtx, float lAty, float lAtz, CameraManager.Mode camMode, Float[][] boundaryPlanes){
	     super(ePx, ePy, ePz, 1, boundaryPlanes, BoundingManager.BoundingBoxTypes.AABB);			   
			
	     showBillboards = false;
	     
		 mProjMatrix = new float[16];
		 mViewMatrix = new float[16];
		 mVPMatrix = new float[16];
		  
		 focus = 1.0f;
		 setLookAtPos(lAtx, lAty, lAtz);
		 upDirec = new float[3];
		 upDirec[0] = 0.0f; upDirec[1] = 1.0f; upDirec[2] = 0.0f; 
		 lookAtDirec = new float[3];
		 
		 isUpdated = true;
		 
		 isTargeting = false;
		 orbitQuaternion = new Quaternion(new float[]{0.0f, 1.0f, 0.0f}, (float) Math.PI/20);
		 orbitAxis = new float[]{0.0f, 1.0f, 0.0f};
	     orbitAngularSpeed = (float) (Math.PI/10.0f);
		 idealFollowDist = 5.0f;		 
		 
		 this.camMod = camMode;
		 
		 setViewMatrix();	
	 }
	 
	 ////
	 public void setLookAtPos(float x, float y, float z){
		 Quaternion.scratchVec1[0] = x-getPosition()[0];
		 Quaternion.scratchVec1[1] = y-getPosition()[1];
		 Quaternion.scratchVec1[2] = z-getPosition()[2];
		 
		 getOrientation().orientFromTo(Quaternion.STANDARD_FORWARD_VECTOR, Quaternion.scratchVec1, false);
	 }	 
	 private void setViewMatrix(){
		 float[] lAtPos = getLookPos();
				 
		 Matrix.setLookAtM(mViewMatrix, 0, getPosition()[0], getPosition()[1], getPosition()[2], 
		                   lAtPos[0], lAtPos[1], lAtPos[2], 
						   upDirec[0], upDirec[1], upDirec[2]);
	 }
	 public void setProjectionMatrix(float ratio){
		 // Ratio parameter passes the ratio of the SurfaceView
		 // width to its height
		 Matrix.frustumM(mProjMatrix, 0, -ratio, ratio, -1, 1, 2, 50);
		 //Matrix.orthoM(mProjMatrix, 0, -ratio, ratio, -1, 1, 2, 25);
	 }
	 public float[] getVPTransformMatrix(){
		 // Calculate and return the projection and view transformation
		 Matrix.multiplyMM(mVPMatrix, 0, mProjMatrix, 0, mViewMatrix, 0);
		 return mVPMatrix;
	 }
 	 public float[] getLookPos(){
		getOrientation().storeDirectionVector(lookAtDirec);
		
		lookAtDirec[0] = getPosition()[0] + lookAtDirec[0]*focus;
		lookAtDirec[1] = getPosition()[1] + lookAtDirec[1]*focus;
		lookAtDirec[2] = getPosition()[2] + lookAtDirec[2]*focus;	
		
		return lookAtDirec;
	 }
	 
 	 ////
 	 @Override
 	 public void animate(){
 		 super.animate();
 		 
 		 if(isUpdated){
 			 setViewMatrix();
 			 isUpdated= false;
 		 }
 	 }
 	 
	 //// 
 	 public void setTargetObj(GDrawable targetObj){
		 if(camMod != CameraManager.Mode.STATIC){
			 this.targetObject = targetObj;
			 this.isTargeting = true;
		 }
	 }  	 
 	 public void setupOrbit(GDrawable target, float[] orbitAxis, float orbitAngularSpeed){
		 if(camMod != CameraManager.Mode.ORBIT){
			 camMod = CameraManager.Mode.ORBIT;
		 }		 
		 
		 setTargetObj(target);
		 setOrbitAxis(orbitAxis[0], orbitAxis[1], orbitAxis[2]);
		 setOrbitAngularSpeed(orbitAngularSpeed);
	 }	 
 	 public void setOrbitAxis(float x, float y, float z){
		 orbitAxis[0] = x;
		 orbitAxis[1] = y;
		 orbitAxis[2] = z;
		 
		 orbitQuaternion.fromAxis_Angle_SelfEquate(orbitAxis, currentOrbitAngle);
	 }
	 public void setOrbitAngularSpeed(float orbitAngularSpeed){
		 this.orbitAngularSpeed = orbitAngularSpeed;
	 }	 
	 
	 public GDrawable getTargetObj(){
		 return targetObject;
	 }
	 public float[] getOrbitingAxis(){
		 return orbitAxis;
	 }
	 public float getOrbitAngularSpeed(){
		 return orbitAngularSpeed;
	 }
	 public float getCurrentOrbitAngle(){
		 return currentOrbitAngle;
	 }
	 public boolean getTargetingState(){
		 return isTargeting;
	 }
	 
	 public void performOrbit(){
		//TODO: Implement a camera that orbits a target around an axis
		 
		if(targetObject.getPosition() != null){
			Quaternion.scratchVec1[0] = Quaternion.scratchVec1[1] = 0.0f;
			Quaternion.scratchVec1[2] = idealFollowDist;		
			
			//Rotate based on orbit axis and current angle
			currentOrbitAngle += orbitAngularSpeed;			
			orbitQuaternion.orientAboutCurrentAxis(currentOrbitAngle);
			orbitQuaternion.rotateVector(Quaternion.scratchVec1);
			
			
			//Translate camera so its 'idealFollowDist' away from the target object 		
			position[0] = Quaternion.scratchVec1[0]+targetObject.getPosition()[0];
			position[1] = Quaternion.scratchVec1[1]+targetObject.getPosition()[1];
			position[2] = Quaternion.scratchVec1[2]+targetObject.getPosition()[2];
			
			isUpdated = true;
		}
	 }
	 public void performStaticTrack(){
		 if(targetObject != null){
			 setLookAtPos(targetObject.getPosition()[0], targetObject.getPosition()[1], targetObject.getPosition()[2]);
		 }
		 
		 //setViewMatrix();	 
		 setModelTransformMatrix();	
	 }
	 
	 ///// 
	 public void setCurrentAngle(float angle){
		 orientationQuaternion.orientAboutCurrentAxis(angle);
	 }
	 public void setShowBillboardsState(boolean state){
		 showBillboards = state;
	 }
	 public void setUpDirec(float x, float y, float z){
		 upDirec[0] = x;
		 upDirec[1] = y;
		 upDirec[2] = z;
		 
		 isUpdated = true;
	 }
	 public void setFocus(float focus){
		 this.focus = focus;
	 } 
	 public void setViewWidth(float viewWidth) {
		 this.viewWidth = viewWidth;
	 }
	 public void setViewHeight(float viewHeight) {
		 this.viewHeight = viewHeight;
	 }
	 public void setCamMode(CameraManager.Mode mode){
		 this.camMod = mode;
	 }
	 @Override
	 public void setPosition(float x, float y, float z){
		 //Prevent orbiting from being tampered
		 if(camMod != Mode.ORBIT){
			 super.setPosition(x, y, z);
		 }
		 isUpdated = true;
	 }

	 public boolean getShowBillboardsState(){
		 return showBillboards;
	 }
	 public float[] getUpDirec(){
		 return upDirec;
	 }
	 public float getFocus(){
		 return focus;
	 }
	 @Override
	 public Quaternion getOrientation(){
		 isUpdated = true;
		 return super.getOrientation();
	 }
	 
	 public float getViewWidth() {
		 return viewWidth;
	 }
	 public float getViewHeight() {
		 return viewHeight;
	 }
	 public float[] getProjMatrix() {
		return mProjMatrix;
	}
	 public float[] getViewMatrix() {
		return mViewMatrix;
	}
	 public CameraManager.Mode getCamMode(){
		 return camMod;
	 }
}
