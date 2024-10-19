package command;

public class SkipTurn implements Command {
    @Override
    public CommandResult execute(String[] commandArguments) {
        return null;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public int getMinNumberOfArguments() {
        return 0;
    }
}
