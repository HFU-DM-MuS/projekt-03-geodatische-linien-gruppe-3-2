package app;


public class Main {

	public static void main(String[] args) {
		Animation animation = null;
//      uncomment the option that you wish to execute.
		//animation = new _1_BasicAnimation(); // modify class Animation.java to demonstrate a less jumpy animation
		//animation = new _2_SmootherAnimationSeveralWindows();
		//animation = new _3_MultipleCirclesAnimation();
		//animation = new _4_TemporalFunctionDemo();
		//animation = new _5_ClockAnimation();
		animation = new _6_YourOwnAnimation();
		animation.start();
	}


}
