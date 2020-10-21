package training.lambda;

import java.util.Arrays;
import java.util.Comparator;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;

public class LambdaExpressions {
	
	public static void aComparatorExample() {
		Comparator<String> comparator;
		//Anonimous class
		comparator = new Comparator<String>() {
			
			@Override
			public int compare(String s1, String s2) {
				return Integer.compare(s1.length(), s2.length());
			}
		};
		
		String[] tabStrings = new String[]{"a1", "b", "c23"};
		Arrays.sort(tabStrings, comparator);
		
		//Lambda 
		comparator = (s1, s2) -> Integer.compare(s1.length(), s2.length());
		
		//or
		comparator = (s1, s2) -> {
			System.out.println("I am comparing strings");
			return Integer.compare(s1.length(), s2.length());
		};
	}

	public static void aRunnableExample() {
		Runnable r;
		//Anonimous class
		r = new Runnable() {
			@Override
			public void run() {
				int i = 0;
				while(i++ < 10) {
					System.out.println("It works! " + i);
				}
			}
		};
		new Thread(r).start();
		
		//Lambda 
		r = () -> {
			int i = 0;
			while(i++ < 10) {
				System.out.println("It works! " + i);
			}
		};
	}
	
	public static void aMethodReferenceExample() {
		class Person {
			public int getAge() {
				return 0;
			}
		}
		
		//One way
		Function<Person, Integer> f = person -> person.getAge();
		
		//Second way
		f = Person::getAge;
	}
	
	public static void aMethodReferenceExample2() {
		//1. lambda
		BinaryOperator<Integer> sum = (i1, i2) -> i1 + i2;
		//2. lambda
		sum = (i1, i2) -> Integer.sum(i1, i2);
		//3. method reference
		sum = Integer::sum;
		BinaryOperator<Integer> max = Integer::max;
	}

	public static void aMethodReferenceExample3() {
		//lambda
		Consumer<String> printer = s -> System.out.println(s);
		//method reference
		printer = System.out::println;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
