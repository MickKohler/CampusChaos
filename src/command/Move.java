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

public class Move implements Command {

    private Position initialPosition;

    @Override
    public CommandResult execute(String[] commandArguments) {
        Session activeSession = SessionManager.getActiveSession();
        if (activeSession == null) {
            return new CommandResult(CommandResultType.FAILURE, "No active session.");
        }

        Player currentPlayer = activeSession.getCurrentPlayer();
        if (!isValidArgs(commandArguments)) {
            return new CommandResult(CommandResultType.FAILURE, "Invalid number of arguments.");
        }

        if (!isValidMove(currentPlayer, commandArguments)) {
            return new CommandResult(CommandResultType.FAILURE, "Invalid move.");
        }

        if (currentPlayer.isFigureOnObstacle()) {
            return new CommandResult(CommandResultType.FAILURE, "Cannot move figure because it is on an obstacle.");
        }

        Figure figure = getFigure(activeSession, commandArguments[0]);
        if (figure == null) {
            return new CommandResult(CommandResultType.FAILURE, "Figure not found.");
        }

        setInitialPosition(figure.getFieldAtPosition().getPosition()); // getFieldAtPos.getPos

        return prepareMove(activeSession, figure, commandArguments);
    }

    private void setInitialPosition(Position initialPosition) {
        this.initialPosition = initialPosition;
    }

    private Position getInitialPosition() {
        return initialPosition;
    }

    private Figure getFigure(Session session, String figureId) {
        if (!session.isFigureOnBoard(figureId)) {
            return null;
        }
        return SessionManager.getActiveSession().getFigureById(figureId);
    }


    private CommandResult prepareMove(Session activeSession, Figure figure, String[] arguments) {
        Position positionOfFigure = figure.getFieldAtPosition().getPosition();
        int column = positionOfFigure.getColumn();
        int row = positionOfFigure.getRow();

        Set<Field> visitedFields = new HashSet<>();
        visitedFields.add(figure.getFieldAtPosition());

        for (int i = 1; i < arguments.length; i += 2) {
            int steps = Integer.parseInt(arguments[i]);
            String direction = arguments[i + 1];

            for (int step = 0; step < steps; step++) {
                switch (direction) {
                    case "up":
                        row--;
                        break;
                    case "down":
                        row++;
                        break;
                    case "left":
                        column--;
                        break;
                    case "right":
                        column++;
                        break;
                    default:
                        return new CommandResult(CommandResultType.FAILURE, "Invalid direction.");
                }
                Position newPosition = new Position(row, column); // new
                Board board = activeSession.getBoard(); // new
                Field endField = activeSession.getBoard().getFieldAt(board,newPosition);
                visitedFields.add(endField);
            }
        }
        Position finalPosition = new Position(row, column); // new
        return executeMove(activeSession, figure, finalPosition);
    }

    private CommandResult executeMove(Session activeSession, Figure figure, Position position) {
        Board board = activeSession.getBoard();

        figure.moveTo(position);
        Field initialField = board.getFieldAt(activeSession.getBoard(), getInitialPosition());
        if (initialField.isForest()) {
            activeSession.getBoard().removeFigureFromForest(figure);
        }
        Field endField = board.getFieldAt(activeSession.getBoard(), position);
        if (endField.isGeneralObstacle()) {
            return new CommandResult(CommandResultType.SUCCESS, null);
        }
        if (endField.isTarget()) {
            activeSession.setWinner(figure.getOwner());
            return new CommandResult(CommandResultType.SUCCESS, activeSession.winnerOutput());
        }
        activeSession.getCurrentPlayer().resetCurrentFigure();
        activeSession.getCurrentPlayer().resetDiceRoll();
        activeSession.nextPlayer();
        return new CommandResult(CommandResultType.SUCCESS, activeSession.nextTurnOutput());
    }



    private boolean isValidArgs(String[] arguments) {
        return arguments.length >= 3 && arguments.length % 2 != 0;
    }

    private boolean isValidMove(Player currentPlayer, String[] commandArguments) {
        int totalSteps = 0;

        for (int i = 1; i < commandArguments.length; i+=2) {
            try {
                totalSteps += Integer.parseInt(commandArguments[i]);
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return totalSteps == currentPlayer.getDiceRoll();
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
