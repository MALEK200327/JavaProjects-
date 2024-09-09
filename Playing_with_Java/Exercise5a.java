import sheffield.*;
public class Exercise5a {
   public static void main(String[] args) {
      EasyReader keyboard = new EasyReader();
      double[] random = new double[10];
      for (int i=0; i<10;i++)
      	  random[i]= Math.random();
      for (int i=0; i<10;i++)
      	  System.out.println(i+" : "+random[i]);
      int index = keyboard.readInt("Please enter an array index: ");
      while ( index>=0 && index<10) {
      	  System.out.println("The value at index "+index+" is "+random[index]);
      	  index = keyboard.readInt("Please enter an array index: ");
      }
      System.out.println("Goodbye");
   }
}