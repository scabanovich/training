package training.map.stream;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MainBuildingStreams {

	public static void main(String[] args) {
		List<Integer> ints = Arrays.asList(0,1,2,3,4,5,6,7,8,9);
		
		Stream<Integer> s1 = ints.stream();
		Stream<Integer> s2 = Stream.of(0,1,2,3,4,5,6,7,8,9);
		
		s1.forEach(System.out::println);
		
		Stream<String> streamOfStrings = Stream.generate(() -> "s");
		streamOfStrings.limit(5).forEach(System.out::println);
		
		Stream<String> streamOfStrings2 = Stream.iterate("+", s -> s + "+");
		streamOfStrings2.limit(5).forEach(System.out::println);

		IntStream si = ThreadLocalRandom.current().ints();
		si.limit(5).forEach(System.out::println);
	}

}
