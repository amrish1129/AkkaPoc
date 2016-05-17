package akka.first.app.mapreduce.actors;

import java.util.HashMap;
import java.util.List;

import akka.actor.UntypedActor;
import akka.first.app.mapreduce.messages.MapData;
import akka.first.app.mapreduce.messages.ReduceData;
import akka.first.app.mapreduce.messages.WordCount;

public class ReduceActor extends UntypedActor {

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof MapData) {
			MapData work = (MapData) message;
			// Reduce the data and send it back to Master Actor
			getSender().tell(reduce(work.getDataList()), getSelf());
		} else {
			unhandled(message);
		}
	}

	private ReduceData reduce(List<WordCount> dataList) {
		HashMap<String, Integer> reducedMap = new HashMap<String, Integer>();
		for (WordCount wordCount : dataList) {
			if (reducedMap.containsKey(wordCount.getWord())) {
				Integer value = (Integer) reducedMap.get(wordCount.getWord());
				value++;
				reducedMap.put(wordCount.getWord(), value);
			} else {
				reducedMap.put(wordCount.getWord(), Integer.valueOf(1));
			}
		}
		return new ReduceData(reducedMap);
	}

}
