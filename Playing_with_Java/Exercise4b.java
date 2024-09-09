//The Hailstone sequence
import sheffield.*;

public class Exercise4b {
	public static final void main(String[] args) {
		
		//Get the initial value and make sure it is positive
		//Always use a do loop if you want to make sure the input is sensible
		EasyReader reader = new EasyReader();
		int n;		
		do {
			n = reader.readInt("Enter the value of n, an integer greater than 0: ");
		} while (   n <= 0  );
		
		//Write out the sequence
		//Use a while loop here because n might start out at 1; a do loop would
		//give the wrong answer in that case
		while (   n != 1   ) { //stop when n gets to 1
			if (   n%2  ==  0  ) 
				n/=2;  //n is even so divide by 2
			else 
				n=(3*n)+1;  //n is odd so multiply by 3 and and add 1
			//Print out n
			System.out.print(n+" ");
		}
		System.out.println();
	}
}
