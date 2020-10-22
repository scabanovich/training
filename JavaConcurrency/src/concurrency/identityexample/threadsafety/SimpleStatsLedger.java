package concurrency.identityexample.threadsafety;

import java.util.HashMap;
import java.util.Map;

import concurrency.identityexample.model.StatsEntry;
import concurrency.identityexample.model.StatsLedger;

public class SimpleStatsLedger extends StatsLedger {
	private volatile Map<String, Integer> firstNameMap = new HashMap<>();
	private volatile Map<String, Integer> lastNameMap = new HashMap<>();
	private volatile Map<Integer, Integer> ageMap = new HashMap<>();
	private volatile Integer recordCount = 0;

	public void recordEntry(StatsEntry statsEntry) {
		increment(firstNameMap,"firstName");
		increment(lastNameMap,"lastName");
		increment(ageMap, 0);
		
		synchronized (this) {
			recordCount++;
		}
	}

	private <T> void increment(Map<T, Integer> map, T key) {
		synchronized(map) {
			Integer count = map.putIfAbsent(key, 1);
			if (count != 1) {
				map.put(key,count + 1);
			}
		}
	}
}
