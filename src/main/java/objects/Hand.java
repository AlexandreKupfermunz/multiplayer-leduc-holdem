package objects;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Separate container for hands, as it might get tricky with multiple players
 */
public class Hand {
    private final List<Card> cards;

    public Hand() {
        cards = new ArrayList<>();
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards); //this means that others can only view, but not modify the hand
    }

    public void clear() {
        cards.clear();
    }
}
