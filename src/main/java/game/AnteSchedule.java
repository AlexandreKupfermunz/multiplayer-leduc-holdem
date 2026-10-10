package game;

public class AnteSchedule {

    private final int[] levels; // example : [1, 2, 3] means ante is 1 for the first N hands, then 2 for the next N hands, then 3 for the next N hands, and then stays at 3 forever
    private final int increaseEveryNHands;

    public AnteSchedule(int[] levels, int increaseEveryNHands) {
        if (levels == null || levels.length == 0) {
            throw new IllegalArgumentException("Need at least one ante level.");
        }
        for (int i = 0; i < levels.length; i++) {
            if (levels[i] < 1) {
                throw new IllegalArgumentException("Every ante level must be at least 1.");
            }
            if (i > 0 && levels[i] < levels[i - 1]) {
                throw new IllegalArgumentException("Ante levels must not go down.");
            }
        }
        if (increaseEveryNHands < 1) {
            throw new IllegalArgumentException("The ante must increase every 1 hand or more.");
        }
        this.levels = levels.clone();
        this.increaseEveryNHands = increaseEveryNHands;
    }

    // a schedule where the ante never changes (only one level)
    public static AnteSchedule constant(int ante) {
        return new AnteSchedule(new int[] {ante}, 1);
    }

    // handNumber starts at 1 (the first hand of the game)
    public int getAnte(int handNumber) {
        if (handNumber < 1) {
            throw new IllegalArgumentException("Hand number starts at 1.");
        }
        int levelIndex = (handNumber - 1) / increaseEveryNHands;
        if (levelIndex >= levels.length) {
            levelIndex = levels.length - 1;
        }
        return levels[levelIndex];
    }
}
