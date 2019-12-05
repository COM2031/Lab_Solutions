import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

/**
 * 
 * Java implementation of Interval Partitioning as discussed in COM2031: Advanced Algorithms
 * 
 * @author Copyright Andre Gruning, University of Surrey, 2016,2017
 *
 *
 * 
 * $Id: IntervalPartitioning.java 1177 2018-10-09 09:32:36Z ag0015 $
 * Debugged for more Test Cases Designed Manal Helal when the module was led by Prof. Steve Schneider in Fall 2019
 */
public class IntervalPartitioning {
	
	/**
	 * Data type that describes an interval with start and finish times. For
	 * convenience, Intervals can also be given a name.
	 * 
	 * @author gruening
	 * 
	 */
	
	/**
	 * Test method for {@link IntervalPartitioning#partition(Interval[])}.
	 */
	@Test
	public final static void testIntervalPartitioning () throws AssertionError {
		
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
			List<List<Interval>> schedule = IntervalPartitioning.partition(intervals);

			System.out.print(
					"The " + n + " intervals are partitioned as follows in this number of classrooms: " + schedule.size() + "\n\n");
			int count = 0;
			for (int room = 0; room < schedule.size(); room ++) {
				System.out.println ("Paritions in room: " + room);
				for (Interval i : schedule.get(room)) {
					System.out.println(i);
					count ++;
				}
				System.out.print("\n\n");
			}
			
			assertEquals("Should have scheduled " + n + " partitions, but your method scheduled  " + count + "!", count, n);
			
			
			
		}
		
		System.out.println ("Passed all test cases without errors!");
	}
	
	static class Interval {

		// * start time of interval
		final int start;
		// * end time of interval
		final int finish;
		// * convenient name for the interval.
		final String name;
	
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
			return "Interval [start=" + start + ", finish=" + finish + ", name=" + name + "]";
		}
	}


	
	
	

	/**
	 * Is a lecture compatible with a room? It is compatible if the lecture's
	 * start time is after the end of the last item already in the room.
	 * 
	 * This takes O(1) times to check.
	 * 
	 * No other interval already in the class room can have a later finish time
	 * than the last one in it by the following reasoning:
	 * 
	 * 1. By the order of intervals, intervals are added to class rooms in the
	 * order of their start time.
	 * 
	 * 2. No interval already in the class room can have a later finish time
	 * then the last interval (ie the one with the latest start time in it), as
	 * in that case such item would overlap with the latest items, and hence not
	 * be compatible with it. However we only add compatible items to the class
	 * room.
	 * 
	 * Therefore for any new item we only need to check whether it overlaps with
	 * the latest item in the room, taking O(1) time. If we had to take all
	 * items i the room, this could take O(n) time; and subsequently as this
	 * method is called within a for loop running n times, overall running time
	 * would be O(n^2).
	 * 
	 * @param room
	 *            rooms to check
	 * @param i
	 *            interval to schedule
	 * @return true if i can be scheduled in room
	 */
	static boolean isCompatible(List<Interval> room, Interval i) {
		return room.get(room.size() - 1).finish <= i.start;
	}

	static List<List<Interval>> partition(Interval[] intervals) {

		// sort intervals in order of ascending start time.
		Arrays.sort(intervals, new Comparator<Interval>() {
			@Override
			public int compare(Interval first, Interval second) {
				return first.start - second.start;
			}
		});

		// list of class rooms and the intervals that are scheduled in them.
		List<List<Interval>> classrooms = new LinkedList<List<Interval>>();

		
		for (Interval i : intervals) {
			boolean found = false;
			for (List<Interval> r : classrooms) {

				if (isCompatible(r, i)) {
					r.add(i);
					found = true;
					break;
				}
			}
			if (found == false) {
				List<Interval> newRoom = new LinkedList<Interval>();
				newRoom.add(i);
				classrooms.add(newRoom);
			}
		}

		return classrooms;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Intervals from Slide 12, slide set 02 Greedy algorithms:
		Interval[] intervals = { new Interval("a", 900, 930),
				new Interval("b", 900, 1230), new Interval("c", 900, 930),
				new Interval("d", 1100, 1230), new Interval("e", 1100, 1400),
				new Interval("f", 1300, 1430), new Interval("g", 1300, 1430),
				new Interval("h", 1400, 1630), new Interval("i", 1500, 1630),
				new Interval("j", 1500, 1630) };

		List<List<Interval>> schedule = IntervalPartitioning.partition(intervals);

		System.out.print(
				"The " + intervals.length + " intervals are scheduled as follows in this number of classrooms: " + schedule.size() + "\n\n");

		int count = 0;
		for (int room = 0; room < schedule.size(); room ++) {
			System.out.println ("Paritions in room: " + room);
			for (Interval i : schedule.get(room)) {
				System.out.println(i);
				count ++;
			}
			System.out.print("\n\n");
		}
		assertEquals("Should have scheduled " + intervals.length + " partitions, but your method scheduled  " + count + "!", count, intervals.length);
		
		
		System.out.println ("One Test Case succeeded, continue the testing!");
		testIntervalPartitioning();
	}
}
