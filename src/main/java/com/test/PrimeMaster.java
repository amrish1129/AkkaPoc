package com.test;

import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.UntypedActor;
import akka.routing.RoundRobinRouter;

public class PrimeMaster extends UntypedActor {
	
	private final ActorRef workerRouter;
	private final ActorRef listener;
	
	private final int numberOfWorkers;
	private int numberOfResults;
	Result1 finalResults = new Result1();
	
	public PrimeMaster( final int numberOfWorkers, ActorRef listener) {
		// TODO Auto-generated constructor stub
		this.numberOfWorkers = numberOfWorkers;
		this.listener = listener;
		
		// Create a new router to distribute messages out to 10 workers
		workerRouter = this.getContext()
                .actorOf( new Props(PrimeWorker.class)
                        .withRouter( new RoundRobinRouter( numberOfWorkers )), "workerRouter" );
	}

	@Override
	public void onReceive(Object message) throws Exception {
		// TODO Auto-generated method stub
		if (message instanceof NumberRangeMessage) {
			// We have a new set of work to perform
			NumberRangeMessage numberRangeMessage = (NumberRangeMessage) message;

			// Just as a demo: break the work up into 10 chunks of numbers
			long numberOfNumbers = numberRangeMessage.getEndNumber() - numberRangeMessage.getStartNumber();
			long segmentLength = numberOfNumbers / 10;

			for (int i = 0; i < numberOfWorkers; i++) {
				// Compute the start and end numbers for this worker
				long startNumber = numberRangeMessage.getStartNumber() + (i * segmentLength);
				long endNumber = startNumber + segmentLength - 1;

				// Handle any remainder
				if (i == numberOfWorkers - 1) {
					// Make sure we get the rest of the list
					endNumber = numberRangeMessage.getEndNumber();
				}

				// Send a new message to the work router for this subset of
				// numbers
				workerRouter.tell(new NumberRangeMessage(startNumber, endNumber), getSelf());
			}
		} else if( message instanceof Result1 ) {
			Result1 result = (Result1) message;
			finalResults.getResults().addAll(result.getResults());
			
			if ( ++numberOfResults >= 10) {
				listener.tell(finalResults, getSelf());
				
				getContext().stop(getSelf());
			} else {
				unhandled(message);
			}
		}
	}
}
