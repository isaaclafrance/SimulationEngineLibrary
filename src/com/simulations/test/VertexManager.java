package com.simulations.test;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import android.opengl.GLES20;

public final class VertexManager {
	//Fields
    public static final int COORDS_PER_VERTEX = 3;	
    public static final int vertexStride = COORDS_PER_VERTEX * GLBuffers.BYTES_PER_FLOAT; // 4 bytes per vertex
    
    private boolean normalized; //Determines whether to do resource intensive normal calculation
    private boolean isUpdated;
    
    private ArrayList<Float> shapeCoords = new ArrayList<Float>(); 
    private ArrayList<Short> indices = new ArrayList<Short>(); 
    private ArrayList<Float> normals = new ArrayList<Float>(); 
    
    //First array dimension identifies coordinate: [0] --> 'x', [1] --> 'y', [1] --> 'z'
    //Second array dimension identifies the min and max values associated with coordinate: [i][0] --> min value in coordinate, [i][1] --> max value in coordinate
    private float[][] positionOffsetMaxNMin; 
    
    private float[] positionOffset;
    
    //[0] = smallest index changed (-1->reallocation, -2->do nothing); [1] = tracks # of changes; [2] = largest index changed;  [3] = 0->do not reallocate, 1->reallocate; [4] = capacity of server buffer
    private int[] vboUpdateData, iboUpdateData, nboUpdateData;
    
	private FloatBuffer vertexClientBuffer, normalClientBuffer;	
	private ShortBuffer indexClientBuffer;

	private int[] vertexServerBufferObjectID, indexServerBufferObjectID, normalServerBufferObjectID;
    private int mVertexPositionHandle, mVertexNormalHandle;    //Used in shader
    
    //Constructors
    public VertexManager(float[] shapeCoords, short[] indices, boolean normalized){
    	positionOffsetMaxNMin = new float[3][2];
    	positionOffsetMaxNMin[0][0] = Float.NEGATIVE_INFINITY;
    	
    	positionOffset = new float[3];
    	
    	vertexServerBufferObjectID = new int[3];
    	indexServerBufferObjectID = new int[3];
    	normalServerBufferObjectID = new int[3];
    	
    	isUpdated = true;
    	
    	vboUpdateData = new int[]{0, 0, shapeCoords.length-1, 1, 0};
    	iboUpdateData = new int[]{0, 0, indices.length-1, 1, 0};
    	nboUpdateData = new int[5];
    	
    	for(int i=0;i<shapeCoords.length;i++){
    		this.shapeCoords.add(shapeCoords[i]); 
    	}
    	for(int i=0;i<indices.length;i++){
    		this.indices.add(indices[i]); 
    	}

    	this.normalized = normalized;
    	setAllNormals();
    }
   
