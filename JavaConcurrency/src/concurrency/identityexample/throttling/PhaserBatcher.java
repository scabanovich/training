package concurrency.identityexample.throttling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import concurrency.identityexample.model.Address;

class PhaserOperation {
	Address address;
	Phaser p;
	PhaserOperation(Address address, Phaser p) {
		this.address = address;
		this.p = p;
	}
	
}

public class PhaserBatcher {
	private BlockingQueue<PhaserOperation> jobQueue = new LinkedBlockingQueue<>();
	private final Phaser batcher;
	private final AtomicInteger batchSize;
	
	private final ExecutorService fetcher = Executors.newFixedThreadPool(500);
	private final ExecutorService waiter = Executors.newFixedThreadPool(500);
	private final ExecutorService runner = Executors.newFixedThreadPool(500);

	private AddressVerifier delegate;

	public PhaserBatcher(int batchSize, int timeout, AddressVerifier delegate) {
		this.batchSize = new AtomicInteger(batchSize);
		this.delegate = delegate;
		this.batcher = new Phaser(batchSize) {

			@Override
			protected boolean onAdvance(int phase, int registeredParties) {
				return false; //makes it reusable
			}
			
		};
		fetcher.submit(() -> {
			int phase = 0;
			while (!Thread.currentThread().isInterrupted()) {
				try {
					batcher.awaitAdvanceInterruptibly(phase, timeout, TimeUnit.MILLISECONDS);
					sendBatch();
				} catch (TimeoutException e) {
					sendBatch();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		});
	}
	
	private void sendBatch() {
		List<PhaserOperation> batch = new ArrayList<>();
		jobQueue.drainTo(batch, batchSize.get());
		
		runner.submit(() -> {
			List<Address> jobs = batch.stream().map(p -> p.address)
					.collect(Collectors.toList());
			delegate.verify(jobs);
			batch.stream().forEach(p -> p.p.arrive());
		});
	}

	public Future<?> submit(List<Address> jobs) {
		Phaser p = new Phaser(jobs.size());
		jobs.stream().forEach(job -> {
			jobQueue.offer(new PhaserOperation(job, p));
			try {
				batcher.arrive();
			} catch (IllegalStateException e) {
				//log this
			}
		});
		return waiter.submit(() -> {
			p.awaitAdvance(0);
		});
	}
}
