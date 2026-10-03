package game;

import java.util.ArrayList;
import java.util.List;

import objects.Action;
import objects.Card;
import objects.GameState;
import objects.Player;

//real implementation of the Leduc Hold'em rules

public class LeducRules implements Rules {

    //raise size is fixed per round: smaller preflop, bigger postflop
    private static final int RAISE_AMOUNT_PRETURN = 2;
    private static final int RAISE_AMOUNT_POSTTURN = 4;

    @Override
    public List<Action> getLegalActions(GameState state) {
        Player current = state.getCurrentPlayer();
        int highestBet = highestActiveBet(state);
        int toCall = highestBet - current.getCurrentBet();
        List<Action> actions = new ArrayList<>();

        actions.add(Action.FOLD);

        if (toCall == 0) {
            //no one has bet more than us yet, we can just pass
            actions.add(Action.CHECK);
        } else {
            actions.add(Action.CALL);
        }

        int raiseAmount = getRaiseAmount(state);
        if (current.getChips() >= toCall + raiseAmount) {
            actions.add(Action.RAISE);
        }

        return actions;
    }

    @Override
    public void applyAction(GameState state, Action action) {
        Player current = state.getCurrentPlayer();
        int highestBet = highestActiveBet(state);
        int toCall = highestBet - current.getCurrentBet();

        switch (action) {
            case FOLD:
                current.fold();
                state.incrementActionsThisRound();
                break;

            case CHECK:
                state.incrementActionsThisRound();
                break;

            case CALL:
            // Cap at what the player actually has this puts them all-in if they can't fully match the bet. Side pots not yet handled (this player may end up "owed" less than a full share on a tie)
            int actualCall = Math.min(toCall, current.getChips());
            current.bet(actualCall);
            state.addToPot(current, actualCall);
            state.incrementActionsThisRound();
            break;

            case RAISE:
                int raiseAmount = getRaiseAmount(state);
                int totalToPutIn = toCall + raiseAmount;
                current.bet(totalToPutIn);
                state.addToPot(current, totalToPutIn);
                //as there are 3-players, raise means everyone else needs to react again,so we restart the action counter for this round
                state.resetActionsThisRound();
                state.incrementActionsThisRound(); //counts the raiser own action
                break;
        }

        if (!state.onlyOnePlayerLeft()) {
            state.advanceToNextPlayer();
        }
    }

    @Override
    public boolean isBettingRoundOver(GameState state) {
        if (state.onlyOnePlayerLeft()) {
            return true;
        }
        boolean everyoneActed = state.getActionsThisRound() >= countActivePlayers(state);
        boolean betsMatched = allActiveBetsEqual(state);
        return everyoneActed && betsMatched;
    }

    @Override
    public Player determineWinner(GameState state) {
        Card community = state.getCommunityCard();
        Player best = null;
        int bestScore = -1;

        for (Player p : state.getPlayers()) {
            if (p.isFolded()) {
                continue;
            }
            int score = handStrength(p, community);
            if (score > bestScore) {
                bestScore = score;
                best = p;
            }
        }
        //NOTE: if two players tie exactly, this picks the first one found, Leduc rules would split the pot on a tie (not handled yet)
        return best;
    }

    //a pair always beats high card
    //we encode "pair" as a big number plus the rank, so it always outranks any non-pair hand, and use the plain rank value when there's no pair
    private int handStrength(Player player, Card community) {
        Card hole = player.getHand().getCards().get(0);
        boolean isPair = hole.getRank() == community.getRank();
        if (isPair) {
            return 1000 + hole.getRank().getValue();
        }
        return hole.getRank().getValue();
    }

    private int getRaiseAmount(GameState state) {
        return state.getRound() == GameState.Round.PRETURN
                ? RAISE_AMOUNT_PRETURN
                : RAISE_AMOUNT_POSTTURN;
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

    private int countActivePlayers(GameState state) {
        int count = 0;
        for (Player p : state.getPlayers()) {
            if (!p.isFolded()) {
                count++;
            }
        }
        return count;
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
