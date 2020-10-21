package training.map.reduce;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.BinaryOperator;

public class MainReduction {
	
	/**
	 * Caveats of reduction
	 * 1. non-associativity of binary operation
	 * 2. no idententity for binary operation
	 */

	public static void main(String[] args) {
		List<Integer> ints = Arrays.asList(0,1,2,3,4,5,6,7,8,9);
		
		List<Integer> ints1 = Arrays.asList(0,1,2,3,4);
		List<Integer> ints2 = Arrays.asList(5,6,7,8,9);

		BinaryOperator<Integer> op = (i1, i2) -> i1 + i2;  //sum is associative
								op = (i1, i2) -> (i1 + i2) * (i1 + i2); //not associative
								op = (i1, i2) -> i1;  //sum is associative
								op = (i1, i2) -> (i1 + i2) / 2; //not associative
								
								op = (i1, i2) -> Integer.max(i1, i2); // has no identity element
		
		int reduction = reduce(ints, 0, op);
		
		System.out.println("Reduction : " + reduction);
		
		//Imitate parallel
		int reduction1 = reduce(ints1, 0, op);
		int reduction2 = reduce(ints2, 0, op);
		reduction = reduce(Arrays.asList(reduction1, reduction2), 0, op);
		
		System.out.println("Reduction in parallel : " + reduction);
	}

	public static int reduce(List<Integer> ints, int valueIfEmpty, BinaryOperator<Integer> reduction) {
		int result = valueIfEmpty;
		for (int i: ints) {
			result = reduction.apply(result, i);
		}
		return result;
	}

}
