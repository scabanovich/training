package concurrency.identityexample.model.concurrent;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import concurrency.identityexample.model.Identity;
import concurrency.identityexample.model.MalformedIdentityRepository;

public class ForkJoinPoolExecutorMalformedIdentityRepository extends MalformedIdentityRepository {
	private MalformedIdentityRepository delegate;
	
											//Difference!
	private ExecutorService pool = Executors.newWorkStealingPool();

	public ForkJoinPoolExecutorMalformedIdentityRepository(MalformedIdentityRepository delegate) {
		this.delegate = delegate;
	}

	@Override
	public void addIdentity(Identity identity, String reason) {
		pool.submit(() -> delegate.addIdentity(identity, reason));
	}

	@Override
	public void addIdentity(InputStream message, String reason) {
		pool.submit(() -> delegate.addIdentity(message, reason));
	}
	
}
