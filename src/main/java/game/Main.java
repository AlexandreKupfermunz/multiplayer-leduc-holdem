package game;

import java.util.ArrayList;
import java.util.List;

import objects.GameState;
import objects.Player;

public class Main {
    public static void main(String[] args) {
        List<Player> players = new ArrayList<>();
        players.add(new Player("Alice", 20));
    players.add(new Player("Bob", 20));
    players.add(new Player("Charlie", 20));

        GameState state = new GameState(players);
        Rules rules = new LeducRules();
        PlayerAgent[] agents = { new RandomAgent(), new RandomAgent(), new RandomAgent() };

        GameFlow flow = new GameFlow(state, rules, agents);
        flow.playHands(10);

        System.out.println("Final chips:");
        for (Player p : players) {
            System.out.println("  " + p.getName() + ": " + p.getChips());
        }
    }
}
