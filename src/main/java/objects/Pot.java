
package objects;

import java.util.HashSet;
import java.util.Set;

public class Pot {
    private int amount;
    private final Set<Player> players;

    public Pot() {
        amount = 0;
        players = new HashSet<>();
    }

    public void addChips(int contributionAmount, Player contributor) {
        amount += contributionAmount;
        players.add(contributor);
    }
}