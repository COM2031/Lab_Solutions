
/* @author Dr. Manal Helal for @Module COM2031 Advanced Computer Algorithms - Computer Science Department
 * Led by Prof. Steve Schneider
 * @date created: November 2019
 * @University of Surrey
 * Week 9 Lab: Project Selection Problem reduced to Ford Fulkerson algorithm of last week  
 */


import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

public class ProjectSelection {

	static class Project {
		public int pv;
		Project (int pv) {
			this.pv = pv;
		}
	}

	// This one traces the rGraph for all reachable vertices remaining in the s cut
	int Selection(int[][] rGraph, int[][] graph, int s, int t, int cut_cap, Project[] nodes) { 
		List<Integer> selProjects = new ArrayList<Integer>();




		// Using the residual graph passed from the Ford Fulkerson algorithm, find vertices reachable from s using DFS
		boolean[] isVisited = new boolean[graph.length];  
		fordFulkerson.dfs(rGraph, s, isVisited); 

		// start from s, trace the residual graph to all remaining reachable vertecies , 
		// skipping the source, and decrementing the Project index to cancel the addition of the source
		System.out.println (" The previous Min-Cut printing is showing the disconnected edges using indices after addition of source and sink");
		System.out.println (" The following are edges from reachable verteces from source after removing the added vertices:");
		for (int i = 1; i < graph.length-1; i++) { 
			for (int j = 1; j < graph.length-1; j++) { 
				if (rGraph[i][j] > 0 && isVisited[i] && isVisited[j]) { 
					System.out.println((i-1) + " - " + (j-1)); 
					if(!selProjects.contains(i-1))
						selProjects.add(i-1);
				} 
			} 
		} 
		// Set selProjects contains
		int netProfit = 0;
		if (selProjects.size() > 0) {
			System.out.print("Selected Projects are: ( " + String.valueOf(selProjects.get(0)));
			netProfit += nodes[selProjects.get(0)].pv;
			for (int i = 1; i<selProjects.size(); i++) {
				System.out.print(", " + String.valueOf(selProjects.get(i)));
				netProfit += nodes[selProjects.get(i)].pv;
			}
			System.out.println(") for net value: " + netProfit);
		}
		return netProfit;
	} 




	// input is projects graph, and return true if there is circulation or false if there is not.
	// the max flow is calculated after adding source and sink and connect 
	// source to all supply vertices with -pv as capacity, and connect all demand vertices to sink with pv as capacity

	public int PStoMaxFlow (int[][] g, Project nodes[]) {

		// if adjacency list is empty return 
		if ((g == null) || (g.length == 0))
			return Integer.MIN_VALUE;



		int s = 0;
		int t = g.length + 1;

		// add source as vertex zero, and add sink as last vertex
		int[][] adjMatrix = new int[g.length + 2][g.length + 2];




		// we copy the values from the  Graph adjacency matrix to the new one
		// take care of indices to account for the newly added vertices
		// a project dependency is the existence of an edge, give it infinite value capacity
		for (int i = 0; i<g.length;i++) {
			for (int j = 0; j<g[i].length;j++) {
				if (g[i][j] > 0)
					adjMatrix[i+1][j+1] = Integer.MAX_VALUE;
			}
		}

		// define new edges between source '0' and all net profit project vertices
		for (int i = 1; i<=g.length;i++) {
			if (nodes[i-1].pv > 0) 
				adjMatrix[s][i]	= nodes[i-1].pv;
		}
		// define new edges between all net cost vertices in second set and the sink and inverting the sign of the cost to make it positive
		for (int i = 1; i<=g.length;i++) {
			if (nodes[i-1].pv < 0) 
				adjMatrix[i][t] = -1 * nodes[i-1].pv; 
		}




		// find max flow
		fordFulkerson maxFlowFordFulkerson = new fordFulkerson(); 
		// the min cut is printed inside the ford Fulkerson method called in the following line
		int[][] maxFlow = maxFlowFordFulkerson.fordFulkerson(adjMatrix, 0, t);
		int netCost = 0;
		for (int i=0;i<adjMatrix.length; i++)
			netCost += maxFlow[i][t];

		//Now this is the netcost for the maximum flow in the graph, but in a positive value, 
		// make this the cost of the original dependency edges instead of infinity  
		// to make the dependency edges in the
		// projects intuitively too expensive to be broken by a cut

		/*
				for (int i = 0; i<g.length;i++) {
					for (int j = 0; j<g[i].length;j++) {
						if (adjMatrix[i+1][j+1] == Integer.MAX_VALUE)
							adjMatrix[i+1][j+1] = netCost;
					}
				}
		 */

		return Selection(maxFlowFordFulkerson.rGraph, adjMatrix, s, t, netCost, nodes);


	}


