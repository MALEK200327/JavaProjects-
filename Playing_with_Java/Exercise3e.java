import sheffield.*;

public class Exercise3e {
    public static  void main(String[] args) {

    	//Get the temperature
  		EasyReader reader = new EasyReader();
	  	int temp = reader.readInt("Please enter a temperature: ");

		  //Translate it
		  if( temp>=101 && temp<=120)
	  			System.out.println("Dripping");
		  if(temp>=91 && temp<=100)
			  	System.out.println("Sweating");
  		  if (temp>=81 && temp<=90 )
		  		System.out.println("Perspiring");
		  if (temp>=71 && temp<=80) 
				  System.out.println("Glowing");
  		  if (temp>=40 && temp<=70) 
		  		System.out.println("Gleaming");
		  else
				  System.out.println("What? Impossible");
    }
}