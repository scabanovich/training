package concurrency.identityexample.throttling;

import java.util.List;
import java.util.concurrent.Semaphore;

import concurrency.identityexample.model.Address;

public class ConnectionLimitingAddressVerifier extends AddressVerifier {
	public static class ConfigurableSemaphore extends Semaphore {
		public ConfigurableSemaphore(int permits) {
			super(permits);
		}

		public void increasePermits(int by) {
			super.release(by);
		}

		public void reducePermits(int by) {
			super.reducePermits(by);
		}
	}

	private ConfigurableSemaphore limiter;
	private AddressVerifier delegate;
	
	public ConnectionLimitingAddressVerifier(AddressVerifier delegate, int permits) {
		this.delegate = delegate;
		this.limiter = new ConfigurableSemaphore(permits);
	}

	public void verify(List<Address> addresses) {
		try {
			limiter.acquire();
			try {
				delegate.verify(addresses);
			} finally {
				limiter.release();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	public void throttledUp(int by) {
		limiter.increasePermits(by);
	}

	public void throttledDown(int by) {
		limiter.reducePermits(by);
	}

}
