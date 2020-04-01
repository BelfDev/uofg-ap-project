/**
 * This enumeration represents all the suit marks available
 * in traditional playing cards.
 */
public enum SuitMark {
    DIAMONDS, CLUBS, HEARTS, SPADES;

    public String getFirstLetter() {
        return String.valueOf(this.toString().charAt(0)).toLowerCase();
    }
}
