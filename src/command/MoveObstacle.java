package command;

import model.Board;
import model.Field;
import model.Figure;
import model.Player;
import model.Position;
import model.Session;
import model.SessionManager;

import java.util.HashSet;
import java.util.Set;

public class MoveObstacle implements Command {
    private static final int UPPER_BOUND_COMMAND_ARGUMENTS_LENGTH = 4;
    private static final String INVALID_NUM_OF_ARG = "Invalid number of arguments.";
    private static final String NO_ACTIVE_SESSION = "No active session.";
    private static final String INVALID_STEPS = "Steps should be a number.";
    private static final String PLAYER_NO_CURRENT_FIGURE = "Player has no current figure.";
    private static final String NO_OBSTACLE = "No obstacle to move.";
    private static final String INVALID_MOVE = "Invalid placement for the obstacle.";
    private static final String DIRECTIONS_SHOULD_DIFFER = "Directions should differ.";
    private static final String SESSION_HAS_ENDED = "Session has ended.";
    private static final String MOVE_UP = "up";
    private static final String MOVE_DOWN = "down";
    private static final String MOVE_LEFT = "left";
    private static final String MOVE_RIGHT = "right";

    @Override
    public CommandResult execute( String[] commandArguments) {
        if (commandArguments.length < 2 || commandArguments.length > UPPER_BOUND_COMMAND_ARGUMENTS_LENGTH
                || commandArguments.length % 2 != 0) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_NUM_OF_ARG);
        }
        if (SessionManager.getActiveSession() == null) {
            return new CommandResult(CommandResultType.FAILURE, NO_ACTIVE_SESSION);
        }
        if (SessionManager.getActiveSession().hasWinner()) {
            return new CommandResult(CommandResultType.FAILURE, SESSION_HAS_ENDED);
        }
        Session activeSession = SessionManager.getActiveSession();
        Player currentPlayer = activeSession.getCurrentPlayer();
        Figure figure = currentPlayer.getCurrentFigure();// TODO: Bug: figure is null -> when trying to move the figure, the figure is not recognized
        Board currentBoard = activeSession.getBoard();

        if (figure == null) {
            return new CommandResult(CommandResultType.FAILURE, PLAYER_NO_CURRENT_FIGURE);
        }

        Field obstacleField = figure.getFieldPosition();
        if (!obstacleField.isGeneralObstacle()) {
            return new CommandResult(CommandResultType.FAILURE, NO_OBSTACLE);
        }

        Position position = obstacleField.getPosition();

        Set<String> directions = new HashSet<>();
        for (int i = 0; i < commandArguments.length; i += 2) {
            int steps;
            try {
                steps = Integer.parseInt(commandArguments[i]);
            } catch (NumberFormatException e) {
                return new CommandResult(CommandResultType.FAILURE, INVALID_STEPS);
            }
            String direction = commandArguments[i + 1].toLowerCase();
            if (!directions.add(direction)) {
                return new CommandResult(CommandResultType.FAILURE, DIRECTIONS_SHOULD_DIFFER);
            }
            switch (direction) {
                case MOVE_UP:
                    position.setRow(position.getRow() - steps);
                    break;
                case MOVE_DOWN:
                    position.setRow(position.getRow() + steps);
                    break;
                case MOVE_LEFT:
                    position.setColumn(position.getColumn() - steps);
                    break;
                case MOVE_RIGHT:
                    position.setColumn(position.getColumn() + steps);
                    break;
                default:
                    return new CommandResult(CommandResultType.FAILURE, INVALID_MOVE);
            }
        }
        if (!currentBoard.moveObstacle(currentBoard, obstacleField, position)) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_MOVE);
        }
        currentPlayer.resetDiceRoll();
        activeSession.nextPlayer();
        return new CommandResult(CommandResultType.SUCCESS, activeSession.nextTurnOutput());
    }

    @Override
    public boolean isAvailable() {
        if (SessionManager.getActiveSession() != null) {
            if (SessionManager.getActiveSession().hasWinner()) {
                return false;
            } else {
                Figure currentFigure = SessionManager.getActiveSession().getCurrentPlayer().getCurrentFigure();
                if (currentFigure == null) {
                    return false;
                }

                Field fieldOfFigure = currentFigure.getFieldAtPosition();
                return fieldOfFigure.isGeneralObstacle();
            }

        }
        return false;
    }

    @Override
    public int getMinNumberOfArguments() {
        return 2;
    }
}
