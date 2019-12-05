/**
 * @author Dr. Manal Helal From the Problem Defined by Prof. Steve Schneider
 * @date created: September 2019
 * @Module COM2031 Advanced Computer Algorithms - Computer Science Department
 * @University of Surrey
 * Week 1 Lab: Find Minimum Missing Point in a 2D unbounded coordinate space in a given list of occupied points. Minimum is measured by the Manhattan Distance (distance from origin)
 */

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.Test;

public class Point implements Comparable<Point>{
	
	int x, y;
	
	Point (int x, int y) {
		this.x = x;
		this.y = y;
	}
	@Override
	public boolean equals (Object o) {
		if (o == this)
			return true;
		if (!(o instanceof Point))
			return false;
		Point p = (Point) o;
		if ((p.x == this.x) && (p.y == this.y))
			return true;
		else
			return false;
	
	}
	
	/**
	 * Test method for {@link FindMinMissing#FindMinMissing(java.lang.String)}.
	 */
	@Test
	public final static void testFindMinMissing2D () throws AssertionError {
		
		for (int n=1; n<=10; n++) { 		// for all arrays of size n, of values of 1 to 10
			List<Point> l = new ArrayList<>();
			
			// initialise all the grid with points that are originally added in order from origin
			Point p = new Point(1, 1);
			
			while (l.size() < n) {
				l.add(p);
				p = nextPoint(p);
			}
			
			
			
			// assume missing is m, search for all missing
			for (int m = 0; m<n; m++) {	// test of removal of an element, from the first to remove all
				// remove current m
				
				Point closest = l.remove(m); 
				//n --;		// since we removed m
				
				Collections.shuffle(l);
				Point result = findMissingMinimum_Brute_Force(l);
				assertEquals("Brute Force not sorted failed for point = (" + closest.x + ", " + closest.y + ") for grid size n = " + n, closest.x, result.x);
				assertEquals("Brute Force not sorted failed for point = (" + closest.x + ", " + closest.y + ") for grid size n = " + n, closest.y, result.y);
				

				result = findMissingMinimum_Sorting(l);
				assertEquals("Sorted failed for point = (" + closest.x + ", " + closest.y + ") for grid size n = " + n, closest.x, result.x);
				assertEquals("Sorted failed for point = (" + closest.x + ", " + closest.y + ") for grid size n = " + n, closest.y, result.y);
				
				Collections.shuffle(l);
				result = findMissingMinimum_Radix(l);
				assertEquals("Radix Count failed for point = (" + closest.x + ", " + closest.y + ") for grid size n = " + n, closest.x, result.x);
				assertEquals("Radix Count failed for point = (" + closest.x + ", " + closest.y + ") for grid size n = " + n, closest.y, result.y);
				
				l.add(m, closest);
				//n ++; 					// return back n 
			}
		}
		
		System.out.println ("Passed all test cases without errors!");
	}
	
	@Override
	public int compareTo(Point p) {
		// TODO Auto-generated method stub
		// this override >= and <= operators, based on Ineteger.compare
		// Parameters:
		//x the first int to compare, from the passed argument
		//y the second int to compare, from the current object
		//Returns:
		//the value 0 if x == y; a value less than 0 if x < y; and a value greater than 0 if x > y
		//. the passed object is > returns -1, equals return 0, smaller returns 1
		
		
		
		if (p.x +p.y < this.x + this.y)
			return 1;
		if ((p.x +p.y == this.x + this.y) && (p.x < this.x))
			return 1;
		if ((p.x == this.x) && (p.y == this.y)) 
			return 0;
		
		else
			return -1;
		
	}
	
	
	
