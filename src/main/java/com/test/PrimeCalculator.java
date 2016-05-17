package com.test;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.actor.UntypedActorFactory;

public class PrimeCalculator {

	public void calculate(long startNumber, long endNumber) {
		// Actor System
		ActorSystem actorSystem = ActorSystem.create("primeCalculator");

		// Listener
		final ActorRef primeListener = actorSystem.actorOf(new Props(PrimeListener.class), "primeListener");

		// Create the PrimeMaster: we need to define an UntypedActorFactory so
		// that we can control
		// how PrimeMaster instances are created (pass in the number of workers
		// and listener reference
		ActorRef primeMaster = actorSystem.actorOf(new Props(new UntypedActorFactory() {
			public UntypedActor create() {
				return new PrimeMaster(10, primeListener);
			}
		}), "primeMaster");

		primeMaster.tell(new NumberRangeMessage(startNumber, endNumber));

	}

	public static void main(String[] args) {
		/*if (args.length < 2) {
			System.out.println("Usage: java com.geekcap.akka.prime.PrimeCalculator <start-number> <end-number>");
			System.exit(0);
		}*/

		long startNumber = 0;
		long endNumber = 1000;

		PrimeCalculator primeCalculator = new PrimeCalculator();
		primeCalculator.calculate(startNumber, endNumber);
	}
}
