package model;

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
