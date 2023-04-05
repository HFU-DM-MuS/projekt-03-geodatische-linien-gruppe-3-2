package app;

import java.util.ArrayList;
import java.util.Timer;

import javax.swing.JFrame;

import utils.ApplicationTime;
import utils.FrameUpdater;

public abstract class Animation {

	public void start() {
		// open new thread for time measurement
		ApplicationTime applicationTimeThread = new ApplicationTime();
		applicationTimeThread.start();
		FrameUpdater frameUpdater = new FrameUpdater(createFrames(applicationTimeThread));
		Timer timer = new Timer();
		// After an initial delay of 100 milliseconds, the timer triggers an event every
		// 1000 milliseconds which leads to a jumpy animation
		timer.scheduleAtFixedRate(frameUpdater, 100, 1000);

	};

	protected abstract ArrayList<JFrame> createFrames(ApplicationTime applicationTimeThread);

}
