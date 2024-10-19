package command;

import model.Session;
import model.SessionManager;

public class Rematch implements Command {
    @Override
    public CommandResult execute(String[] commandArguments) {
        Session activeSession = SessionManager.getActiveSession();
        if (commandArguments.length != 0) {
            return new CommandResult(CommandResultType.FAILURE, "Invalid number of arguments.");
        }

        if (activeSession == null) {
            return new CommandResult(CommandResultType.FAILURE, "No active session.");
        }

        if (!activeSession.hasWinner()) {
            return new CommandResult(CommandResultType.FAILURE, "Session has not ended yet.");
        }

        activeSession.getCurrentPlayer().resetDiceRoll();
        activeSession.getBoard().resetBoard();
        activeSession.resetHasWinner();
        activeSession.getPlayers().clear();

        return new CommandResult(CommandResultType.SUCCESS, activeSession.nextTurnOutput());


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
