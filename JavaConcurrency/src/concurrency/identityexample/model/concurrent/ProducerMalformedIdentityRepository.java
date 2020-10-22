package concurrency.identityexample.model.concurrent;

import java.io.InputStream;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RecursiveAction;

import concurrency.identityexample.model.Identity;
import concurrency.identityexample.model.MalformedIdentityRepository;

public class ProducerMalformedIdentityRepository extends MalformedIdentityRepository {
	private MalformedIdentityRepository delegate;
	
	private BlockingQueue<Runnable> todo = new LinkedBlockingQueue<>();
//	private ExecutorService pool = Executors.newWorkStealingPool();

	public ProducerMalformedIdentityRepository(MalformedIdentityRepository delegate) {
		this.delegate = delegate;
		new ForkJoinPool(4).execute(new Consumer());
	}

	@Override
	public void addIdentity(Identity identity, String reason) {
		todo.offer(() -> delegate.addIdentity(identity, reason));
	}

	@Override
	public void addIdentity(InputStream message, String reason) {
		todo.offer(() -> delegate.addIdentity(message, reason));
	}

	// For simplicity consumer is implemented here
	
	private class Consumer extends RecursiveAction {
		protected void compute() {
			try {
				Runnable r = todo.take();
				new Consumer().fork(); 
				r.run();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}
	
}
