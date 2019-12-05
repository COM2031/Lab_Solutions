/**
 * @author Dr. Manal Helal From the Problem Defined by Prof. Steve Schneider
 * @date created: September 2019
 * @Module COM2031 Advanced Computer Algorithms - Computer Science Department
 * @University of Surrey
 * Week 1 Lab: Find Minimum Missing Number in a list of numbers
 */

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.jfree.data.xy.XYSeries;
import org.jfree.ui.RefineryUtilities;
import org.junit.Test;


public class FindMinMissing {
	/**
	 * Used in the testing. You already have some test files to read from at the beginning.
	 */
	public static int [] createArray (int n) {
		
		List<Integer> l = IntStream.rangeClosed(1, n).boxed().collect(Collectors.toList());  

	   
		Collections.shuffle(l);
		
		return l.stream().mapToInt(i->i).toArray();
		
}
	
	/**
	 * Test method for {@link FindMinMissing#FindMinMissing(java.lang.String)}.
	 */
	@Test
	public final static void testFindMinMissing()  throws AssertionError  {
		
		for (int n=1; n<=10; n++) { 		// for all arrays of size n, of values of 1 to 10
			List<Integer> l = IntStream.rangeClosed(1, n).boxed().collect(Collectors.toList());  

			   
			Collections.shuffle(l);
			int [] num = l.stream().mapToInt(i->i).toArray();
			
			// assume missing is m, search for all missing
			for (int m = 1; m<=n; m++) {	// test of removal of the first element, up to no removals
				// remove current m
				
				l.remove((Integer) m); 
				n --;		// since we removed m
				Collections.shuffle(l);
				num = l.stream().mapToInt(i->i).toArray();
				int result = FindMinMissing.findMissingMinimum_Brute_Force(num, n, false);
				assertEquals("Brute Force not sorted failed for minimum = " + m + " for size n = " + n, m, result);
				result = FindMinMissing.findMissingMinimum_Sorting(num, n);
				assertEquals("Divide Conquer Sorted failed for minimum = " + m + " for size n = " + n, m, result);
				num = l.stream().mapToInt(i->i).toArray(); // return the array before the sorting in the previous step
				result = FindMinMissing.findMissingMinimum_Radix(num, n);
				assertEquals("Radix Sorted failed for minimum = " + m + " for size n = " + n, m, result);
				l.add((Integer) m);		// return the removed m to remove another in the next iteration 
				n ++; 					// return back n 
			}
		}
	}
	
	/**
	 * Here is you first method (1.1). Please follow the explanations in the slides and the lab sheet
	 */
	public static int findMissingMinimum_Brute_Force (int [] num, int n, boolean sorted) {
		// TODO implement the brute force method in finding the missing minimum
		
		int min = 1;
		
		for (int i = 0; i < n; i++)	// Traverse the numbers 
			if (min == num[i]) { 	// if you find the current minimum, 
				min ++;				// increment its value, and search again from first element. should be easier if list is already sorted
				if (!sorted)
					i=-1; 			// it will be incremented to zero in the loop
			}
		//System.out.println(min);
		return min;
	}
	
	 /**
	  * Here is you second method (1.2). Please follow the explanations in the slides and the lab sheet
	  */
	public static int findMissingMinimum_Sorting (int [] num, int n) {
		// TODO implement the sorted method in finding the missing minimum after sorting the input	
		Arrays.sort(num); //Dual-Pivot Quicksort - O(N log N)
		
		
		return findMissingMinimum_Brute_Force (num, n, true);
	}
	
	/**
	 * A utility function to get maximum value in arr[], where do you think you will need to find the maximum?!
	 */
	 
    static int getMax(int num[], int n) 
    { 
    	int mx = 0;
    	if (n > 0) {
	        mx = num[0]; 
	        for (int i = 1; i < n; i++) 
	            if (num[i] > mx) 
	                mx = num[i]; 
    	}
    	
        return mx; 
    } 
    
    /**
   	 * Here is you third method (1.3). Please follow the explanations in the slides and the lab sheet
   	 */ 
    static int findMissingMinimum_Radix(int num[], int n) 
    { 
    	// TODO implement the Radix method in finding the missing minimum
        
        int i; 
        int m = getMax(num, n); 
        int count[] = new int[m]; 
        Arrays.fill(count,0); 
  
        // Store count of occurrences in count[] 
        for (i = 0; i < n; i++) 
            count[(num[i])-1]++; 
  
        //Find Minimum not used index
        for (i = 0; i < n; i++) 
        { 
            if (count[i] == 0) 
            	return i+1; 
        } 
  
        return m+1;
    } 
   
    /**
  	 * This function reads the n numbers from the passed file name.
  	 */
	
