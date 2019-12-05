import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Sample implementation of QuickSort for COM2031 André Grüning, 2015-2017
 * 
 * $Id: QuickSort.java 1177 2018-10-09 09:32:36Z ag0015 $
 * 
 * October 2019: Manal Helal added the naive implementation for conceptual understanding 
 * of the idea behind quick sort, and simplified the code, and fixed a few bugs
 * adding more comments to explain the theory
 * 
 * Revision $Rev: 1177 $
 */
public class QuickSort {
	
	/**
	 * After the call to this method, S will have the elements at position a
	 * and b swapped.
	 * 
	 * @param S
	 *            array to operate on
	 * @param a
	 *            index of the one element to swap
	 * @param b
	 *            index of the other element to swap.
	 */
	public static void swap(final int[] S, final int a, final int b) {
		if ((a < 0) || (a>=S.length) ||(b < 0) || (b>=S.length))
			return;
		final int temp = S[a];
		S[a] = S[b];
		S[b] = temp;
	}

	
	/**
	 * This is what is known as the ingenious Quick Sort algorithm.
	 * It partitions and moves elements in place (in memory) recursively , better than 
	 * delaying the sorting to after partitioning in merge sort.
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
	 */
	public static void QSort(final int[] S, final int start, final int end) 
	{
		// base case: nothing to do.
		if (start >= end)
			return;

				
		final int pivot = S[end];

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
			while (left <= right && S[left] <= pivot) {
				left++;
			}
			// find element <= P, starting from right
			while (left <= right && S[right] > pivot) {
				right--;
			}
			// swap the two elements to the other part of the list.
			if (left < right) {
				swap(S, left, right);
			}
		}
		// swap pivot into middle, left always ends up pointing to the first
		// element of the "right half"
		swap(S, left, end);

		// left is the position of the swapped pivot, that we need to repartition around in the next recursive  call
		
		
		QSort(S, start, left - 1);
		QSort(S, left + 1, end);
	}
	/**
	 * The following 3 methods provide the naive implementation of quick sort to reveal the intuition of its design without worrying about memory efficiency at the moment
	 * The intuition of quick sort is to show the importance of constant reductions in Asymptotic complexity when you combine the sorting step with the partitioning step
	 * MergeSort is O(c1.n log n) which is the same for best, average and worst case, and you do all the sorting in the merge step
	 * and Quick Sort is O(c2.n log n), in best and average, but worst case is O(n^2) based on the value that happened to be in the pivot index
	 * It is easy to show that c2 < c1, because of the chance of using a pivot value that divides the list into equal halves in each step, and because
	 * because combining the sorting while partitioning, you are doing less operations (c2) than the merge step in mergeSort (c1) 
	 * @param S
	 *            array of elements to sort
	 * 
	 */
	
	static class ReturnValue {
		int [] S_left = null;
		int pivot;
		int [] S_right = null;
		
		ReturnValue (int [] A1, int pivot, int [] A2) {
			this.S_left = A1;
			this.pivot = pivot;
			this.S_right = A2;
		}
	}
	
	public static int [] QSort_naive(final int[] S) 
	{
		// Any recursive call require a termination condition, return the list in case it contains zero or 1 element
		if (S.length <= 1) 
			return S;
		else {
			// Otherwise, choose a pivot, can be the first element or the last element, and there is research on random selection benefits
			// the pivot value will hopefully split the array into 2 equal sublists (or as even as possible) - best case making the depth of recursion O(log n)
			// however the value can be for a sorted or reverse sorted input array, in which the recursion will always return empty list in A1 or A2, 
			// and the rest of the element in the other, making the complexity O(n^2)
			int pivot = S[S.length - 1];
			// since we do not care about space efficiency for now, copying to new places in memory is not too bad.

			// The idea here is to return the unsorted list of S elements less than the pivot in A1, 
			// and the unsorted S elements that are greater than the pivot in A2
			// in one scan of S, O(n) - c2 here is less than c1 in he merge step - check yourself
			ReturnValue parts = Partition(S, pivot); 
			// here is the recursive calls making use of Java API to concatenate the returned arrays
			// the recursion keeps on until the lists size is 1 and return their concatenation only up the recursion tree until all is done.
			
			return concatenate(QSort_naive(parts.S_left), pivot, QSort_naive(parts.S_right));
		}
	}
	
	public static ReturnValue Partition(final int[] S, int pivot) {
		// TODO Auto-generated method stub
		List<Integer> l1 = new ArrayList<>(); // create a list to append the values less than the pivot
		List<Integer> l2 = new ArrayList<>(); // create a list to append the values greater than the pivot
		for (int i=0;i<S.length;i++) {
			if (S[i] < pivot)
				l1.add(S[i]);
			if (S[i] > pivot)
				l2.add(S[i]);
		}
		
		return new ReturnValue(l1.stream().mapToInt(i->i).toArray(), pivot, l2.stream().mapToInt(i->i).toArray()); //  return to int[]
	}
	
	public static int [] concatenate(final int [] S_left, int pivot, final int [] S_right) {
		int [] S = new int[S_left.length + S_right.length + 1];
		for (int i = 0; i<S_left.length; i++) {
			S[i]= S_left[i];
		}
		S[S_left.length] = pivot;
		for (int i = 0; i<S_right.length; i++) {
			S[i+S_left.length+1]= S_right[i];
		}
		
		return S;
		
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
		//int[] s = new int[1000];
		//int[] t = new int[s.length];
		int n = 100000;
		// and a random number generator;
		Random r = new Random();

		// count the number of errors our algorithms makes
		int error1_count = 0;
		int error2_count = 0;
		List<Integer> l1 = IntStream.rangeClosed(1, n).boxed().collect(Collectors.toList());
		
		double ratio = 0;
		
		
		// do a 1000 times:
		for (int i = 0; i < n; i++) {
			// create new random sequence of numbers to fill both arrays
			// identically:
			

			// System.out.println();
			Collections.shuffle(l1);
			int [] s = l1.stream().mapToInt(kk->kk).toArray();
			// sort s using our implementation:
			
			long start = System.currentTimeMillis();
			s = QSort_naive(s);
			long end = System.currentTimeMillis();
			
			long elapsed_n =  end - start;
			if (elapsed_n == 0)
				elapsed_n= 1;
			
			Collections.shuffle(l1);
			int [] qs = l1.stream().mapToInt(kk->kk).toArray();
			
			start = System.currentTimeMillis();
			QSort(qs, 0, qs.length - 1);
			end = System.currentTimeMillis();
			
			long elapsed_qs =  end - start;
			
			if (elapsed_qs == 0)
				elapsed_qs= 1;
			if (i == 0) 
				ratio = elapsed_qs / elapsed_n;
			else
				ratio = ((elapsed_qs / elapsed_n) + ratio) / (i+1);
			
			Collections.shuffle(l1);
			int [] t = l1.stream().mapToInt(kk->kk).toArray();
			
			// sort t using Java's implementation:
			Arrays.sort(t);

			// check whether results are the same for both algorithms
			if (!Arrays.equals(t, s)) {
				error1_count++;
			}
			
			if (!Arrays.equals(t, qs)) {
				error2_count++;
			}
		}

		// for(int i: s) {
		// System.out.print(i);
		// System.out.print(' ');
		// }
		// System.out.println();

		// Output the number of errors made, should be 0, or we need to search
		// for bugs...
		System.out.println("Naive Quick Sort  Errors: " + error1_count);
		System.out.println("In-memory Quick Sort Errors: " + error2_count);
		
		System.out.println(" In average, In memory Quick sort is " + ratio + " faster than Naive Quick Sort");
	}
}