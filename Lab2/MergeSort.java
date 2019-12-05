import java.util.Arrays;
import java.util.Random;

/**
 * Sample implementation of MergeSort for COM2031 Andre Gruning, 2015-2018
 * 
 * $Id: MergeSort.java 1177 2018-10-09 09:32:36Z ag0015 $
 */
public class MergeSort {

	/**
	 * takes two sorted lists and returns a sorted merged list. 
	 * 
	 * Prerequisite: 
	 * left and right *must* be in sorted order.
	 * 
	 * @param L
	 *            a list that is already sorted
	 * @param R
	 *            another list that is already sorted
	 * @return new list with all elements of L and R in sorted order.
	 */
	public static int[] merge(final int[] L, final int[] R) {

		// space for new merged list
		int[] M = new int[L.length + R.length];

		// index into L
		int l = 0;

		// index into R
		int r = 0;

		// copy current smallest element to M
		while (l < L.length && r < R.length) {
			M[l + r] = (L[l] < R[r]) ? L[l++] : R[r++];
		}
		// deal with remaining elements if any;
		while (l < L.length) {
			M[l + r] = L[l++];
		}
		while (r < R.length) {
			M[l + r] = R[r++];
		}
		return M;
	}

	/**
	 * recursive part of MergeSort. This method returns the elements between
	 * start and end in sorted order in a new list
	 * 
	 * @param S
	 *            array of elements to sort
	 * @param start
	 *            first element of S to be included in sort (inclusive)
	 * @param end
	 *            last element of S to be included in sort (*exclusive*)
	 * @return new array of length end-start that contains the elements of S
	 *         between start and end in sorted order.
	 */
	public static int[] MSort(final int[] S, final int start, final int end) {

		// base case: nothing to sort if list contains just one element as
		// one-element lists are always sorted!
		if (end - start == 1) {
			int[] E = new int[1];
			E[0] = S[start];
			return E;
		}

		// DIVIDE STEP: split list in two O(1), note: integer devision -- check
		// for yourself that this always produces the correct split
		final int split = start + (end - start) / 2;

		// CONQUER STEP: recursion 2*T(n/2)
		int[] L = MSort(S, start, split);
		int[] R = MSort(S, split, end);

		// COMBINE STEP:
		return merge(L, R);
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

		// allocate two arrays
		int[] s = new int[1000];
		int[] t = new int[s.length];

		// and a random number generator;
		Random r = new Random();

		// count the number of errors our algorithms makes
		int error_count = 0;

		// do a 1000 times:
		for (int i = 0; i < 1000; i++) {
			// create new random sequence of 1000 numbers and fill both arrays
			// identically:
			for (int j = 0; j < s.length; j++) {
				t[j] = s[j] = r.nextInt(1000000);
			}

			// sort s using our implementation:
			s = MSort(s, 0, s.length);

			// sort t using Java's implementation:
			Arrays.sort(t);

			// check whether results are the same for both algorithms
			if (!Arrays.equals(t, s)) {
				error_count++;
			}
		}

		// Output the number of errors made, should be 0, or we need to search
		// for bugs...
		System.out.println("Errors: " + error_count);
	}
}