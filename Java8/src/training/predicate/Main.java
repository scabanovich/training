package training.predicate;

public class Main {

	public static void main(String[] args) {
		MyPredicate<String> p1 = s -> s.length() < 20;

		boolean b = p1.test("Hello");
		System.out.println("Hello is shorter than 20 chars: " + b);

		MyPredicate<String> p2 = s -> s.length() > 5;
		
		MyPredicate<String> p3 = p1.and(p2);
		
		String[] tests = new String[] {"Yes", "Good morning", "Good morning gentlemen"};
		for (String t: tests) {
			System.out.println("P3 for " + t + ": " + p3.test(t));
		}
		
		MyPredicate<String> p4 = p1.or(p2);
		for (String t: tests) {
			System.out.println("P4 for " + t + ": " + p4.test(t));
		}
		
		MyPredicate<String> p5 = MyPredicate.isEqualTo("Yes");
		for (String t: tests) {
			System.out.println("P5 for " + t + ": " + p5.test(t));
		}
		
		MyPredicate<Integer> p6 = MyPredicate.isEqualTo(8);
	}

}
