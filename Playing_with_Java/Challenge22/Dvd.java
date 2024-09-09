public class Dvd {
	//The possible Genres
	enum Genre {ACTION, COMEDY, DRAMA, DOCUMENTARY, SCIENCE_FICTION};
	
	//static constants, variables and methods
	private static final double DEFAULT_PRICE=5;
	private static final double MINIMUM_PRICE=0.01;
	
	private static int counter = 0;
	
	public static int getCount() {  
		return counter;
	}
	
	//The instance variable
	private String title;
	private double price;
	private int id; 
	private Genre genre; 
		
	//The methods
	
	//private methods
	private boolean isValidPrice(double p) {
		return p >= MINIMUM_PRICE;
	}
	
	//Constructors	
	public Dvd() {
		this("UNKNOWN", DEFAULT_PRICE, 0, null); 		
	}
	
	public Dvd(String t, double p, int i, Genre g) { 
		title = t;
		if  (  isValidPrice(p)  )
			price = p;
		else 
			price = DEFAULT_PRICE;
		genre = g;
		id = i;
		counter++;
		id = counter;
	}
	
	//Accessors and mutators
	public String getTitle() {  return title;   }
	
	public void setTitle(String t) { title = t;  }
	
	public double getPrice() {  return price;	}
	
	public void setPrice(double p) {
		if  (  isValidPrice(p)  )
			price = p;
		else 
			price = DEFAULT_PRICE;
	}
	
	public int getID() {  return id;	}
	
	public void setID(int i) { 	id = i; 	}
	
	public Genre getGenre() { return genre; 	}
	
	public void setGenre(Genre g) { genre = g;  }
	
	//The toString method
	public String toString() {
		if  (  genre == null  )
			return title+", ID="+id+", price="+price+", genre is unknown";
		else 
			return title+", ID="+id+", price="+price+", genre="+genre;
	}
	
	//The main method
	public static void main (String args[]) {
		Dvd pussInBoots = new Dvd("Puss in Boots", 7.00, 2133839, Genre.COMEDY);
		System.out.println(pussInBoots);
		Dvd dummy = new Dvd();
		System.out.println(dummy);
		System.out.println(Dvd.getCount()+" DVDs have been created");
	}
}