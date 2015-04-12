package com.simulations.test;

import java.util.ArrayList;
import java.util.HashMap;

public class LineGraph extends Graph2D {
	protected final float STANDARD_AXIS_LENGTH = 1.0f;
	protected String xLabel;
	protected String yLabel;
	
	////
	public LineGraph(){
		super();
		title = "LINE GRAPH";
		xLabel = "X AXIS";
		yLabel = "Y AXIS";
		dataPairGroupMap = new HashMap<String, ArrayList<Float[]>>();
	}
	public LineGraph(String title, String xLabel, String yLabel){
		super(title, xLabel, yLabel);
		dataPairGroupMap = new HashMap<String, ArrayList<Float[]>>();
	}
	public LineGraph(int width, int height){
		super(width, height);
		dataPairGroupMap = new HashMap<String, ArrayList<Float[]>>();
	}
	
	////
	public void drawAxis(boolean xAxisNegative, boolean yAxisNegative){
		GT_Line axis = new GT_Line(0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f);
		
		//y-axis
		if(!xAxisNegative && yAxisNegative){
			
		}
		axis.addLine_noOffset(0.0f, 0.0f, 0.0f, STANDARD_AXIS_LENGTH, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f);
	}
	
	////
	@Override
	protected void setupDrawableObjects(){
		
	}
	
	////
	
	
	////
	@Override
	public void animate(){
		super.animate();
	}
}
