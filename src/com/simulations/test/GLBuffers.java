package com.simulations.test;
import java.nio.*;
import java.util.ArrayList;

public final class GLBuffers
{
	public static final int BYTES_PER_FLOAT = 4;
	public static final int BYTES_PER_INTEGER = 4;
	public static final int BYTES_PER_SHORT = 2;
	
	private GLBuffers(){}
	
	public static FloatBuffer getFloatBuffer(float[] data){
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
		// (number of coordinate values * 4 bytes per float)
		data.length * BYTES_PER_FLOAT);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());
		
		//Convert to float buffer from byte buffer
		FloatBuffer fb = bb.asFloatBuffer();
		//Set the buffer to read the first coordinate		
		fb.put(data).position(0);

		return fb;
	}
	public static ShortBuffer getShortBuffer(short[] data){
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
			// (number of coordinate values * 4 bytes per float)
			data.length * BYTES_PER_SHORT);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());
		
		//Convert to short buffer from byte buffer
		ShortBuffer sb = bb.asShortBuffer();
		//Set the buffer to read the first coordinate		
		sb.put(data).position(0);
		
		return sb;
	}

	public static FloatBuffer getFloatBuffer(ArrayList<Float> data){
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
		// (number of coordinate values * 4 bytes per float)
		data.size() * BYTES_PER_FLOAT);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());
		
		//Convert to float buffer from byte buffer
		FloatBuffer fb = bb.asFloatBuffer();
		//Add data to buffer
		for(Float f:data){
			fb.put(f);
		}
		fb.position(0);
		
		return fb;
	}
	public static ShortBuffer getShortBuffer(ArrayList<Short> data){
		// initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(
			// (number of coordinate values * 4 bytes per float)
			data.size() * BYTES_PER_SHORT);
		// use the device hardware's native byte order
		bb.order(ByteOrder.nativeOrder());
		
		//Convert to short buffer from byte buffer
		ShortBuffer sb = bb.asShortBuffer();
		//Add data to buffer
		for(Short s:data){
			sb.put(s);
		}
		sb.position(0);
		
		return sb;
	}
}
