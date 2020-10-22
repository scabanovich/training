package concurrency.identityexample;

import java.io.InputStream;

import concurrency.identityexample.model.Identity;
import concurrency.identityexample.model.StatsEntry;

public class IdentityPipeline extends AbstractIdentityPipelene {

	public void process(InputStream input) {
		Identity i;
		while ((i = readIdentity(input)) != null) {
			final Identity identity = i;
			try {
				validateAddress(identity);
				
				phoneNumberFormatter.format(identity);;
				emailFormatter.format(identity);
				
				if (!identityService.persistOrUpdateBestMatch(identity)) {
					statsLedger.recordEntry(new StatsEntry(identity));
				}
				
			} catch (Exception e) {
				malformed.addIdentity(identity, e.getMessage());
			}
		}
	}

	Identity readIdentity(InputStream input) {
		return null;
	}

}
