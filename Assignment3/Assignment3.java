import sheffield.*;

public class Assignment3 {

   public static void main(String [] args)  {
      EasyReader keyboard = new EasyReader();
      do  {
        new Cluedo().play(keyboard);
      }
      while  (   keyboard.readBoolean("Play again? ")   );
   }

}
