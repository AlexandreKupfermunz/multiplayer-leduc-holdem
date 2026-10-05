package objects;

/**
 * Names of cards in a suit and their corresponding value.
 * Not sure if ace should be high or low, but can be easily changed.
 * Includes a value getter for the cards.
 */
public enum Rank {
    JACK(11), QUEEN(12), KING(13), ACE(14);

    private final int value;

    Rank(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
