package command;

public class Quit implements Command{

    private final CommandHandler commandHandler;

    /**
     * Constructs a new QuitCommand.
     * @param commandHandler the command handler
     */
    Quit(CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public CommandResult execute(String[] commandArguments) {
        commandHandler.quit();
        return new CommandResult(CommandResultType.QUIT, null);
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getMinNumberOfArguments() {
        return 0;
    }
}
