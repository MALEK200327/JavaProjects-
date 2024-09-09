public class Pythagoras{
	public static void main ( String [] args) {
		double a = 3.0;
		double b = 4.0;
		double sidea = Math.pow(a,2.0);
		double sideb = Math.pow(b,2.0);
		double hypotenuse = sidea + sideb;
		System.out.println("The square of the hypotenuse of a right angled triangle whose other sides are" + a + "and" + b + " is " + hypotenuse);
	}
}