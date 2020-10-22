package concurrency.identityexample.threadsafety;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import concurrency.identityexample.model.StatsEntry;
import concurrency.identityexample.model.StatsLedger;

public class LockableStatsLedger extends StatsLedger {
	private volatile StatsLedger delegate;
	
	private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public LockableStatsLedger(StatsLedger delegate) {
		this.delegate = delegate;
		Runnable command = null;
		scheduler.scheduleAtFixedRate(command, 1000, 5000, TimeUnit.MILLISECONDS);
	}
	
	private <T> T withLock(Supplier<T> supplier) {
		synchronized (delegate) {
			return supplier.get();
		}
	}
	
	public void recordEntry(StatsEntry statsEntry) {
		withLock(() -> { delegate.recordEntry(statsEntry); return null; });
	}
	
}
