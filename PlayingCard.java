/**
 * This class represents the PlayingCard model. It contains
 * the card's suit, value, and asset name.
 */
public class PlayingCard {
    private final SuitMark suit;
    private final String value;
    private final String assetName;

    /**
     * Constructs a PlayingCard model.
     *
     * @param suit  the card suit.
     * @param value the card value.
     */
    public PlayingCard(SuitMark suit, String value) {
        this.suit = suit;
        this.value = value;
        this.assetName = String.format("card_$s_$s_ic", value, suit.getFirstLetter());
    }

    public SuitMark getSuit() {
        return suit;
    }

    public String getValue() {
        return value;
    }

    public String getAssetName() {
        return assetName;
    }

    @Override
    public String toString() {
        return "PlayingCard{" +
                "suit=" + suit +
                ", value='" + value + '\'' +
                ", assetName='" + assetName + '\'' +
                '}';
    }
}
