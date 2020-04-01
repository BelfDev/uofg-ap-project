/**
 * This interface dictates which methods views should implement
 * in order to be considered a card receiver and take advantage
 * of abstracted methods throughout the code base.
 */
public interface CardReceiver {
    int getNumberOfCards();

    void addCard(CardView card);

    void removeCards();

    void setScore(int value);
}
