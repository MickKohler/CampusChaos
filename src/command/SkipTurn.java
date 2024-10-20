package command;

import model.Player;
import model.SessionManager;

public class SkipTurn implements Command {
    private static final String INVALID_NUM_OF_ARG = "Invalid number of arguments.";
    private static final String NO_ACTIVE_SESSION = "No active session.";
    private static final String HASNT_ROLLED_YET = "player %s hasn't rolled the dice yet";
    private static final String SESSION_HAS_ENDED = "Session has ended.";
    private static final String SKIP_TURN_DESCRIPTION = "Skip the turn for the current player.";
    private static final String PLAYER_HAS_TO_MOVE = "Player has to move a figure.";

    @Override
    public CommandResult execute(String[] commandArguments) {
        if (commandArguments.length != 0) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_NUM_OF_ARG);
        }
        if (!SessionManager.hasActiveSession()) {
            return new CommandResult(CommandResultType.FAILURE, NO_ACTIVE_SESSION);
        }
        if (SessionManager.getActiveSession().hasWinner()) {
            return new CommandResult(CommandResultType.FAILURE, SESSION_HAS_ENDED);
        }
        Player currentPlayer = SessionManager.getActiveSession().getCurrentPlayer();

        if (currentPlayer.getDiceRoll() == 0) {
            return new CommandResult(CommandResultType.FAILURE, HASNT_ROLLED_YET.formatted(currentPlayer.getId()));
        }

        if (currentPlayer.isFigureOnObstacle() || currentPlayer.canMakeValidMove()) {
            return new CommandResult(CommandResultType.FAILURE, PLAYER_HAS_TO_MOVE);
        }

        currentPlayer.resetDiceRoll();
        SessionManager.getActiveSession().nextPlayer();

        return new CommandResult(CommandResultType.SUCCESS, SessionManager.getActiveSession().nextTurnOutput());
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
