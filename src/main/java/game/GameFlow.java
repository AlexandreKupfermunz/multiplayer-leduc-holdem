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
    private final AnteSchedule anteSchedule;
    private int handsPlayed;

    public GameFlow(GameState state, Rules rules, PlayerAgent[] agents) {
        public GameFlow(GameState state, Rules rules, PlayerAgent[] agents) {
        this(state, rules, agents, AnteSchedule.constant(1));
    }
    
    public GameFlow(GameState state, Rules rules, PlayerAgent[] agents, AnteSchedule anteSchedule) {
        if (agents.length != state.getPlayers().size()) {
            throw new IllegalArgumentException("Need exactly one agent per player.");
        }
        this.state = state;
        this.rules = rules;
        this.agents = agents;
        this.anteSchedule = anteSchedule;
        this.handsPlayed = 0;
    }

    // plays a single hand: deal -> bet -> reveal -> bet -> showdown -> payout
    public void playHand() {
        handsPlayed++;
        state.setAnte(anteSchedule.getAnte(handsPlayed));

        state.resetForNewHand();
        for (Player p : state.getPlayers()) {
            if (p.getChips() < ANTE) p.fold();
            if (p.getChips() < state.getAnte()) p.fold();
        }
        state.skipFoldedPlayers();
        System.out.println("Ante: " + state.getAnte() + ", first to act: " + state.getCurrentPlayer().getName());

        collectAntes();;
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
            if (playersWhoCanPlay() < 2) {
                System.out.println("Not enough players with chips left. Game over");
                return;
            }
            System.out.println("Hand " + (i + 1));
            System.out.println("Hand " + (handsPlayed + 1));
            playHand();
            System.out.println();
        }
    }

    private int playersWhoCanPlay() {
        int nextAnte = anteSchedule.getAnte(handsPlayed + 1);
        int n = 0;
        for (Player p : state.getPlayers()) {
            if (p.getChips() >= ANTE) n++;
            if (p.getChips() >= nextAnte) n++;
        }
        return n;
    }

    // the steps

    private void collectAntes() {
        for (Player p : state.getPlayers()) {
            if (p.isFolded()) continue;
            p.bet(ANTE);
            state.addToPot(p, ANTE);
            p.bet(state.getAnte());
            state.addToPot(p, state.getAnte());
        }
    }

    private void dealHoleCards() {
        for (Player p : state.getPlayers()) {
            if (p.isFolded()) continue;
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
                if (p.isFolded()) continue; // use if (p.getHands().getCards().isEmpty()) continue; for debugging since folded cards will be visible then
                System.out.println(p.getName() + " had " + p.getHand().getCards());
            }
            winner = rules.determineWinner(state);
            System.out.println(winner.getName() + " wins showdown, pot = " + state.getPotTotal());
        }
        winner.addChips(state.getPotTotal());
    }
}