    ////
    private void setAllNormals(){
    	if(normalized){ 
	     	if(!((indices.size() % 3) == 0) ){
	    		for(int i=0; i<(indices.size() % 3);i++){
	    			indices.remove(indices.size()-1);
	    		}
	    	}   		
    		
	    	normals = new ArrayList<Float>();
	        
	    	if(indices.size() != 0){
				 for(int i=0; i<indices.size(); i+=3){
					 for(int j=0;j<9;j++){
						 normals.add((i/3)*9+j, 0.0f); //Initializes space for vertices normals as needed
					 }
					 setVertexNormal(i/3);
				 }
				 
			     nboUpdateData[0] = nboUpdateData[1] = nboUpdateData[4] = 0;
			     nboUpdateData[2] = normals.size()-1;
			     nboUpdateData[3] = 1;
	
			     isUpdated = true;
    		}
        }
    }
    private void setVertexNormal(int triangleIndex){
    	if(normalized && (indices.size() % 3) == 0){
			 //Use the indices to get the three vertices of a particular triangle 
			 float[] v1 = {shapeCoords.get(indices.get(triangleIndex*3)*3), shapeCoords.get(indices.get(triangleIndex*3)*3+1), shapeCoords.get(indices.get(triangleIndex*3)*3+2)};
			 float[] v2 = {shapeCoords.get(indices.get(triangleIndex*3+1)*3), shapeCoords.get(indices.get(triangleIndex*3+1)*3+1), shapeCoords.get(indices.get(triangleIndex*3+1)*3+2)};
			 float[] v3 = {shapeCoords.get(indices.get(triangleIndex*3+2)*3), shapeCoords.get(indices.get(triangleIndex*3+2)*3+1), shapeCoords.get(indices.get(triangleIndex*3+2)*3+2)};
			 
			 //Use the vertices to create three two vectors from which a normal can be calculated
			 float[] vec1 = { v1[0]-v2[0], v1[1]-v2[1], v1[2]-v2[2] };
			 float[] vec2 = { v1[0]-v3[0], v1[1]-v3[1], v1[2]-v3[2] };
			 float[] vec3 = { vec1[1]*vec2[2] - vec1[2]*vec2[1], vec1[2]*vec2[0] - vec1[0]*vec2[2], vec1[0]*vec2[1] - vec1[1]*vec2[0]};
			 
			 //Fill array with normals corresponding to each vertex
			 float magn = (float) Math.sqrt(vec3[0]*vec3[0]+vec3[1]*vec3[1]+vec3[2]*vec3[2]);
			 
			 normals.set(triangleIndex,vec3[0]/magn);
			 normals.set(triangleIndex+1,vec3[1]/magn);
			 normals.set(triangleIndex+2, vec3[2]/magn);
			 normals.set(triangleIndex+3, vec3[0]/magn);
			 normals.set(triangleIndex+4, vec3[1]/magn);
			 normals.set(triangleIndex+5, vec3[2]/magn);
			 normals.set(triangleIndex+6, vec3[0]/magn);
			 normals.set(triangleIndex+7, vec3[1]/magn);
			 normals.set(triangleIndex+8, vec3[2]/magn);
    	}
    }
    public void setNormalizedState(boolean state){
    	normalized = state;
    	if(normalized){
    		setAllNormals();
    	}
    	else if(!normalized){
    		normalClientBuffer = null;
			GLES20.glDeleteBuffers(1, normalServerBufferObjectID, 0);
			normals.clear();
			nboUpdateData[0] = nboUpdateData[1] = nboUpdateData[2] = nboUpdateData[3] = nboUpdateData[4] = 0;
    	}
    }
        
	public void setServerBuffers(){
		 //Set VBO
		 GLES20.glGenBuffers(1, vertexServerBufferObjectID, 0);
		 
		 //Set IBO
		 GLES20.glGenBuffers(1, indexServerBufferObjectID, 0);

	     vboUpdateData[0] = vboUpdateData[1] = vboUpdateData[4] = 0;
	     vboUpdateData[2] = (shapeCoords.size() == 0)?0:shapeCoords.size()-1;
	     vboUpdateData[3] = 1;

	     iboUpdateData[0] = iboUpdateData[1] = iboUpdateData[4] = 0;
	     iboUpdateData[2] = (indices.size() == 0)?0:indices.size()-1;
	     iboUpdateData[3] = 1;		 
		 
		 //Set NBO
		 if(normalized){
			 GLES20.glGenBuffers(1, normalServerBufferObjectID, 0);
			 
		     nboUpdateData[0] = nboUpdateData[1] = nboUpdateData[4] = 0;
		     nboUpdateData[2] = (normals.size() == 0)?0:normals.size()-1;
		     nboUpdateData[3] = 1;			 
		 }

	     isUpdated = true;
	}
	
	////
	private void updateVertexClientBuffer(){
		if(vboUpdateData[3] == 1){
			vertexClientBuffer = GLBuffers.getFloatBuffer(shapeCoords);
		}else{
			vertexClientBuffer.position(vboUpdateData[0]);				
			for (int i = vboUpdateData[0]; i < vboUpdateData[2]; i++) {
				vertexClientBuffer.put(shapeCoords.get(i));
			}
			vertexClientBuffer.position(0);
		}			
	}
	private void updateIndexClientBuffer(){
		if(iboUpdateData[3] == 1){
			indexClientBuffer = GLBuffers.getShortBuffer(indices);
		}else{
			indexClientBuffer.position(iboUpdateData[0]);				
			for (int i = iboUpdateData[0]; i < iboUpdateData[2]; i++) {
				indexClientBuffer.put(indices.get(i));
			}
			indexClientBuffer.position(0);
		}				
	}
	private void updateNormalClientBufer(){
		if(normalized){
			if(nboUpdateData[3] == 1){
				normalClientBuffer = GLBuffers.getFloatBuffer(normals);
			}else{
				normalClientBuffer.position(nboUpdateData[0]);				
				for (int i = nboUpdateData[0]; i < nboUpdateData[2]; i++) {
					normalClientBuffer.put(normals.get(i));
				}
				normalClientBuffer.position(0);
			}			
		}		
	}
	public void updateClientBuffers(){
		if (isUpdated) {
			updateVertexClientBuffer();
			updateIndexClientBuffer();
			updateNormalClientBufer();
		}
	}
	public void updateServerBuffers(){	
		 if (isUpdated) {
			//Set VBO
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,
					vertexServerBufferObjectID[0]);
			if (vboUpdateData[3] == 1 && vertexClientBuffer.capacity() > vboUpdateData[4]) {
				GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
						vertexClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT,
						vertexClientBuffer, GLES20.GL_DYNAMIC_DRAW);	
			} else if(vboUpdateData[3] == 1 && vertexClientBuffer.capacity() < vboUpdateData[4]){ 
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						0*GLBuffers.BYTES_PER_FLOAT, vertexClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, vertexClientBuffer);
				
