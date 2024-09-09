import sheffield.*;
public class Kilo { 
	public static void main(String[] arg) {
		final double MTOY = 1760;
		final double MTOKM = 1.60934;
		EasyReader keyboard = new EasyReader();
		double mile = keyboard.readInt("Enter the distance in miles: "); 
		double yards = MTOY*mile;
		double km = MTOKM*mile;
		System.out.println( mile +" miles and " + yards + " yards is equivalent to " + km +"km");
	}
}
