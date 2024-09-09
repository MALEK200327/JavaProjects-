import sheffield.*;
public class Exercise3B1 {
	public static void main( String[] arg) {
		EasyReader keyboard = new EasyReader();
		int first = keyboard.readInt("Enter first number: ");
		int second = keyboard.readInt("Enter second number: ");
		int larger,smaller;
		if (first<second) {
			smaller = first;
		    larger = second;
		}
		else {
			smaller = second;
			larger = first;
		}
		System.out.println("The sum is " + (smaller+larger));
		System.out.println("The difference is " + (larger-smaller));
	}
}
			
		