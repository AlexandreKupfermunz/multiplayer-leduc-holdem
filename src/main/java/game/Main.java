package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import objects.GameState;
import objects.Player;

public class Main {

    private static final int[] ANTE_LEVELS = {1, 2, 3, 5, 10, 20};
    
    public static void main(String[] args) {

        Scanner in = new Scanner (System.in);
        
        int numPlayers = askPlayers(in);
        boolean extraRank = yesOrNo(in, "Do you want to add an extra rank? (yes or no): ");
        boolean extraSuit = yesOrNo(in, "Do you want to add an extra suit (clubs)? (yes or no): ");
        int increaseEvery = askPositiveInt(in, "Increase the ante every how many hands? (1 or more): ");

        int numRanks = extraRank ?4 : 3;
        int numSuits = extraSuit ?3 : 2;

        String[] names = {"Alice", "Bob", "Charlie"};
        List<Player> players = new ArrayList<>();
        PlayerAgent[] agents = new PlayerAgent[numPlayers];
        for (int i=0; i < numPlayers; i++) {
            players.add(new Player(names[i], 20));
            agents[i] = new RandomAgent();
        }

        GameState state = new GameState(players, numRanks, numSuits);
        Rules rules = new LeducRules();

        GameFlow flow = new GameFlow(state, rules, agents);
        AnteSchedule anteSchedule = new AnteSchedule(ANTE_LEVELS, increaseEvery);
        GameFlow flow = new GameFlow(state, rules, agents, anteSchedule);
        flow.playHands(10);

        System.out.println("Final chips:");
        for (Player p : players) {
            System.out.println("  " + p.getName() + ": " + p.getChips());
        }
    }

    private static boolean yesOrNo(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim().toLowerCase();
            if (s.equals("yes") || s.equals ("y")) return true;
            if (s.equals("no") || s.equals("n")) return false;
            System.out.println("Please answer yes or no");
        }
    }

    private static int askPositiveInt(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = in.nextLine().trim();
            try {
                int value = Integer.parseInt(s);
                if (value >= 1) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // the user typed something that is not a number (like "abc"), ask again below
            }
            System.out.println("Please enter a whole number of 1 or more");
        }
    }

    private static int askPlayers(Scanner in) {
        while (true) {
            System.out.print("Enter the number of players (2 or 3): ");
            String s = in.nextLine().trim();
            if (s.equals("2") || s.equals("3")) return Integer.parseInt(s);
            System.out.println("Please enter 2 or 3");
        }
    }
}
