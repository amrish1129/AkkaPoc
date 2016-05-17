package akka.first.app.mapreduce;

import java.time.Duration;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.first.app.mapreduce.actors.MasterActor;
import akka.first.app.mapreduce.messages.Result;
import akka.pattern.Patterns;
import akka.util.Timeout;
import scala.concurrent.Await;
import scala.concurrent.Future;

public class MapReduceApplication {
	public static void main(String... args) throws Exception {
		Timeout timeout = new Timeout(Duration.ofSeconds(5).toMillis());
		ActorSystem _system = ActorSystem.create("mapreduceapp");
		ActorRef master = _system.actorOf(new Props(MasterActor.class), "master");
		master.tell("The quick brown fox tried to jump over the lazy dog and fell on the dog", master);
		master.tell("Dog is man's best friend", master);
		master.tell("Dog and Fox belong to the same family", master);
		Thread.sleep(5000);
		Future<Object> future = Patterns.ask(master, new Result(), timeout);
		String result = (String) Await.result(future, timeout.duration());
		System.out.println(result);
		_system.shutdown();
	}
}
