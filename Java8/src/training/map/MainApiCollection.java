package training.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import training.comparator.Person;

public class MainApiCollection {

	public static void main(String[] args) {
		Person p1 = new Person("Alice", "", 23);
		Person p2 = new Person("Brian", "", 56);
		Person p3 = new Person("Chelsea", "", 46);
		Person p4 = new Person("David", "", 28);
		Person p5 = new Person("Erica", "", 37);
		Person p6 = new Person("Francosco", "", 18);
		
		List<Person> people = new ArrayList<>(Arrays.asList(p1, p2, p3, p4, p5, p6));
		
		people.removeIf(person -> person.getAge() < 20);
		
		people.replaceAll(person -> new Person(person.getFirstName().toUpperCase(), person.getLastName(), person.getAge()));
		
		people.sort(Comparator.comparing(Person::getAge).reversed()); //by age descending
		
		people.forEach(System.out::println);
		
		City newYork = new City("newYork");
		City shanhai = new City("Shanhai");
		City paris = new City("Paris");
		
		Map<City, List<Person>> map = new HashMap<>();
		
		//Add way 1
		map.putIfAbsent(paris, new ArrayList<>());
		map.get(paris).add(p1);
		
		//Add way 2
		map.computeIfAbsent(newYork, city -> new ArrayList<>()).add(p2);
		
		System.out.println("People from Paris: " + map.getOrDefault(paris, Collections.emptyList()));
		System.out.println("People from New York: " + map.getOrDefault(newYork, Collections.emptyList()));

		Map<City, List<Person>> map2 = new HashMap<>();
		map.computeIfAbsent(newYork, city -> new ArrayList<>()).add(p4);
		map.computeIfAbsent(shanhai, city -> new ArrayList<>()).add(p5);
		map.computeIfAbsent(shanhai, city -> new ArrayList<>()).add(p6);
		
		map2.forEach((city,people1) -> System.out.println(city + ": " + people1));
		
		map2.forEach(
				(city,people1) -> {
					map.merge(city, people1, (peopleFromMap1,peopleFromMap2) -> {
						peopleFromMap1.addAll(peopleFromMap2);
						return peopleFromMap1;
					});
				}
		);
	}

}
