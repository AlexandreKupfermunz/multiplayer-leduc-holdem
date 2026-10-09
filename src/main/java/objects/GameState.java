package objects;

import java.util.List;
import java.util.Random; 

public class GameState {

    public enum Round {
        PRETURN, 
        POSTTURN, 
        SHOWDOWN
    }

    private final List<Player> players;
    private Deck deck;
    private final Pot pot;
    private final int numRanks;
    private final int numSuits;

    private Card communityCard;
    private int potTotal;
    private int currentPlayerIndex;
    private int firstPlayerIndex;
    private Round round;
    private int actionsThisRound;
    private int ante;

    public GameState(List<Player> players, int numRanks, int numSuits) {
        this(players, numRanks, numSuits, new Random());
    }

    public GameState(List<Player> players, int numRanks, int numSuits, Random random) {
        this.players = players;
        this.numRanks = numRanks;
        this.numSuits = numSuits;
        this.deck = new Deck(numRanks, numSuits);
        this.pot = new Pot();
        this.firstPlayerIndex = 0;
        this.firstPlayerIndex = random.nextInt(players.size());
        this.ante = 1;
    }

    public void resetForNewHand() {
        for (Player p : players) {
            p.resetForNewHand();
        }
        deck = new Deck(numRanks, numSuits);
        deck.shuffle();
        communityCard = null;
        potTotal = 0;
        round = Round.PRETURN;
        currentPlayerIndex = firstPlayerIndex;
        actionsThisRound = 0;
    }

    // when moving from preturn to postturn -> counter starts over
    public void resetActionsThisRound() {
        actionsThisRound = 0;
    }

    public void incrementActionsThisRound() {
        actionsThisRound++;
    }

    public int getActionsThisRound() {
        return actionsThisRound;
    }

    public void rotateFirstPlayer() {
        firstPlayerIndex = (firstPlayerIndex + 1) % players.size();
    }

        public int getAnte() {
        return ante;
    }

        public void setAnte(int ante) {
        this.ante = ante;
    }

    // pot
    public void addToPot(Player contributor, int amount) {
        pot.addChips(amount, contributor);
        potTotal += amount;
    }

    public int getPotTotal() {
        return potTotal;
    }

    // turning
    public void advanceToNextPlayer() {
        do {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        } while (getCurrentPlayer().isFolded() && !onlyOnePlayerLeft());
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    // getters
    public List<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }

    public Card getCommunityCard() {
        return communityCard;
    }

    public Round getRound() {
        return round;
    }

    public Player getLastPlayerStanding() {
        for (Player p : players) {
            if (!p.isFolded()) {
                return p;
            }
        }
        return null;
    }

    // setters
    public void setCommunityCard(Card communityCard) {
        this.communityCard = communityCard;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public boolean onlyOnePlayerLeft() {
        int count = 0;
        for (Player p : players) {
            if (!p.isFolded()) {
                count++;
            }
        }
        return count <= 1;
    }

    public void skipFoldedPlayers() {
        while (getCurrentPlayer().isFolded()) {
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        }
    }
}