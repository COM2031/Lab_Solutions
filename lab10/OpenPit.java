
import static org.junit.Assert.assertEquals;

public class OpenPit {

	//Driver Program 
	public static void main(String args[]) { 

		// First Example: Question 1 in Lab 10 Sheet, scanning blocks from left to right, top to bottom. 
		// Obviously we will excavate all blocks in this example, and maximum profit is all values added together (profit as positive and cost as negative)

		ProjectSelection.Project [] nodes = {new ProjectSelection.Project(1), // index 0 in first row left to right
				new ProjectSelection.Project(-2), // index 1
				new ProjectSelection.Project (-2), // index 2
				new ProjectSelection.Project (-2), // index 3
				new ProjectSelection.Project (-2), // index 4
				new ProjectSelection.Project (5), // index 5 in second row left to right
				new ProjectSelection.Project (6), // index 6
				new ProjectSelection.Project (-3), // index 7
				new ProjectSelection.Project (4)};// index 8 in bottom row

		// the dependencies are between a block and the
		// blocks in the level above that must be mined in order to mine it, within a given angle
		// we will assume this angle to be 45, and only the top block, and left upper diagonal, and right upper diagonal block are adjacent to any block defined
		// this means, we will skip the blocks on the first row, and start from the second row
		int g[][] = new int[nodes.length][nodes.length]; 
		g[5][0] = 1; g[5][1] = 1; g[5][2] = 1;  // 3 top neighbours of the first block in the second row
		g[6][1] = 1; g[6][2] = 1; g[6][3] = 1; // 3 top neighbours of the second block in the second row
		g[7][2] = 1; g[7][3] = 1; g[7][4] = 1; // 3 top neighbours of the third block in the second row

		g[8][5] = 1; g[8][6] = 1; g[8][7] = 1; // 3 top neighbours of the only block in the bottom row


		ProjectSelection ps = new ProjectSelection(); 

		int profitTotal = ps.PStoMaxFlow(g, nodes);
		System.out.println( "Interpret the above results as blocks instead of projects, or stop the printing in the ProjectSelection Methods to generalise."); 
		System.out.println( "Maximum Profit of blocks to excavate in Example 1 in Question 1 is: "+profitTotal); 



		assertEquals("Maximum Profit after exacaveting the defined blocks in Question 1 is calculated incorrectly as " + profitTotal, profitTotal, 6);

		// Second Example: Question 2 in Lab 10 Sheet, scanning blocks from left to right, top to bottom.
		// Again it is obvious that we will excavate all blocks in this example, and maximum profit is all values added together (profit as positive and cost as negative)
		ProjectSelection.Project [] nodes_2 = {new ProjectSelection.Project(-1), // index 0 in first row left to right
				new ProjectSelection.Project(-2), // index 1
				new ProjectSelection.Project (-2), // index 2
				new ProjectSelection.Project (-2), // index 3
				new ProjectSelection.Project (-2), // index 4
				new ProjectSelection.Project (2), // index 5 in second row left to right
				new ProjectSelection.Project (-4), // index 6
				new ProjectSelection.Project (7), // index 7
				new ProjectSelection.Project (6)};// index 8 in bottom row

		int g2[][] = new int[nodes.length][nodes.length]; 
		g2[5][0] = 1; g2[5][1] = 1; g2[5][2] = 1;  // 3 top neighbours of the first block in the second row
		g2[6][1] = 1; g2[6][2] = 1; g2[6][3] = 1; // 3 top neighbours of the second block in the second row
		g2[7][2] = 1; g2[7][3] = 1; g2[7][4] = 1; // 3 top neighbours of the third block in the second row

		g2[8][5] = 1; g2[8][6] = 1; g2[8][7] = 1; // 3 top neighbours of the only block in the bottom row




		profitTotal = ps.PStoMaxFlow(g2, nodes_2);
		System.out.println( "Interpret the above results as blocks instead of projects, or stop the printing in the ProjectSelection Methods to generalise."); 
		System.out.println( "Maximum Profit of blocks to excavate in Question 2 is: "+profitTotal); 


		assertEquals("Maximum Profit after exacaveting the defined blocks in Question 2 is calculated incorrectly as " + profitTotal, profitTotal, 2);


		// Third Example: Question 3 in Lab 10 Sheet, scanning blocks from left to right, top to bottom.

		ProjectSelection.Project [] nodes_3 = {new ProjectSelection.Project(-1), // index 0 in first row left to right
				new ProjectSelection.Project (-1), // index 1
				new ProjectSelection.Project (-1), // index 2
				new ProjectSelection.Project (-1), // index 3
				new ProjectSelection.Project (-1), // index 4
				new ProjectSelection.Project (-1), // index 5 
				new ProjectSelection.Project (-2), // index 6 in second row left to right
				new ProjectSelection.Project (-3), // index 7
				new ProjectSelection.Project (2), // index 8
				new ProjectSelection.Project (-3), // index 9
				new ProjectSelection.Project (-2), // index 10
				new ProjectSelection.Project (-3), // index 11
				new ProjectSelection.Project (-3),// index 12 in third row left to right
				new ProjectSelection.Project (-2),// index 13 
				new ProjectSelection.Project (5),// index 14
				new ProjectSelection.Project (6),// index 15
				new ProjectSelection.Project (-4),// index 16
				new ProjectSelection.Project (-4),// index 17
				new ProjectSelection.Project (-9),// index 18 in bottom row 
				new ProjectSelection.Project (-4),// index 19 
				new ProjectSelection.Project (7),// index 20 
				new ProjectSelection.Project (-1),// index 21 
				new ProjectSelection.Project (6)// index 22 
		};

		int g3[][] = new int[nodes_3.length][nodes_3.length]; 
		g3[6][0] = 1; g3[6][1] = 1;  // 2 top neighbours of the first block in the second row
		g3[7][0] = 1; g3[7][1] = 1; g3[7][2] = 1; // 3 top neighbours of the second block in the second row
		g3[8][1] = 1; g3[8][2] = 1; g3[8][3] = 1; // 3 top neighbours of the third block in the second row
		g3[9][2] = 1; g3[9][3] = 1; g3[9][4] = 1; // 3 top neighbours of the fourth block in the second row
		g3[10][3] = 1; g3[10][4] = 1; g3[10][5] = 1; // 3 top neighbours of the fifth block in the second row
		g3[11][4] = 1; g3[11][5] = 1;  // 2 top neighbours of the sixth block in the second row

		g3[12][6] = 1; g3[12][7] = 1;  // 2 top neighbours of the first block in the third row
		g3[13][6] = 1; g3[13][7] = 1; g3[13][8] = 1; // 3 top neighbours of the second block in the third row
		g3[14][7] = 1; g3[14][8] = 1; g3[14][9] = 1; // 3 top neighbours of the third block in the third row
		g3[15][8] = 1; g3[15][9] = 1; g3[15][10] = 1; // 3 top neighbours of the fourth block in the third row
		g3[16][9] = 1; g3[16][10] = 1; g3[16][11] = 1; // 3 top neighbours of the fifth block in the third row
		g3[17][10] = 1; g3[17][11] = 1;  // 2 top neighbours of the sixth block in the third row

		g3[18][12] = 1; g3[18][13] = 1;  // 2 top neighbours of the first block in the bottom row
		g3[19][12] = 1; g3[19][13] = 1; g3[19][14] = 1; // 3 top neighbours of the second block in the bottom row
		g3[20][13] = 1; g3[20][14] = 1; g3[20][15] = 1; // 3 top neighbours of the third block in the bottom row
		g3[21][14] = 1; g3[21][15] = 1; g3[21][16] = 1; // 3 top neighbours of the fourth block in the bottom row
		g3[22][15] = 1; g3[22][16] = 1; g3[22][17] = 1; // 3 top neighbours of the fifth block in the bottom row



		profitTotal = ps.PStoMaxFlow(g3, nodes_3);
		System.out.println( "Interpret the above results as blocks instead of projects, or stop the printing in the ProjectSelection Methods to generalise."); 
		System.out.println( "Maximum Profit of blocks to excavate in Question 3 is: "+profitTotal); 

		assertEquals("Maximum Profit after exacaveting the defined blocks in Question 3 is calculated incorrectly as " + profitTotal, profitTotal, 2);

		// Fourth Example: Question 4 in Lab 10 Sheet, scanning blocks from left to right, top to bottom.

		ProjectSelection.Project [] nodes_4 = {new ProjectSelection.Project(-1), // index 0 in first row left to right
				new ProjectSelection.Project (-1), // index 1
				new ProjectSelection.Project (-1), // index 2
				new ProjectSelection.Project (-1), // index 3
				new ProjectSelection.Project (-1), // index 4
				new ProjectSelection.Project (-1), // index 5 
				new ProjectSelection.Project (-2), // index 6 in second row left to right
				new ProjectSelection.Project (-3), // index 7
				new ProjectSelection.Project (2), // index 8
				new ProjectSelection.Project (-3), // index 9
				new ProjectSelection.Project (-2), // index 10
				new ProjectSelection.Project (-3), // index 11
				new ProjectSelection.Project (-3),// index 12 in third row left to right
				new ProjectSelection.Project (-2),// index 13 
				new ProjectSelection.Project (5),// index 14
				new ProjectSelection.Project (6),// index 15
				new ProjectSelection.Project (-4),// index 16
				new ProjectSelection.Project (-4),// index 17
				new ProjectSelection.Project (-9),// index 18 in fourth row 
				new ProjectSelection.Project (-4),// index 19 
				new ProjectSelection.Project (7),// index 20 
				new ProjectSelection.Project (-1),// index 21 
				new ProjectSelection.Project (6), // index 22 
				new ProjectSelection.Project (6), // index 23 in bottom row
				new ProjectSelection.Project (8) // index 24
		};

		int g4[][] = new int[nodes_4.length][nodes_4.length]; 
		g4[6][0] = 1; g4[6][1] = 1;  // 2 top neighbours of the first block in the second row
		g4[7][0] = 1; g4[7][1] = 1; g4[7][2] = 1; // 3 top neighbours of the second block in the second row
		g4[8][1] = 1; g4[8][2] = 1; g4[8][3] = 1; // 3 top neighbours of the third block in the second row
		g4[9][2] = 1; g4[9][3] = 1; g4[9][4] = 1; // 3 top neighbours of the fourth block in the second row
		g4[10][3] = 1; g4[10][4] = 1; g4[10][5] = 1; // 3 top neighbours of the fifth block in the second row
		g4[11][4] = 1; g4[11][5] = 1;  // 2 top neighbours of the sixth block in the second row

		g4[12][6] = 1; g4[12][7] = 1;  // 2 top neighbours of the first block in the third row
		g4[13][6] = 1; g4[13][7] = 1; g4[13][8] = 1; // 3 top neighbours of the second block in the third row
		g4[14][7] = 1; g4[14][8] = 1; g4[14][9] = 1; // 3 top neighbours of the third block in the third row
		g4[15][8] = 1; g4[15][9] = 1; g4[15][10] = 1; // 3 top neighbours of the fourth block in the third row
		g4[16][9] = 1; g4[16][10] = 1; g4[16][11] = 1; // 3 top neighbours of the fifth block in the third row
		g4[17][10] = 1; g4[17][11] = 1;  // 2 top neighbours of the sixth block in the third row

		g4[18][12] = 1; g4[18][13] = 1;  // 2 top neighbours of the first block in the fourth row
		g4[19][12] = 1; g4[19][13] = 1; g4[19][14] = 1; // 3 top neighbours of the second block in the fourth row
		g4[20][13] = 1; g4[20][14] = 1; g4[20][15] = 1; // 3 top neighbours of the third block in the fourth row
		g4[21][14] = 1; g4[21][15] = 1; g4[21][16] = 1; // 3 top neighbours of the fourth block in the fourth row
		g4[22][15] = 1; g4[22][16] = 1; g4[22][17] = 1; // 3 top neighbours of the fifth block in the fourth row

		g4[23][19] = 1; g4[23][20] = 1; g4[23][21] = 1; // 3 top neighbours of the first block in the bottom row
		g4[24][20] = 1; g4[24][21] = 1; g4[24][22] = 1; // 3 top neighbours of the second block in the bottom row

		profitTotal = ps.PStoMaxFlow(g4, nodes_4);
		System.out.println( "Interpret the above results as blocks instead of projects, or stop the printing in the ProjectSelection Methods to generalise."); 
		System.out.println( "Maximum Profit of blocks to excavate in Question 4 is: "+profitTotal); 

		assertEquals("Maximum Profit after exacaveting the defined blocks in Question 4 is calculated incorrectly as " + profitTotal, profitTotal, 4);


	} 

}
