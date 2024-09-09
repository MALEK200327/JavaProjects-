import sheffield.*;
public class Greeting {
	public static void main ( String [] arg) {
		EasyReader keyboard = new EasyReader();
		String name = keyboard.readString("please enter your name ");
		String family = keyboard.readString("please enter your family name ");
		System.out.println(" Hello, nice to meet you " +  name + " " + family ); 
	}
}
	
	