	/**
	 * A utility function to get ManhattanDistance between the object point, and a passed point
	 */
	public int ManhattanDistance (Point p) {
		return (Math.abs((p.x - this.x))+ Math.abs((p.y - this.y)));
	}
	/**
	 * A utility static function to get distanceFromOrigin for the passed point, simply the sum of the coordinates
	 */
	public static int distanceFromOrigin (Point p) {
		return Math.abs(p.x + p.y);
	}
	/**
	 * A utility static function to get distanceFromOrigin for the passed point, simply the sum of the coordinates
	 */
	public Point closerToOrigin (Point p1, Point p2) {
		int p1Dist = distanceFromOrigin(p1);
		int p2Dist = distanceFromOrigin(p2);
		
		if (p1Dist < p2Dist)
			return p1;
		else if (p1Dist == p2Dist)
			if (p1.x < p2.x)
				return p1;
		
		return p2;
	}
	
	
	/**
	 * Here is 2.1.1 method to implement. The wave front traversal of points by closest to origin for an open grid. 
	 */
	
	public static Point nextPoint(Point p) {
		int x, y;
		// wave front traversal starting from 1, 1, assuming open grid, will keep increasing forever, 
		// need to end your traversal loop some other way from the calling function
		
		// starting from point (1, 1), first iteration
		// get north point first by incrementing y, and making x = 1  -> (1, 2)
		// next iteration keeping the same distance from origin, decrement y, and increment x -> (2, 1)
		
		// conditioned in (p.y-1 > 1), we will not go below 1, 1 as minimum coordinate
		// else we will set y to the last value in x incremented by 1, and set x = 1, this will start the new wave from the most north value in it.
		
		// higher distance from origin iterations will go as follows for distance 4 from origin starting at most north point: (1, 3)
		// decrement y, and increment x -> (2, 2)
		// decrement y, and increment x -> (3, 1)
		
		// again conditioned in (p.y-1 > 1), we will not go below 1, 1 as minimum coordinate on both axis
		// else we will set y to the last value in x incremented by 1, and set x = 1, this will start the new wave from the most north value in it.
		
		// higher distance from origin iterations will go as follows for distance 4 from origin starting at most north point: (1, 4)
		// decrement y, and increment x -> (2, 3)
		// decrement y, and increment x -> (3, 2)
		// decrement y, and increment x -> (4, 1)
		
		// again conditioned in (p.y-1 > 1), we will not go below 1, 1 as minimum coordinate on both axis
		// else we will set y to the last value in x incremented by 1, and set x = 1, this will start the new wave from the most north value in it.
		
		// higher distance from origin iterations will go as follows for distance 4 from origin starting at most north point: (1, 5)
		// decrement y, and increment x -> (2, 4)
		// decrement y, and increment x -> (3, 3)
		// decrement y, and increment x -> (4, 2)
		// decrement y, and increment x -> (5, 1)
		
		// this continues infinitely from any passed point for an unbounded grid
		
		if (p.y > 1){  // keeping the same distance from origin, but distributing it over both axis, by incrementing x and decrementing y
			x = p.x + 1;
			y = p.y - 1; 
		}
		else  { //(p.y = 1)  // incrementing the distance from origin, by starting from x = 1 and the new distance for y reached at x in the previous point
			x = 1; 
			y = p.x + 1;
		}
				
		p = new Point (x, y);
		
		
		return p; 
	}
	
	/**
	 * next point with an increment value. 
	 */
	
