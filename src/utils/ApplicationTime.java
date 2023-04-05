package utils;

import app._0_Constants;
import java.util.concurrent.atomic.*;

public class ApplicationTime extends Thread {
	//time in ms
	public double timeSinceStart = 0;
	public long currentTime = 0;
	public long formerTime = 0;
	private double timeScale = _0_Constants.TIMESCALE;
	private final AtomicBoolean isPaused = new AtomicBoolean(false);
	private final AtomicBoolean running = new AtomicBoolean(true);
	
	public ApplicationTime() {
	}
	
	@Override
	public void run() {
			
		formerTime = System.currentTimeMillis();
		while(running.get()) {
			currentTime = System.currentTimeMillis();			
			if(!isPaused.get()) {
				timeSinceStart += (currentTime - formerTime) * timeScale;
			}			
			formerTime = currentTime;
		}
	}
	
	//returns the current timer in milliseconds
	public double getTime() {
		return timeSinceStart;
	}
	
	//returns the current time in seconds
	public double getTimeInSeconds() {
		return timeSinceStart / 1000;
	}
	
	// change the timer speed (1 = realtime)
	public void changeTimeScaling(double newValue) {
		timeScale = newValue;
	}
	
	public void pauseTime() {
		while(true) {
			if(isPaused.compareAndSet(isPaused.get(), true))
				System.out.println("Application Time is paused");
				return;
		}
	}
	
	public void continueTime() {
		while(true) {
			if(isPaused.compareAndSet(isPaused.get(), false))
				System.out.println("Application time continues");
				return;
		}
	}
	
	public void endThread() {
		while(true) {
			if(running.compareAndSet(running.get(), false))
				this.interrupt(); //In case an interruptable method like Thread.sleep is being used
				System.out.println("Application Time has been interrupted");
				return;
		}
	}
	
}
