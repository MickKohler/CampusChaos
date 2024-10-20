package model;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// TODO: zuordnen von startfeldern beim Einlesen der Datei fehlt evtl.
public class Player {
    private static final int NUMBER_OF_PLAYERS = 5;
    private final String id;
    private final List<Figure> figures;
    private List<Figure> figuresOnBoard;
    private Queue<Figure> figuresOfBoard;
    private Figure currentFigure;
    private int diceRoll;
    private Field startField = null;
    private Board board;
    // shahzeb hat noch board als Attribut für move Validation hinzugefügt


    public Player(String id, Field startField, Board board) {
        this.id = id;
        this.figures = new ArrayList<>();
        this.figuresOnBoard = new ArrayList<>();
        this.figuresOfBoard = new LinkedList<>();
        this.currentFigure = null;
        this.diceRoll = 0;
        this.startField = startField;
        this.board = board;
        addFigures();
    }

    public void addFigures() {
        for(int i = 1; i <= NUMBER_OF_PLAYERS; i++) {
            Figure figure = new Figure(this.id + i, this, board);
            figures.add(figure);
            figuresOfBoard.add(figure);
        }
    }

    public void bringNewFigureIntoPlay () {
        Figure figure = figuresOfBoard.poll();

        if(!startField.isOccupied() && figure != null) {
                startField.setOccupyingFigure(figure);
                figuresOfBoard.add(figure);
                figure.setPosition(startField);
                figuresOnBoard.add(figure);
            System.out.println(figure.getId().toUpperCase());
                // TODO: hier bei Shaz sout(figure.getId) aber besser woanders machen
        }

    }

    public void addToFiguresOfBoard(Figure figure) {
        figuresOfBoard.add(figure);
    }

    public void removeFigureFromBoard(Figure figure) {
        figuresOnBoard.remove(figure);
        figuresOfBoard.add(figure);
        figure.setPosition(null);

    }

    public void clearFiguresOnBoard() {
        figuresOfBoard.clear();
    }

    public boolean hasFiguresOfBoard() {
        return !figuresOfBoard.isEmpty();
    }

    public int getNumberOfFiguresOnBoard() {
        return figuresOnBoard.size();
    }



    public boolean isFigureOnObstacle() {

        for(Figure figure : figures) {
            if (figure.isOnBoard() && figure.getFieldPosition().isGeneralObstacle()) {
                return true;
            }
        }
        return false;
    }


//    public boolean canMakeValidMove() {
//
//    }

















    public String getId() {
        return id;
    }

    /**
     * Returns the figures of the player.
     * @return the figures of the player
     */
    public List<Figure> getFigures() {
        return figures;
    }

    /**
     * Returns the figures of the player on the board.
     * @return the figures of the player on the board
     */
    public List<Figure> getFiguresOnBoard() {
        return figuresOnBoard;
    }

    /**
     * Returns if the player has the figure.
     * @param figure the figure
     * @return the figure or null if no such figure exists
     */
    public boolean hasFigure(Figure figure) {
        return figures.contains(figure);
    }

    /**
     * Returns the start field of the player.
     * @return the start field of the player
     */
    public Field getStartField() {
        return startField;
    }

    /**
     * Returns the dice roll of the player.
     * @return the dice roll of the player
     */
    public int getDiceRoll() {
        return diceRoll;
    }

    /**
     * Sets the dice roll of the player.
     * @param diceRoll the dice roll of the player
     */
    public void setDiceRoll(int diceRoll) {
        this.diceRoll = diceRoll;
    }

    /**
     * Resets the dice roll of the player.
     */
    public void resetDiceRoll() {
        this.diceRoll = 0;
    }

    /**
     * Returns the current figure of the player.
     * @return the current figure of the player
     */
    public Figure getCurrentFigure() {
        return currentFigure;
    }

    /**
     * Sets the current figure of the player.
     * @param currentFigure the current figure of the player
     */

    public void setCurrentFigure(Figure currentFigure) {
        this.currentFigure = currentFigure;
    }

    /**
     * Resets the current figure of the player.
     */
    public void resetCurrentFigure() {
        this.currentFigure = null;
    }

    /**
     * Returns if the player has a figure on the board, that can make a valid move.
     * @return if the player has a figure on the board that can make a valid move
     */
    public boolean canMakeValidMove() {
        for (Figure figure : figures) {
            if (figure.canMove(board, diceRoll)) {
                return true;
            }
        }
        return false;
    }

}
