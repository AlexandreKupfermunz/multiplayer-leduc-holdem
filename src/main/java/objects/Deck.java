package objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    // adds all the cards to a deck, easy to scale if we change suits/number of cards
    public Deck() {
        cards = new ArrayList<>();
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
    }
     // basic actions for deck
    public void  shuffle() {
        Collections.shuffle(cards);
    }

    public Card deal() {
        if (cards.isEmpty()) {
            throw new IllegalStateException("Cannot deal from an empty deck.");
        }
        return cards.remove(cards.size() - 1);
    }

    public int remaining() {
        return cards.size();
    }
}
