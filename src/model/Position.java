package model;

import java.util.Objects;

/**
 * This class represents a position on the game board.
 *
 * @author untxx
 */
public class Position {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

//    /**
//     * Calculates target position of an obstacle to be moved.
//     * @param currentPosition the current position of the obstacle
//     * @param direction the direction in which to move the obstacle
//     * @param distance the distance by which to move the obstacle
//     * @return the new position of the obstacle
//     */
//    public static Position calculateNewPosition(Position currentPosition, Direction direction, int distance) {
//        return switch (direction) {
//            case LEFT -> new Position(currentPosition.getRow(), currentPosition.getColumn() - distance);
//            case RIGHT -> new Position(currentPosition.getRow(), currentPosition.getColumn() + distance);
//            case UP -> new Position(currentPosition.getRow() - distance, currentPosition.getColumn());
//            case DOWN -> new Position(currentPosition.getRow() + distance, currentPosition.getColumn());
//        };
//    }



    public Position getAbove(){
        return new Position(row - 1, column);
    }

    public Position getBelow(){
        return new Position(row + 1, column);
    }

    public Position getLeft(){
        return new Position(row, column - 1);
    }

    public Position getRight(){
        return new Position(row, column + 1);
    }


//    public Position getNeighbour(Direction direction) {
//        return switch (direction) {
//            case LEFT -> getLeft();
//            case RIGHT -> getRight();
//            case UP -> getAbove();
//            case DOWN -> getBelow();
//        };
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Position position = (Position) o;
        return row == position.row && column == position.column;  // TODO: width/height are supposed to be final although Positio
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, column);
    }

    @Override
    public String toString() {
        return "(" + row + ", " + column + ")";
    }

}

