package concurrency.identityexample;

import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.StreamSupport;

import concurrency.identityexample.model.Identity;
import concurrency.identityexample.model.StatsEntry;

public class CountDownIdentityPipeline extends AbstractIdentityPipelene {

	private ExecutorService pool = Executors.newWorkStealingPool();

	public void process(InputStream input) {
		StreamSupport.stream(new IdentityIterable(input).spliterator(), true)
		.forEach((identity) -> {
			System.out.println("Processing identity #" + identity.getId());
			try {
				CountDownLatch cdl = new CountDownLatch(3);
				pool.submit(() -> { 
					validateAddress(identity);
					cdl.countDown();
				});
				pool.submit(() -> { 
					phoneNumberFormatter.format(identity);
					cdl.countDown();
				});
				pool.submit(() -> { 
					emailFormatter.format(identity);
					cdl.countDown();
				});
				
				pool.submit(() -> {
					try {
					if (cdl.await(3000, TimeUnit.MILLISECONDS)) {
						if (!identityService.persistOrUpdateBestMatch(identity)) {
							statsLedger.recordEntry(new StatsEntry(identity));
						}
					} else {
						malformed.addIdentity(identity, "Too long");
					}
					} catch (InterruptedException e) {
						
					}
				});
			} catch (Exception e) {
				malformed.addIdentity(identity, e.getMessage());
			}
		});
	}
	
}
