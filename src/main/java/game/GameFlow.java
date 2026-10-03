package game;

import objects.Action;
import objects.Card;
import objects.GameState;
import objects.Player;

import java.util.List;

public class GameFlow {

    private static final int ANTE = 1;

    private final GameState state;
    private final Rules rules;
    private final PlayerAgent[] agents;

    public GameFlow(GameState state, Rules rules, PlayerAgent[] agents) {
        if (agents.length != state.getPlayers().size()) {
            throw new IllegalArgumentException("Need exactly one agent per player.");
        }
        this.state = state;
        this.rules = rules;
        this.agents = agents;
    }

    // plays a single hand: deal -> bet -> reveal -> bet -> showdown -> payout
    public void playHand() {
        state.resetForNewHand();

        collectAntes();
        dealHoleCards();

        runBettingRound();

        if (!state.onlyOnePlayerLeft()) {
            state.setCommunityCard(state.getDeck().deal());
            state.setRound(GameState.Round.POSTTURN);
            state.resetActionsThisRound();
            runBettingRound();
        }

        resolveShowdownAndPayout();
        state.rotateFirstPlayer();
    }

    public void playHands(int count) {
        for (int i = 0; i < count; i++) {
            System.out.println("=== Hand " + (i + 1) + " ===");
            playHand();
            System.out.println();
        }
    }

    // the steps

    private void collectAntes() {
        for (Player p : state.getPlayers()) {
            p.bet(ANTE);
            state.addToPot(p, ANTE);
        }
    }

    private void dealHoleCards() {
        for (Player p : state.getPlayers()) {
            p.getHand().addCard(state.getDeck().deal());
        }
    }

    private void runBettingRound() {
        while (!rules.isBettingRoundOver(state) && !state.onlyOnePlayerLeft()) {
            Player current = state.getCurrentPlayer();
            PlayerAgent agent = agents[state.getCurrentPlayerIndex()];

            List<Action> legalActions = rules.getLegalActions(state);
            Action chosen = agent.chooseAction(state, legalActions);

            System.out.println(current.getName() + " -> " + chosen);
            rules.applyAction(state, chosen);
        }
    }

    private void resolveShowdownAndPayout() {
        Player winner;
        if (state.onlyOnePlayerLeft()) {
            winner = state.getLastPlayerStanding();
            System.out.println(winner.getName() + " wins (opponent folded), pot = " + state.getPotTotal());
        } else {
            state.setRound(GameState.Round.SHOWDOWN);
            Card community = state.getCommunityCard();
            System.out.println("Community card: " + community);
            for (Player p : state.getPlayers()) {
                System.out.println(p.getName() + " had " + p.getHand().getCards());
            }
            winner = rules.determineWinner(state);
            System.out.println(winner.getName() + " wins showdown, pot = " + state.getPotTotal());
        }
        winner.addChips(state.getPotTotal());
    }
}
