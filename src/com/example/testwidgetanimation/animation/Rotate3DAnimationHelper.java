package com.example.testwidgetanimation.animation;



public class Rotate3DAnimationHelper {
	
	private static final String TAG ="Rotate3DAnimationHelper";
	
	public static void start3DAnimation(AnimationFrameUpdate update, int id, int centerX, int centerY) {
		Rotate3DAnimationDriver driver = new Rotate3DAnimationDriver(update);
		driver.startAnimation(id, 0, 90, centerX, centerY, 310f);
	}
	

}
