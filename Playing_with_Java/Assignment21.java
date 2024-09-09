import sheffield.*;
public class Assignment21 {
	public static void main(String [] args) {
		EasyReader dataFile = new EasyReader("treee.txt");
		EasyGraphics g = new EasyGraphics(120,128);
		g.setColor(0,0,128);
		g.fillRectangle(0, 0, 128, 120);
		g.setColor(0,128,0);
		g.fillRectangle(0, 0, 128, 40);
		String data = dataFile.readString();
		char[] dataArray = new char[data.length()];
		int x = 0;
		int y = 127;
		for (int i = 0; i < data.length(); i++) {
			dataArray[i] = data.charAt(i);
		}
		for ( char c : dataArray) {
			if (x>127) {
				x = 0;
				y=y-1;
			}
		}
	}
}
		
		
		