package com.test;

import akka.actor.UntypedActor;

public class PrimeListener extends UntypedActor {

	@Override
	public void onReceive( Object message ) throws Exception {
		// TODO Auto-generated method stub
		if (message instanceof Result1) {
			Result1 result = ( Result1 )message;
			System.out.println("Results: ");
			for (Long value : result.getResults()) {
				System.out.println(value + ", ");
			}
			System.out.println();

			// Exit
			getContext().system().shutdown();
		}  else {
			unhandled( message );
		}
	}
}
