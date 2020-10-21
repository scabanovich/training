package training.predicate;

@FunctionalInterface
public interface MyPredicate<T> {
	public boolean test(T t);

	public default MyPredicate<T> and(MyPredicate<T> p) {
		return (t) -> test(t) && p.test(t);
	}

	public default MyPredicate<T> or(MyPredicate<T> p) {
		return (t) -> test(t) || p.test(t);
	}

	public static <U> MyPredicate<U> isEqualTo(U t) {
		return (a) -> a.equals(t);
	}
}
