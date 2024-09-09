import sheffield.*;
public class Cluedo  {

   public Cluedo() {
      Weapon.initializeThem();
      Suspect.initializeEveryone();
      Room.listThem();
   }

   public void play(EasyReader keyboard) {

     Scenario secret = new Scenario();
     secret.setAtRandom();
     if  (  secret.isUnknown()  )  return;

     Scenario guess = new Scenario();
     guess.setByAsking(keyboard);
     if  (  guess.isUnknown()  )  return;

     while (  !  secret.hasBeenDiscovered(guess)  )
          guess.askAboutWrongOnes(secret,keyboard);

     System.out.println("Well done!");
     System.out.println(secret);

   }

}
