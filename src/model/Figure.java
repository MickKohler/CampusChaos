package model;

import java.util.HashSet;
import java.util.Set;

public class Figure {
    private final String id;
    private final Player owner;
    private Field position;
    private Board board;

    public Figure(String id, Player owner, Board board) {
        this.id = id;
        this.owner = owner;
        this.board = board;
        this.position = null;
    }

    /**
     * Returns whether the figure can move.
     *
     * @param board the board the figure is on
     * @param diceRoll the dice roll
     * @return true if the figure can move, false otherwise
     */
    public boolean canMove(Board board, int diceRoll) {
        if (position == null) {
            Field startField = owner.getStartField();
            return checkValidMoves(diceRoll, startField.getPosition(), new HashSet<>());
        }
        return checkValidMoves(diceRoll, position.getPosition(), new HashSet<>());
    }

    /**
     * Checks whether the figure can make a valid move or not.
     *
     * @param diceRoll the dice roll
     * @param position the position of the figure
     * @param visited the set of visited fields
     * @return true if the figure can make a valid move, false otherwise
     */
    private boolean checkValidMoves(int diceRoll, Position position, Set<Field> visited) {
        if (diceRoll == 0) {
            return true;
        }

        Field currentField = board.getFieldAt(board, position);
        visited.add(currentField);

        for (Field adjacentField : Board.getAdjacentFields(board, currentField)) {

            if (!visited.contains(currentField)) {
                boolean isValidMove = (diceRoll == 1) ?
                        Board.isTargetFieldValid(board, adjacentField.getPosition(), owner)
                        : Board.isMoveValid(board, adjacentField.getPosition().getColumn(), adjacentField.getPosition().getRow());

                if (isValidMove && checkValidMoves(diceRoll -1, position, visited)) {
                    return true;
                }
            }
        }
        return false;

    }


    /**
     * Returns the board the figure is on.
     *
     * @return the board the figure is on
     */
    public Field getFieldPosition() {
        return position;
    }

    /**
     * Sets the position of the figure.
     *
     * @param position the new position of the figure
     */
    public void setPosition(Field position) {
        this.position = position;
    }

    public Player getOwner() {
        return owner;
    }

    public String getId() {
        return id;
    }

    public boolean isOnBoard() {
        return position != null;
    }


    public void moveTo(Position newPosition) {
        if (this.position != null) {
            this.position.removeOccupyingFigure();
        }
        this.position = board.getFieldAt(board, newPosition);

        if (this.position != null) {
            this.position.setOccupyingFigure(this);
        }

    }


    public Field getFieldAtPosition() {
        return position;
    }

}
