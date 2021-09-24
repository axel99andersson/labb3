package Threads;

import monitor.LiftMonitor;
import lift.*;

public class LiftThread extends Thread {
	private LiftView view;
	private LiftMonitor mon;
	
	public LiftThread(LiftView view, LiftMonitor mon) {
		this.view = view;
		this.mon = mon;
	}
	
	@Override
	public void run() {
		view.openDoors(0);
		while(true) {
			try {
			int floor = mon.getFloor();
			int dir = mon.getDir();
			mon.goToNewFloor();
			view.moveLift(floor, floor+dir);
			mon.atNewFloor();
			
			} catch(InterruptedException e) {
				e.printStackTrace();
				throw new Error("Exception in Lift-Thread", e);
			}
		}
	}
}
