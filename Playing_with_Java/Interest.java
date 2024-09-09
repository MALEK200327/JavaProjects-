import javax.swing.JOptionPane;
public class Interest {
	public static void main ( String [] args) {
		final double p = 0.4;
		double n = Integer.valueOf(
			JOptionPane.showInputDialog("Please enter the number of months to the loan"));
		double c = Integer.valueOf(
			JOptionPane.showInputDialog(" what is your loan amount? "));
		double m = c*p/1200*Math.pow(1+p/1200,n)/(Math.pow(1+p/1200,n)-1);
		System.out.println(" The monthly payment " + m + " for a loan on a principal sum of " + c + " pounds, paid back over " + n + " months at an annual interest rate of " + p + " percent ");
	}
}
		
		