	//Driver Program 
	public static void main(String args[]) { 

		// First Example: From Lab 9 Sheet, 1)a

		Project [] nodes = {new Project(6), new Project(4), new Project (-3), new Project (-6)};
		int g[][] = new int[nodes.length][nodes.length]; 
		g[0][2] = 1;
		g[1][2] = 1;
		g[1][3] = 1;

		ProjectSelection ps = new ProjectSelection(); 

		int profitTotal = ps.PStoMaxFlow(g, nodes);
		System.out.println( "Maximum Profit of projects selected is: "+profitTotal); 



		assertEquals("Maximum Profit for the selected Projects is calculated incorrectly as " + profitTotal, profitTotal, 3);

		// Second Example: From Lab 9 Sheet, 1)b
		Project [] nodes_2 = {new Project(-1), // Index 0 - 1 in the visualisation, A in the table
				new Project(4), // Index 1 - 2 in the visualisation, B in the Table
				new Project (-3), // Index 2- 3 in the visualisation, C in the table
				new Project (5), // Index 3- 4 in the visualisation, D in the table
				new Project (2), // Index 4- 5 in the visualisation, E in the table
				new Project (-3), // Index 5- 6 in the visualisation, F in the table
				new Project (3), // Index 6- 7 in the visualisation, G in the table
				new Project (5), // Index 7- 8 in the visualisation, H in the table
				new Project (-4), // Index 8- 9 in the visualisation, I in the table
				new Project (-6),// Index 9- 10 in the visualisation, J in the table
				new Project (-6), // Index 10- 11 in the visualisation, K in the table
				new Project (10) // Index 11- 12 in the visualisation, L in the table
		};
		int g2[][] = new int[nodes_2.length][nodes_2.length]; 
		g2[1][0] = 1;

		g2[3][2] = 1;
		g2[4][3] = 1;

		g2[6][5] = 1;
		g2[7][6] = 1;

		g2[9][8] = 1;

		g2[11][10] = 1;



		profitTotal = ps.PStoMaxFlow(g2, nodes_2);
		System.out.println( "Maximum Profit of projects selected is: "+profitTotal); 

		assertEquals("Maximum Profit for the selected Projects is calculated incorrectly as " + profitTotal, profitTotal, 16);
		
		// Third Example: From Lecture Slides in Week 8
		
		Project [] nodes_3 = {new Project(4), // Index 0 -  A in the table
				new Project(2), // Index 1 -  B in the Table
				new Project (-3), // Index 2-  C in the table
				new Project (-5), // Index 3- D in the table
				new Project (6), // Index 4- E in the table
				new Project (-3) // Index 5-  F in the table
				
		};
		int g3[][] = new int[nodes_3.length][nodes_3.length]; 
		g3[0][2] = 1; //A require C, D
		g3[0][3] = 1;

		g3[1][0] = 1; // B require A, F
		g3[1][5] = 1;

		g3[3][2] = 1; // D require C
		

		g3[4][3] = 1; // E require D

		g3[5][0] = 1; // F require A



		profitTotal = ps.PStoMaxFlow(g3, nodes_3);
		System.out.println( "Maximum Profit of projects selected is: "+profitTotal); 
		// If you work out the indices you will find the solution in the slides: {A,C,E,D}. with net total profit 2
		assertEquals("Maximum Profit for the selected Projects is calculated incorrectly as " + profitTotal, profitTotal, 2);



	} 

}

