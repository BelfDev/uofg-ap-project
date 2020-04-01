import java.util.Collections;
import java.util.Stack;

/**
 * This class provides methods to fabricate playing cards decks.
 * It uses all traditional SuitMarks and card values.
 */
public class PlayingCardFactory {

    private PlayingCardFactory() {
        // Private constructor to enforce static call
    }

    /**
     * Creates a playing cards deck with 52 cards.
     *
     * @return a deck with 52 cards.
     */
    public static Stack<PlayingCard> getPlayingCardsDeck() {
        Stack<PlayingCard> deck = new Stack<>();
        for (SuitMark suit : SuitMark.values()) {
            deck.add(new PlayingCard(suit, "a"));
            for (int value = 2; value <= 10; value++) {
                PlayingCard card = new PlayingCard(suit, String.valueOf(value));
                deck.add(card);
            }
            deck.add(new PlayingCard(suit, "j"));
            deck.add(new PlayingCard(suit, "q"));
            deck.add(new PlayingCard(suit, "k"));
        }
        Collections.shuffle(deck);
        return deck;
    }

}
