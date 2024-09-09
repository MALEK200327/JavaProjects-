import sheffield.*;
public class Exercise4a{
	enum Suit {HEARTS, CLUBS, DIAMONDS, SPADES };
	public static void main(String[] args) {
		EasyReader keyboard = new EasyReader();
		Suit card = Suit.valueOf(keyboard.readString("Which suit? ").toUpperCase());
		switch (card) {
			case HEARTS,  DIAMONDS ->
				System.out.println("That is a red card");
			case CLUBS,  SPADES ->
				System.out.println("That is a black card");
		}

	}
}