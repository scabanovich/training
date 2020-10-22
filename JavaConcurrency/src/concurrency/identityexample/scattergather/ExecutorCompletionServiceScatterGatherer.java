package concurrency.identityexample.scattergather;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import concurrency.identityexample.model.Identity;

public class ExecutorCompletionServiceScatterGatherer extends ScatterGatherer {
	private final ExecutorService pool = Executors.newCachedThreadPool();
	
	public Identity go(Scatterer s, Gatherer g) {
		//maintains its own queue
		ExecutorCompletionService<Identity> ecs = new ExecutorCompletionService<>(pool);
		while (s.hasNext() ) {
			ecs.submit(() -> s.next().call());
		}
		while (g.needsMore()) {
			try {
				Identity i = ecs.take().get();
				g.gatherResult(i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return g.getFinalResult();
	}

}
