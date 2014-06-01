package com.simulations.test;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import android.opengl.GLES20;

public final class VertexManager {
	//Fields
    public static final int COORDS_PER_VERTEX = 3;	
    public static final int vertexStride = COORDS_PER_VERTEX * GLBuffers.BYTES_PER_FLOAT; // 4 bytes per vertex
    
    public boolean normalized; //Determines whether to do resource intensive normal calculation
    public boolean isUpdated;
    
    private ArrayList<Float> shapeCoords = new ArrayList<Float>();
    private ArrayList<Short> indices = new ArrayList<Short>(); 
    private ArrayList<Float> normals = new ArrayList<Float>(); 
    
    //[0] = smallest index changed (-1->reallocation, -2->do nothing); [1] = # of vertex changes
    private int[] vboUpdateData, iboUpdateData, nboUpdateData;
    
	private FloatBuffer vertexClientBuffer, normalClientBuffer;	
	private ShortBuffer indexClientBuffer;

	public int[] vertexServerBufferObjectID, indexServerBufferObjectID, normalServerBufferObjectID;
    public int mVertexPositionHandle, mVertexNormalHandle;    //Used in shader
    
    //Constructors
    public VertexManager(float[] shapeCoords, short[] indices, boolean normalized){
    	vertexServerBufferObjectID = new int[3];
    	indexServerBufferObjectID = new int[3];
    	normalServerBufferObjectID = new int[3];
    	
    	vboUpdateData = new int[2];
    	iboUpdateData = new int[2];
    	
    	isUpdated = false;
    	
    	for(int i=0;i<shapeCoords.length;i++){
    		this.shapeCoords.add(shapeCoords[i]); 
    	}
    	for(int i=0;i<indices.length;i++){
    		this.indices.add(indices[i]); 
    	}

    	this.normalized = normalized;
    	if(normalized){
    		setAllNormals();
    	}
    }
    
    private void setAllNormals(){
         normals = new ArrayList<Float>(indices.size());
    	
		 for(int i=0; i<indices.size(); i+=3){
			 setVertexNormal(i);
		 }
    }
    private void setVertexNormal(int index){
    	 for(int i=index; i<index+3; i++){
			 //Use the indices to get the three vertices of a particular triangle 
			 float[] v1 = {shapeCoords.get(indices.get(i)*3), shapeCoords.get(indices.get(i)*3+1), shapeCoords.get(indices.get(i)*3+2)};
			 float[] v2 = {shapeCoords.get(indices.get(i+1)*3), shapeCoords.get(indices.get(i+1)*3+1), shapeCoords.get(indices.get(i+1)*3+2)};
			 float[] v3 = {shapeCoords.get(indices.get(i+2)*3), shapeCoords.get(indices.get(i+2)*3+1), shapeCoords.get(indices.get(i+2)*3+2)};
			 
			 //Use the vertices to create three two vectors from which a normal can be calculated
			 float[] vec1 = { v1[0]-v2[0], v1[1]-v2[1], v1[2]-v2[2] };
			 float[] vec2 = { v1[0]-v3[0], v1[1]-v3[1], v1[2]-v3[2] };
			 float[] vec3 = { vec1[1]*vec2[2] - vec1[2]*vec2[1], vec1[2]*vec2[0] - vec1[0]*vec2[2], vec1[0]*vec2[1] - vec1[1]*vec2[0]};
			 
			 //Fill array with normals corresponding to each vertex
			 float magn = (float) Math.sqrt(vec3[0]*vec3[0]+vec3[1]*vec3[1]+vec3[2]*vec3[2]);
			 
			 normals.set(i,vec3[0]/magn);
			 normals.set(i+1,vec3[1]/magn);
			 normals.set(i+2, vec3[2]/magn);
			 normals.set(i+3, vec3[0]/magn);
			 normals.set(i+4, vec3[1]/magn);
			 normals.set(i+5, vec3[2]/magn);
			 normals.set(i+6, vec3[0]/magn);
			 normals.set(i+7, vec3[1]/magn);
			 normals.set(i+8, vec3[2]/magn);
    	 }
    }
    
