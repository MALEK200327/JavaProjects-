
		//Now i declared a constant mass on jupiter compared to earth 
		final double JUPMASS = 2.53;
		//Now i declared a constant mass on mercury compared to earth 
		final double MERCMASS = 0.38;
		// I used the class EasyReader to create an input
	
		// I used the class EasyReader  an input of a text file
		
		/* I used the class EasyReader to read what is in the first line of 
		 text file called jupiter*/
		String jupiter = fileInput1.readString();
		/* I used the class EasyReader to read what is in the second line of 
		 text file called mercury*/
		String mercury = fileInput1.readString();
		// Input message and the user should enter it as an integer
		int weight1 = weight.readInt("Please type in a weight in pounds : ");
		// converting the weight from pounds to kg and make to 3 decimal place
		double weightinkg3 = Math.round(((double) weight1) * PTOKG*1000)/1000.0;
		/* converting the weight from kg on earth to kg on jupiter 
		and make to 4 decimal place*/
		double jupinkg4 = Math.round(((double) weight1)*JUPMASS* PTOKG*10000)/10000.0;
		// converting the weight from pounds to kg and make to 4 decimal place
		double weightinkg4 = Math.round(((double) weight1) * PTOKG*10000)/10000.0;
		// converting the weight from pounds to kg on mercury and make to 2 decimal place
		double mercinkg2 = Math.round(((double) weight1)*MERCMASS* PTOKG*100)/100.0;
		//converting the weight from pounds to kg on jupiter and make to 2 decimal place
		double jupinkg2 = Math.round(((double) weight1)*JUPMASS* PTOKG*100)/100.0;
		//converting the weight from pounds to kg on earth and make to 2 decimal place
		double weightinkg2 = Math.round(((double) weight1) * PTOKG*100)/100.0;
		// printing out the weight in Kg for the user
	    System.out.println("That is " + weightinkg3 + " kilograms " );
	    // Making a space between Task1 and Task2
	    System.out.println();
	    // printing the first line in the text file called planets.txt 
	    System.out.println( jupiter );
	    // printing 
	    System.out.println( weight1 + " pounds on Earth weighs " + weightinkg4 + 
	    	" kilograms " );
	    
	    System.out.println( weight1 + " pounds on Jupiter weighs " + jupinkg4 + 
	    	" kilograms " );
	    // Making a space between Task2 and Task3
	    System.out.println();
	    
	    System.out.println("Earth" + "          "  + weightinkg2);
	    
	    System.out.println("Jupiter" + "       " + jupinkg2 );
	    
	    System.out.println("Mercury" + "         " + mercinkg2 );
	    
	}
}