	public static int [] readNumbers (int n, String filename) {
		
		
		int [] num = new int[n];
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filename));
			String line = reader.readLine();
			int i = 0;
			while ((line != null) && (i < n)) {
				//System.out.println(line);
				num[i] = Integer.parseInt(line);
				i ++;
				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return num;
		
	}
	
	/**
	 * This function is meant to make scaling happen for all simulations, by changing one line only.
	 */
	public static long getElapsedTime (long endTime, long startTime) {
		return (endTime - startTime);		// can add any scaling equation as required
	}
	
	/**
	 * In this function: all files are read, you can change that to start by one file and test, and then add more.
	 * It calls all algorithms, you can also comment out, and focus on one until done
	 * It also calls the chart comparison, and you can disable this by sending a false flag.
	 * It also generates a csv file with the input sizes, execution times for each algorithms, such that you take that to excel for better graphs there and more trend analysis.
	 */
	public static  void startSimulating(boolean displayChart)  {
		
		chartComparisons chart = new chartComparisons ("Find Minimum Algorithms");
		
		String filenamePrefix = "numbers_"; 
		String filename = filenamePrefix;
		
		int [] num_orig, num ;
		long startTime, endTime, elapsedtime;
		
		XYSeries series_a = chart.createSeries("A) Brute Force - No Sorting - Worst Case");
		XYSeries series_b = chart.createSeries("B) Sorting - Worst Case");
		XYSeries series_c = chart.createSeries("C) Radix Sorting - Worst Case");
		
		// create FileWriter object with file as parameter 
    
        FileWriter csvWriter;
		try {
			csvWriter = new FileWriter(new File("simulations.csv"));
		
			// adding header to csv 
	        csvWriter.append("Algorithm"); 
	        csvWriter.append(",");
	        csvWriter.append("N");
	        csvWriter.append(",");
	        csvWriter.append("Elapsed Time");
	        csvWriter.append(",");
	        csvWriter.append("Result");
	        csvWriter.append("\n");
			
			int simulations = 0;
		    for (int n=1000; n<=100000; n=n+10000) {
		    	
				// read the file using file naming convention explained in the lab sheet
				
				filename = filenamePrefix + n +  ".txt";
				num_orig = readNumbers (n, filename);
				
				
				startTime = System.currentTimeMillis();
				int result = findMissingMinimum_Brute_Force (num_orig, n, false); // start the simulations, by reading start and end system times, and subtract to get the elapsed time for this algorithm 
				endTime = System.currentTimeMillis();
				elapsedtime = getElapsedTime(endTime, startTime);
				
				chart.addPoint(series_a, n, elapsedtime); // add the x and y coordinates to the series representing this algorithm, as the size of the problem on the x-axis, and the elapsed time on the y-axis
				
				System.out.println ("bruteForce_FM Worst Case for n =: " + n + " = "+ result + " Elapsed Time = " + elapsedtime); // printout the results
				csvWriter.append("bruteForce_FM"); 
		        csvWriter.append(",");
		        csvWriter.append(Integer.toString(n));
		        csvWriter.append(",");
		        csvWriter.append(Long.toString(elapsedtime));
		        csvWriter.append(",");
		        csvWriter.append(Integer.toString(result));
		        csvWriter.append("\n");
				
				// return num to originally read from file, such that previous sorting is cancelled for the next simulation
				num = new int[num_orig.length];
				System.arraycopy(num_orig, 0, num, 0, num_orig.length);
				
				startTime = System.currentTimeMillis();
				result = findMissingMinimum_Sorting (num, n);
				endTime = System.currentTimeMillis();
				elapsedtime = getElapsedTime(endTime, startTime);
				
				chart.addPoint(series_b, n, elapsedtime);
				
				System.out.println ("sort_FM Worst Case for n =: " + n + " = "+ result + " Elapsed Time = " + elapsedtime);
				csvWriter.append("sort_FM"); 
		        csvWriter.append(",");
		        csvWriter.append(Integer.toString(n));
		        csvWriter.append(",");
		        csvWriter.append(Long.toString(elapsedtime));
		        csvWriter.append(",");
		        csvWriter.append(Integer.toString(result));
		        csvWriter.append("\n");
				
				// return num to originally read from file, such that previous sorting is cancelled for the next simulation
				num = new int[num_orig.length];
				System.arraycopy(num_orig, 0, num, 0, num_orig.length);
	
				startTime = System.currentTimeMillis();
				result = findMissingMinimum_Radix(num, n); 
				endTime = System.currentTimeMillis();
				elapsedtime= getElapsedTime(endTime, startTime);
				
				chart.addPoint(series_c, n, elapsedtime);
			
				System.out.println ("radixCount_FM Worst Case for n =: " + n + " = "+ result + " Elapsed Time = " + elapsedtime);
				csvWriter.append("radixCount_FM"); 
		        csvWriter.append(",");
		        csvWriter.append(Integer.toString(n));
		        csvWriter.append(",");
		        csvWriter.append(Long.toString(elapsedtime));
		        csvWriter.append(",");
		        csvWriter.append(Integer.toString(result));
		        csvWriter.append("\n");
		        
		        simulations ++;
			}
		    csvWriter.flush();
		    csvWriter.close();
		    
		    if (displayChart) {
			    List<XYSeries> seriesList = new ArrayList<>();
			    seriesList.add(series_a);
			    seriesList.add(series_b);
			    seriesList.add(series_c);
			    
			    chart.createDataset(seriesList, simulations);
			    
		
			    chart.pack();
				RefineryUtilities.centerFrameOnScreen(chart);
				chart.setVisible(true);
		    }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String args[])  {
		// calling your simulations.
		startSimulating(true);
		// you can comment this out until you have something in the three algorithms that you want to test.
		try {
			testFindMinMissing();
		}
		catch (AssertionError a) {
			System.out.println(a.getMessage());
		}
	
	}

}
