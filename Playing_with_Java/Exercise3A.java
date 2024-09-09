import sheffield.*;
public class Exercise3A { 
	public static void main(String[] arg) {
		EasyReader keyboard = new EasyReader();
		double age = keyboard.readInt("what is your age?");
		if( 18>=age )
			System.out.println(" can't buy Alchol");
		if(( 18<=age ) && (25>=age))
			System.out.println(" can buy Alchol but need a proof");
		if( 25<=age)
			System.out.println(" can buy Alchol but need no proof");
	}
}