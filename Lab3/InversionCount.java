import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * This implementation starts from MergeSort.java with just minimal changes to
 * the algorithm.
 * 
 * $Id: InversionCount.java 1177 2018-10-09 09:32:36Z ag0015 $
 * 
 * It also contains for comparison an implementation of Brute Force inversion
 * count based on BubbleSort. Both implementation should always deliver the same
 * number of inversions as a sanity check of my D&C implmentation.
 * 
 * @author Andre Gruning, University of Surrey, COM2031. 2016-2017.
 * 
 * @licence Beer Licence.
 */
public class InversionCount {

	/**
	 * swaps 2 adjacent members of the array
	 * 
	 * @param L
	 *            : array
	 * @param index
	 *            : elements at index and index+1 are swapped.
	 * 
	 * @note Caller to ensure array bounds are respected.
	 */

	private static void swapNeighbours(final int[] L, final int index) {
		int temp = L[index];
		L[index] = L[index + 1];
		L[index + 1] = temp;
	}

	/**
	 * implements bubbleSort -- brute force sorting by swapping elements
	 * 
	 * @param L
	 *            the list to sort
	 * @return number of swaps performed
	 * 
	 *         _ * the length of L.
	 * 
	 *         Brute Force approach to sorting.
	 */

	public static int bubbleSort(int[] L) {

		int count = 0;

		for (int j = L.length - 1; j > 0; j--) {
			for (int i = 0; i < j; i++) {
				if (L[i] > L[i + 1]) {
					swapNeighbours(L, i);
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * Brute force implementation of inversion count Runtime O(n^2)
	 * 
	 * Inversion are the number of swaps that bubbleSort has to execute in order
	 * to sort its input data.
	 * 
	 * @param L
	 * @return
	 */
	public static int countInversionsBruteForce(final int[] L) {

		int inversions = 0;

		// for loops are such that always i < j
		for (int i = 0; i < L.length - 1; i++) {
			for (int j = i + 1; j < L.length; j++) {
				if (L[i] > L[j]) {
					inversions++;
				}
			}
		}
		return inversions;
	}

	/**
	 * Implementation of InversionCount with explicit sorting -- all in all we
	 * are order nlog^2(n)
	 * 
	 * @param L
	 * @param left
	 *            inclusive: first index of sublist (inclusive)
	 * @param end
	 *            last index of sublist (exclusive)
	 * @return
	 */
	static int countInversions(int[] list) {
		return CountAndMergeSort.inversionCount(list);
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

		// to contain a permutation of numbers 0...999
		int[] v = new int[1000];

		// helper to create the permutation
		List<Integer> u = new LinkedList<Integer>();

		// fill u with numbers 0...999
		for (int i = 0; i < v.length; i++) {
			u.add(i);
		}

		Random r = new Random();

		// select an element from u randomly and move to v.
		for (int i = 0; u.size() > 0; i++) {
			int index = r.nextInt(u.size());
			v[i] = u.get(index);
			// System.out.println("Index " + index + " size " + u.size()
			// + " element " + v[index]);
			u.remove(index);
		}
		// v contains now a random permutation of numbers 0...999

		/*
		for (int j : v) {
			System.out.print(j + " ");
		}
		System.out.println();
		// */

		// just a (deep) copy of v
		int[] w = Arrays.copyOf(v, v.length);
		int[] y = Arrays.copyOf(v, v.length);

		// count the number of errors our algorithm makes:
		int error_count = 0;

		// find out how many swapps bubble sort does.
		int count1 = bubbleSort(v);
		System.out.println("Bubble swaps: " + count1);

		// cross check with brute forte implementation
		int count2 = countInversionsBruteForce(w);
		System.out.println("Brute Force: " + count2);

		// determine the number of swaps
		int count3 = CountAndMergeSort.inversionCount(y);
		System.out.println("Via MergeSort: " + count3);

		// determine the number of swaps
		// int count2 = DiscontinuedcountInversions(w, 0, w.length);
		// System.out.println("CountInversions: " + count2);

		// just a test whether our implementation of bubblesort works as
		// expected.
		Arrays.sort(w);
		if (count1 != count3 || count3 != count2) {
			error_count++;
		}
		System.out.println("Errors: " + error_count);
	}
}