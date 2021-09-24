package monitor;

import lift.*;
import Threads.*;

/**
 * - En person ska gå in/ut ur hissen per våning(?) 
 * - 
 * 
 * @author axelandersson
 *
 */
public class LiftMonitor {
	private LiftView view;
	private int floor;
	private boolean moving;
	private int direction;
	private int[] waitEntry;
	private int[] waitExit;
	private int load;
		
	public LiftMonitor(LiftView view) {
		this.view = view;
		this.floor = 0;
		this.moving = false;
		this.direction = 1;
		this.waitEntry = new int[7];
		this.waitExit = new int[7];
		this.load = 0;
	}
	
	//----------------------- Passenger methods ----------------------
	
	public synchronized void passengerEntry(Passenger pass) throws InterruptedException {
		waitEntry[pass.getStartFloor()]++;
		view.showDebugInfo(waitEntry, waitExit);
		while(pass.getStartFloor() != floor || moving || load > 3) {
			wait();
		}
		load++;
	}
	
	public synchronized void passengerInLift(Passenger pass) throws InterruptedException {
		notifyAll();
		waitExit[pass.getDestinationFloor()]++;
		waitEntry[pass.getStartFloor()]--;
		view.showDebugInfo(waitEntry, waitExit);
		while(pass.getDestinationFloor() != floor || moving) {
			wait();
		}
		//notifyAll();
	}
	
	public synchronized void passengerExit(Passenger pass) {
		waitExit[pass.getDestinationFloor()]--;
		load--;
		view.showDebugInfo(waitEntry, waitExit);
		notifyAll();
	}
	
	//------------------------ Lift methods ------------------------
	
	public synchronized void goToNewFloor() throws InterruptedException {
		while(waitEntry[floor] != 0 || waitExit[floor] != 0) {
			wait();
		}
		moving = true;
		floor = floor + direction;
		if(floor == 0) {
			direction = 1;
		} else if(floor == 6) {
			direction = -1;
		}
	}
	
	public synchronized void atNewFloor() {
		moving = false;
		notifyAll();
	}
	
	public synchronized void waitUntilPassenger() throws InterruptedException {
		while(zeroPassengers()) {
			wait();
		}
	}
	
	private boolean zeroPassengers() {
		for(int i = 0; i < waitEntry.length; i++) {
			if(waitEntry[i] != 0 || waitExit[i] != 0) {
				return false;
			}
		}
		return true;
	}
	
	public synchronized int getFloor() {
		return floor;
	}
	
	public synchronized int getDir() {
		return direction;
	}	
}