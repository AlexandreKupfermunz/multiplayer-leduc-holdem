package objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    // adds all the cards to a deck, easy to scale if we change suits/number of cards
    public Deck(int numRanks, int numSuits) {
        Rank[] ranks = Rank.values();
        Suit[] suits = Suit.values();
        if (numRanks < 1 || numRanks > ranks.length || numSuits < 1 || numSuits > suits.length) {
            throw new IllegalArgumentException("Invalid deck size");
        }
        cards = new ArrayList<>();
        for (int s = 0; s < numSuits; s++) {
            for (int r = 0; r < numRanks; r++) {
                cards.add(new Card(ranks[r], suits[s]));
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
