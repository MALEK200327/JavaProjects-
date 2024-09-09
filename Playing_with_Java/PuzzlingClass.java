public class PuzzlingClass {
  private static int[] nums = {1,2,3,4,5,6};
  public static int[] methodWithoutASensibleName(int[] a){
    int[] b = new int[a.length];
    int pos;
    for(int i=0; i<a.length; i++){
      pos = (a.length-1)-i;
      b[pos] = a[i];
    }
    return b;
  }

  public static void main(String[] args){
    int[] b = new int[nums.length];
    b = methodWithoutASensibleName(nums);
    System.out.println(b[2]);
    Thing green = new Thing();
Thing hairy = new Thing(); 
Thing slimy = new Thing(); 
green.setLegs(3);
hairy.setLegs(5);
Thing peculiar = hairy.copy();
Thing strange = slimy;
peculiar.setLegs(4);
strange.setLegs(green.getLegs()+hairy.getLegs());
System.out.println(strange.getLegs()+slimy.getLegs());
  }
}