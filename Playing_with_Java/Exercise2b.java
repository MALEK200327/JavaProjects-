import sheffield.*; 
public class Exercise2b {
    public static void main(String[] args) { 
		
		//The contants
		final int YARDS_IN_A_MILE = 1760;
		final double KILOMETERS_IN_A_MILE = 1.60934;
		
		// read the distance in miles and yards
		System.out.println("Enter a distance in miles and yards");
		EasyReader reader = new EasyReader();         
		int miles = reader.readInt("How many miles? : ");         
		int yards = reader.readInt("How many yards? : ");   
      
		// Work out the kilometers 
		double kilometers = ( miles + yards/(double)YARDS_IN_A_MILE ) *
				      KILOMETERS_IN_A_MILE;
		
		//Print it out
		System.out.print(miles + " miles and " + yards +
				" yards is equivalent to " + Math.round(kilometers) + "km");	
		
	} 
}
