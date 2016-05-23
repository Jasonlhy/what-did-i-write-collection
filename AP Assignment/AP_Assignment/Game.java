/*
**************************************************
* Name: Liu Ho Yin
* Class: 41303 1S
* Title: Game
* Student ID: 111176087
* Last modified by Liu Ho Yin on 2/5/2012
**************************************************
*/
import java.util.Arrays;

public class Game implements java.io.Serializable {
	LinkedList queue;
	ListStackComp[] numberStacks = new ListStackComp[9];
	ListStackComp undoStack = new ListStackComp();
	int score = 0, heightLimit = 9;
	String gameType;
	
	/* initialise a free play game */
	public Game() {
		this(new LinkedList());
		gameType = "FreeGame";
		for (int i=1;i<=heightLimit ;i++ ) 
		    addToQueue();
	}

	/* initialise a test mode game */
	public Game(LinkedList queue) {
		gameType = "TestGame";
		this.queue = queue;
		for (int i = 0; i < numberStacks.length; i++)
			numberStacks[i] = new ListStackComp();

	}
/*
	 * put the next number in the number sequence to the specified grid position
	 * Returns a list of integer representing the score earned in each combo
	 */
	public LinkedList putNext(int position) {
		/**user action*/
		if (numberStacks[position].size()<heightLimit) {
		    undoStack.push(position);
		    numberStacks[position].push(queue.removeFromHead());
		}
		else 
		    System.out.println("The Stack is full !!!!");

		//special treatment for free mode
		if (gameType.equals("FreeGame"))
		    addToQueue();

		/**matching*/
		LinkedList comboList = new LinkedList();
		boolean isComboing;
		int combo = 1;
		int[][] cases = new int[][] { { 0, 3, 6 }, { 1, 4, 7 }, { 2, 5, 8 },
				{ 0, 1, 2 }, { 3, 4, 5 }, { 6, 7, 8 }, { 0, 4, 8 }, { 2, 4, 6 } };
		
		do {
			isComboing = false;

			// Move in one time
			Move move = new Move();

			// check all matching cases
			for (int i = 0; i < cases.length; i++)
				if (isSameLevel(cases[i][0], cases[i][1], cases[i][2])
						&& isMatch(cases[i][0], cases[i][1], cases[i][2], move))
					isComboing = true;

			// this round movement involves numbers matching
			if (isComboing) {
				removeMatchingNumber(move);
				int roundMarks = (int) (move.countRemoval() * 10 * Math.pow(3,
						(combo - 1)));
				move.setMarks(roundMarks);
				score += roundMarks;
				undoStack.push(move);
				comboList.addToTail(roundMarks);
				combo++;
			}
		} while (isComboing);

		return comboList;

	}

	/* Retrieve the next number from the sequence of queue */
	public int getNext() {
		return (Integer) queue.get(1);
	}

	/* retrieve the height limit of the game stacks */
	public int getLimit() {
		return heightLimit;
	}

	/* Retrieve the next three queue from the sequence of queue */
	public LinkedList getNextThree() {
		LinkedList list = new LinkedList();
		int size=3;

		// nearly no number in the queue
		if (queue.count()<3 &&gameType.equals("TestGame")) 
		    size=queue.count();

		// get the next numbers (normally three number)
		for (int i = 1; i <=size; i++)
			list.addToTail(queue.get(i));

		return list;

	}
	
	/*Add a random number into the queue*/
	public void addToQueue() {
		int random = (int) (Math.random() * 9 + 1);
		queue.addToTail(random);
	}

	/* return a stack height at the specified grid position */
	public int stackHeight(int position) {
		return numberStacks[position].size();

	}

	/* return the number at the top of stack at the specified grid position */
	public int top(int position) {
		return (Integer) (numberStacks[position].peek());
	}

	/* set the maximum height of stack to k */
	public void setLimit(int k) {
		heightLimit = k;
	}

	/* return the score of the player */
	public int getScore() {
		return score;

	}

	/*
	 * To undo one previous step.The previous number will be place at the front
	 * of the number sequence All matching made and score involved will be
	 * rolled back
	 */
	public void undo() {

		Object popOut;

		// all move was push back into game stacks until a number come
		while ((popOut = undoStack.pop()) instanceof Move) {
			Move move = (Move) popOut;

			// number have been removed will be added back into the game stack
			int[] addedBackNum = move.getRemoveNumbers();
			for (int i = 0; i < addedBackNum.length; i++)
				if (addedBackNum[i] != Move.MOVE_DEFAULT_VALUE)
					numberStacks[i].push(addedBackNum[i]);

			// deduct back the marks that was added
			score -= move.getMarks();

		}

		// a integer(user input) finally come
		if (popOut instanceof Integer) {
			int stackIndex = (Integer) popOut;
			queue.addToHead(numberStacks[stackIndex].pop());
		}

	}

	/* return if the game is already ended */
	public boolean gameOver() {
		for (int i = 0; i < numberStacks.length; i++) 
			if (numberStacks[i].size() < heightLimit)
				return false;
		
		return true;
	}

	/* display the top of the grids */
	public String toString() {
		String s = "";
		for (int i = 0; i < numberStacks.length; i++) {

			// display the top elements
			if (numberStacks[i].empty())
				s += "#";
			else
				s += numberStacks[i].peek();

			// further display the sizes of stack on the same line and jump to
			// the next line
			if ((i + 1) % 3 == 0) {
				s += "   "; // separating space

				// height 
				for (int j = (i - 2); j <= i; j++)
					s += numberStacks[j].size();

				s += "\n";
			}

		} // end for loop
		return s;
	}

	// check the value in sequence or not
	public boolean isSequence(int a, int b, int c) {
		int[] number = { a, b, c };
		Arrays.sort(number);

		return ((number[1] - number[0] ==1) &&  (number[2] - number[1]==1));
	}

	/* check the value is the same or not */
	public boolean isTheSame(int a, int b, int c) {
		return (a == b && b == c);
	}

	/* get the value and do isMatch */
	public boolean isMatch(int a, int b, int c, Move move) {
		int aValue = (Integer) top(a);
		int bValue = (Integer) top(b);
		int cValue = (Integer) top(c);

		// matched-> pass to move to store 
		if (isSequence(aValue, bValue, cValue)
				|| isTheSame(aValue, bValue, cValue)) {
			int removal[] = {aValue,bValue,cValue};
			int removalIndex[] = {a,b,c};
			
			move.add(removal,removalIndex);

			return true;
		}

		return false;
	}

	/*To remove the matching number */
	public void removeMatchingNumber(Move move) {
		int[] removeNumber = move.removeNumbers;
		for (int i = 0; i < removeNumber.length; i++) {
			if (removeNumber[i] != Move.MOVE_DEFAULT_VALUE)
				numberStacks[i].pop();
		}
	}

	/* determine whether the stacks are same level */
	public boolean isSameLevel(int a, int b, int c) {
		return (numberStacks[a].size() == numberStacks[b].size()
				&& numberStacks[b].size() == numberStacks[c].size()
				&& !numberStacks[a].empty() && !numberStacks[b].empty() && !numberStacks[c]
					.empty());

	}
	
}

