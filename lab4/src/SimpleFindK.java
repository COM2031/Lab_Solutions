import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Sample implementation of FindK for COM2031 Andre Gruning, 2017
 * 
 * Updated by Manal Helal in October 2019 to add FindKPair
 * 
 * $Id: SimpleFindK.java 1177 2018-10-09 09:32:36Z ag0015 $
 */
public class SimpleFindK {

	/**
	 * recursive part of QuickSort. On return the list S is sorted.
	 * 
	 * @param k
	 *            We are looking for the (k+1)th smallest element.
	 * @param S
	 *            array of elements to sort
	 */
	public static int FindK(final int k, List<Integer> S) {

		// sanity check
		assert (S.size() > k);

		// select pivot
		final int P = S.get(S.size() - 1);
		S.remove(S.size() - 1);

		/*
		 * make space for the split lists -- we do not know their precise size
		 * yet, but their maximal size is less than or equal to the size of S.
		 */
		List<Integer> Left = new ArrayList<Integer>(S.size());
		List<Integer> Right = new ArrayList<Integer>(S.size());

		// split the list.
		for (int s : S) {
			if (s < P)
				Left.add(s);
			else // s >=P
				Right.add(s);
		}

		if (Left.size() == k) {
			// pivot must be (k+1)-th smallest element.
			return P;
		} else if (Left.size() > k) {
			// (k+1)th smallest element must be in the left half:
			return FindK(k, Left);
		} else { // Left.size() < k
			// (k+1) th element must be in the right half.
			return FindK(k - (Left.size()+1), Right);
		}
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

		// allocate lists
		int[] t = new int[10000];
		List<Integer> s = new ArrayList<Integer>(t.length);
		
		// and a random number generator;
		Random r = new Random();

		// count the number of errors our algorithms makes
		int errors = 0;

		// do a couple of times:
		for (int i = 0; i < 1000; i++) {
			s.clear();
			// create new random sequence of numbers to fill both arrays
			// identically:
			for (int j = 0; j < t.length; j++) {
				int u = r.nextInt(1000000000);
				t[j] = u;
				s.add(u);
			}

			final int k = r.nextInt(t.length);

			// sort s using our implementation:
			final int kSmallest = FindK(k, s);

			// sort t using Java's implementation:
			Arrays.sort(t);

			// check whether results are the same for both algorithms
			if (kSmallest != t[k]) {
				errors++;
			}
		}

		// Output the number of errors made, should be 0, or we need to search
		// for bugs...
		System.out.println("Errors: " + errors);
	}
}