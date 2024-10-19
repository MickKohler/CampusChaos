package model;

import java.util.Random;

public class Dice {
    private final Random random;
    private int lastRoll;

    /**
     * Creates a Dice object without specified seed for random number generation.
     */
    public Dice() {
        this.random = new Random();
    }

    /**
     * Creates a Dice object with a specific seed for random number generation.
     * This ensures reproducibility of the dice rolls.
     * @param seed the seed for the random number generator
     */
    public Dice(long seed) {
        this.random = new Random(seed);
    }

    /**
     * Rolls the dice and returns a number between 1 and 6.
     * @return the result of the dice roll
     */
    public int roll() {
        this.lastRoll = random.nextInt(6) + 1;
        return lastRoll;
    }

    /**
     * Returns the result of the last roll.
     * @return the result of the last roll
     */
    public int getLastRoll() {
        return lastRoll;
    }
}
