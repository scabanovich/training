package concurrency.identityexample.scattergather;

import concurrency.identityexample.model.Identity;

public class SingleThreadScatterGatherer extends ScatterGatherer {
	public Identity go(Scatterer s, Gatherer g) {
		while (s.hasNext() && g.needsMore()) {
			try {
				Identity result = s.next().call();
				g.gatherResult(result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return g.getFinalResult();
	}

}
