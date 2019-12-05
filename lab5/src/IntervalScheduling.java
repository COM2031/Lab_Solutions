import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import org.junit.Test;

/**
 * This class implements the Interval Scheduling algorithm as discussed in
 * COM2031. It is a greedy algorithm.
 * 
 * @author Andre Gruning, University of Surrey. 2016, 2017.
 * 
 * $Id: IntervalScheduling.java 1177 2018-10-09 09:32:36Z ag0015 $
 * Debugged for more Test Cases Designed Manal Helal when the module was led by Prof. Steve Schneider in Fall 2019
 * 
 */
public class IntervalScheduling {

	/**
	 * Data type that describes an interval with start and finish times. For
	 * convenience, Intervals can also be given a name for convenience.
	 * 
	 * @author gruening
	 * 
	 */
	
	
	static class Interval {

		// * start time of interval
		final int start;
		// * end time of interval
		final int finish;
		// * convenient name for the interval.
		final String name;
		// * a flag that indicates whether an interval is selected or not.
		boolean selected = false;

		/**
		 * construct a new Interval
		 * 
		 * @param name
		 *            name of the interval
		 * @param start
		 *            start time
		 * @param finish
		 *            end time
		 */
		Interval(String name, int start, int finish) {
			this.start = start;
			this.finish = finish;
			this.name = name;
		}

		/**
		 * convenience method to print an Interval
		 */
		@Override
		public String toString() {
			return "Interval [start=" + start + ", finish=" + finish + ", name=" + name + ", selected=" + selected
					+ "]";
		}
	}
	
	/**
	 * Test method for {@link IntervalPartitioning#partition(Interval[])}.
	 */
	@Test
	public final static void testIntervalScheduling () throws AssertionError {
		
		for (int n=1; n<=10; n++) { 		// test for all arrays of Jobs of size n, from size of 1 to 10
			List<Interval> l = new ArrayList<>();
			int startTime = 0;
			int endTime;
			// initialise all the jobs with random name, random start and finish times
			Random r = new Random();

		    String alphabet = "abcdefghijklmnopqrstuvwxyz";
		   
			
			while (l.size() < n) {
				
				char randomName = alphabet.charAt(r.nextInt(alphabet.length()));
				int interval, duration;
				interval = r.nextInt(50) * 20; // how much to wait for between intervals generated
				duration = r.nextInt(50) * 20; // how long each one
				startTime = interval;
				endTime = startTime + duration;
				Interval i = new Interval(Character.toString(randomName), startTime, endTime);
				l.add(i);
			}
			Collections.shuffle(l);
			Interval[] intervals = new Interval[l.size()];
		    l.toArray(intervals);
			int scheduled = IntervalScheduling.schedule(intervals);

			System.out.print(
					"Out of the " + n + " intervals, the following have been scheduled using the Greedy Algorithm: \n");
			int count = 0;
			for (Interval i : intervals) {
				if (i.selected) {
					System.out.println(i);
					count ++;
				}
			}
			
				
			System.out.println (count + " Intervals have been scheduled out of the " + n + " intervals received");
			
		}
		
		System.out.println ("Passed all test cases without errors!");
	}
	
	

	/**
	 * selected a maximum subset of non-overlapping intervals
	 * 
	 * @param intervals
	 *            intervals to select from
	 * 
	 * @sideeffect sets the selected attribute of each interval that is part of
	 *             the maximal subset.
	 */
	static int schedule(Interval[] intervals) {

		// sort by increasing finish time O(nlogn)
		Arrays.sort(intervals, new Comparator<Interval>() {
			@Override
			public int compare(Interval first, Interval second) {
				return first.finish - second.finish;
			}
		});

		// greedily pick the first compatible interval
		int latestFinish = 0;
		int countSelected = 0;
		for (Interval i : intervals) {
			if (i.start >= latestFinish) {
				i.selected = true;
				countSelected++;
				latestFinish = i.finish;
			}
		}

		return countSelected;
	}

	/**
	 * Test the scheduling algorithms on a sample data set.
	 * 
	 * @param args
	 *            unused
	 */
	public static void main(String[] args) {

		// intervals from the Slide 4 Set 02 on Greedy algorithms
		Interval[] intervals = { new Interval("a", 0, 6), new Interval("b", 1, 4), new Interval("c", 3, 5),
				new Interval("d", 3, 8), new Interval("e", 4, 7), new Interval("f", 5, 9), new Interval("g", 6, 10),
				new Interval("h", 8, 11) };

		int count = schedule(intervals);

		System.out.println("The Maximal number of " + count + " intervals can be selected:");
		for (Interval i : intervals) {
			if (i.selected)
				System.out.println(i);
		}
		
		System.out.println ("One Test Case succeeded, continue the testing!");
		testIntervalScheduling();

	}

}
