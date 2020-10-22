package concurrency.identityexample;

import java.io.InputStream;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import concurrency.identityexample.model.Identity;
import concurrency.identityexample.model.StatsEntry;

public class ContinuationPassingIdentityPipeline extends AbstractIdentityPipelene {

	private ExecutorService pool = Executors.newWorkStealingPool();

	public void process(InputStream input, Runnable processCompleted) {
		read(input, processCompleted, (i) -> {
			format(i, this::fail, (i2) -> {
				persist(i2, this::fail, (i3) -> {
					statsLedger.recordEntry(new StatsEntry(i3));
				});
			});
		});
	}

	private void read(InputStream input, Runnable end, Consumer<Identity> next) {
		ForkJoinPool.commonPool().submit(() -> {
			Identity identity = null; //identityReader.read(input);
			if (identity != null) {
				read(input, end, next);
				System.out.println("Processing identity #" + identity.getId());
				next.accept(identity);
			} else {
				end.run();
			}
		});
	}
	
	private void format(Identity identity, BiConsumer<Identity,Throwable> failed, Consumer<Identity> identityFormatted) {
		pool.submit(() -> { 
			phoneNumberFormatter.format(identity);
			emailFormatter.format(identity);
			try {
				validateAddress(identity);
				identityFormatted.accept(identity);
			} catch (Exception e) {
				failed.accept(identity, e);
			}
		});
	}
	
	void fail(Identity identity, Throwable t) {
		pool.submit(() -> { 
			malformed.addIdentity(identity, t.getMessage());
		});		
	}
	
	private void persist(Identity identity, BiConsumer<Identity,Throwable> failed, Consumer<Identity> identityPersisted) {
		pool.submit(() -> { 
			if (!identityService.persistOrUpdateBestMatch(identity)) {
				statsLedger.recordEntry(new StatsEntry(identity));
			}
		});
		
	}
	
}
