package concurrency.identityexample.throttling;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.RejectedExecutionHandler;

import concurrency.identityexample.model.Address;

public class BackpressureAddressVerifier extends AddressVerifier {
	private ThreadPoolExecutor pool;
	private AddressVerifier delegate;

	public BackpressureAddressVerifier(AddressVerifier delegate,
			int workers, int lineLength) {
		this.delegate = delegate;
		this.pool = new ThreadPoolExecutor(
			workers, 							//corePoolSize
			workers,	 		//maximumPoolSize
			60L,	 					//keepAliveTime
			TimeUnit.SECONDS, 			// unit, 
			new ArrayBlockingQueue<>(lineLength),	//workQueue
//			new ThreadPoolExecutor.AbortPolicy()	//handler
			new RejectedExecutionHandler() {
				@Override
				public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
					
				}
			}
		);
	}

	public void verify(List<Address> addresses) {
		try {
			pool.submit(() -> {
				delegate.verify(addresses);
			}).get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			
			throw new IllegalStateException(e);
		}
	}

	public void throttledUp(int by) {
		pool.setMaximumPoolSize(pool.getMaximumPoolSize() + by);
	}

	public void throttledDown(int by) {
		pool.setMaximumPoolSize(pool.getMaximumPoolSize() - by);
	}

	public void close() {
		pool.shutdown();
	}
}
