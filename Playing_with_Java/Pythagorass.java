import javax.swing.JOptionPane;
public class Pythagorass{
	public static void main ( String [] args) {
		int side1 = Integer.valueOf(
			JOptionPane.showInputDialog("Enter the first side of the triangle"));
		int side2 = Integer.valueOf(
			JOptionPane.showInputDialog("Enter the second side of the triangle"));	
		double sidea = Math.pow(side1,2.0);
		double sideb = Math.pow(side2,2.0);
		double hypotenuse = sidea + sideb;
		System.out.println("The square of the hypotenuse of a right angled triangle whose other sides are" +  side1  + " and " +  side2  + " is " + hypotenuse);
	}
}