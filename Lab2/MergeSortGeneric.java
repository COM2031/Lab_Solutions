import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Sample implementation of MergSort for COM2031 Andre Gruning, 2015
 * 
 * $Id: MergeSortGeneric.java 1177 2018-10-09 09:32:36Z ag0015 $
 * 
 * Revision $Rev: 1177 $
 */
public class MergeSortGeneric {

	/**
	 * takes to sorted list and returns a sorted merged list. Prerequisite: left
	 * and right *must* be in sorted order.
	 * 
	 * @param L
	 *            a list that is already sorted
	 * @param R
	 *            another list that is already sorted
	 * @return new list with all elements of L and R in sorted order.
	 */
	private static <T> T[] merge(final T[] L, final T[] R, final Comparator<T> c) {

		// space for new merged list
		@SuppressWarnings("unchecked")
		T[] M = (T[]) new Object[L.length + R.length];

		// index into L
		int l = 0;

		// index into R
		int r = 0;

		// copy current smallest element to M
		while (l < L.length && r < R.length) {
			M[l + r] = (c.compare(L[l], R[r]) < 0) ? L[l++] : R[r++];
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
	 * start and end in sorted order.
	 * 
	 * @param <T>
	 * 
	 * @param S
	 *            array of elements to sort
	 * @param start
	 *            first element of S to be included in sort (inclusive)
	 * @param end
	 *            last element of S to be included in sort (*exclusive*)
	 * @return array of length start-end that contains the elements of S between
	 *         start and end in sorted order.
	 */
	public static <T> T[] MSort(T[] S, final int start, final int end, final Comparator<T> c) {

		// base case: nothing to sort if list contains just one element as
		// one-element lists are always sorted!
		if (end - start == 1) {
			@SuppressWarnings("unchecked")
			T[] E = (T[]) new Object[1];
			E[0] = S[start];
			return E;
		}

		// DIVIDE STEP: split list in two O(1), note: integer devision -- check
		// for yourself that this always produces the correct split
		final int split = start + (end - start) / 2;

		// CONQUER STEP: recursion 2*T(n/2)
		T[] L = MSort(S, start, split, c);
		T[] R = MSort(S, split, end, c);

		// COMBINE STEP:
		return merge(L, R, c);

	}

	// allocate a random number generator;
	static Random r = new Random();

	/**
	 * helper method to generate a random string of length n
	 * 
	 * @param n
	 *            length of random string to generate
	 * @return generated random string of length n
	 * 
	 */
	static String randomString(int n) {
		char str[] = new char[n];

		for (int i = 0; i < n; i++) {
			str[i] = (char) r.nextInt(128); // just generating ASCII chars 0 -
											// 127
		}
		return new String(str); // convert char array to String
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
		String[] S = new String[1000];
		String[] T = new String[S.length];

		// count the number of errors our algorithms makes
		int error_count = 0;

		// do a 1000 times?:
		for (int i = 0; i < 1000; i++) {
			// create new random sequence of 1000 numbers and fill both arrays
			// identically:
			for (int j = 0; j < S.length; j++) {
				int n = r.nextInt(1000);
				T[j] = S[j] = randomString(n);
			}

			// sort s using our implementation:
			Object[] Sorted = MSort(S, 0, S.length, new Comparator<String>() {

				@Override
				public int compare(String arg0, String arg1) {
					return arg0.compareTo(arg1);
				}

			});

			// sort t using Java's implementation:
			Arrays.sort(T);

			// check whether results are the same for both algorithms
			if (!Arrays.deepEquals(T, Sorted)) {
				error_count++;
			}
		}

		System.out.println("Errors: " + error_count);

	}
}