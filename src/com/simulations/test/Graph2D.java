package com.simulations.test;

//TODO: Finish implementing two dimensional graph plotting abstract class 

public abstract class Graph2D extends Scene {
	protected String title;
	protected String xLabel;
	protected String yLabel;
	protected int width, height;
	
	////
	public Graph2D(){
		super();
		title = "2D Graph";
		width = 100;
		height = 100;
	}
	public Graph2D(String title, String xLabel, String yLabel){
		this();
		this.title = title;
		this.xLabel = xLabel;
		this.yLabel = yLabel;
	}
	public Graph2D(int width, int height){
		this.title = "2D Graph";
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
	public void runNDraw() {
		super.runNDraw();
	}
}
