package command;

/**
 * This interface represents an executable command.
 */
public interface Command {

    /**
     * Executes the command.
     *
     * @param commandArguments the arguments of the command
     * @return the result of the command
     */
    CommandResult execute(String[] commandArguments);

    /**
     * Returns whether the command is available for the help command.
     *
     * @return whether the command is available
     */
    boolean isAvailable();

    /**
     * Returns the minimum number of arguments that the command expects.
     *
     * @return the minimum number of arguments that the command expects
     */
    int getMinNumberOfArguments();
}