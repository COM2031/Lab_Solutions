import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class BubbleSortGeneric {

	/**
	 * swaps two adjacent members of the array. Caller to ensure array bounds.
	 * 
	 * $Id: BubbleSortGeneric.java 1177 2018-10-09 09:32:36Z ag0015 $
	 * 
	 * @author Andre Gruning for COM2031 2015-2017 -- Beer Licence.
	 * 
	 * @param <T>
	 *            type of elements in the array
	 * @param L
	 *            array
	 * @param index
	 *            elements at index and index+1 are swapped.
	 */

	private static <T> void swapNeighbours(final T[] L, final int index) {
		T temp = L[index];
		L[index] = L[index + 1];
		L[index + 1] = temp;
	}

	/**
	 * implements bubbleSort -- brute force sorting by swapping adjacent
	 * elements if they are not in the right order. Maximal number of swaps
	 * performed is proportional to n^2 if n is the length of L, ie bubbleSort
	 * is O(n^2).
	 * 
	 * @param <T>
	 *            type of elements to sort
	 * 
	 * @param L
	 *            list to sort
	 * @return number of swaps performed
	 */

	public static <T> int bubbleSort(final T[] L, final Comparator<T> c) {

		int count = 0;

		for (int j = L.length - 1; j > 0; j--) {
			for (int i = 0; i < j; i++) {
				if (c.compare(L[i], L[i + 1]) < 0) {
					swapNeighbours(L, i);
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * a helper method that creates a random permutation of number from 0 to
	 * n-1.
	 * 
	 * @param n
	 *            length of permutation
	 * @return a random permutation of numbers from 0 to n-1
	 */
	private static Long[] randomPermutation(final int n) {

		// space to store the permutation
		Long[] v = new Long[n];

		// a random generator
		Random r = new Random();

		// helper to create the permutation
		List<Long> u = new LinkedList<Long>();

		// file u with numbers 0...n-1
		for (long i = 0; i < v.length; i++) {
			u.add(i);
		}

		// select an element from u randomly and move to v until u has no more
		// elements.
		int vIndex = 0;
		while (u.size() > 0) {
			int randomIndex = r.nextInt(u.size());
			v[vIndex++] = u.get(randomIndex);
			u.remove(randomIndex);
		}
		// v contains now a random permutation of numbers 0...n-1
		return v;
	}

	/**
	 * For testing purposes: generate some random data to sort and check against
	 * Java's own library function whether our implementation yields the same
	 * results. (Assuming that the Java library is well debugged.)
	 * 
	 * @param args
	 *            command line arguments -- unused
	 */
	public static void main(String[] args) {

		// sample data to sort to contain a permutation of numbers 0...999
		Long v[] = randomPermutation(1000);

		// just a (deep) copy of v
		Long[] w = Arrays.copyOf(v, v.length);

		// count the number of errors our algorithm makes:
		int error_count = 0;

		// how does a Long compare to another Long?
		Comparator<Long> comp = new Comparator<Long>() {

			@Override
			public int compare(Long arg0, Long arg1) {

				if (arg0 > arg1)
					return -1;
				else if (arg1 > arg0)
					return 1;
				return 0;
			}

		};

		// sort v and find out how many swaps done
		int swapsBubble = bubbleSort(v, comp);
		System.out.println("Bubble swaps: " + swapsBubble);

		// test against Java library whether our implementation of bubblesort
		// works as expected.
		Arrays.sort(w);
		if (!Arrays.deepEquals(v, w)) {
			error_count++;
		}
		System.out.println("Errors: " + error_count);
	}
}