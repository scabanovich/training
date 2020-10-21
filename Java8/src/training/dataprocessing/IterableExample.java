package training.dataprocessing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import training.comparator.Person;

public class IterableExample {

	public static void example() {
		List<Person> people = new ArrayList<>(); //...
		people.forEach(System.out::println);
		people.removeIf(person -> person.getAge() < 20);
		people.replaceAll(person -> new Person());
	}
	
	public static void exampleReplaceAll() {
		List<String> names = new ArrayList<>(); //...
		names.replaceAll(name -> name.toUpperCase());
		names.replaceAll(String::toLowerCase);
	}

	public static void exampleSort() {
		List<Person> people = new ArrayList<>(); //...
		people.sort(Comparator.comparing(Person::getFirstName)
						.thenComparing(Person::getAge)	);
	}
}
