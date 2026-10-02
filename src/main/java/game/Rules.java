package game;

import objects.Action;
import objects.GameState;
import objects.Player;

import java.util.List;
/**
 * interface for the rules, does not include the logic
 */

public interface Rules {

    List<Action> getLegalActions(GameState state);

    void applyAction(GameState state, Action action);

    boolean isBettingRoundOver(GameState state);

    Player determineWinner(GameState state);
}
