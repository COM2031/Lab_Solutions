/**
 * Implementation of the Knapsack algorithm for COM2031
 * 
 * @author Manal Helal when the module leader was Steve Schneider 2019 based on the Gruning Solution of 2015-2017.

 * @version $Id: KnapSackSimple.java 999 2019-11-1 §
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.junit.Test;



public class KnapSackSimple {
	
	/**
	 * Test method for {@link KnapSackSimple#knapSack(Interval[])}.
	 */
	@Test
	public final static void testKnapSackSimple () throws AssertionError {
		
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
			
			int M[][] = knapSack(items, maxWeight); 
	        System.out.println("" + M[items.length][maxWeight]);
	        findSolution(M, items);
						
			
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

	/**
	 * Iteratively find which items to include from the table M of memoised OPT
	 * values.
	 * 
	 * @param numItems
	 *            for this many first items from items.
	 * @param weightLimit
	 *            with this remaining weight avaiable
	 */
	private static void findSolution(int M[][], final Item[] items) {

		int numItems = M.length-1;
		int w = M[0].length-1;

		for (int i = numItems; i > 0; i--) {
			// if up to current item OPtimisation matrix  is higher value than the previous one,
			// then this item is selected
			if (M[i][w] > M[i - 1][w]) {
				Item selected = items[i - 1];
				System.out.println("Selected item: " + selected);
				w -= selected.weight;
			}
		}
	}

	
  
    // Returns the maximum value that can be put in a knapsack 
    // of capacity W 
    static int [][] knapSack(final Item[] items, final int maxWeight) 
    { 
        int i, w; 
        int M[][] = new int[items.length + 1][maxWeight + 1]; 
        // sort by weight -- O(nlogn)
 		Arrays.sort(items, new Comparator<Item>() {
 			@Override
 			public int compare(Item arg0, Item arg1) {
 				return arg0.weight - arg1.weight;
 			}
 		});
        // Build table M[][] iteratively top down
        for (i = 0; i<= items.length; i++) {  // for all items
            for (w = 0; w<= maxWeight; w++) { // for all weights
                if (i == 0 || w == 0) 
                    M[i][w] = 0; // border values when no weight carried yet and no value accumulated yet
                else if (items[i - 1].weight<= w) { // if taking current item is still within the allowed weight
                    M[i][w] = Math.max(items[i - 1].value + M[i - 1][w - items[i - 1].weight], M[i - 1][w]); 
                    // the optimisation matrix takes the maximum of either taking this item by 
                    // adding its value to the previously accumulated values or the previous 
                    // item only if higher value. the above parameters to max is as follows:
                    int valueWithoutCurrent = M[i - 1][w];

    				int valueWithCurrent = items[i - 1].value + M[i - 1][w - items[i - 1].weight];
    				
    				
                }	
                else // if current weight when added will exceed maximum weight, then current item is not taken, and the optimisation matrix take up to the previous item total value 
                    M[i][w] = M[i - 1][w]; 
            } 
        } 
        
        return M; 
    } 

	/**
	 * Test with data from the slides
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {

        final Item[] items = { new Item("1", 1, 6), new Item("2", 6, 2), new Item("3", 18, 5), new Item("4", 22, 6),
				new Item("5", 28, 7) };

        final int maxWeight = 11;
        int M[][] = knapSack(items, maxWeight); 
        System.out.println("" + M[items.length][maxWeight]);
        findSolution(M, items);
		testKnapSackSimple();
	}
}