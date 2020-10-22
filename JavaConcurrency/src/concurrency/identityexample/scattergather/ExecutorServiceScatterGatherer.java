package concurrency.identityexample.scattergather;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import concurrency.identityexample.model.Identity;

public class ExecutorServiceScatterGatherer extends ScatterGatherer {
	private final ExecutorService pool = Executors.newCachedThreadPool();
	
	public Identity go(Scatterer s, Gatherer g) {
		Queue<Future<Identity>> results = new LinkedList<>(); 
		while (s.hasNext() ) {
			Future<Identity> futureResult = pool.submit(() -> s.next().call());
			results.add(futureResult);
		}
		while (!results.isEmpty() && g.needsMore()) {
			try {
				Future<Identity> futureResult = results.poll();
				Identity result = futureResult.get();
				g.gatherResult(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return g.getFinalResult();
	}

}
