package training.map.reduce;

import java.util.ArrayList;
import java.util.List;

import training.comparator.Person;

public class ReduceExample {

	//Talk about JDK 7 way to implement it
	public void reduceExample() {
		//average of people older than 20
		//Map, Filter, Reduce
		List<Person> people = new ArrayList<Person>(); //...
		
		//1 map. This solution would duplicate the list.
//		List<Integer> ages = Lists.map(   ) ;
		//2 filter. Again almost duplicate.
//		List<Integer> agesGT20 = Lists.filter( ... ) ;
		//3 reduce. Finally compute
//		int sum = Lists.reduce( ... ) ;
	}
}
