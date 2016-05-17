package com.test;

import akka.actor.UntypedActor;

/**
 * PrimeWorker is the class that performs the labor of computing prime numbers. 
 * It inspects a received message and if it is a NumberRangeMessage it processes it. 
 * Otherwise PrimeWorker invokes the unhandled() method to notify its parent that it did not handle the message.
 * @author amrish
 *
 */
public class PrimeWorker extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		Result1 result = new Result1();
		if (message instanceof NumberRangeMessage ) {
			NumberRangeMessage nrm = (NumberRangeMessage) message;
			
			for ( long i = nrm.getStartNumber(); i < nrm.getEndNumber(); i++ ) {
				if (isPrimeNumber(i))
					result.getResults().add(i);
			}
			
			getSender().tell(result, getSelf());
		} else {
			unhandled(message);
		}
		
		
	}
	
	private boolean isPrimeNumber(long l) {

		if (l == 1 || l == 2 || l == 3) {
			return true;
		}

		if (l % 2 == 0) {
			return false;
		}
		
		for (int i = 3 ; i*i <= l; i++) {
			if ( l % i == 0) {
				return false;
			}
		}
		
		return true;
	}

}
