import sheffield.*;

public class Exercise3d {
    public static  void main(String[] args) {

    	//Get the note
  		EasyReader reader = new EasyReader();
	  	char note = reader.readChar("Please enter a note name: ");

		  //Translate it
		  switch(note.toUpperCase()) {
  			case 'C' ->
	  			System.out.println("Doh");
		  	case 'D' ->
			  	System.out.println("Ray");
  			case 'E' ->
		  		System.out.println("Me");
			  case 'F' ->
				  System.out.println("Fah");
  			case 'G' ->
		  		System.out.println("Soh");
			  case 'A' ->
				  System.out.println("La");
  			case 'B' ->
		  		System.out.println("Ti");
			  default ->
				  System.out.println("What? That is not a note");
		  }
    }
}


