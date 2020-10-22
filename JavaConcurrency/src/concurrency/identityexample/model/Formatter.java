package concurrency.identityexample.model;

public class Formatter {
	public void format(Identity identity) {
		
	}

	public static Formatter createPhoneNumberFormatter() {
		return new Formatter();
	}

	public static Formatter createEmailFormatter() {
		return new Formatter();
	}
}
