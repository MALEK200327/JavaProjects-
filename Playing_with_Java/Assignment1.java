// I call the sheffield package by import 
import sheffield.*;
public class Assignment1 {
	public static void main ( String [] args) {
		// Now i declared a constant as 1pound = 0.453592kg 
		final double PTOKG = 0.453592;
		// I used the class EasyReader to create an input
		EasyReader weight = new EasyReader();
		//I used the class EasyReader  an input of a text file
		EasyReader planets1 = new EasyReader("planets.txt");
		//I used the class EasyWriter named it screen to print 
		System.out.println(planets1);
	}
}