				//Invalidate unused portion of server buffer
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						vboUpdateData[2]*GLBuffers.BYTES_PER_FLOAT, vboUpdateData[4]-vertexClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, null);	
			}
			else{
				GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
						vboUpdateData[0]*GLBuffers.BYTES_PER_FLOAT, vertexClientBuffer.capacity()
								* GLBuffers.BYTES_PER_FLOAT, vertexClientBuffer);				
			}
			GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
			
			//Set IBO
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER,
					indexServerBufferObjectID[0]);
			if (iboUpdateData[3] == 1 && indexClientBuffer.capacity() > iboUpdateData[4]) {
				GLES20.glBufferData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
						indexClientBuffer.capacity()
								* GLBuffers.BYTES_PER_SHORT, indexClientBuffer,
						GLES20.GL_DYNAMIC_DRAW);
			} else if(iboUpdateData[3] == 1 && indexClientBuffer.capacity() < iboUpdateData[4]){
				GLES20.glBufferSubData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
						0*GLBuffers.BYTES_PER_SHORT, indexClientBuffer.capacity()
								* GLBuffers.BYTES_PER_SHORT, indexClientBuffer);
				
				//Invalidate unused portion of server buffer
				GLES20.glBufferSubData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
						iboUpdateData[2]*GLBuffers.BYTES_PER_SHORT, iboUpdateData[4]-indexClientBuffer.capacity()
								* GLBuffers.BYTES_PER_SHORT, null);	
			}else{
				GLES20.glBufferSubData(GLES20.GL_ELEMENT_ARRAY_BUFFER,
						iboUpdateData[0]*GLBuffers.BYTES_PER_SHORT, indexClientBuffer.capacity()
								* GLBuffers.BYTES_PER_SHORT, indexClientBuffer);				
			}
			GLES20.glBindBuffer(GLES20.GL_ELEMENT_ARRAY_BUFFER, 0);
			
			//Set NBO
			if (normalized) {
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER,
						normalServerBufferObjectID[0]);
				if (nboUpdateData[3] == 1 && normalClientBuffer.capacity() > nboUpdateData[4]) {
					GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER,
							normalClientBuffer.capacity()
									* GLBuffers.BYTES_PER_FLOAT,
							normalClientBuffer, GLES20.GL_DYNAMIC_DRAW);
				}else if(nboUpdateData[3] == 1 && normalClientBuffer.capacity() < nboUpdateData[4]){
					GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
							0*GLBuffers.BYTES_PER_FLOAT, normalClientBuffer.capacity()
									* GLBuffers.BYTES_PER_FLOAT,
							normalClientBuffer);
					
					//Invalidate unused portion of server buffer
					GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
							nboUpdateData[2]*GLBuffers.BYTES_PER_FLOAT, nboUpdateData[4]-normalClientBuffer.capacity()
									* GLBuffers.BYTES_PER_FLOAT, null);	
				}else{
					GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER,
							nboUpdateData[0]*GLBuffers.BYTES_PER_FLOAT, normalClientBuffer.capacity()
									* GLBuffers.BYTES_PER_FLOAT, normalClientBuffer);				
				}
				GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
			}

			//Store current sizes of server buffer objects
			vboUpdateData[4] = vertexClientBuffer.capacity();
			iboUpdateData[4] = indexClientBuffer.capacity();
			if(normalized){
				nboUpdateData[4] = normalClientBuffer.capacity();				
			}

			resetBOUpdateDataObjects();
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
		 
		 resetBOUpdateDataObjects();
	}
	public void clearAll(){
		shapeCoords.clear();
		indices.clear();
		if(normalized){
			normals.clear();
		}
		
		clearClientBuffer();
		clearServerBuffer();
	}
	
	private void resetBOUpdateDataObjects(){
		//Reset ""boUpdateData objects
		vboUpdateData[0] = vboUpdateData[1] = vboUpdateData[2] = vboUpdateData[3] = 0;
		iboUpdateData[0] = iboUpdateData[1] = iboUpdateData[2] = iboUpdateData[3] = 0;
		if(normalized){
			nboUpdateData[0] = nboUpdateData[1] = nboUpdateData[2] = nboUpdateData[3] = 0;			
		}
	
		isUpdated = false;
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
		if((indices.size() % 3) == 0 && normalized){
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
	public float[] calculatePositionOffset(int startIndex){
		 //Determines initial position before any transformations.
		 //Does this by finding the average position using the maximum and minimum value
		 //for each axis i.e. x,y,z. Starting at the 'startIndex'.
		
		 if(1 - ((startIndex+1) - indices.size()) > 2){
			 for(int axis = 0; axis < 3; axis++){
				 float max = (positionOffsetMaxNMin[0][0] == Float.NEGATIVE_INFINITY)? shapeCoords.get(indices.get(startIndex)+axis):positionOffsetMaxNMin[axis][0];
				 float min = (positionOffsetMaxNMin[0][0] == Float.NEGATIVE_INFINITY)? shapeCoords.get(indices.get(0)+axis):positionOffsetMaxNMin[axis][1];
				 for(int coord = startIndex; coord < indices.size(); coord++){
					if( shapeCoords.get(axis+3*indices.get(coord)) > max){
				   	   max = shapeCoords.get(axis+3*indices.get(coord));}
					if( shapeCoords.get(axis+3*indices.get(coord)) < min){
					   min = shapeCoords.get(axis+3*indices.get(coord)); }
				
				 }
				 positionOffsetMaxNMin[axis][0] = min;
				 positionOffsetMaxNMin[axis][1] = max;
				 
				 positionOffset[axis] = -(max+min)/2; 
			 }
		 }
		 return positionOffset;
	}
	
	////
	public void addVertex_noOffset(float x, float y, float z){
		shapeCoords.add(x);
		shapeCoords.add(y);
		shapeCoords.add(z);
		
		vboUpdateData[3] = 1; //Set vbo to allocate larger memory
		vboUpdateData[2] = shapeCoords.size()-1;
		vboUpdateData[0] = 0;
		vboUpdateData[1]++;
		
		postArrayDataChangeUpdate(0, true);	
	}
	public float[] addVertex_withOffset(float x, float y, float z){
		addVertex_noOffset(x, y, z);
		
		//Recalculate and return position offset
		return calculatePositionOffset(0);	
	}
	public void addVertices_noOffset(float[] vertices){
		vboUpdateData[0] = shapeCoords.size()-1;
		for(int i=0; i<vertices.length; i++){
			shapeCoords.add(vertices[i]);
		}

		//Set vbo to allocate larger memory
		vboUpdateData[3] = 1;
		vboUpdateData[2] = shapeCoords.size()-1;
		vboUpdateData[1]++;
		
		postArrayDataChangeUpdate(0, true);
	}
	public float[] addVertices_withOffset(float[] vertices){
		addVertices_noOffset(vertices);	
		return calculatePositionOffset(0);
	}
	
	private void replaceVertex_noOffset(float x, float y, float z, int vertLocationIndex){ //Only accessible through alterations indices due to easier calculation of normal
		shapeCoords.set(vertLocationIndex*3, x);
		shapeCoords.set(vertLocationIndex*3 + 1, y);
		shapeCoords.set(vertLocationIndex*3 + 2, z);
		
		postArrayDataChangeUpdate(vertLocationIndex, true);
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

		iboUpdateData[3] = nboUpdateData[3] = 1; //Set ibo and nbo to allocate larger memory	
		iboUpdateData[0] = nboUpdateData[0] = 0;
		iboUpdateData[2] = indices.size()-1;
		nboUpdateData[2] = (int) Math.floor(normals.size()/3);

		postArrayDataChangeUpdate(0, false);
	}
	public float[] addIndex_withOffset(short index, int locationIndex){ 
		addIndex_noOffset(index, locationIndex);
		return calculatePositionOffset(locationIndex);
	}
	public void addIndices_noOffset(short[] indices, int locationIndex){ 
		for(int i=0;i<indices.length;i++){
			this.indices.add(locationIndex++, indices[i]);
		}

		iboUpdateData[3] = nboUpdateData[3] = 1; //Set ibo and nbo to allocate larger memory	
		iboUpdateData[0] = nboUpdateData[0] = 0;
		iboUpdateData[2] = this.indices.size()-1;
		nboUpdateData[2] = (int) Math.floor(normals.size()/3);
		
		postArrayDataChangeUpdate(0, false);
	}
	public float[] addIndices_withOffset(short[] indices, int locationIndex){ 
		addIndices_noOffset(indices, locationIndex);
		return calculatePositionOffset(locationIndex);
	}
	
	public void removeIndex_noOffset(int locationIndex){ 
		indices.remove(locationIndex);
		
		isUpdated = true;
		
		iboUpdateData[3] = nboUpdateData[3] = 1; //Set ibo and nbo to allocate larger memory	
		iboUpdateData[0] = nboUpdateData[0] = 0;
		iboUpdateData[2] = indices.size()-1;
		nboUpdateData[2] = (int) Math.floor(normals.size()/3);
		
		postArrayDataChangeUpdate(0, false);
	}
	public float[] removeIndex_withOffset(int locationIndex){ 
		removeIndex_noOffset(locationIndex);
		return calculatePositionOffset(locationIndex);
	}
	
	public void replaceIndex_noOffset(short newIndex, int indexLocation){
		indices.set(indexLocation, newIndex);
		postArrayDataChangeUpdate(indexLocation, false);
	}
	public float[] replaceIndex_withOffset(short newIndex, int indexLocation){
		replaceIndex_noOffset(newIndex, indexLocation);
		return calculatePositionOffset(indexLocation);
	}
	public void replaceIndex_noOffset(float x, float y, float z, int indexLocation){ //Changes the vertex to which the element in the index array is pointing to. 
		replaceVertex_noOffset(x, y, z, indices.get(indexLocation));
		postArrayDataChangeUpdate(indexLocation, false);
	}
	public float[] replaceIndex_withOffset(float x, float y, float z, int indexLocation){
		replaceIndex_noOffset(x, y, z, indexLocation);
		return calculatePositionOffset(indexLocation);
	}
	
	private void postArrayDataChangeUpdate(int changeLocationIndex, boolean isVertexChanged){
		if(isVertexChanged){
			if(vboUpdateData[3] != 1){
				if(changeLocationIndex < vboUpdateData[0]){
					vboUpdateData[0] = changeLocationIndex;			
				}
				if(changeLocationIndex > vboUpdateData[2]){
					vboUpdateData[2] = changeLocationIndex;			
				}
				vboUpdateData[1]++;
			}
		}else{
			if(iboUpdateData[3] != 1){
				//Store smallest index location
				if(changeLocationIndex < iboUpdateData[0]){
					iboUpdateData[0] = changeLocationIndex;	
					if(normalized){
						nboUpdateData[0] = (int) Math.floor((changeLocationIndex)/3);						
					}
				}
				//Store largest index location
				if(changeLocationIndex > iboUpdateData[2]){
					iboUpdateData[2] = changeLocationIndex;	
					if(normalized){
						nboUpdateData[2] = (int) Math.floor((changeLocationIndex)/3);						
					}
				}
				iboUpdateData[1]++;
				if(normalized){
					nboUpdateData[1]++;					
				}
	
				if(normalized){
					//Recalculate normals at triangle index associated with specified location
					setVertexNormal((int) Math.floor((changeLocationIndex)/3));			
				}
			}			
		}
		isUpdated = true;
	}
	
	////
	public int[] getVertexServerBufferObjectID(){
		return vertexServerBufferObjectID;
	}
	public int[] getIndexServerBufferObjectID(){
		return indexServerBufferObjectID;
	}
	public int[] getNormalServerBufferObjectID(){
		return  normalServerBufferObjectID;
	}
	public int get_mVertexPositionHandle(){
		return mVertexPositionHandle;
	}
	public int get_mVertexNormalHandle(){
		return mVertexNormalHandle;
	}
	public boolean getNormalizedState(){
		return normalized;
	}
	
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

    public int getNumOfDrawnVertices(){
    	return indices.size();
    }
    public int getNumOfTotalVertices(){
    	return shapeCoords.size()/COORDS_PER_VERTEX;
    }
}
