import sheffield.*;

public class Exercise3b {
	
	public static void main(String[] args) {
		//Age limits
		final int CAN_BUY = 18;
		final int NEED_NOT_PROVE_AGE = 25;
		
		//Find the age
		EasyReader keyboard = new EasyReader();
		int age = keyboard.readInt("How old are you? ");
		
		//Output the result
		if  (  age < CAN_BUY  )
			System.out.println("You can't buy alcohol");
		else {
			System.out.print("You can buy alcohol");
			if  (  age <  NEED_NOT_PROVE_AGE  )
				 System.out.println(" but you will probably need to prove your age");
			else
			 	 System.out.println();
		}
	}
}
