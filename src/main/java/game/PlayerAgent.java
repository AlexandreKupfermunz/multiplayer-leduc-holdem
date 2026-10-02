package game;

import objects.Action;
import objects.GameState;

import java.util.List;

/**
 * interface that lets different types of agents (human, bot, etc) 
 * choose an action using the same method
 */
public interface PlayerAgent {
    Action chooseAction(GameState state, List<Action> legalActions);
}
