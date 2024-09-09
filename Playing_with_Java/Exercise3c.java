import sheffield.*;

public class Exercise3c {
	public static void main (String [] args) {
	
		EasyReader keyboard = new EasyReader();		
		boolean needSunscreen;
		
		//Find out if it is sunny
		if  (  keyboard.readBoolean("Is it sunny? ")  )  {
			//If it is ask what number (1 -12) the month is. 
			int month = keyboard.readInt("What month (1-12) is it? ");
			//We need sunscreen between May and Septembr if it is sunny
			needSunscreen =  month >= 5 && month <= 9;
		}
		else 
			needSunscreen = false;
		
		//Print out the appropriate advice
		if   (  needSunscreen  )
			System.out.println("Remember to use sunscreen");
		else 
			System.out.println("You don't need sunscreen unless you burn very easily");
		
	}
}

