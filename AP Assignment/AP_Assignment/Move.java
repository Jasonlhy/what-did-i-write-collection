/*
**************************************************
* Name: Liu Ho Yin
* Class: 41303 1S
* Title: Move
* Student ID: 111176087
* Last modified by Liu Ho Yin on 2/5/2012
**************************************************
*/

import java.util.Arrays;

public class Move implements java.io.Serializable{
	int[] removeNumbers; // non duplicate data set for according index
	int marks; // marks in each round movement
	final static int MOVE_DEFAULT_VALUE = 9999; // a constant indicate data in a particular index is empty
	int noOfAdded = 0;

	// default contructor
	public Move() {
		removeNumbers = new int[9];
		for (int i = 0; i < removeNumbers.length; i++)
			if (removeNumbers[i] == 0)
				removeNumbers[i] = MOVE_DEFAULT_VALUE;
	}

	
	// add the number inside data set
	public void add(int [] removal, int [] removalIndex) {
		// not empty -> add data inside 

	    for (int i=0; i<removal.length; i++){ 
		if (removeNumbers[removalIndex[i]] == MOVE_DEFAULT_VALUE) {
			removeNumbers[removalIndex[i]] = removal[i];
			noOfAdded++;
		}
	    }	
	}

	// return the numbers have to be poped out in the game stacks
	public int[] getRemoveNumbers() {
		return removeNumbers;
	}

	// set the makr for matching 
	public void setMarks(int marks) {
		this.marks = marks;
	}

	// get the mark
	public int getMarks(){
		return marks;
	}
	
	//count how many numbers matched and were removed
	public int countRemoval() {
		return noOfAdded;

	}

	//Overiding toString , for testing 
	public String toString() {
		String s = "";
		for (int i = 0; i < removeNumbers.length; i++) {
			if (removeNumbers[i] == MOVE_DEFAULT_VALUE)
				s += "#";
			else
				s += "" + removeNumbers[i];
		}
		return s;
	}
}
