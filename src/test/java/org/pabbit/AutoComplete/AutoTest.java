package org.pabbit.AutoComplete;

import java.util.Arrays;
import java.util.Random;

import org.junit.Test;
import static org.junit.Assert.assertTrue;


import edu.emory.mathcs.cs323.sort.AbstractSort;
import edu.emory.mathcs.cs323.sort.InsertionSort;
import edu.emory.mathcs.cs323.utils.DSUtils;

public class AutoTest 
{
	@Test
	public void testAccuracy()
	{
		final int ITERATIONS = 100;
		final int SIZE = 1000;
		
		testAccuracy(ITERATIONS, SIZE, new InsertionSort<Integer>());
	}
	
	void testAccuracy(final int ITERATIONS, final int SIZE, AbstractSort<Integer> engine)
	{
		final Random rand = new Random(0);
		Integer[] original, sorted;
		
		for (int i=0; i<ITERATIONS; i++)
		{
			original = DSUtils.getRandomIntegerArray(rand, SIZE, SIZE);
			sorted = Arrays.copyOf(original, SIZE);

			engine.sort(original);
			Arrays.sort(sorted);
		
			assertTrue(DSUtils.equals(original, sorted));
		}
	}


}
