package concurrency.identityexample;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import concurrency.identityexample.model.Identity;
import concurrency.identityexample.model.MalformedIdentityRepository;
import concurrency.identityexample.scattergather.ScatterGatherer;

public class MultiStrategyReader {
	MultiStrategyReader primary = new MultiStrategyReader();
	MultiStrategyReader secondary = new MultiStrategyReader();
	MalformedIdentityRepository malformed = new MalformedIdentityRepository();
	
	ScatterGatherer scatterGatherer; // initialize

	public Identity read(InputStream is) {
		try (CopyingInputStream cis = new CopyingInputStream(is)) {
			Identity result = primary.read(cis);
			if (isOkay(result)) {
				return result;
			}
			
//			result = scatterGatherer.go(new ReaderScatterer(cis, secondary), new IdentityGatherer());

			if (isOkay(result)) {
				return result;
			}
			malformed.addIdentity(cis.reread(), "");
		} catch (IOException e) {
			throw new IllegalStateException();
		}
		
		return read(is);
	}
	
	//just for simplicity 
	class CopyingInputStream extends BufferedInputStream {
		InputStream is;
		public CopyingInputStream(InputStream is) throws IOException {
			super(is);
			this.is = is;
		}
		
		public InputStream reread() {
			return is;
		}
	}
	
	boolean isOkay(Identity result) {
		return true;
	}
}
