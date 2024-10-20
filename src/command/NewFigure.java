package command;

import model.Figure;
import model.Player;
import model.Session;
import model.SessionManager;

public class NewFigure implements Command {
    private static final String INVALID_NUM_OF_ARG = "Invalid number of arguments.";
    private static final String REACHED_MAX_FIGURES = "Player has reached maximum amount of figures.";
    private static final String NO_ACTIVE_SESSION = "No active session.";
    private static final String SESSION_HAS_ENDED = "Session has ended.";
    private static final String START_FIELD_OCCUPIED = "Start field is occupied.";
    private static final String NEW_FIGURE_DESCRIPTION = "Bring a new figure into play for the current player.";
    private static final int MAX_FIGURES = 5;

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
        if (!currentPlayer.hasFiguresOfBoard()) {
            return new CommandResult(CommandResultType.FAILURE, REACHED_MAX_FIGURES);
        }
        if (currentPlayer.getStartField().isOccupied()) {
            return new CommandResult(CommandResultType.FAILURE, START_FIELD_OCCUPIED);
        }

        currentPlayer.bringNewFigureIntoPlay();

        return new CommandResult(CommandResultType.SUCCESS, null);
    }

    @Override
    public boolean isAvailable() {
        if (SessionManager.hasActiveSession()) {

            Player currentPlayer = SessionManager.getActiveSession().getCurrentPlayer();
            boolean notAllFiguresInGame = currentPlayer.hasFiguresOfBoard();
            boolean emptyStartField = !currentPlayer.getStartField().isOccupied();
            boolean hasWinner = SessionManager.getActiveSession().hasWinner();

            return notAllFiguresInGame && emptyStartField && !hasWinner;
        }

        return false;


    }

    @Override
    public int getMinNumberOfArguments() {
        return 0;
    }
}
