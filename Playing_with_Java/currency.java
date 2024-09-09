import sheffield.*;
public class Currency {
	public static void main ( String [] args) {
		EasyReader fileInput = new EasyReader("money1a.txt");
		double amount1 = fileInput.readDouble()*1.15;
		double amount2 = fileInput.readDouble()*1.15;
		double total = amount1+amount2;
		EasyWriter screen = new EasyWriter();
		screen.println(amount1,2);
		screen.println(amount2,2);
		screen.println(total,2);
	}
}