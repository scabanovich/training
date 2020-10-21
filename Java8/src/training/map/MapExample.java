package training.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import training.comparator.Person;

public class MapExample {

	public static void forEachExample() {
		Map<String, List<Person>> map = new HashMap<>(); // ...
		
		map.forEach(    
			(city, list) -> System.out.println(city + ": " + list.size() + " people")
		);
	}

	public static void getOrDefaultExample() {
		Map<String, List<Person>> map = new HashMap<>(); // ...
		
		map.getOrDefault("Boston", Collections.emptyList());
		
	}

	public static void putIfAbsentExample() {
		Map<String, List<Person>> map = new HashMap<>(); // ...
		
		map.putIfAbsent("Boston", Collections.emptyList());
		map.get("Boston").add(new Person());
	}

	public static void replaceExample() {
		Map<String, List<Person>> map = new HashMap<>(); // ...
		
		map.replace("Boston", Collections.emptyList());
		map.replaceAll((city, list) -> list);

	}

	public static void computeExample() {
		Map<String, List<Person>> map = new HashMap<>(); // ...
		
		map.compute("Boston", (city, list) -> list);
		map.computeIfAbsent("Boston", (city) -> new ArrayList<>());
		map.computeIfPresent("Boston", (city, list) -> new ArrayList<>(list));

	}

	public static void mapOfMapsExample() {
		Map<String, Map<String,Person>> map = new HashMap<>(); // ...
		
		map.computeIfAbsent("Boston", (city) -> new HashMap<>())
			.put("a", new Person()); //adding to returned value
	}

	public static void mergeExample() {
		Map<String, Person> map = new HashMap<>(); // ...
		map.merge("key", new Person(), (oldPerson, newPerson) -> newPerson);
		
		Map<String, List<Person>> map1 = new HashMap<>(); // ...
		Map<String, List<Person>> map2 = new HashMap<>(); // ...
		map2.forEach((city, list) -> map1.merge(city, list, (oldList, newList) -> {
			oldList.addAll(newList);
			return oldList;
		}));
	}
}
