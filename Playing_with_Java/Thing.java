public class Thing { 

   private static int count = 0; 

   public Thing(int n) { count+=n; } 

   public static int getCount() { return count; } 

   public static void main(String[] args) { 
      Thing test1 = new Thing(2); 
      Thing test2 = new Thing(3); 
      Test test3 = new Test(4); 
      System.out.println(Test.getCount()); 
   } 

}