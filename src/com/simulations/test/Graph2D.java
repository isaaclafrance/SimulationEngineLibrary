package com.simulations.test;

import java.util.ArrayList;
import java.util.Map;

//TODO: Finish implementing two dimensional graph plotting abstract class 

public abstract class Graph2D extends Scene {
	protected String title;
	protected Map<String, ArrayList<Float[]>> dataPairGroupMap; //Stores data pairs associated with a group name
	protected int width, height;
	
	////
	public Graph2D(){
		super();
		title = "2D GRAPH";
		width = 100;
		height = 100;
	}
	public Graph2D(String title){
		this();
		this.title = title;
	}
	public Graph2D(int width, int height){
		this.title = "2D GRAPH";
		this.width = width;
		this.height = height;
	}
	
	////
	@Override
	protected void setupDrawableObjects(){
		//Create border
		//Create title
		//Create axis labels
	}

	/////
	@Override
	public void animate() {
		super.animate();
	}
}
