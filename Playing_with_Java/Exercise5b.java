import sheffield.*;
public class Exercise5b {
	public static void main(String[] args) {
		EasyReader keyboard = new EasyReader();
		int[] randomv = new int[10];
		for(int i=0;i<10; i++)
			 randomv[i] = keyboard.readInt("please enter a value");
		 
	}
}