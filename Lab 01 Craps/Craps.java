/******************************************************************************
 * Compilation: javac Craps.java
 * Execution:   java Craps
 *
 *
 * The casino game of craps for one player, without betting. Plays one game by default.
 * Commandline parameters can be used to run the game in test mode (plays until forced
 * to yield the dice) or to compute the odds of winning over a given number of games.
 *
 * % java Craps
 *	 Simulates one game of craps: Phase I Come Out + Phase II Point.
 *
 * % java Craps test
 *	 Plays until forced to yield the dice.
 *
 * % java Craps odds
 *	 Computes the odds of winning craps over a default number of 10,000 games
 *
 * % java Craps odds [int number of games]
 * 	 Computes the odds of winning craps over the given number of games.
 *
 * @author Sami Cemek
 * 02/09/2021
 ******************************************************************************/

import java.util.Random;

public class Craps
{
    /**
     * Roll a pair of 6-sided dice.
     * @param verbose (bool) true if the throw should be printed to the terminal
     * @return sum (int) the sum of 2 rolled dice
     */
    public static int roll2(boolean verbose)
    {
        int sides = 6;
        Random rand = new Random();
        int throw1 = rand.nextInt(sides) +1;
        int throw2 = rand.nextInt(sides) +1;
        int sum = throw1 + throw2;
        if (verbose)
        {
            System.out.println("\tThrew " + throw1 + "+" + throw2 + "=" + sum);
        }
        return sum;
    }

    /**
     * Phase I of the game of craps: The "Come Out." Roll until the "point" is established.
     * @param verbose (bool) true if Phase I's progress should be printed, throw by throw
     * @return point (int) the value of the die that must be rolled again before a 7
     */
    public static int comeOut (boolean verbose)
    {
        if(verbose)
        {
            System.out.println("Phase I, the Come Out");
        }
        int target = roll2(verbose);
        if(verbose)
        {
            System.out.println("\tTarget established:" + target);
        }
        return target;
    }

    /**
     * Phase II of the game craps: The "Point." Roll until the "point" is repeated
     * (the player wins) or the user sevens out (loses).
     * @param target (int) the dice total established during the come out.
     * @param verbose (bool) true if Phase II's progress should be printed, throw by throw
     * @return win (bool) true if the player wins the game, false if they seven out
     */
    public static boolean point (int target, boolean verbose)
    {
        if(verbose)
        {
            System.out.println("Phase II, the Point");
        }
        int total = roll2( verbose );
        while ((total != 7) && (total != target))
        {
            total = roll2( verbose );
        }
        boolean win = total == target;
        return win;
    }

    /**
     * Play a game of craps: the "come out" (phase I) and the "point" (phase II).
     * @param verbose (bool) true if the game's progress should be printed, throw by throw
     * @return win (bool) true if the player won, false if they lost
     */
    public static boolean playToReplay (boolean verbose)
    {
        boolean replay = false;
        int target = comeOut( verbose );
        if(  target == 7 ||  target == 11)
        {
            if(verbose)
            {
                System.out.println("A natural! You've won the game, but must yield the dice to the next player.");
            }
            replay = true;
        }
        else if( target==2 || target==3 || target==12)
        {
            if(verbose)
            {
                System.out.println("You've crapped out, but may play again.");
            }
            replay = false;
        }
        else
        {
            replay = point( target, verbose );
            if (verbose)
            {
                if (replay)
                {
                    System.out.println("A pass! You've won the game, and may play again.");
                }
                else
                {
                    System.out.println("You've sevened out, and must yield the dice to the next player.");
                }
            }
        }
        return replay;
    }

    /**
     * Play a game of craps: the "come out" (phase I) and the "point" (phase II).
     * @param verbose (bool) true if the game's progress should be printed, throw by throw
     * @return win (bool) true if the player won, false if they lost
     */
    public static boolean playToWin (boolean verbose)
    {
        boolean win = false;
        int target = comeOut( verbose );
        if (target == 7 || target == 11)
        {
            if (verbose)
            {
                System.out.println("A natural! You've won the game, but must yield the dice to the next player.");
            }
            win = true;
        }
        else if (target == 2 || target == 3 || target == 12)
        {
            if (verbose)
            {
                System.out.println("You've crapped out, but may play again.");
            }
            win = false;
        }
        else
        {
            win = point( target, verbose );
            if (verbose)
            {
                if (win)
                {
                    System.out.println("A pass! You've won the game, and may play again.");
                }
                else
                {
                    System.out.println("You've sevened out, and must yield the dice to the next player.");
                }
            }
        }
        return win;
    }

    /**
     * Test the game of craps by replaying until the player sevens out.
     */
    public static void test()
    {
        System.out.println("Welcome to the craps table.");
        boolean replay = playToReplay( true );
        while (replay)
        {
            System.out.println("\nPlaying again...");
            replay = playToReplay( true );
        }
    }

    /**
     * Compute the odds: how likely it is to win the game of craps.
     * @param numGames (int) the number of craps games over which the odds will be calculated
     * @return odds (float) the percent of games won: naturals + passes out of all numGames games
     */
    public static double computeOdds (int numGames)
    {
        int wins = 0;
        for(int i=0; i < numGames; i++)
        {
            if (playToWin(false))
            {
                wins++;
            }
        }

        double odds = (double)wins / numGames; // Caution: potential source of a curious Java bug...
        //float odds = wins / numGames;
        System.out.println("\nOut of" + numGames + "games:");
        System.out.println("\tWins:" + wins);
        System.out.println("\tOdds:" + odds);
        return odds;
    }

    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            if(args[0].equals("test"))
            {
                test(); // Make sure that the craps game is functioning as expected
            }
            else if (args[0].equals("odds"))
            {
                int numGames = 1000;
                if (args.length > 1)
                {
                    numGames = Integer.parseInt(args[1]);
                }
                computeOdds(numGames); // How likely is it to win the game of craps?
            }
        }
        else {
            playToWin(true); // Play a normal game
        }
    }

}

