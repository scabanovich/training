package concurrency.identityexample.threadsafety;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import concurrency.identityexample.model.StatsEntry;
import concurrency.identityexample.model.StatsLedger;

public class ThreadSafeStatsLedger extends StatsLedger {
	//volatile is not needed
	//LongAdder is better than AtomicInteger
	private Map<String, AtomicInteger> firstNameMap = new ConcurrentHashMap<>();
	private Map<String, AtomicInteger> lastNameMap = new ConcurrentHashMap<>();
	private Map<Integer, AtomicInteger> ageMap = new ConcurrentHashMap<>();
	private AtomicInteger recordCount = new AtomicInteger();

	public void recordEntry(StatsEntry statsEntry) {
		increment(firstNameMap,"firstName");
		increment(lastNameMap,"lastName");
		increment(ageMap, 0);
		
		recordCount.incrementAndGet();
	}

	private <T> void increment(Map<T, AtomicInteger> map, T key) {
//		AtomicInteger count = map.putIfAbsent(key, new AtomicInteger(1));
//		if (count != null) {
//			count.incrementAndGet();
//		}
		map.computeIfAbsent(key, k -> new AtomicInteger(0)).incrementAndGet();
	}
}