	public static Point nextPoint(Point p, int inc, Point n) {
		int x, y;
		// wave front traversal starting from 1, 1, assuming open grid, will keep increasing forever, 
		// need to end your traversal loop some other way from the calling function
		
		// starting from point (1, 1), first iteration
		// get north point first by incrementing y, and making x = 1  -> (1, 2)
		// next iteration keeping the same distance from origin, decrement y, and increment x -> (2, 1)
		
		// conditioned in (p.y-1 > 1), we will not go below 1, 1 as minimum coordinate
		// else we will set y to the last value in x incremented by 1, and set x = 1, this will start the new wave from the most north value in it.
		
		// higher distance from origin iterations will go as follows for distance 4 from origin starting at most north point: (1, 3)
		// decrement y, and increment x -> (2, 2)
		// decrement y, and increment x -> (3, 1)
		
		// again conditioned in (p.y-1 > 1), we will not go below 1, 1 as minimum coordinate on both axis
		// else we will set y to the last value in x incremented by 1, and set x = 1, this will start the new wave from the most north value in it.
		
		// higher distance from origin iterations will go as follows for distance 4 from origin starting at most north point: (1, 4)
		// decrement y, and increment x -> (2, 3)
		// decrement y, and increment x -> (3, 2)
		// decrement y, and increment x -> (4, 1)
		
		// again conditioned in (p.y-1 > 1), we will not go below 1, 1 as minimum coordinate on both axis
		// else we will set y to the last value in x incremented by 1, and set x = 1, this will start the new wave from the most north value in it.
		
		// higher distance from origin iterations will go as follows for distance 4 from origin starting at most north point: (1, 5)
		// decrement y, and increment x -> (2, 4)
		// decrement y, and increment x -> (3, 3)
		// decrement y, and increment x -> (4, 2)
		// decrement y, and increment x -> (5, 1)
		
		// this continues infinitely from any passed point for an unbounded grid
		
		if (p.y > inc){  // keeping the same distance from origin, but distributing it over both axis, by incrementing x and decrementing y
			x = p.x + inc;
			y = p.y - inc; 
		}
		else  { //(p.y = inc)  // incrementing the distance from origin, by starting from x = 1 and the new distance for y reached at x in the previous point
			x = inc; 
			y = p.x + inc;
		}
				
		
		// here is where it is different than the unbounded version, knowing when to stop, 
				// and when to get another point within the bounds of a grid (n x n)
				
		if (Point.distanceFromOrigin(new Point(x, y)) > n.x * n.y) // we finished traversing the grid
			return null;
		
		p = new Point (x, y);
		
		if ((x < inc) || (y < inc) || (x > n.x) || (y > n.y))  // if we incremented one axis outside the grid, continue to next point within the grid
			p = nextPoint(p, inc, n);
		
		return p; 
	}
	

	/**
	 * Here is your 2.1.2 method to implement. Please follow the explanations in the slides and the lab sheet
	 */
	public static Point findMissingMinimum_Brute_Force (List<Point> points) {
		// TODO implement the brute force method in finding the missing minimum
		
		Point minP = new Point (1, 1);
    	// loop until null is retrieved
		boolean found = containsPoint(points, minP); // O(n) // this should eventually be not found;
    	while (found) {
    		minP = nextPoint(minP); // O(1) // this will keep going forever, 
    		found = containsPoint(points, minP); // O(n) // this should eventually be not found
    	}
    	
		return minP;
	}
	
	/**
	 * Here is your 2.2 method. Please follow the explanations in the slides and the lab sheet
	 */
	public static Point findMissingMinimum_Sorting (List<Point> points) {
		// TODO implement the sorted method in finding the missing minimum after sorting the input
		
		Collections.sort(points);
		
		Point minP = new Point (1, 1);
    	// loop until null is retrieved
    	for (Point p : points) {
    		if (minP.equals(p))
    			minP = nextPoint(minP); // O(1) // this will keep going forever, 
    	}	
    	return minP;
    }
	
	// A utility function to get maximum value in arr[] 
    static Point getMax(List<Point> points) { 
    	Point max = null;
    	int maxDist = 0;
    	if (points.size() > 0) {
    		 max = points.get(0); 
        		
	        for (Point p: points) 
		            if (distanceFromOrigin (p) > maxDist) {
						maxDist = distanceFromOrigin (p);
						max = p;
					}
		
    	}
    	
        return max; 
    } 
    
