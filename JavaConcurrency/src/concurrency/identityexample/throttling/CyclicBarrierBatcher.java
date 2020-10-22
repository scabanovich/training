package concurrency.identityexample.throttling;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import concurrency.identityexample.model.Address;

class BatchOperation {
	Address address;
	CountDownLatch cdl;
	BatchOperation(Address address, CountDownLatch cdl) {
		this.address = address;
		this.cdl = cdl;
	}
	
}

public class CyclicBarrierBatcher {
	private BlockingQueue<BatchOperation> jobQueue = new LinkedBlockingQueue<>();
	
	private final CyclicBarrier batcher;
	
	private final int batchSize;
	private final int timeout;
	
	private final ExecutorService fetcher = Executors.newFixedThreadPool(500);
	private final ExecutorService sender = Executors.newFixedThreadPool(500);
	private final ExecutorService latch = Executors.newFixedThreadPool(500);

	private AddressVerifier delegate;
	
	public CyclicBarrierBatcher(int batchSize, int timeout, AddressVerifier delegate) {
		this.batchSize = batchSize;
		this.timeout = timeout;
		this.delegate = delegate;
		batcher = new CyclicBarrier(batchSize);
	}
	
	private void fetchThenSendBatch() {
		List<BatchOperation> batch = new ArrayList<>();
		jobQueue.drainTo(batch, batchSize);

		sender.submit(() -> {
			List<Address> addresses = batch.stream()
					.map(b -> b.address)
					.collect(Collectors.toList());
			delegate.verify(addresses);
			batch.stream().forEach((b) ->b.cdl.countDown()); ;
		});
	}
	
	public Future<?> submit(List<Address> jobs) {
		CountDownLatch cdl = new CountDownLatch(jobs.size());
		
		jobs.stream().forEach(job -> {
			jobQueue.offer(new BatchOperation(job, cdl));
			fetcher.submit(() -> {
				try {
					if (batcher.await(timeout, TimeUnit.MILLISECONDS) == 0) {
						fetchThenSendBatch();
					}
				} catch (TimeoutException | BrokenBarrierException e) {
					fetchThenSendBatch();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			});
		});
		
		return latch.submit(() -> {
			try {
				cdl.wait();
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		});
	}
	
}
