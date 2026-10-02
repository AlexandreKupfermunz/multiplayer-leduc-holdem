package game;

import objects.Action;
import objects.Card;
import objects.GameState;
import objects.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * temporary placeholder for the rules implementation (to be able to run and test GameFlow)
 * it does not use real leduc holdem rules
 * replace this line in main to the actual rules implementation:
 * Rules rules = new PlaceholderRules();
 */
public class PlaceholderRules implements Rules {

    private static final int RAISE_AMOUNT = 2; // arbitrary placeholder, not the real Leduc sizing

    @Override
    public List<Action> getLegalActions(GameState state) {
        Player current = state.getCurrentPlayer();
        int highestBet = highestActiveBet(state);
        List<Action> actions = new ArrayList<>();

        actions.add(Action.FOLD);
        if (current.getCurrentBet() == highestBet) {
            actions.add(Action.CHECK);
        } else {
            actions.add(Action.CALL);
        }
        if (current.getChips() >= RAISE_AMOUNT) {
            actions.add(Action.RAISE);
        }
        return actions;
    }

    @Override
    public void applyAction(GameState state, Action action) {
        Player current = state.getCurrentPlayer();
        int highestBet = highestActiveBet(state);

        switch (action) {
            case FOLD:
                current.fold();
                break;
            case CHECK:
                break; // no chips move
            case CALL:
                current.bet(highestBet - current.getCurrentBet());
                state.addToPot(current, highestBet - current.getCurrentBet());
            case RAISE:
                int toPutIn = (highestBet - current.getCurrentBet()) + RAISE_AMOUNT;
                current.bet(toPutIn);
                state.addToPot(current, toPutIn);
                break;
        }

        state.incrementActionsThisRound();
        if (!state.onlyOnePlayerLeft()) {
            state.advanceToNextPlayer();
        }
    }

    @Override
    public boolean isBettingRoundOver(GameState state) {
        if (state.onlyOnePlayerLeft()) {
            return true;
        }
        boolean everyoneActed = state.getActionsThisRound() >= state.getPlayers().size();
        boolean betsMatched = allActiveBetsEqual(state);
        return everyoneActed && betsMatched;
    }

    @Override
    public Player determineWinner(GameState state) {
        // Placeholder: highest hole-card rank wins, ignoring pairs with the
        // community card entirely. The real rules must check for pairs first.
        Player best = null;
        int bestValue = -1;
        for (Player p : state.getPlayers()) {
            if (p.isFolded()) {
                continue;
            }
            List<Card> cards = p.getHand().getCards();
            if (cards.isEmpty()) {
                continue;
            }
            int value = cards.get(0).getRank().getValue();
            if (value > bestValue) {
                bestValue = value;
                best = p;
            }
        }
        return best;
    }

    private int highestActiveBet(GameState state) {
        int highest = 0;
        for (Player p : state.getPlayers()) {
            if (!p.isFolded()) {
                highest = Math.max(highest, p.getCurrentBet());
            }
        }
        return highest;
    }

    private boolean allActiveBetsEqual(GameState state) {
        int reference = -1;
        for (Player p : state.getPlayers()) {
            if (p.isFolded()) {
                continue;
            }
            if (reference == -1) {
                reference = p.getCurrentBet();
            } else if (p.getCurrentBet() != reference) {
                return false;
            }
        }
        return true;
    }
}
