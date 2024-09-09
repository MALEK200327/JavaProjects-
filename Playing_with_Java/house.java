import sheffield.*;
public class house {
  public static void main(String[] args) {
    final double THETA= Math.PI/3.0;
    EasyReader keyboard = new EasyReader();
    int sideLen = keyboard.
          readInt("Enter the side length: ");
    EasyGraphics g = new EasyGraphics();
    g.moveTo(10,10);
    g.lineTo(
      10+(int)Math.round(sideLen*Math.cos(THETA)),
      10+(int)Math.round(sideLen*Math.sin(THETA)));
    g.lineTo(10+sideLen,10);
    g.lineTo(10,10);
  }
}
 
