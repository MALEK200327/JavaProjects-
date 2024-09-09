
import sheffield.*;

public class Suspect {

  private final static int MAX_NUMBER = 10;

  private static Suspect[] allTheSuspects;
  private static int numberOfSuspects;

  public static void initializeEveryone() {
    EasyReader file = new EasyReader("Assignment3/suspects.txt");
    allTheSuspects = new Suspect[MAX_NUMBER];
    numberOfSuspects = 0;
    while (!file.eof() && (numberOfSuspects < MAX_NUMBER)) {
      allTheSuspects[numberOfSuspects] = new Suspect(file.readString());
      numberOfSuspects++;
    }
    listTheSuspects();
    // file.close();
  }

  public static Suspect askWho(EasyReader keyboard) {
    String input = keyboard.readString("Who do you accuse?");
    if (input == null)
      return null;

    String output = input.toUpperCase();
    if (output.indexOf("THE ") > -1)
      output = output.substring(4);
    for (int i = 0; i < allTheSuspects.length; i++) {
      if (allTheSuspects[i].name.equalsIgnoreCase(output) == true) {
        return allTheSuspects[i];
      }
    }

    return null;
  }

  public static void listTheSuspects() {
    System.out.print("The suspects are " + allTheSuspects[0]);
    for (int i = 1; i < numberOfSuspects - 1; i++)
      System.out.print(", " + allTheSuspects[i]);
    System.out.println(" and " + allTheSuspects[numberOfSuspects - 1]);
  }

  public static Suspect pickedAtRandom() {
    int random = (int) (Math.random() * numberOfSuspects);
    return allTheSuspects[random];
  }

  private String name;

  private Suspect(String n) {
    name = n;
  }

  public String toString() {
    return name;
  }

}
