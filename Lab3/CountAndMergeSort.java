import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

/**
 * Sample implementation of inversionCount based on MergeSort for COM2031 Andre
 * Gruning, 2015 Solution of Q3 of the coursework.
 * 
 * Note that my class is NOT called InversionCount as I am running this class in
 * parallel with your submitted class -- this is to avoid name conflicts [A
 * cleaner solution would have been to but your and my class with the same name
 * "InversionCount" into different packages]
 * 
 * $Id: CountAndMergeSort.java 1177 2018-10-09 09:32:36Z ag0015 $
 * 
 * Revision $Rev: 1177 $
 */
public class CountAndMergeSort {

	/**
	 * class to hold a list and a count at the same time. In C/C++ you would
	 * call this a struct.
	 */
	public static class ReturnValue {
		public final int[] S;
		public final int count;

		public ReturnValue(final int[] S, final int count) {
			this.S = S;
			this.count = count;
		}
	}

	/**
	 * recursive part of MergeSort. This method returns the elements between
	 * start and end in sorted order. At the same time it implements the Divide
	 * and Conquer algorithms for counting inversions from the coursework.
	 * 
	 * @param S
	 *            array of elements to sort
	 * @param start
	 *            first element of S to be included in sort (inclusive)
	 * @param end
	 *            last element of S to be included in sort (*exclusive*)
	 * @return RetVal consisting of array of length start-end that contains the
	 *         elements of S between start and end in sorted order and the count
	 *         of inversion between start and end.
	 */
	public static ReturnValue MSort(int[] S, final int start, final int end) {

		// if nothing to sort:
		if (end - start <= 1) {
			final int[] E = new int[1];
			E[0] = S[start];
			return new ReturnValue(E, 0);
		}

		// split list in two O(1), note: integer devision
		final int split = start + (end - start) / 2;

		// recursion 2*T(n/2)
		ReturnValue L = MSort(S, start, split);
		ReturnValue R = MSort(S, split, end);

		return merge(L, R);

	}

	private static ReturnValue merge(ReturnValue L, ReturnValue R) {

		int l = 0;
		int r = 0;

		final int[] S = new int[L.S.length + R.S.length];
		int count = L.count + R.count;

		while (r < R.S.length && l < L.S.length) {
			if (R.S[r] < L.S[l]) {
				/*
				 * Left array should hold the smaller values:
				 * #inversions = number of elements remaining in the left array that the right element
				 * has to jump over.
				 */
				S[r + l] = R.S[r++];
				count += L.S.length - l; // the crucial count of inversions!
			} else { // Left array holds the smaller value, no jumps, no
				// inversions
				S[r + l] = L.S[l++];
			}
		}
		// deal with remaining elements if any -- no swaps happening!
		while (r < R.S.length) {
			S[r + l] = R.S[r++];
		}
		while (l < L.S.length) {
			S[r + l] = L.S[l++];
		}
		return new ReturnValue(S, count);
	}

	/**
	 * wrapper for easy handling and to conform with the method signature in the
	 * coursework handout 2015:
	 * 
	 * @param s
	 *            array to count inversions in
	 * @return the count of inversions.
	 */
	public static int inversionCount(int[] s) {
		if (s == null)
			return 0;
		ReturnValue V = MSort(s, 0, s.length);
		return V.count;
	}

	/**
	 * For initial testing purposes whether sorting works -- not directly
	 * relevant for the algorithms or for the solution of the coursework 2015!
	 * 
	 * @param args
	 *            command line arguments -- unused
	 */
	public static void main(String[] args) {

		// allocate three arrays
		int[] s = new int[1000];
		int[] t = new int[s.length];
		Integer[] U = new Integer[s.length];
		int[] u = new int[U.length];

		// and a random number generator;
		Random r = new Random();

		// count the number of errors our algorithms makes
		int errorCount = 0;

		// do a 1000 times:
		for (int i = 0; i < 1000; i++) {
			// create new random sequence of 1000 numbers and fill arrays
			// identically:
			for (int j = 0; j < s.length; j++) {
				t[j] = s[j] = U[j] = r.nextInt(1000000);
			}

			// sort s and count using our implementation:
			ReturnValue sRet = MSort(s, 0, s.length);
			System.out.println("Merge Inversion Count: " + sRet.count);

			// sort t using Java's implementation for comparision
			Arrays.sort(t);

			Comparator<Integer> c = new Comparator<Integer>() {

				@Override
				public int compare(Integer arg0, Integer arg1) {
					return arg1 - arg0;
				}
			};
			// sort u and count swaps using our implementation of bubbleSort
			int bubbleCount = BubbleSortGeneric.bubbleSort(U, c);
			System.out.println("Bubble Inversion Count: " + bubbleCount);

			// copy from Integer to int for easier handling below
			for(int k = 0; k < U.length; k++) {
				u[k] = U[k];
			}
			
			// check whether sort results are the same for all three algorithms
			if (!Arrays.equals(t, sRet.S) || !Arrays.equals(t, u))  {
				errorCount++;
			}
			// check also whether count of swaps/inversions agrees:
			if (sRet.count != bubbleCount)
				errorCount++;
		}

		// Output the number of errors made, should be 0, otherwise we need to
		// search
		// for bugs...
		System.out.println("Errors: " + errorCount);

	}
}