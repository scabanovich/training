package training.predicate;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import training.comparator.Person;

public class FunctionalExample {
	
	// accept()
	public static void consumerExample() {
		Consumer<Person> personConsumer = (person) -> System.out.println(person);
		
		//Another way - method reference
		personConsumer = System.out::println;
		
		personConsumer.accept(new Person());
	}
	
	// get()
	public static void supplierExample() {
		Supplier<Person> pesonSupplier = () -> new Person();
		
		//Another way - constructor reference
		pesonSupplier = Person::new;
		
		pesonSupplier.get();
	}

	// apply()
	public static void functionExample() {
		Function<Person, Integer> ageMapper = person -> person.getAge();

		//Another way - method reference
		ageMapper = Person::getAge;
		
		ageMapper.apply(new Person());
		
		//BiFunction
		//UnaryOperator
	}
	
	public static void predicateExample() {
		//Anonymous type
		Predicate<String> p = new Predicate<String>() {
			@Override
			public boolean test(String t) {
				return t.length() < 20;
			}
		};
		
		//Lambda
		p = (t) -> t.length() < 20;
		
		p.test("");
		
		//BiPredicate
	}

	public static void primitiveExample() {
		//IntPredicate, IntFunction, etc
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
