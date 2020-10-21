package training.map.stream;

import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import training.comparator.Person;

/**
 * 1. Does not hold data
 * 2. Does not modify data in collection
 * 3. May be unbounded.
 */
public class Streams {

	public void patternsToBuildStream() {
		//1
		List<Person> people = new ArrayList<Person>();
		Stream<Person> stream = people.stream();
		//2
		Stream.empty();
		//3
		Stream.of("one");
		Stream.of("one", "two");
		//4  infinity
		Stream.generate(() -> "one");
		//5  growing
		Stream.iterate("+", (s) -> s + "+");
		//6 random
		ThreadLocalRandom.current().ints();
		//7
		IntStream str = "hello".chars();
		//8 regex
		Stream<String> words = Pattern.compile("[^\\p{javaLetter}]").splitAsStream("my book");
		//9
//		Stream<String> lines = Files.lines(path);
		
		// Builder
		Stream.Builder<String> builder = Stream.builder();
		builder.add("one").add("two");
		Stream<String> s = builder.build(); // final
		
		//Use stream
		stream.forEach(System.out::println);
	}
	
	public void mapFilterReduceExample() {
		List<Person> people = new ArrayList<Person>(); //...
		people.stream()						//Stream<Person>
			.map(p -> p.getAge())			//Stream<Integer>
			.filter(age -> age > 20)		//Stream<Integer>
			.forEach(System.out::println);		//Prints age

		people.stream()						//Stream<Person>
//			.map(p -> p.getAge())			
			.filter(p -> p.getAge() > 20)	//Stream<Person>
			.forEach(System.out::println);		//Prints oerson

		people.stream()						//Stream<Person>
			.map(p -> p.getAge())			//Stream<Integer>
			.peek(System.out::println)			//do action on intermediate result
			.filter(age -> age > 20)		//Stream<Integer>
			.forEach(System.out::println);		//Prints age
		
		//peek - intermediate
		//forEach - terminal

		people.stream()						
			.skip(2)
			.limit(3)
			.map(p -> p.getAge())			
			.filter(age -> age > 20)		
			.forEach(System.out::println);	
	}
	
	public void matchExamples() {
		List<Person> people = new ArrayList<Person>(); //...
		
		people.stream()
			.allMatch(p -> p.getAge() > 20);
		
		people.stream()
			.noneMatch(p -> p.getAge() > 20);

		people.stream()
			.anyMatch(p -> p.getAge() > 20);
	}

	public void findExamples() {
		List<Person> people = new ArrayList<Person>(); //...
		
		Optional<Person> opt = people.stream()
			.filter(p -> p.getAge() > 20)	
			.findFirst();
		
		opt = people.stream()
			.filter(p -> p.getAge() > 20)	
			.findAny();

		people.stream()
			.anyMatch(p -> p.getAge() > 20);
	}

	public void reduceExamples() {
		List<Person> people = new ArrayList<Person>(); //...
		
		//result is int because identity is provided
		int sumOfAges = people.stream()
				.map(p -> p.getAge())
				.reduce(0, (p1, p2) -> p1 + p2);
		
		int maxOfAges = people.stream()
				.map(p -> p.getAge())
				.reduce(0, (p1, p2) -> Integer.max(p1, p2)); //it works here because ages are positive

		//no identity provided
		Optional<Integer> opt = people.stream()
				.map(p -> p.getAge())
				.reduce((p1, p2) -> Integer.max(p1, p2));
		
		//General: Identity, Accumulator, Combiner
	}
}