	public void setClientBuffers(){
		float[] sC = new float[shapeCoords.size()];
		for(int i=0; i<sC.length; i++){
			sC[i] = shapeCoords.get(i);
		}
		
		short[] sI = new short[indices.size()];
		for(int i=0; i<sI.length; i++){
			sI[i] = indices.get(i);
		}
		
		vertexClientBuffer = GLBuffers.getFloatBuffer(shapeCoords.size(), sC);
		indexClientBuffer = GLBuffers.getShortBuffer(indices.size(), sI);
		
		if(normalized){
			float[] sN = new float[normals.size()];
			for(int i=0; i<normals.size(); i++){
				sN[i] = normals.get(i);
			}
			
			normalClientBuffer = GLBuffers.getFloatBuffer(normals.size(), sN);	
		}
	}
	public void setServerBuffers(){
		 //Set VBO
		 GLES20.glGenBuffers(1, vertexServerBufferObjectID, 0);
		 GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexServerBufferObjectID[0]);
		 GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertexClientBuffer.capacity()*GLBuffers.BYTES_PER_FLOAT, vertexClientBuffer, GLES20.GL_DYNAMIC_DRAW);
		 GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		 
		 //Set IBO
		 GLES20.glGenBuffers(1, indexServerBufferObjectID, 0);
		 GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexServerBufferObjectID[0]);
		 GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER, indexClientBuffer.capacity()*GLBuffers.BYTES_PER_SHORT, indexClientBuffer, GLES20.GL_DYNAMIC_DRAW);
		 GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
		 
		 //Set NBO
		 if(normalized){
			 GLES20.glGenBuffers(1, normalServerBufferObjectID, 0);
			 GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, normalServerBufferObjectID[0]);
			 GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, normalClientBuffer.capacity()*GLBuffers.BYTES_PER_FLOAT, normalClientBuffer, GLES20.GL_DYNAMIC_DRAW);
			 GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
		 }
	}
	
	public void updateClientBuffers(){
		if (isUpdated) {
			float[] sC = new float[shapeCoords.size()];
			for (int i = vboUpdateData[0]; i < sC.length; i++) {
				sC[i] = shapeCoords.get(i);
			}
			short[] sI = new short[indices.size()];
			for (int i = iboUpdateData[0]; i < sI.length; i++) {
				sI[i] = indices.get(i);
			}
			vertexClientBuffer.position(vboUpdateData[0]);
			vertexClientBuffer.put(sC).position(0);	
			indexClientBuffer.position(iboUpdateData[0]);
			indexClientBuffer.put(sI).position(0);
			
			if (normalized) {
				float[] sN = new float[normals.size()];
				for (int i = nboUpdateData[0]; i < normals.size(); i++) {
					sN[i] = normals.get(i);
				}
				normalClientBuffer.position(nboUpdateData[0]);
				normalClientBuffer.put(sN).position(0);
			}
		}
	}
	public void updateServerBuffers(){	
		 if (isUpdated) {
			//Set VBO
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,
					vertexServerBufferObjectID[0]);
			if (vboUpdateData[0] == -1) {
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
						vertexClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT,
						vertexClientBuffer, GLES20.GL_DYNAMIC_DRAW);
			} else if (vboUpdateData[1] == 0) {
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						vboUpdateData[0], 1 * GLBuffers.BYTES_PER_FLOAT,
						vertexClientBuffer);
			} else if (vboUpdateData[1] > 0) {
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						vboUpdateData[0], vertexClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, vertexClientBuffer);
			}
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
			
			//Set IBO
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
					indexServerBufferObjectID[0]);
			if (iboUpdateData[0] == -1) {
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
						indexClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, indexClientBuffer,
						GLES20.GL_DYNAMIC_DRAW);
			} else if (iboUpdateData[1] == 0) {
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						iboUpdateData[0], 1 * GLBuffers.BYTES_PER_FLOAT,
						indexClientBuffer);
			} else if (iboUpdateData[1] > 0) {
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						iboUpdateData[0], indexClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, indexClientBuffer);
			}
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
			
			//Set NBO
			if (normalized) {
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,
						normalServerBufferObjectID[0]);
				if (nboUpdateData[0] == -1) {
					GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
							normalClientBuffer.capacity()
									* GLBuffers.BYTES_PER_FLOAT,
							normalClientBuffer, GLES20.GL_DYNAMIC_DRAW);
				} else if (nboUpdateData[1] == 0) {
					GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
							nboUpdateData[0], 1 * GLBuffers.BYTES_PER_FLOAT,
							normalClientBuffer);
				} else if (nboUpdateData[1] > 0) {
					GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
							nboUpdateData[0], normalClientBuffer.capacity()
									* GLBuffers.BYTES_PER_FLOAT,
							normalClientBuffer);
				}
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
			}
			//Reset ""boUpdateData objects
			vboUpdateData[0] = -2; vboUpdateData[1] = 0;
			iboUpdateData[0] = -2; iboUpdateData[1] = 0;
			nboUpdateData[0] = -2; nboUpdateData[1] = 0;
			
			isUpdated = false;
		}
	}
		
	public void clearClientBuffer(){
		vertexClientBuffer = null;
		indexClientBuffer = null;
		normalClientBuffer = null;
	}
	public void clearServerBuffer(){
		 GLES20.glDeleteBuffers(1, vertexServerBufferObjectID, 0);
		 GLES20.glDeleteBuffers(1, indexServerBufferObjectID, 0);
		 if(normalized){
			 GLES20.glDeleteBuffers(1, normalServerBufferObjectID, 0);
		 }
	}
    
	public void clearAll(){
		shapeCoords.clear();
		indices.clear();
		normals.clear();
		
		clearClientBuffer();
		clearServerBuffer();
	}
	
	//Binds vertex and normal buffers to their corresponding attributes in shader program 
	public void setVertexPositionAttribute_Server(int mProgram){
		 //BIND VBO
		 GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vertexServerBufferObjectID[0]);		 
		 
		 // get handle to vertex shader's vPosition member
		 mVertexPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");

		 // Prepare the triangle coordinate data
		 GLES20.glVertexAttribPointer(mVertexPositionHandle, COORDS_PER_VERTEX,
				 						GLES20.GL_FLOAT, false,
				 						VertexManager.vertexStride, 0); 
		 
		 // Enable a handle to the triangle vertices		 
		 GLES20.glEnableVertexAttribArray(mVertexPositionHandle);		 
		 	 
		 //UNBIND VBO
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}
	public void setNormalAttribute_Server(int mProgram){
		 //BIND NBO
		 GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, normalServerBufferObjectID[0]);		 
		 
		 // get handle to vertex shader's vPosition member
		 mVertexNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
		 
		 // Prepare the triangle coordinate data
		 GLES20.glVertexAttribPointer(mVertexNormalHandle, COORDS_PER_VERTEX,
				 						GLES20.GL_FLOAT, false,
				 						VertexManager.vertexStride, 0); 
		 
		 // Enable a handle to the triangle vertices		 
		 GLES20.glEnableVertexAttribArray(mVertexNormalHandle);				 
		 	 
		 //UNBIND NBO
	     GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);	
	}
	
	public void setVertexPositionAttribute_Client(int mProgram){
		 // get handle to vertex shader's vPosition member
		 mVertexPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");

		 // Prepare the triangle coordinate data
		 GLES20.glVertexAttribPointer(mVertexPositionHandle, COORDS_PER_VERTEX,
				 						GLES20.GL_FLOAT, false,
				 						VertexManager.vertexStride, vertexClientBuffer); 
		 
			 // Enable a handle to the triangle vertices		 
		 GLES20.glEnableVertexAttribArray(mVertexPositionHandle);	
	}
	public void setNormalAttribute_Client(int mProgram){
		 // get handle to vertex shader's vPosition member
		 mVertexNormalHandle = GLES20.glGetAttribLocation(mProgram, "aNormal");
		 
		 // Prepare the triangle coordinate data
		 GLES20.glVertexAttribPointer(mVertexNormalHandle, COORDS_PER_VERTEX,
				 						GLES20.GL_FLOAT, false,
				 						VertexManager.vertexStride, normalClientBuffer); 
		 
		 // Enable a handle to the triangle vertices		 
		 GLES20.glEnableVertexAttribArray(mVertexNormalHandle);		
	}
	
	public static float[] calculatePositionOffset(ArrayList<Float> coords){
		 //Determines initial position before any transformations.
		 //Does this by finding the average position using the maximum and minimum value
		 //for each axis i.e. x,y,z.
		 float[] offset = new float[3];
		 if(coords.size() > 0){
			 for(int axis = 0; axis < 3; axis++){
				 float max = coords.get(axis);
				 float min = coords.get(axis);
				 for(int coord = 0; coord <= coords.size()-3; coord += 3){
					if( coords.get(axis+coord) > max){
				   	   max = coords.get(axis+coord);}
					if( coords.get(axis+coord) < min){
					   min = coords.get(axis+coord); }
				
				 }
				 offset[axis] = -(max+min)/2; 
			 }
		 }
		 return offset;
	}
	public float[] calculatePositionOffset(int startPositionIndex){
		 //Determines initial position before any transformations.
		 //Does this by finding the average position using the maximum and minimum value
		 //for each axis i.e. x,y,z. Starting at the 'startPositionIndex'
		 float[] offset = new float[3];
		 if(1 - (indices.size() - (startPositionIndex+1)) > 2){
			 for(int axis = 0; axis < 3; axis++){
				 float max = shapeCoords.get(indices.get(startPositionIndex)+axis);
				 float min = shapeCoords.get(indices.get(0)+axis);
				 for(int coord = startPositionIndex; coord < indices.size(); coord++){
					if( shapeCoords.get(axis+3*indices.get(coord)) > max){
				   	   max = shapeCoords.get(axis+3*indices.get(coord));}
					if( shapeCoords.get(axis+3*indices.get(coord)) < min){
					   min = shapeCoords.get(axis+3*indices.get(coord)); }
				
				 }
				 offset[axis] = -(max+min)/2; 
			 }
		 }
		 return offset;
	}
	
	////
	public void addVertex_noOffset(float x, float y, float z){
		shapeCoords.add(x);
		shapeCoords.add(y);
		shapeCoords.add(z);
		
		isUpdated = true;

		//Set vbo to allocate larger memory
		vboUpdateData[0] = -1;
		vboUpdateData[1]++;
	}
	public float[] addVertex_withOffset(float x, float y, float z){
		addVertex_noOffset(x, y, z);
		
		//Recalculate and return position offset
		return calculatePositionOffset(0);	
	}
	public void addVertices_noOffset(float[] vertices){
		for(int i=0; i<vertices.length; i++){
			shapeCoords.add(vertices[i]);
		}
		
		isUpdated = true;

		//Set vbo to allocate larger memory
		vboUpdateData[0] = -1;
		vboUpdateData[1]++;
	}
	public float[] addVertices_withOffset(float[] vertices){
		addVertices_noOffset(vertices);	
		return calculatePositionOffset(0);
	}
	
	private void replaceVertex_noOffset(float x, float y, float z, int vertLocationIndex){ //Only accessible through alterations indices due to easier calculation of normal
		shapeCoords.set(vertLocationIndex*3, x);
		shapeCoords.set(vertLocationIndex*3 + 1, y);
		shapeCoords.set(vertLocationIndex*3 + 2, z);
		
		isUpdated = true;
		
		//Store smallest index location
		if(vertLocationIndex < vboUpdateData[0]){
			vboUpdateData[0] = vertLocationIndex;			
		}
		vboUpdateData[1]++;
	}
	
	public void removeVertex_noOffset(float x, float y, float z, int vertLocationIndex){
		//TODO: Use hash to allow efficient updating of remaining vertices' new index location in index array 
		isUpdated = true;
	}
	public void removeObsoleteVertices(){
		//TODO: Removes vertices not referenced in the index array and those repeated multiple times. Possible use a hash.
		isUpdated = true;
	}	
	
	////
	public void addIndex_noOffset(short index, int locationIndex){ 
		indices.add(locationIndex, index);
		
		isUpdated = true;
		
		//Store smallest index location
		if(locationIndex < iboUpdateData[0]){
			iboUpdateData[0] = locationIndex;	
			nboUpdateData[0] = (int) Math.floor((locationIndex+1)/3);
		}
		iboUpdateData[1]++;
		nboUpdateData[1]++;
		
		//Recalculate normals at specified location
		setVertexNormal((int) Math.floor((locationIndex+1)/9));
	}
	public float[] addIndex_withOffset(short index, int locationIndex){ 
		addIndex_noOffset(index, locationIndex);
		return calculatePositionOffset(locationIndex);
	}
	public void addIndices_noOffset(short[] indices, int locationIndex){ 
		for(int i=0;i<indices.length;i++){
			this.indices.add(locationIndex++, indices[i]);
		}
		
		isUpdated = true;
		
		//Store smallest index location
		if(locationIndex < iboUpdateData[0]){
			iboUpdateData[0] = locationIndex;	
			nboUpdateData[0] = (int) Math.floor((locationIndex+1)/3);
		}
		iboUpdateData[1]++;
		nboUpdateData[1]++;
		
		//Recalculate normals at specified location
		setVertexNormal((int) Math.floor((locationIndex+1)/9));
	}
	public float[] addIndices_withOffset(short[] indices, int locationIndex){ 
		addIndices_noOffset(indices, locationIndex);
		return calculatePositionOffset(locationIndex);
	}
	
	public void removeIndex_noOffset(int locationIndex){ 
		indices.remove(locationIndex);
		
		isUpdated = true;
		
		//Store smallest index location
		if(locationIndex < iboUpdateData[0]){
			iboUpdateData[0] = locationIndex;	
			nboUpdateData[0] = (int) Math.floor((locationIndex+1)/3);
		}
		iboUpdateData[1]++;
		nboUpdateData[1]++;
	}
	public float[] removeIndex_withOffset(int locationIndex){ 
		removeIndex_noOffset(locationIndex);
		return calculatePositionOffset(locationIndex);
	}
	
	public void replaceIndex_noOffset(short newIndex, int indexLocation){
		indices.set(indexLocation, newIndex);
		
		isUpdated = true;
		
		//Store smallest index location
		if(indexLocation < iboUpdateData[0]){
			iboUpdateData[0] = indexLocation;	
			nboUpdateData[0] = (int) Math.floor((indexLocation+1)/3);
		}
		iboUpdateData[1]++;
		nboUpdateData[1]++;
		
		//Recalculate normals at specified location
		setVertexNormal((int) Math.floor((indexLocation+1)/9));
	}
	public float[] replaceIndex_withOffset(short newIndex, int indexLocation){
		replaceIndex_noOffset(newIndex, indexLocation);
		return calculatePositionOffset(indexLocation);
	}
	public void replaceIndex_noOffset(float x, float y, float z, int indexLocation){ //Changes the vertex to which the element in the index array is pointing to. 
		replaceVertex_noOffset(x, y, z, indices.get(indexLocation));
		
		isUpdated = true;
		
		//Store smallest index location
		if(indexLocation < iboUpdateData[0]){
			iboUpdateData[0] = indexLocation;	
			nboUpdateData[0] = (int) Math.floor((indexLocation+1)/3);
		}
		iboUpdateData[1]++;
		nboUpdateData[1]++;
		
		//Recalculate normals at specified location
		setVertexNormal((int) Math.floor((indexLocation+1)/9));
	}
	public float[] replaceIndex_withOffset(float x, float y, float z, int indexLocation){
		replaceIndex_noOffset(x, y, z, indexLocation);
		return calculatePositionOffset(indexLocation);
	}
	////
	
 	public ArrayList<Float> getShapeCoords(){
		return shapeCoords;
	}
    public float[] getShapeCoordsArray(){
		float[] sC = new float[shapeCoords.size()];
		for(int i=0; i<sC.length; i++){
			sC[i] = shapeCoords.get(i);
		}
		
		return sC;
    }
    public short[] getIndicesArray(){
		short[] sI = new short[indices.size()];
		for(int i=0; i<sI.length; i++){
			sI[i] = indices.get(i);
		}
		
		return sI;
    }
    public float[] getNormalsArray(){
		float[] sN = new float[normals.size()];
		for(int i=0; i<sN.length; i++){
			sN[i] = normals.get(i);
		}
		
		return sN;
    }

    public int getNumOfDrawnVertices(){
    	return indices.size();
    }
    public int getNumOfTotVertices(){
    	return shapeCoords.size()/COORDS_PER_VERTEX;
    }
}
