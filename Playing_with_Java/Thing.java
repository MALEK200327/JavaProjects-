public class Test { 

   private static int count = 0; 

   public Test(int n) { count+=n; } 

   public static int getCount() { return count; } 

   public static void main(String[] args) { 
      Test test1 = new Test(2); 
      Test test2 = new Test(3); 
      Test test3 = new Test(4); 
      System.out.println(Test.getCount()); 
   } 

}