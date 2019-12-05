import java.util.Arrays;
import java.util.Random;

/**
 * Sample implementation of FindK for COM2031 Andre Gruning, 2015 (Find the
 * kth-lowest element in a list of n)
 * 
 * - This is based on Quicksort with only minor changes.
 * 
 * $Id: FindK.java 1177 2018-10-09 09:32:36Z ag0015 $
 * 
 * Revision $Rev: 1177 $
 * 
 *  Updated by Manal Helal in October 2019 to add FindKPair, and simplify QSort
 *  
 */
public class FindK extends QuickSort {

	/**
	 * recursive part of FindK. After a call to this method, the array S will be
	 * sorted between indices start and final.
	 * 
	 * @param S
	 *            array of elements to sort
	 * @param start
	 *            first element of S to be included in sort (inclusive)
	 * @param end
	 *            last element of S to be included in sort (inclusive)
	 * @param k
	 *            order of element to find: (k+1)-th smallest.
	 * @return
	 */
	
	/**
	 * This is Quick Sort algorithm, adjusted to look for the kth minimum value only, 
	 * and ignores the partitions where it is not expected.
	 * It partitions and moves elements in place (in memory) recursively , better than 
	 * delaying the sorting to after partitioning in merge sort.
	 *  
	 * 
	 * After a call to this method, the array S
	 * will be sorted between indices start and final.
	 * 
	 * @param S
	 *            array of elements to sort
	 * @param start
	 *            first element of S to be included in sort (inclusive)
	 * @param end
	 *            last element of S to be included in sort (inclusive)
	 * @param k
	 *            the kth element index you are looking for
	 */
	public static int findK(final int[] S, final int start, final int end, final int k) 
	{
	
		
		assert (k <= end); // k is beyond snippet
		assert (k >= start); // k is beyond snippet
		assert (end < start); // list is empty or worse.
				
		int pivot_value = S[end];

		/**
		 * reorder elements in S between left and end such that all elements that
		 * are smaller than or equal to the pivot element are to the left of the
		 * pivot element and all elements that are greater than the pivot are to the
		 * right of it.
		 */
		
		// new rightmost to deal with:
		int left = start;
		int right = end - 1;

		while (left <= right) {
			// find element > P, starting from left
			while (left <= right && S[left] <= pivot_value) {
				left++;
			}
			// find element <= P, starting from right
			while (left <= right && S[right] > pivot_value) {
				right--;
			}
			// swap the two elements to the other part of the list.
			if (left < right) {
				QuickSort.swap(S, left, right);
			}
		}
		// swap pivot into middle, left always ends up pointing to the first
		// element of the "right half"
		QuickSort.swap(S, left, end);

		// left is the position of the swapped pivot, that we need to repartition around in the next recursive  call
		int pivot_Index = left;
		
		
		if (pivot_Index == k) {
			return S[k];
		} else if (k < pivot_Index) {
			return findK(S, start, pivot_Index - 1, k);
		} else
			// k > pivot_index
			return findK(S, pivot_Index + 1, end, k);
		
		
	}
	

	/**
	 * For testing purposes: - generate some random data to sort and check
	 * against Java's own library function whether our implementation yields the
	 * same results.
	 * 
	 * @param args
	 *            command line arguments -- unused
	 */
	public static void main(String[] args) {

		// allocate 2 arrays
		int[] s = new int[1000];
		int[] t = new int[s.length];

		// and a random number generator;
		Random r = new Random();

		// count the number of errors our algorithms makes
		int errors = 0;
		int errors_m = 0;

		// do a couple of times:
		for (int i = 0; i < 10000; i++) {
			// create new random sequence of numbers to fill both arrays
			// identically:
			for (int j = 0; j < s.length; j++) {
				t[j] = s[j] = r.nextInt(1000000000);
			}

			final int k = r.nextInt(s.length);

			// sort s using our implementation:
			final int kSmallest = findK(s, 0, s.length - 1, k);
			// sort t using Java's implementation:
			Arrays.sort(t);
						
			double median = 0;
			double median_t = 0;
			if (s.length % 2 ==0) {// if S is even (2n) , median is the mean of the two middle values, i.e. at n and (n+1) 
				final int median1 = findK(s, 0, s.length - 1, s.length/2);
				final int median2 = findK(s, 0, s.length - 1, (s.length/2) + 1);
				median = (median1 + median2) / 2;
				median_t = (t[ s.length/2] + t[(s.length/2) + 1]) / 2;
			}
			else {
				median = findK(s, 0, s.length - 1, s.length/2);
				median_t = t[s.length/2];
			}
			
			

			// check whether results are the same for both algorithms
			if (kSmallest != t[k]) {
				errors++;
				System.out.println("kSmallest = " + kSmallest + " t[k] = " + t[k] );
			}
			
			// check whether results are the same for both algorithms
			if (median != median_t) {
				errors_m++;
				System.out.println("median = " + median + " median_t = " + median_t );
			}

			
		}

		// Output the number of errors made, should be 0, or we need to search
		// for bugs...
		System.out.println("Errors: " + errors);
		System.out.println("Median Errors: " + errors_m);
	}
}