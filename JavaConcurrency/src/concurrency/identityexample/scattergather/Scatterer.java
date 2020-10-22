package concurrency.identityexample.scattergather;

public interface Scatterer {

	boolean hasNext();
	
	Worker next();
}
