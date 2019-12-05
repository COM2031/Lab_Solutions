/**
 * Implementation of the Dynamic programming Sequence Alignment algorithm for COM2031
 * 
 * @author Manal Helal when the module leader was Steve Schneider 2019 
 */

import java.util.Arrays;

public class SeqAlign {
	// function to find out  
	// the minimum penalty 
	static void Needleman_Wunsch(String X, String Y, int M[][], 
	                              int misMatchPenalty, int gapPenalty) 
	{ 
		
		for ( int i=0 ; i < X.length(); i++) 
		  M[i][0] = gapPenalty*i;
		for (int j=0; j < Y.length(); j++) 
		  M[0][j] = gapPenalty*j;
		for ( int i=1 ; i <= X.length(); i++) {
			for (int j=1; j <= Y.length(); j++) {
				int Match;
				if (X.charAt(i - 1) == Y.charAt(j - 1)) 
					Match = M[i-1][j-1];
				else
					Match = M[i-1][j-1] + misMatchPenalty;
			    int Delete = M[i-1][j] + gapPenalty;
			    int Insert = M[i][j-1] + gapPenalty;
			    M[i][j] = Math.min(Match,  Math.min(Insert, Delete));
		  }
		}
	} 
	
	static  String traceBack (String X, String Y, int M[][], int misMatchPenalty, int gapPenalty) {
		StringBuilder AlignmentX = new StringBuilder();
		StringBuilder AlignmentY = new StringBuilder();
		int i = X.length();
		int j = Y.length();
		while ((i > 0) || (j > 0)) {
			if ((i > 0) && (j > 0) && (X.charAt(i - 1) == Y.charAt(j - 1))) { 
				AlignmentX.append(X.charAt(i-1));
				AlignmentY.append(Y.charAt(j-1));
		    
				i --;
				j --;
			}
			
			else if ((i > 0) && (j > 0) && (M[i][j] == M[i-1][j-1] + misMatchPenalty)) {
			  AlignmentX.append(X.charAt(i-1));
			  AlignmentY.append(Y.charAt(j-1));
		    
			  i --;
			  j --;
			}
		  else if ((i > 0) && (M[i][j] == M[i-1][j] + gapPenalty)) {
		  
			  AlignmentX.append(X.charAt(i-1));
			  AlignmentY.append("-");
			  i --;
		  }
		  else {
			AlignmentX.append("-");
		    AlignmentY.append(Y.charAt(j-1));
		    j --;
		  }
		}
		AlignmentX = AlignmentX.reverse();
		AlignmentY = AlignmentY.reverse();
		return "The alignment is: \n" + AlignmentX.toString() + "\n" + AlignmentY.toString() + "\n" ;
	}
	  
	/**
	 * pretty-print The scoring MAtrix
	 */
	
	public static String printM(int M[][], String X, String Y) {

		StringBuffer s = new StringBuffer();

		// Table Header:
		s.append("String Y in columns:\n-\t");
		for (int j = 0; j < Y.length(); j++) {
			s.append(Y.charAt(j) + "\t");
		}
		s.append("\nString X in rows\n-\t");
		for (int j = 0; j < Y.length(); j++) {
			s.append(M[0][j] + "\t");
		}
		s.append("\n");
		// Table Content:
		for (int i = 1; i <= X.length(); i++) {
			s.append(X.charAt(i-1) + " \t");
			for (int j = 1; j <= Y.length(); j++) {
				s.append(M[i][j] + "\t");
			}
			s.append("\n");
		}

		return s.toString();
	}
	
	// Driver code 
	public static void main(String[] args) 
	{ 
	    // input strings 
	    String gene1 = "AGGGCT"; 
	    String gene2 = "AGGCA"; 
	      
	    // intialsing penalties of different types 
	    int misMatchPenalty = 3; 
	    int gapPenalty = 2; 
	  
	    // calling the function to  calculate the result 
	    final int M[][] = new int[gene1.length()  + 1][gene2.length() + 1]; 
	    Needleman_Wunsch(gene1, gene2, M, misMatchPenalty, gapPenalty); 
	    System.out.println(printM(M, gene1, gene2));
	    System.out.println(traceBack(gene1, gene2, M, misMatchPenalty, gapPenalty)); 
	} 
}
