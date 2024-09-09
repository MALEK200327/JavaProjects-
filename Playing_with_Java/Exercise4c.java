/*
Written by SDN 24 August 2015
*/
import sheffield.*;
public class Exercise4c {
	public static void main(String [] args) {
		//The maximum width
		final int MAX = 40;
		
		//Get the height
		EasyReader keyboard = new EasyReader();
		int height;
		boolean gotGoodHeight = false;
		do {
			//Read it in
			height = keyboard.readInt("How high would you like your triangle? ");
			
			//Check it
			if  (  height % 2 == 0  )
				System.out.println("I need an odd number. Please try again");
			else if (  height > MAX  )
				System.out.println("That is too big. Please try again");
			else if ( height < 0 )
				System.out.println("That is too small. Please try again");
			else gotGoodHeight = true;
			
		}  while (  ! gotGoodHeight  );
		
		//Print the triangle
		for (int r=0; r<height; r++)  {
			//write the leading spaces; 0 for the last line, height-1 for the first
			for (int s=0; s<height-1-r;  s++)
				System.out.print(" ");
			//write the asterisks
			for (int a=0; a<2*r+1; a++) 
				System.out.print("*");
			//And on to the next row
			System.out.println();
		}
		
	}
}