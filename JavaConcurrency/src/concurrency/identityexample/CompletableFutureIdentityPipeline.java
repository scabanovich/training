package concurrency.identityexample;

import java.io.InputStream;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.StreamSupport;

import concurrency.identityexample.model.StatsEntry;

public class CompletableFutureIdentityPipeline extends AbstractIdentityPipelene {

	private ExecutorService verifyPool = Executors.newWorkStealingPool();
	private ExecutorService persistPool = Executors.newWorkStealingPool();

	public void process(InputStream input, Runnable processCompleted) {
		StreamSupport.stream(new IdentityIterable(input).spliterator(), true)
		.forEach((identity) -> {
			System.out.println("Processing identity #" + identity.getId());
			try {
				CompletableFuture<Void> address = 
						CompletableFuture.runAsync(() -> validateAddress(identity), 
								verifyPool);
				CompletableFuture<Void> phoneNumber = 
						CompletableFuture.runAsync(() -> phoneNumberFormatter.format(identity), 
								verifyPool);
				CompletableFuture<Void> emailNumber = 
						CompletableFuture.runAsync(() -> emailFormatter.format(identity), 
								verifyPool);
				
				CompletableFuture.allOf(address, phoneNumber, emailNumber)
					.thenRunAsync(() -> {
					if (!identityService.persistOrUpdateBestMatch(identity)) {
						statsLedger.recordEntry(new StatsEntry(identity));
					}
				}, persistPool)
					.exceptionally((ex) -> {
						malformed.addIdentity(identity, ex.getMessage());
						return null;
					});
			} catch (Exception e) {
				malformed.addIdentity(identity, e.getMessage());
			}
		});
	}

}
