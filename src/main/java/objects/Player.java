package objects;

public class Player {
    private final String name;
    private int chips;
    private Hand hand;
    private int currentBet;
    private boolean folded;
    private boolean active;

    public Player(String name, int startingChips) {
        this.name = name;
        this.chips = startingChips;
        this.hand = new Hand();
        this.currentBet = 0;
        this.folded = false;
        this.active = true;
    }

    public String getName() { return name; }
    public int getChips() { return chips; }
    public Hand getHand() { return hand; }
    public int getCurrentBet() { return currentBet; }
    public boolean isFolded() { return folded; }
    public boolean isActive() { return active; }

    public void bet(int amount) {
        if (amount < 0) { throw new IllegalArgumentException("Cannot bet negative amount."); }
        if (amount > chips) {
            throw new IllegalArgumentException("Cannot bet more chips than available.");
        }
        chips -= amount;
        currentBet += amount;
    }

    public void fold() {
        folded = true;
    }

    public void addChips(int amount) {
        if (amount < 0) { throw new IllegalArgumentException("Cannot add negative chips."); }
        chips += amount;
    }

    public void resetForNewHand() {
        hand = new Hand();
        currentBet = 0;
        folded = false;
        active = true;
    }
}
