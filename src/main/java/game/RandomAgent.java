package game;

import objects.Action;
import objects.GameState;

import java.util.List;
import java.util.Random;

/**
 * chooses an action for the bot so more advanced bots can be added later
 * now used as a placeholder
 */
public class RandomAgent implements PlayerAgent {

    private final Random random = new Random();

    @Override
    public Action chooseAction(GameState state, List<Action> legalActions) {
        int index = random.nextInt(legalActions.size());
        return legalActions.get(index);
    }
}
