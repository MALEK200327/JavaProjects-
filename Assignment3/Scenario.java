import sheffield.*;

public class Scenario {

   private Suspect attacker;
   private Room attackedIn;
   private Weapon attackedWith;

   public void setAtRandom() {
      attackedWith = Weapon.pickedAtRandom();
      attacker = Suspect.pickedAtRandom();
      attackedIn = Room.pickedAtRandom();
   }

   public void setByAsking(EasyReader keyboard) {
      attackedIn = Room.askWhichOne(keyboard);
      attacker = Suspect.askWho(keyboard);
      attackedWith = Weapon.askWhichOne(keyboard);
   }

   public void askAboutWrongOnes(Scenario rightOne, EasyReader keyboard) {
      if (attackedIn != rightOne.attackedIn)
         attackedIn = Room.askWhichOne(keyboard);
      if (attacker != rightOne.attacker)
         attacker = Suspect.askWho(keyboard);
      if (attackedWith != rightOne.attackedWith)
         attackedWith = Weapon.askWhichOne(keyboard);
   }

   public boolean hasBeenDiscovered(Scenario guess) {
      if (attackedIn == guess.attackedIn && attacker == guess.attacker
            && attackedWith == guess.attackedWith) {
         return true;
      } else {
         String roomHint = " ";
         String weaponHint = " ";
         String attackerHint = " ";
         if (attackedIn != guess.attackedIn)
            roomHint = String.format("in the %s", guess.attackedIn);
         if (attacker != guess.attacker)
            attackerHint = String.format(", by %s", guess.attacker);
         if (attackedWith != guess.attackedWith)
            weaponHint = String.format(",with %s", guess.attackedWith);
            

         String hint = String.format("The victim was not attacked %s %s  %s", roomHint, attackerHint, weaponHint);
         System.out.println(hint);
         return false;
      }
   }

   public boolean isUnknown() {
      return attacker == null || attackedIn == null || attackedWith == null;
   }

   public String toString() {
      return "The victim was attacked in " + attackedIn + " by " +
            attacker + " with " + attackedWith;
   }

}
