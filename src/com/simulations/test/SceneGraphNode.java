package com.simulations.test;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import android.opengl.Matrix;

//TODO: Add some scene graph functionality. Include the ability to do node cascade transformations.

public abstract class SceneGraphNode extends AnimatedObject{
	protected String nodeName;
	private SceneGraphNode parentNode;
	private Map<String, SceneGraphNode> childrenNodesMap;
	
	private final Quaternion nodeOrientation;
	private float[] nodeTranslation;
	private float[] nodeScale;
	protected float[] worldPosition;
	protected float[] worldTransformationMatrix;
	private float[]  rotationMatrix;
	protected boolean isUpdated;
	
	public SceneGraphNode(){
		isUpdated = false;
		nodeName = this.toString();
		parentNode = this;
		childrenNodesMap = new HashMap<String, SceneGraphNode>();
		worldPosition = new float[4];
		worldPosition[3] = 1.0f;
		
		nodeTranslation = new float[3];
		nodeOrientation = new Quaternion();
		nodeScale = new float[3];
		
		worldTransformationMatrix = new float[16];
		rotationMatrix = new float[16];
	}
	public SceneGraphNode(String nodeName){
		this();
		this.nodeName = nodeName;
	}
	
	////
	public void addNode(SceneGraphNode node){
		if(!childrenNodesMap.containsValue(node)){
			childrenNodesMap.put(node.getNodeName(), node);
			node.setParentNode(this);
		}
	}
	public void addNode(String nodeName, SceneGraphNode node){
		if(!childrenNodesMap.containsValue(node)){
			childrenNodesMap.put(nodeName, node);
			node.setParentNode(this);
		}
	}
	public void removeNode(SceneGraphNode childNode){
		childrenNodesMap.remove(childNode.getNodeName());
		childNode.clearParentNode();
	}
	public void removeNode(String nodeName){ 
		if(childrenNodesMap.containsKey(nodeName)){
			removeNode(childrenNodesMap.get(nodeName));
		}
	}
	
	////
	public void calculateWorldTransformationMatrixNPosition(){
		//Set world transformation matrix
		nodeOrientation.storeRotationMatrix(rotationMatrix);
		Matrix.setIdentityM(worldTransformationMatrix, 0);
		
		Matrix.translateM(worldTransformationMatrix, 0, nodeTranslation[0], nodeTranslation[1], nodeTranslation[2]); //Applies translation relative to self
		Matrix.multiplyMM(worldTransformationMatrix, 0, worldTransformationMatrix, 0, rotationMatrix, 0); //Applies rotation relative to self
		if(parentNode != this){
			Matrix.multiplyMM(worldTransformationMatrix, 0, worldTransformationMatrix, 0, parentNode.worldTransformationMatrix, 0); //Applies translation of parent node	
		}

		//Set position of node relative to world coordinates
		worldPosition[0] = worldPosition[1] = worldPosition[2] = 0.0f;
		Matrix.multiplyMV(worldPosition, 0, worldTransformationMatrix, 0, worldPosition, 0);
	}
	
	////
	public void setNodeName(String nodeName){
		this.nodeName = nodeName;
	}
	public String getNodeName(){
		return nodeName;
	}
	
	private void setParentNode(SceneGraphNode parentNode){
		this.parentNode = parentNode;
	}
	private void clearParentNode(){
		parentNode = this;
	}
	
	public SceneGraphNode getParentNode(){
		return parentNode;
	}
	public Collection<SceneGraphNode> getChildrenNodes(){
		return childrenNodesMap.values();
	}
	 
	public float[] getWorldPosition(){
		return worldPosition;
	}
	
	public void setUpdateState(boolean state){
		isUpdated = state;
	}
	public boolean getUpdateState(){
		return isUpdated;
	}
	public boolean getParentUpdateState(){
		boolean state;
		
		if(parentNode.isUpdated == false){
			state = false;
		}else if(parentNode != this){
			state = parentNode.getParentUpdateState();
		}else{
			state = true;
		}
		
		return state;
	}
	
	public void setTranslation(float x, float y, float z){
		nodeTranslation[0] = x;
		nodeTranslation[1] = y;
		nodeTranslation[2] = z;
		isUpdated = true;
	}
	public float[] getTranslation(){
		return nodeTranslation;
	}
	
	public Quaternion getOrientation(){
		isUpdated = true;
		return nodeOrientation;
	}
	////
	@Override
	public void animate(){
		if(isUpdated || getParentUpdateState()){
			calculateWorldTransformationMatrixNPosition();
			isUpdated = false;
		}
	}
}
