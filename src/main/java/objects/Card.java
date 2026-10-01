package objects;

import java.util.Objects;

public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public Rank getRank() {
        return rank;
    }
    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + "of" + suit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;
        Card other = (Card) o;
        return rank == other.rank && suit == other.suit;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rank, suit);
    }
}
