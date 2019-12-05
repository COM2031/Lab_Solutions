import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.junit.Test;



/**
 * Implementation of the Knapsack algorithm for COM2031
 * 
 * @author gruening, 2015-2017.
 * 
 * Test Cases added by Manal Helal

 * @version $Id: KnapSack.java 999 2017-11-15 09:56:28Z ag0015 $
 */
public class KnapSack {

	/**
	 * Test method for {@link KnapSack#pack(Interval[])}.
	 */
	@Test
	public final static void testKnapSack () throws AssertionError {
		
		for (int n=1; n<=10; n++) { 		// test for all arrays of Jobs of size n, from size of 1 to 10
			
			

			List<Item> l = new ArrayList<>();
			
			// initialise all the jobs with random name, random start and finish times
			Random r = new Random();

		    String alphabet = "abcdefghijklmnopqrstuvwxyz";
		   
		    System.out.println ("\n\nTest Case with " + n + " Items:");
			while (l.size() < n) {
				
				char randomName = alphabet.charAt(r.nextInt(alphabet.length()));
				
				int weight = r.nextInt(50); // how much to wait for between intervals generated
				int value = r.nextInt(50); // how long each one
				
				
				Item i = new Item(Character.toString(randomName), weight, value);
				l.add(i);
				System.out.println ("Item " + i.name + " weight: " + i.weight + ",  value: " + i.value);
			}
			Collections.shuffle(l);
			Item[] items = new Item[l.size()];
		    l.toArray(items);
		    final int maxWeight = r.nextInt(50) * 5;
			

		    final KnapSack knapSack = new KnapSack(items, maxWeight);

			final int maxValue = knapSack.pack();

			System.out.print(knapSack);
			System.out.println("Maximal Value of knapsack with maximal Weight " + maxWeight + ": " + maxValue);

			knapSack.printSelectedItems();

						
			
		}
		
		System.out.println ("\n\nAll Test cases finished. There are no assertions, you neeed to check by eye!");
	}
	/** represents items we want to store in the knapsack */
	public static class Item {

		/** item name -- for convenience */
		private final String name;
		/** value of this item */
		private final int value;
		/** weight of this item */
		private final int weight;

		/**
		 * creates a new item with
		 * 
		 * @param name
		 *            its name
		 * @param value
		 *            its value
		 * @param weight
		 *            its weight.
		 */
		public Item(final String name, final int value, final int weight) {
			this.weight = weight;
			this.value = value;
			this.name = name;
		}

		/**
		 * Convenience method for pretty printing of an item.
		 */
		@Override
		public String toString() {
			return "Item [name=" + name + ", value=" + value + ", weight=" + weight + "]";
		}
	}

	/** list of items to select from. */
	private final Item[] items;
	/** maximal weights this Knapsack can hold. */
	private final int maxWeight;

	/**
	 * creates a new instance of the Knapsack problem, and sorts the items given
	 * into
	 * 
	 * @param items
	 *            these items available by their weight.
	 * @param maxWeight
	 *            maximal weight the knapsack can hold.
	 */
	public KnapSack(final Item[] items, final int maxWeight) {

		this.items = items; // shallow copy
		this.maxWeight = maxWeight;

		// sort by weight -- O(nlogn)
		Arrays.sort(this.items, new Comparator<Item>() {
			@Override
			public int compare(Item arg0, Item arg1) {
				return arg0.weight - arg1.weight;
			}
		});
	}

	/**
	 * 2D array for memoization. First index is number of items considered, and
	 * second index is current weight limited considered.
	 */
	private int[][] M;

	/**
	 * initialise the array. Note that this tkake O(nW) time with n the number
	 * of items, and W the maximal weight allowed for the Knapsack.
	 * 
	 * -1 marks that we haven't yet filled a slot in M. (We could as well take
	 * any other negative value, or even type Integer and assing null to it.)
	 */
	private void initCalculation(final int maxWeight) {

		for (int i = 0; i < this.items.length + 1; i++) {
			for (int j = 1; j < maxWeight + 1; j++) {
				M[i][j] = -1;
			}
		}
	}

	/**
	 * calculate maximal value implemented as a wrapper for the recursive call
	 * of calculateOPT.
	 * 
	 * Try both version of the algorithm -- be sure to uncomment initcalculation
	 * when using the recursive version.
	 **/
	public int pack() {
		// +1 because we also have entries for M[0,.] and M[.,0].
		M = new int[this.items.length + 1][maxWeight + 1];
		// initCalculation(maxWeight);
		// return calculateOPTrecursive(this.items.length, maxWeight);
		return calculateOPTiterative(this.items.length, maxWeight);
	}

