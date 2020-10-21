package training.comparator;

import java.util.Comparator;
import java.util.function.Function;

@FunctionalInterface
public interface MyComparator<T> {

	public int compare(T t1, T t2);
	
	public default MyComparator<T> thenComparing(MyComparator<T> cmp) {
		return (t1,t2) -> compare(t1, t2) == 0 ? cmp.compare(t1, t2) : compare(t1, t2);
	}

	public default MyComparator<T> thenComparing(Function<T,Comparable> f) {
		return thenComparing(comparing(f));
	}

	public static <U> MyComparator<U> comparing(Function<U,Comparable> f) {
		return (p1, p2) -> ((Comparable)f.apply(p1)).compareTo(f.apply(p2));
	}

}
