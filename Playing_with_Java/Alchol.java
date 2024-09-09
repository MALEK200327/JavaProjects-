import sheffield.*; 
public class Alchol {
	public static void main (String [] args) {
		EasyReader fileInput = new EasyReader("tree.txt");
		EasyGraphics Window = new EasyGraphics(120,128*3);
// creating a 2 dimentional array of 128 Rows and 120 column as integer
		int[][] integer = new int[128][120];
// Making a for loop to read each charcter in the text file aand change it to numeric value
		for (int x=0;x<128;x++){
			for (int y=0;y<120;y++){
				integer [x] [y] = (int) fileInput.readChar();
			}
		}
// For loop to loop 128  making the summer season
		for (int y=0;y<128;y++){
			for (int x=0;x<120;x++){
// if condition checks if y and x are odd to make tree
				if ((integer[y][x]%2) == 1) {
					Window.setColor(0,57,0);
					Window.plot(x,3*128-(y+1)); 
				}
// if condition checks if y and x are even to make the background
				if ((integer[y][x]%2) == 0){
					Window.setColor(135, 206, 235);		
					Window.plot(x,3*128-(y+1));
				}
//if condition checks if y and x are divisible by 5 to make the trunk of the tree
				else if ((integer [y] [x] % 5) == 0){
					Window.setColor(79,51,11);
					Window.plot(x,3*128-(y+1));
			}}
		}
		
//making the grass	
		for (int y=84;y<128;y++){
			for (int x=0;x<120;x++){
				if((integer[y][x]%2) == 0){
					Window.setColor(0,154,23);			
					Window.plot(x,(3*128-(y+1)));
				}
		}	
		}
//Autumn season Almost same as the summer season but color difference
			
		for (int y=0;y<128;y++){
			for (int x=0;x<120;x++){
				if ((integer[y][x]%2) == 1) {
					Window.setColor(147,69,37);
					Window.plot(x,128*2-(y+1)); 
				}
				if ((integer[y][x]%2) == 0){
					Window.setColor(135, 206, 235);			
					Window.plot(x,128*2-(y+1));
				}
				else if ((integer [y] [x] % 5) == 0){
					Window.setColor(79,51,11);
					Window.plot(x,128*2-(y+1));
			}}
		}
		for (int y=84;y<128;y++){
			for (int x=0;x<120;x++){
				if((integer[y][x]%2) == 0){
					Window.setColor(0,154,23);			
					Window.plot(x,(128*2-(y+1)));
				}
		}	
		}
// Spring season
		for (int y=0;y<128;y++){
			for (int x=0;x<120;x++){
				if ((integer[y][x]%2) == 1) {
					Window.setColor(135, 206, 235);	
					Window.plot(x,128-(y+1)); 
				}
// this new line to make the tree look with no leaves 
				if ((integer[y][x]%7) == 0) {
					Window.setColor(147,69,37);
					Window.plot(x,128-(y+1)); 
				}
				if ((integer[y][x]%2) == 0){
					Window.setColor(135, 206, 235);			
					Window.plot(x,128-(y+1));
				}
				else if ((integer [y] [x] % 5) == 0){
					Window.setColor(79,51,11);
					Window.plot(x,128-(y+1));
			}}
		}
// this is to show that leaves fall on the ground 
		for (int y=84;y<128;y++){
			for (int x=0;x<120;x++){
				if((integer[y][x]%2) == 0){
					Window.setColor(97,57,7);	
					Window.plot(x,(128-(y+1)));
				}
				if((integer[y][x]%7) == 0){
					Window.setColor(15,115,5);	
					Window.plot(x,(128-(y+1)));
				}
				
		}	
		}
		
	
	
	}
		
		}

	