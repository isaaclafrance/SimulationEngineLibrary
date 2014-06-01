package com.simulations.test;

public class VerletEngine extends PhysicsEngine{
	
	public VerletEngine(float[] const_accel){
		super(const_accel);
	}
	public VerletEngine(){
		super(new float[]{0.0f, 0.0f, 0.0f});
	}
	
	@Override
	public void run(float steps){
		float delta = 1.0f/steps;
		for(int i=0; i<steps; i++){
			inertia(delta);			
			acceleration(delta);
			
			if(getLinks() != null){
				for(Linkable link:getLinks()){
					link.animate();
				}
			}
			
			getCollisionHandler().run(15, 5);	
			//getCollisionHandler().run();
		}
	}
	
	@Override
	public void inertia(float delta){
		float x, y, z;
		for(PhysicalObject object:getPhyObjects()){
			x = object.getPosition()[0]*2 - object.getOldPosition()[0];
			y = object.getPosition()[1]*2 - object.getOldPosition()[1];
			z = object.getPosition()[2]*2 - object.getOldPosition()[2];
			
			object.setOldPosition(object.getPosition()[0], object.getPosition()[1], object.getPosition()[2]);
			
			object.setPosition(x, y, z);
		}
	}
	
	@Override
	public void acceleration(float delta){
		for(PhysicalObject object:getPhyObjects()){			
			object.getPosition()[0] += (object.acceleration[0]+ getConstAcceleration()[0])*delta*delta;
			object.getPosition()[1] += (object.acceleration[1]+ getConstAcceleration()[1])*delta*delta;
			object.getPosition()[2] += (object.acceleration[2]+ getConstAcceleration()[2])*delta*delta;
			
            object.update();
		}
	}
}
