public interface CardReceiver {
    int getNumberOfCards();
    void addCard(CardView card);
    void removeCards();
    void setScore(int value);
}