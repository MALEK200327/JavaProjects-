import sheffield.*; 
public class Indent { 
	public static void main(String[] arg) { 
		
		//The numbers to be output
		double x = 2.184918284982; 
		double y = 127.318291823; 
    
		//Prepare the file for output
		EasyWriter file = new EasyWriter("output.txt"); 
		
		//Printing them in various ways	
		//      same as System.out.println(x)   
		file.println(x);     
		//      show three decimal places     
		file.println(x,3); 		
		//      show five decimal places in a field of 10 spaces 
		file.println(x,5,10); 
		file.println(y,5,10);
		
		//Confirm the program has worked
		EasyWriter screen = new EasyWriter();
		screen.println("The data has been written to the file" + yu);
		
   	} 
} 