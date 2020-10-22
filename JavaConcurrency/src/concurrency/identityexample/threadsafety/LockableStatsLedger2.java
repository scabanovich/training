package concurrency.identityexample.threadsafety;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import concurrency.identityexample.model.StatsEntry;
import concurrency.identityexample.model.StatsLedger;

public class LockableStatsLedger2 extends StatsLedger {
	private volatile StatsLedger delegate;
	
	private ReentrantLock lock = new ReentrantLock();
	
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public LockableStatsLedger2(StatsLedger delegate) {
		this.delegate = delegate;
		Runnable command = null;
		scheduler.scheduleAtFixedRate(command, 1000, 5000, TimeUnit.MILLISECONDS);
	}
	
	private <T> T withLock(Supplier<T> supplier) {
		lock.lock();
		try {
			return supplier.get();
		} finally {
			lock.unlock();
		}
	}
	
	public void recordEntry(StatsEntry statsEntry) {
		withLock(() -> { delegate.recordEntry(statsEntry); return null; });
	}
	
}
