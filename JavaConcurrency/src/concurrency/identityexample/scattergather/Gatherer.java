package concurrency.identityexample.scattergather;

import concurrency.identityexample.model.Identity;

public interface Gatherer {
	
	public boolean needsMore();
	public void gatherResult(Identity identity);
	public Identity getFinalResult(); 

}
