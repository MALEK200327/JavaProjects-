package small_project;
import java.util.*;


public class ChangeTime {
      public static void miniMaxSum(String s) {
        int num1 = Integer.parseInt(s.substring(0, 2));
        int num2 = Integer.parseInt(s.substring(3, 5));
        int num3 = Integer.parseInt(s.substring(6, 8));
        String time = s.substring(s.length()-2);
        if("AM".equalsIgnoreCase(time) && num1 == 12) {
            num1 = 00;
            System.out.println(String.valueOf(num1)+":"+String.valueOf(num2)+":"+String.valueOf(num3));
        } else if ("PM".equalsIgnoreCase(time)&& num1==12) {
            num1 = 12;
            System.out.println(String.valueOf(num1)+":"+String.valueOf(num2)+":"+String.valueOf(num3));
        } else if ("PM".equalsIgnoreCase(time)){
            num1 = num1+12;
            System.out.println(String.valueOf(num1)+":"+String.valueOf(num2)+":"+String.valueOf(num3));
        } else {
            System.out.println(String.valueOf(num1)+":"+String.valueOf(num2)+":"+String.valueOf(num3));
        }

      }
      public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        miniMaxSum(s);
      }
}

