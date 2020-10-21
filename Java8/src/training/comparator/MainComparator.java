package training.comparator;

import java.util.function.Function;

public class MainComparator {

	public static void main(String[] args) {
		MyComparator<Person> cmpAge = (p1, p2) -> p2.getAge() - p1.getAge();
		MyComparator<Person> cmpFirstName = (p1, p2) -> p2.getFirstName().compareTo(p1.getFirstName());
		MyComparator<Person> cmpLastName = (p1, p2) -> p2.getLastName().compareTo(p1.getLastName());

		Function<Person, Integer> f1 = (p) -> p.getAge();
		Function<Person, String> f2 = (p) -> p.getFirstName();
		Function<Person, String> f3 = (p) -> p.getLastName();
		
		f1 = Person::getAge; //etc

//		Worked when comparing had type parameter Integer
//		MyComparator<Person> cmpPerson = MyComparator.comparing(f1);
		
		MyComparator<Person> cmpPersonAge =	MyComparator.comparing(Person::getAge);
		MyComparator<Person> cmpPersonLastName =	MyComparator.comparing(Person::getLastName);
		
		MyComparator<Person> cmp = cmpPersonAge.thenComparing(cmpPersonLastName);
		
		//finally
		cmp = MyComparator.comparing(Person::getLastName)
				.thenComparing(Person::getFirstName)
				.thenComparing(Person::getAge);
	}

}
