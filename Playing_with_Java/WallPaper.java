import sheffield.*;
public class WallPaper { 
	public static void main(String[] arg) {
		EasyReader keyboard = new EasyReader();
        int length = keyboard.readInt("Enter the length: "); 
        int width = keyboard.readInt("Enter the width: "); 
        int height = keyboard.readInt("Enter the height: ");
        System.out.println("Your room needs " + length*width + " square metres of carpet and"); 
		System.out.println(height*2*(length+width) + " square metres of wallpaper");
	}
}