import sheffield.*;;

public class Challenge22 {

    public static void main(String[] args) {

        EasyReader input = new EasyReader();

        int counter = 1;
        double sum = 0;

        do {
            String num = input.readString("Enter a number");
            try {
//               int number = Integer.parseInt(num);
                double number = Double.parseDouble(num);
                counter++;
                sum += number;
            } catch (NumberFormatException nfe) {
                System.out.println("Invalid number");
            }
        } while (counter <= 5);

        System.out.println("The sum of the 5 numbers = " + sum);
    }
}
