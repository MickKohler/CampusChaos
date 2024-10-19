package command;

import model.Player;
import model.Session;
import model.SessionManager;

public class RollDice implements Command {
    @Override
    public CommandResult execute(String[] commandArguments) {
        if (SessionManager.getActiveSession() == null) {
            return new CommandResult(CommandResultType.FAILURE, "No active session.");
        }
        Long seed = SessionManager.getActiveSession().getSeed();
        int rolledNumber;
        Session activeSession = SessionManager.getActiveSession();
        Player currentPlayer = SessionManager.getActiveSession().getCurrentPlayer();

        if (seed == null) {
            if (commandArguments.length != 1) {
                return new CommandResult(CommandResultType.FAILURE, "argument length needs to be 1");
            }
            try {
                rolledNumber = Integer.parseInt(commandArguments[0]);
                currentPlayer.setDiceRoll(rolledNumber);

            } catch (NumberFormatException e) {
                return new CommandResult(CommandResultType.FAILURE, "Invalid input arguments");
            }
        } else {
            rolledNumber = activeSession.rollDice();
            currentPlayer.setDiceRoll(rolledNumber);
        }

        return new CommandResult(CommandResultType.SUCCESS, String.valueOf(rolledNumber));

    }

    @Override
    public boolean isAvailable() {
        return SessionManager.getActiveSession() != null && !SessionManager.getActiveSession().hasWinner()
                && SessionManager.getActiveSession().getCurrentPlayer().getDiceRoll() == 0;
    }

    @Override
    public int getMinNumberOfArguments() {
        return 0;
    }
}
