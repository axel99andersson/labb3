package Threads;

import java.util.Random;
import lift.*;
import monitor.LiftMonitor;


public class PassengerThread extends Thread {
	private LiftView view;
	private LiftMonitor mon;
	private Random rand;
	
	public PassengerThread(LiftView view, LiftMonitor mon) {
		this.view = view;
		this.mon = mon;
		rand = new Random();
	}
	
	@Override
	public void run() {
		try {
		Passenger pass = view.createPassenger();
		Thread.sleep(rand.nextInt(20000));
		
		pass.begin();
		mon.passengerEntry(pass);
		pass.enterLift();
		mon.passengerInLift(pass);
		pass.exitLift();
		mon.passengerExit(pass);
		pass.end();
		
		} catch(InterruptedException e) {
			e.printStackTrace();
			throw new Error("Exception in Passenger thread", e);
		}
	}
}