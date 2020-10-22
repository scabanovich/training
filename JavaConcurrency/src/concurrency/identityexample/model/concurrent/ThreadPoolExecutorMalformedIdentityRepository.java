package concurrency.identityexample.model.concurrent;

import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import concurrency.identityexample.model.Identity;
import concurrency.identityexample.model.MalformedIdentityRepository;

public class ThreadPoolExecutorMalformedIdentityRepository extends MalformedIdentityRepository {
	private MalformedIdentityRepository delegate;
	private ExecutorService pool = Executors.newCachedThreadPool();

	public ThreadPoolExecutorMalformedIdentityRepository(MalformedIdentityRepository delegate) {
		this.delegate = delegate;
	}

	@Override
	public void addIdentity(Identity identity, String reason) {
//		addIdentityBlocking(identity, reason);
//		addIdentityOldWay(identity, reason);
		pool.submit(() -> delegate.addIdentity(identity, reason));
	}

	@Override
	public void addIdentity(InputStream message, String reason) {
		pool.submit(() -> delegate.addIdentity(message, reason));
	}
	
	private void addIdentityBlocking(Identity identity, String reason) {
		delegate.addIdentity(identity, reason);
	}

	//SLOWER!!!
	private void addIdentityOldWay(Identity identity, String reason) {
		new Thread(()-> delegate.addIdentity(identity, reason)).start();
	}

}
