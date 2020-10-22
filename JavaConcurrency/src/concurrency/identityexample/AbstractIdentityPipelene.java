package concurrency.identityexample;

import java.io.InputStream;
import java.util.Iterator;

import concurrency.identityexample.model.Formatter;
import concurrency.identityexample.model.Identity;
import concurrency.identityexample.model.IdentityService;
import concurrency.identityexample.model.MalformedIdentityRepository;
import concurrency.identityexample.model.StatsLedger;

public class AbstractIdentityPipelene {
	protected IdentityService identityService = new IdentityService();
	protected Formatter phoneNumberFormatter = Formatter.createPhoneNumberFormatter();
	protected Formatter emailFormatter = Formatter.createEmailFormatter();
	protected StatsLedger statsLedger = new StatsLedger();
	protected MalformedIdentityRepository malformed = new MalformedIdentityRepository();

	protected void validateAddress(Identity identity) {
		
	}
	
	class IdentityIterable implements Iterable<Identity> {
		InputStream input;
		
		public IdentityIterable(InputStream input) {
			this.input = input;
		}

		@Override
		public Iterator<Identity> iterator() {
			return null;
		}
		
	}
}
