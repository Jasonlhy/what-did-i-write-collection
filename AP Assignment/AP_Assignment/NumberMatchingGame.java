/*
**************************************************
* Name: Liu Ho Yin
* Class: 41303 1S
* Title: NumberMatchingGame
* Student ID: 111176087
* Last modified by Liu Ho Yin on 2/5/2012
**************************************************
*/

import java.io.*;
import java.util.Scanner;

public class NumberMatchingGame {

	public static void main(String[] args) {
		/** determine game mode */
		Game game = null;
		LinkedList list = new LinkedList();
		if (args.length == 0) // no file is provided
			game = new Game();
		else { // get the file content
			String[] numbers = null;
			try {   // read the file
				File file = new File(args[0]);
				BufferedReader reader = new BufferedReader(new FileReader(file));

				numbers = (reader.readLine()).split(" ");

				for (int i = 0; i < numbers.length; i++)
					list.addToTail(Integer.parseInt(numbers[i]));

				game = new Game(list);
			} catch (Exception e) {
				System.out.println("Error exist in file input");
				System.exit(0);
			}

		}

		/** start to play the game */
		Scanner scanner = new Scanner(System.in);
		do { 	//UI in console
			LinkedList comBoList = new LinkedList();
			System.out.println(game);
			System.out.println("Current score is :" + game.score);
			System.out.println("Next three numbers area : "
					+ game.getNextThree());
			System.out.print("Which position (1-9,0 to undo)? ");
			
			try {
			    //user choice 
			    int position = scanner.nextInt();
			    
			    if (position == 0 )
				game.undo();
			    else if (position>=0 && position<=9)
				comBoList = game.putNext(position-1);
			    else 
				System.out.println("Please enter a valid number again !!!");
				
			} catch(Exception e) {
			    System.out.println("Error Exist!");
			    System.exit(0);
			}

			//Display combolist 
			if (!comBoList.isEmpty())
			    System.out.println(LinkedList.displayCombo(comBoList));
			
		} while (!game.gameOver());
		System.out.println("the game is over!!");

	}

}
