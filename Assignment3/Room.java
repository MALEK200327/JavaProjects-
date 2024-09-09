import sheffield.*;

public enum Room {

  KITCHEN,
  BALLROOM,
  CONSERVATORY,
  BILLIARD_ROOM,
  LIBRARY,
  STUDY,
  HALL,
  LOUNGE,
  DINING_ROOM;

  static int roomCount = Room.values().length;

  static Room askWhichOne(EasyReader keyboard) {
    String place = keyboard.readString("Where are you?");
    place = place.toUpperCase();
    if (place.indexOf("THE ") > -1)
      place = place.substring(4);
    place = place.replace(" ", "_");
    if (place == null)
      return null;
    else {
      return switch (place.toUpperCase()) {
        case "KITCHEN" -> KITCHEN;
        case "BALLROOM" -> BALLROOM;
        case "CONSERVATORY" -> CONSERVATORY;
        case "BILLIARD_ROOM" -> BILLIARD_ROOM;
        case "LIBRARY" -> LIBRARY;
        case "STUDY" -> STUDY;
        case "HALL" -> HALL;
        case "LOUNGE" -> LOUNGE;
        case "DINING_ROOM" -> DINING_ROOM;
        default -> null;
      };
    }
  }

  static Room pickedAtRandom() {
    int random = (int) (Math.random() * roomCount);
    return Room.values()[random];
  }

  public static void listThem() {
    System.out.print("The rooms are ");
    for (Room r : Room.values()) {
      System.out.print(String.format(" %s, ", r.toString()));
    }
    System.out.println(" ");
  }

  public String toString() {
    return "the " + name().toLowerCase().replace("_", " ");
  }

}
