


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class MBP {
	
	
	// Prints the matching by tracing all reachable nodes using the max flow 
	// Consider matching M = set of edges from L to R with f(e) = 1.
	void matching(int[][] flow, int[][] graph, int s, int t, int cut_cap, int startOfSecondset) { 
		int u,v; 
		List<Integer> A = new ArrayList<Integer>();
		List<Integer> B = new ArrayList<Integer>();

		// Using the max Flow passed from the Ford Fulkerson algorithm, find vertices reachable from s using DFS
		boolean[] isVisited = new boolean[graph.length];  
		fordFulkerson.dfs(flow, s, isVisited); 
		
		// find all edges from a reachable vertex to a reachable vertex from the max flow found by Ford Fulkerson Algorithm
		// add the source vertex to set A, 
		// add the destination vertex to set B
		
		System.out.println ("The matching is as follows " + cut_cap + ". The following are edges from reachable vertex to non-reachable vertex:");
		// skipping first and last vertices for the added source and sink vertices to reduce the problem to max flow
		for (int i = 1; i < graph.length-1; i++) { 
			for (int j = 1; j < graph.length-1; j++) { 
				if (flow[i][j] > 0 && isVisited[i] && isVisited[j]) { 
					System.out.println("{" + String.valueOf(i-1) + " -> " + String.valueOf(j-startOfSecondset) + " }");
					if(!A.contains(i-1))
					       A.add(i-1);
					if(!B.contains(j-startOfSecondset))
					       B.add(j-startOfSecondset);
				} 
			} 
		}
		
		// Set A contains
		if (A.size() > 0) {
			System.out.print("First Set contains: ( " + String.valueOf(A.get(0)));
			for (int i = 1; i<A.size(); i++) {
				System.out.print(", " + String.valueOf(A.get(i)));
			}
			System.out.println(")");
		}
		if (B.size() > 0) {
			System.out.print("Second Set contains: ( " + String.valueOf(B.get(0)));
			for (int i = 1; i<B.size(); i++) {
				System.out.print(", " + String.valueOf(B.get(i)));
			}
			System.out.println(") ");
		}
	} 
		
		
	// input is in form of BiPartitie Edmond Matrix, and return max flow after adding source and sink and connect 
	// source to all vertices in first set, and connect all vertices in second set to sink
	// Run the Ford Fulkerson algorithm and print the result using the matching algorithm
	// this algorithm takes extra space

	public int BPMtoMaxFlow (boolean[][] bpGraph) {
		
		// the bipartite graph is defined as two sets of vertices U, and V
		// the first dimension is the number of edges in U, and the second dimension is the set in V
		if ((bpGraph == null) || (bpGraph.length == 0))
			return -1;
		if (bpGraph[0].length == 0)
			return -1;
		
		int s = 0;
		int t = bpGraph.length + bpGraph[0].length + 1;
		int startOfSecondset = bpGraph.length + 1; // helps in the matching function
		// add source as vertex zero, and add sink as last vertex
		int[][] adjMatrix = new int[bpGraph.length + bpGraph[0].length + 2][bpGraph.length + bpGraph[0].length + 2];
		
		// this way we created one adjacency matrix containing all vertices in the 2 sets
		// adding source and sink
		
		
		// we copy the values from the biPartite Graph to the adjacency matrix
		// take care of indices to account for the newly added vertices
		for (int i = 0; i<bpGraph.length;i++) {
			for (int j = 0; j<bpGraph[i].length;j++) {
				if (bpGraph[i][j])
					adjMatrix[i+1][j+bpGraph.length+1]	= Integer.MAX_VALUE;
				else
					adjMatrix[i+1][j+bpGraph.length+1]	= 0; // not really needed as the default initialisation of int is zero
			}
		}
		
		// define new edges between source '0' and all vertices in first set
		for (int i = 1; i<=bpGraph.length;i++) {
			adjMatrix[s][i]	= 1;
		}
		// define new edges between all vertices in second set and the sink 'bpGraph[0].length-1'
		for (int j = 1; j<=bpGraph[0].length;j++) {
			adjMatrix[j+bpGraph.length][t]	= 1;
		}
		
		
		// the interest/preference is the capacity of each edge, 1 = true, and 0 = false.
		
		// find max flow
		fordFulkerson maxFlowFordFulkerson = new fordFulkerson(); 
		// the min cut is printed inside the ford Fulkerson method called in the following line
		int[][] maxFlow = maxFlowFordFulkerson.fordFulkerson(adjMatrix, 0, t);
		int flowValue = 0;
		for (int i=0;i<adjMatrix.length; i++)
			flowValue += maxFlow[i][t];

		System.out.println("The maximum possible flow of the given graph is calculated as " + flowValue); 
		System.out.println("The matching of the first Bipartitite set with the second is calculated from the min-cut as follows:" ); 
		// This method prints the flow that is reachable after the min-cut, should identify the matching
		matching(maxFlow, adjMatrix, s, t, flowValue, startOfSecondset);
		
		return flowValue;
		
	}
	
	// A  DFS based recursive function that returns true if a matching for  
    // vertex u is possible 
    boolean bpm(boolean bpGraph[][], int u,  
                boolean seen[], int matchR[]) 
    { 
        // Try every job one by one 
        for (int v = 0; v < bpGraph.length; v++) 
        { 
            // If applicant u is interested  
            // in job v and v is not visited 
            if (bpGraph[u][v] && !seen[v]) 
            { 
                  
                // Mark v as visited 
                seen[v] = true;  
  
                // If job 'v' is not assigned to 
                // an applicant OR previously 
                // assigned applicant for job v (which 
                // is matchR[v]) has an alternate job available. 
                // Since v is marked as visited in the  
                // above line, matchR[v] in the following 
                // recursive call will not get job 'v' again 
                if (matchR[v] < 0 || bpm(bpGraph, matchR[v], 
                                         seen, matchR)) 
                { 
                    matchR[v] = u; 
                    return true; 
                } 
            } 
        } 
        return false; 
    } 
  
    // Returns maximum number  of matching from M to N 
    int maxBPM(boolean bpGraph[][]) 
    { 
        // An array to keep track of the  
        // applicants assigned to jobs.  
        // The value of matchR[i] is the  
        // applicant number assigned to job i,  
        // the value -1 indicates nobody is assigned. 
        int matchR[] = new int[bpGraph.length]; 
  
        // Initially all jobs are available 
        for(int i = 0; i < bpGraph.length; ++i) 
            matchR[i] = -1; 
  
        // Count of jobs assigned to applicants 
        int result = 0;  
        for (int u = 0; u < bpGraph[0].length; u++) 
        { 
            // Mark all jobs as not seen  
            // for next applicant. 
            boolean seen[] =new boolean[bpGraph.length] ; 
            for(int i = 0; i < bpGraph.length; ++i) 
                seen[i] = false; 
  
            // Find if the applicant 'u' can get a job 
            if (bpm(bpGraph, u, seen, matchR)) 
                result++; 
        } 
        return result; 
    } 

	//Driver Program 
	public static void main(String args[]) { 

		// First Example: Job Applicant mactching
		// Input is in the form of Edmonds matrix which is a 2D array 
		// with M rows (first set of vertices such as  M job applicants
		// and N columns (second set of vertices such as N jobs.
		// The value bpGraph[i][j] is true if i’th applicant is interested in j’th job, otherwise false.
		
        boolean bpGraph[][] = new boolean[][]{ 
                              {false, true, true, false, false, false}, 
                              {true, false, false, true, false, false}, 
                              {false, false, true, false, false, false}, 
                              {false, false, true, true, false, false}, 
                              {false, false, false, false, false, false}, 
                              {false, false, false, false, false, true}}; 
        MBP bpm = new MBP(); 
        System.out.println( "Maximum number of applicants that can"+ 
                            " get job is "+bpm.maxBPM(bpGraph)); 
        
        int flowValue = bpm.BPMtoMaxFlow(bpGraph);
        assertEquals("Maximum Flow for the given graph is calculated incorrectly as " + flowValue, flowValue, 5);

        
        // this means 5 applicants can get 5 jobs at maximum. This is not perfect matching as 1 applicant will be left out
        // From the matching method printing, work out the indices to specify which applicant will get which job as the maximum applicant/job matching
        /*
         * 	{0 -> 1 }
			{1 -> 0 }
			{2 -> 2 }
			{3 -> 3 }
			{5 -> 5 }
         */
        
        
        // Second Example: Musician Instruments matching
        // the example is laid out in the lab 8 sheet 
        //  as 11 musicians and 11 instruments with the following preferences and indices between squares:
        /*
         * Alice [0]:	Guitar[0], Oboe[1], Drums[2], Saxophone[3]
		Bob[1]: 	Violin[4], Cello[5], Guitar[0]
		Chris[2]:	Piano[6], Drums[2], Saxophone[3]
		Donna[3]:	Flute[7], Oboe[1]
		Eric[4]:	Bassoon[8], Clarinet[9], Cello[10]
		Freddy[5]:	Saxophone[3], Flute[7], Guitar[0]
		Gabriella[6]:	Cello[10], Guitar[0], Piano[6], Bassoon[8]
		Henry[7]:	Oboe[1], Saxophone[3]
		Ian[8]:	Bassoon[8], Flute[7], Clarinet[9]
		Jess[9]:	Oboe[1], Guitar[0], Drums[2]
		Kelly[10]:	Guitar[0], Drums[2], Flute[7]

		It was originally Bass and Bassoon as different instruments, I considered them the same since we are 
		told we have 11 instruments.
		
		I started giving a unique index to every instrument name, then searching for all its occurrences and assign 
		the same index, and remember the latest number reached to assign to the new instrument found.
		
		You can either define your adjacency matrix as above, in one go for all 11 by 11 2D boolean matrix.
		Or as follows, use the fact that the default value for a boolean (primitive) is false in Java, and only set to true the preferences indices as defined above
         */
        
        boolean bpGraph2[][] = new boolean[11][11]; 
        bpGraph2[0][0] = bpGraph2[0][1] = bpGraph2[0][2] = bpGraph2[0][3] = true; // Alice preferences: Alice [0]:	Guitar[0], Oboe[1], Drums[2], Saxophone[3]
        bpGraph2[1][4] = bpGraph2[1][5] = bpGraph2[1][6] = true; // Bob preferences: Bob[1]: 	Violin[4], Cello[5], Guitar[0]
        bpGraph2[2][6] = bpGraph2[2][2] = bpGraph2[2][3] = true; // Chris preferences: Chris[2]:	Piano[6], Drums[2], Saxophone[3]
        bpGraph2[3][7] = bpGraph2[3][1] = true; // Donna preferences: Donna[3]:	Flute[7], Oboe[1]
        bpGraph2[4][8] = bpGraph2[4][9] = bpGraph2[4][10] = true; // Eric preferences: Eric[4]:	Bassoon[8], Clarinet[9], Cello[10]
        bpGraph2[5][3] = bpGraph2[5][7] = bpGraph2[5][0] = true; // Freddy preferences: Freddy[5]:	Saxophone[3], Flute[7], Guitar[0]
        bpGraph2[6][10] = bpGraph2[6][0] = bpGraph2[6][6] = bpGraph2[6][8] = true; // Gabriella preferences: Gabriella[6]:	Cello[10], Guitar[0], Piano[6], Bassoon[8]
        bpGraph2[7][1] = bpGraph2[7][3] = true; // Henry preferences: Henry[7]:	Oboe[1], Saxophone[3]
        bpGraph2[8][8] = bpGraph2[8][7] = bpGraph2[8][9] = true; // Ian preferences: Ian[8]:	Bassoon[8], Flute[7], Clarinet[9]
        bpGraph2[9][1] = bpGraph2[9][0] = bpGraph2[9][2] = true; // Jess preferences: Jess[9]:	Oboe[1], Guitar[0], Drums[2]
        bpGraph2[10][0] = bpGraph2[10][2] = bpGraph2[10][7] = true; // Kelly preferences: Kelly[10]:	Guitar[0], Drums[2], Flute[7]
        
       
       System.out.println( "Maximum number of Musicians that can"+ 
          " play Instruments is "+bpm.maxBPM(bpGraph2)); 

       flowValue = bpm.BPMtoMaxFlow(bpGraph2);
       
       assertEquals("Maximum Flow for the given graph is calculated incorrectly as " + flowValue, flowValue, 8);
       
       // this means 8 musicians can play 8 instruments at maximum. This is not perfect matching as 3 musicians will be left out
       // From the matching method printing, work out the indices to specify which musician will play which instrument as the maximum collective play
       /*
        * First Set contains: ( 0, 1, 2, 3, 4, 5, 6, 8)
		  Second Set contains: ( 0, 4, 2, 1, 8, 3, 6, 7)
		  
		  Alice [0]: Guitar[0]
		  Bob[1]: 	Violin[4]
		  Chris[2]:	Drums[2]
		  Donna[3]:	Oboe[1]
		  Eric[4]:	Bassoon[8]
		  Freddy[5]:	Saxophone[3]
		  Gabriella[6]:	Piano[6]
		  Ian[8]:	Flute[7]
		  
		  
		  Henry, Jess, and Kelly will not play
		  Similarily you can find out which instruments are not played
		   
        */
       
       // Third Example is laid out in the lab 8 sheet as 1b
       //  as 10 characters and 10 digits with the following edges defined in the image:
       /*
        * a [0]:	1[0], 3[2], 4[3]
		b[1]: 	1[0], 2[1], 8[7]
		c[2]:	1[0], 3[2]
		d[3]:	3[2], 4[3]
		e[4]:	3[2], 4[3]
		f[5]:	2[1], 5[4], 7[6]
		g[6]:	5[4], 6[5], 10[9]
		h[7]:	5[4], 6[5], 7[6], 8[7], 10[9]
		i[8]:	9[8]
		j[9]:	4[3], 9[8]

		to keep things simpler, the digits are labelled as zero based, instead of keeping the vertex order
		
		
		I will reuse the fact that the default value for a boolean (primitive) is false in Java, and only set to true the preferences indices as defined above
        */
       
       boolean g[][] = new boolean[11][11]; 
       g[0][0] = g[0][2] = g[0][3] = true; //  a [0]:	1[0], 3[2], 4[3]
       g[1][0] = g[1][1] = g[1][7] = true; // b[1]: 	1[0], 2[1], 8[7]
       g[2][0] = g[2][2] = true; // c[2]:	1[0], 3[2]
       g[3][2] = g[3][3] = true; // d[3]:	3[2], 4[3]
       g[4][2] = g[4][3] = true; // e[4]:	3[2], 4[3]
       g[5][1] = g[5][4] = g[5][6] = true; // f[5]:	2[1], 5[4], 7[6]
       g[6][4] = g[6][5] = g[6][9] = true; // g[6]:	5[4], 6[5], 10[9]
       g[7][4] = g[7][5] = g[7][6] = g[7][7] = g[7][9] = true; // h[7]:	5[4], 6[5], 7[6], 8[7], 10[9]
       g[8][8] = true; // i[8]:	9[8]
       g[9][3] = g[9][8] = true; // j[9]:	4[3], 9[8]
      
       System.out.println( "Maximum number of alphabet that can"+ 
    	          " match to digits is "+bpm.maxBPM(g)); 

       flowValue = bpm.BPMtoMaxFlow(g);
    	       
       assertEquals("Maximum Flow for the given graph is calculated incorrectly as " + flowValue, flowValue, 8);
       
       // this means 8 musicians can play 8 instruments at maximum. This is not perfect matching as 3 musicians will be left out
       // From the matching method printing, work out the indices to specify which musician will play which instrument as the maximum collective play
       /*
        * First Set contains: ( 0, 1, 2, 3, 5, 6, 7, 8)
		  Second Set contains: ( 0, 1, 2, 3, 4, 5, 6, 8) 
		  
		  Maximum Matching: ( a->1, b->2, c->3, d->4, f->5, g->6, h->7, i->9)
        */
       
	} 

}