    /**
	 * Here is your 3.1 method. Please follow the explanations in the slides and the lab sheet
	 */ 
    static Point findMissingMinimum_Radix(List<Point> points) 
    { 
    	// TODO implement the Radix method in finding the missing minimum
		
        int i; 
        Point max = getMax(points); 
        if (max == null)
        	return new Point(1,1);
        int maxCoord = distanceFromOrigin (max); // for simplicity we will great a 2D grid of counts of the maximum distance from origin point in the grid returned could be (1, 10), which is 11 from origin, then we create a grid if (11, 11)
        int count[][] = new int[maxCoord][maxCoord]; 
        
  
        for (Point p : points) {
        	count[(p.x)-1][(p.y)-1]++;
        }
          // Store count of occurrences in count[] 

        //Find Minimum not used index
        Point minP = new Point (1, 1);
    	// loop until null is retrieved
		boolean found = false;
    	while ((!found) && (minP.x <= maxCoord) && (minP.y <= maxCoord)) {
    		if (count[minP.x-1][minP.y-1] == 0) 
    			found = true; // O(n) // this should eventually be not found, either be exceeding the maximum point in the input list, or a closer point to origin that is not in the input list
    		else
    			minP = nextPoint(minP); // O(1) // this will keep going forever, 
    	}
    	
        return minP;
    } 
    
    
    
	 /**
	 * A utility function to generate the test cases, by knowing in advance which point to remove
	 */
	public static void removePoint (List<Point> points, Point p1) {
		for (Point p: points)
			if ((p.x == p1.x) && (p.y == p1.y)) {
				points.remove(p);
				return;
			}
		
	}
	/**
	 * A utility function to test if the list of points contains a particular point. Where do you think you will need this?!!
	 */
	public static boolean containsPoint (List<Point> points, Point p1) {
		for (Point p: points)
			if (p.equals(p1)) {
				return true;
			}
		return false;
		
	}
	public static void main(String args[])  {
	
		
		int n = 5; // number of points to create, to be like the example using grid size of 5;
		
		List<Point> l = new ArrayList<>();
		
		// initialise all the grid with points
		for (int i = 1; i<=n; i++) {
			for (int j = 1; j<=n; j++) {
				l.add(new Point (i, j));
			}
		}
		
		// remove the points as illustrated in the image in the lab sheet
		int rmX = 4;
		int rmY = 3;
		Point remPoint = new Point(rmX, rmY);
		removePoint (l, remPoint);
		
		rmX = 1;
		rmY = 5;
		remPoint = new Point(rmX, rmY);
		removePoint (l, remPoint);
		
		rmX = 3;
		rmY = 5;
		remPoint = new Point(rmX, rmY);
		removePoint (l, remPoint);
		rmX = 5;
		rmY = 5;
		remPoint = new Point(rmX, rmY);
		removePoint (l, remPoint);
		
		Collections.shuffle(l);;
		// we know that the closest to origin is this one
		
		Point closest = new Point(1, 5);
		
		Point result = findMissingMinimum_Brute_Force(l);
		assertEquals("Brute Force not sorted failed for point = (" + closest.x + ", " + closest.y + ") for grid size n = " + n, closest.x, result.x);
		assertEquals("Brute Force not sorted failed for point = (" + closest.x + ", " + closest.y + ") for grid size n = " + n, closest.y, result.y);
		

		result = findMissingMinimum_Sorting(l);
		assertEquals("Sorted failed for point = (" + closest.x + ", " + closest.y + ") for grid size n = " + n, closest.x, result.x);
		assertEquals("Sorted failed for point = (" + closest.x + ", " + closest.y + ") for grid size n = " + n, closest.y, result.y);
		
		
		result = findMissingMinimum_Radix(l);
		assertEquals("Radix Count failed for point = (" + closest.x + ", " + closest.y + ") for grid size n = " + n, closest.x, result.x);
		assertEquals("Radix Count failed for point = (" + closest.x + ", " + closest.y + ") for grid size n = " + n, closest.y, result.y);

		System.out.println ("If no errors in lab Sheet example, then success, try the comprehensive test cases");
		
		try {
			testFindMinMissing2D();
		}
		catch (AssertionError a) {
			System.out.println(a.getMessage());
		}
	
		
	}
	
}
