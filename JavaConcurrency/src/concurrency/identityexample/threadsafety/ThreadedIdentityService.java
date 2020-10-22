package concurrency.identityexample.threadsafety;

import java.util.ArrayList;
import java.util.List;

import concurrency.identityexample.model.Identity;
import concurrency.identityexample.model.IdentityService;

public class ThreadedIdentityService extends IdentityService {
	List<Identity> verifiedIdentities = new ArrayList<>();

	@Override
	public boolean persistOrUpdateBestMatch(Identity identity) {
		for (Identity i: verifiedIdentities) {
			
		}
		return false;
	}

	void scoreMatch(Identity i1, Identity i2) {
		
	}
}
