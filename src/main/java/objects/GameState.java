package objects;

import java.util.List;

public class GameState {

    public enum Round {
        PRETURN, 
        POSTTURN, 
        SHOWDOWN
    }

    private final List<Player> players;
    private Deck deck;
    private final Pot pot;

    private Card communityCard;
    private int potTotal;
    private int currentPlayerIndex;
    private int firstPlayerIndex;
    private Round round;
    private int actionsThisRound;

    public GameState(List<Player> players) {
        this.players = players;
        this.deck = new Deck();
        this.pot = new Pot();
        this.firstPlayerIndex = 0;
    }

    public void resetForNewHand() {
        for (Player p : players) {
            p.resetForNewHand();
        }
        deck = new Deck();
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
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
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
}