	/**
	 * recursively calculate OPT values for a given weight limit.
	 * 
	 * @param noItems
	 *            the number of initial items to consider from this.items.
	 * @param weightLimit
	 *            weight still available in the Knapsack.
	 */
	private int calculateOPTrecursive(final int noItems, final int weightLimit) {

		if (noItems == 0)
			return 0; // no items left, end of recursion.
		if (weightLimit <= 0)
			return 0; // no space left in knapsack, end of recursion.

		// itemsLeft are at this point at least 1 and weightLimit is positive.

		// if not yet memoised, calculate the value:
		if (M[noItems][weightLimit] == -1) {

			// current is the item with the largest weight still available.
			final Item current = items[noItems - 1];

			// retrieve OPT value for all Knapsack solutions up to but excluding
			// the current item:
			final int valueWithoutCurrent = calculateOPTrecursive(noItems - 1, weightLimit);

			// enough space left for current item at all?
			if (current.weight > weightLimit) {
				// no -- then we can't add it
				M[noItems][weightLimit] = valueWithoutCurrent;
			} else { // enough space left!

				// Get value if we included the current interval.
				int valueWithCurrent = current.value + calculateOPTrecursive(noItems - 1, weightLimit - current.weight);
				// and compare to the maximal OPT without the current item so
				// far.
				M[noItems][weightLimit] = Math.max(valueWithCurrent, valueWithoutCurrent);
			}
		}
		return M[noItems][weightLimit];
	}

	/**
	 * iteratively calculate OPT values for a given weight limit.
	 * 
	 * @param noItems
	 *            the number of initial items to consider from this.items.
	 * @param weightLimit
	 *            weight still available in the Knapsack.
	 */
	private int calculateOPTiterative(final int noItems, final int weightLimit) {

		// no item to pack, no value:
		for (int w = 0; w <= weightLimit; w++) {
			M[0][w] = 0;
		}

		for (int i = 1; i <= noItems; i++) {

			// do part of the initialisation: no weight left, no value:
			M[i][0] = 0;

			final Item current = items[i - 1];

			for (int w = 0; w <= weightLimit; w++) {

				final int weightLeft = w - current.weight;
				if (weightLeft < 0) // no space for current item.
					M[i][w] = M[i - 1][w];
				else { // space for current item: do we gain value if taken?
					M[i][w] = Math.max(current.value + M[i - 1][weightLeft], M[i - 1][w]);
				}
			}
		}
		return M[noItems][weightLimit];
	}

	/**
	 * pretty-print this Knapsack problem in LaTeX.
	 */
	@Override
	public String toString() {

		StringBuffer s = new StringBuffer();

		// Table Header:
		s.append("Weight:\t");
		for (int j = 0; j < maxWeight + 1; j++) {
			s.append(j + "\t");
		}
		s.append("\n");

		// Table Content:
		for (int i = 0; i < items.length + 1; i++) {
			s.append(i + " Items\t");
			for (int j = 0; j < maxWeight + 1; j++) {
				s.append(M[i][j] + "\t");
			}
			s.append("\n");
		}

		return s.toString();
	}

	/**
	 * Iteratively find which items to include from the table M of memoised OPT
	 * values.
	 * 
	 * @param numItems
	 *            for this many first items from items.
	 * @param weightLimit
	 *            with this remaining weight avaiable
	 */
	private void findSolutionIterative(final int numItems, final int weightLimit) {

		int w = weightLimit;

		for (int i = numItems; i > 0; i--) {
			if (M[i][w] > M[i - 1][w]) {
				Item selected = items[i - 1];
				System.out.println("Selected item: " + selected);
				w -= selected.weight;
			}
		}
	}

	/**
	 * Recursively find which items to include from the table M of memoised OPT
	 * values.
	 * 
	 * @param numItems
	 *            for this many first items from items.
	 * @param weightLimit
	 *            with this remaining weight avaiable
	 */
	private void findSolutionRecursive(final int numItems, final int weightLimit) {

		if (numItems == 0)
			// end of recursion: exhausted all items
			return;
		if (weightLimit <= 0)
			// end of recursion: exhausted weight limit.
			return;

		final Item current = items[numItems - 1];

		// if inclusion of current item for given weightLimit increases OPT
		// value, take it.
		if (M[numItems][weightLimit] > M[numItems - 1][weightLimit]) {
			/*
			 * just print it out in this case, but we could also add to a
			 * dedicated list of the selected intervals -- what you do precisely
			 * depends on the user of your algorithm.
			 */
			System.out.println("Selected item: " + current);
			findSolutionRecursive(numItems - 1, weightLimit - current.weight);
		} else
			findSolutionRecursive(numItems - 1, weightLimit);
	}

	/**
	 * print selected items for this Knapsack
	 */
	public void printSelectedItems() {
		//findSolutionRecursive(items.length, maxWeight);
		findSolutionIterative(items.length, maxWeight);
	}

	/**
	 * Test with data from the slides
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {

		final Item[] items = { new Item("1", 1, 1), new Item("2", 6, 2), new Item("3", 18, 5), new Item("4", 22, 6),
				new Item("5", 28, 7) };

		final int maxWeight = 11;
		final KnapSack knapSack = new KnapSack(items, maxWeight);

		final int maxValue = knapSack.pack();

		System.out.print(knapSack);
		System.out.println("Maximal Value of knapsack with maximal Weight " + maxWeight + ": " + maxValue);

		knapSack.printSelectedItems();
		
		testKnapSack();
	}
}