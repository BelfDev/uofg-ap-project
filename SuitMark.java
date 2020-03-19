public enum SuitMark {
    DIAMONDS, CLUBS, HEARTS, SPADES;

    public String getFirstLetter() {
        return String.valueOf(this.toString().charAt(0));
    }
}
