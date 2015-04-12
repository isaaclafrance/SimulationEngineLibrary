package com.simulations.test;
import android.opengl.*;

public class Transformable extends AnimatedObject
{
	protected float[] position;
	protected float[] scale;
	
	protected final Quaternion orientationQuaternion;
	
 	protected int mMVMatrixHandle; 
 	protected float[] mMVMatrix = new float[16];
    
    protected int mMVPMatrixHandle;  
	protected float[] mMVPMatrix = new float[16];
	
	public float[] mSceneTransformMatrix = new float[16];
	
	public float[] mModelMatrix = new float[16];
	public float[] mRotMatrix = new float[16];
	public float[] mTransMatrix = new float[16];
	public float[] mScaleMatrix = new float[16];
	public float[] mTransOffsetMatrix = new float[16]; //Holds the offset translation matrix that centers object in world coordinates
	
	public Transformable(float x, float y, float z){	
		this();	
		position = new float[]{x,y,z};
	}
	public Transformable(){
		scale = new float[]{1.0f, 1.0f, 1.0f};
		
		position = new float[]{0.0f, 0.0f, 0.0f};
		
		orientationQuaternion = new Quaternion(new float[]{0.0f, 1.0f, 0.0f}, (float) Math.PI/20);
		
		Matrix.setIdentityM(mSceneTransformMatrix, 0);
        Matrix.setIdentityM(mModelMatrix, 0);	
        //Matrix.setIdentityM(mRotMatrix, 0);
        Matrix.setIdentityM(mTransMatrix, 0);
        Matrix.setIdentityM(mScaleMatrix, 0);
        Matrix.setIdentityM(mTransOffsetMatrix, 0); 
        Matrix.setIdentityM(mMVMatrix, 0); 
        Matrix.setIdentityM(mMVPMatrix, 0); 
	}
	
	protected void setMVTransfMatrix(float[] mVMatrix){
		Matrix.multiplyMM(mMVMatrix, 0, mVMatrix, 0, mModelMatrix, 0);
	}
	protected void setMVPTransfMatrix(float[] mVMatrix, float[] mPMatrix){
		setModelTransformMatrix();
		setMVTransfMatrix(mVMatrix);
		Matrix.multiplyMM(mMVPMatrix, 0, mPMatrix, 0, mMVMatrix, 0);
	}	
	
	public void setSceneTransformMatrix(float[] transformMatrix){
		mSceneTransformMatrix = transformMatrix;
	}

	public void setModelTransformMatrix(){		
		getOrientation().storeRotationMatrix(mRotMatrix);
		Matrix.translateM(mTransMatrix, 0, position[0], position[1], position[2]);
			
		//Applies scene translations and rotations
		Matrix.multiplyMM(mModelMatrix, 0, mModelMatrix, 0, mSceneTransformMatrix, 0);				
		
		//Applies model translations and rotations
		Matrix.multiplyMM(mModelMatrix, 0, mModelMatrix, 0, mTransMatrix, 0);
		Matrix.multiplyMM(mModelMatrix, 0, mModelMatrix, 0, mRotMatrix, 0);			
		
		//Scales object
		Matrix.scaleM(mScaleMatrix, 0, scale[0], scale[1], scale[2]);
		Matrix.multiplyMM(mModelMatrix, 0, mModelMatrix, 0, mScaleMatrix, 0);
		
		//Centers object to origin in order to allow for proper rotation
		Matrix.multiplyMM(mModelMatrix, 0, mModelMatrix, 0, mTransOffsetMatrix, 0);
	}
	
	public void rotate(float[] axis, float angle){
		orientationQuaternion.fromAxis_Angle_SelfMultiply(axis , angle);
	}
	
	public void resetMatrcies(){
        Matrix.setIdentityM(mTransMatrix, 0);
        //Matrix.setIdentityM(mRotMatrix, 0);
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.setIdentityM(mScaleMatrix, 0);
	}
	
	//
	public void setPosition(float x, float y, float z){
		this.position[0] = x;
		this.position[1] = y;
		this.position[2] = z;
	}
	public void setScale(float x, float y, float z){
		scale[0] = x;
		scale[1] = y;
		scale[2] = z;
	}
	
 	public Quaternion getOrientation(){
		return orientationQuaternion;
	}
	public float[] getPosition(){
		return position;
	}
	public float[] getScale(){
		return scale;
	}
	
	@Override
	public void animate() {
	}
	
}
