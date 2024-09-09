import sheffield.*;

public class Weapon {

  private final static int MAX_NUMBER = 6;
  private static Weapon[] allTheWeapons;

  public static void initializeThem() {
    EasyReader file = new EasyReader("Assignment3/weapons.txt");
    allTheWeapons = new Weapon[MAX_NUMBER];
    int numWeapons = 0;
    while (!file.eof() && (numWeapons < MAX_NUMBER)) {
      allTheWeapons[numWeapons] = new Weapon(file.readString());
      numWeapons++;
    }
    listTheWeapons();
  }

  public static Weapon askWhichOne(EasyReader keyboard) {
    String input = keyboard.readString("With what weapon?");
    if (input == null)
      return null;
    String output = input.toUpperCase();
    if (output.indexOf("THE ") > -1)
      output = output.substring(4);
    for (int i = 0; i < allTheWeapons.length; i++) {
      if (allTheWeapons[i].weapon.equalsIgnoreCase(output) == true) {
        return allTheWeapons[i];
      }
    }

    return null;
  }

  public static void listTheWeapons() {
    System.out.print("The weapons are " + allTheWeapons[0]);
    for (int i = 1; i < MAX_NUMBER - 1; i++)
      System.out.print(", " + allTheWeapons[i]);
    System.out.println(" and " + allTheWeapons[MAX_NUMBER - 1]);
  }

  public static Weapon pickedAtRandom() {
    int random = (int) (Math.random() * allTheWeapons.length);
    return allTheWeapons[random];
  }

  private String weapon;

  private Weapon(String n) {
    weapon = n;
  }

  public String toString() {
    return "the " + weapon.toLowerCase();
  }

}
