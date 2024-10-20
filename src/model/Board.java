package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private static final char CHAR_PATHWAY = 'P';
    private static final char CHAR_VILLAGE_PATHWAY = 'p';
    private static final char CHAR_OBSTACLE = 'O';
    private static final char CHAR_VILLAGE_OBSTACLE = 'o';
    private static final char CHAR_TARGET = 'T';
    private static final char CHAR_FOREST = 'f';
    private static final char CHAR_ZONE = 'Z';
    private static final char CHAR_EMPTY = ' ';
    private static final String HIT_FIGURE_FORMAT = "Player %s has hit Player %s.";
    private final List<String> linesOfGrid = new ArrayList<>();
    private List<Figure> figuresInForest = new ArrayList<>();
    private static Field[][] grid;
    private Field[][] gridForReset;


    public Board() {
        // TODO: empty constructor
    }


    public void initializeGrid(List<String> lines) {
        int heigth = lines.size();
        int width = calculateMaxWidth(lines);

        grid = new Field[heigth][width];

        for(int row = 0; row < heigth; row++) {
            String line = lines.get(row);
            line = fillUpLine(line, width);
            parseLine(line, row);
        }
    }


    private int calculateMaxWidth(List<String> lines) {
        int maxWidth = 0;

        for(String line : lines) {
            if(line.length() > maxWidth) {
                maxWidth = line.length();
            }
        }
        return maxWidth;
    }


    private String fillUpLine(String line, int width) {
        StringBuilder builder = new StringBuilder(line);

        while (builder.length() < width) {
            builder.append(CHAR_EMPTY);
        }
        return builder.toString();
    }

    private void parseLine(String line, int row) {
        for (int column = 0; column < line.length(); column++) {
            char typeChar = line.charAt(column);
            parseField(typeChar, row , column);
        }
    }

    // TODO: createFieldFromChar bei Shabzed hab ich nicht/anders
    private void parseField(char typeChar, int row, int column) {
        FieldType fieldType = Field.fromChar(typeChar); // TODO: marks P as start field
        Position position = new Position(row,column);

        Field field = new Field(fieldType, position, typeChar);
        grid[row][column] = field;
    }


    private List<Field> getFieldsOfType(FieldType type) {
        List<Field> list = new ArrayList<>();

        for (Field[] fields : grid) {
            for (Field field : fields) {
                if (field.getType().equals(type)) {
                    list.add(field);
                }
            }
        }
        return list;
    }

    public List<Field> getVillageFields() {
        List<Field> villageFields = new ArrayList<>();
        villageFields.addAll(getFieldsOfType(FieldType.VILLAGE_PATH));
        villageFields.addAll(getFieldsOfType(FieldType.VILLAGE_OBSTACLE));
        return villageFields;
    }

    public List<Field> getAllObstacleFields() {
        List<Field> obstacleFields = new ArrayList<>();
        obstacleFields.addAll(getFieldsOfType(FieldType.OBSTACLE));
        obstacleFields.addAll(getFieldsOfType(FieldType.VILLAGE_OBSTACLE));
        return obstacleFields;
    }

    public Field getStartFieldForPlayer(Board board, char fieldChar) {
        for (Field[] fields : grid) {
            for (Field field : fields) {
                if (field.isStart() && fieldChar == field.getFieldChar()) {
                    return field;
                }
            }
        }
        return null;
    }

    public boolean isForestOccupied() {
        return !figuresInForest.isEmpty();
    }

    public static boolean isWithinBounds(Board board, Position position) {
        return position.getRow() >= 0 && position.getRow() < board.getGrid().length && position.getColumn() >= 0 && position.getColumn() < board.getGrid()[0].length;
    }

    public static boolean isTargetFieldValid(Board board, Position position, Player currentPlayer) {
        Field targetField = getFieldAt(board, position);
        if (!isWithinBounds(board, position) || targetField == null || targetField.isEmpty() || targetField.isForest()
                || targetField.isStart()) {
            return false;
        }
        if (targetField.isGeneralObstacle() || targetField.isTarget()) {
            return true;
        }
        Figure occupyingFigure = targetField.getOccupyingFigure();
        if (targetField.isZone()) {
            return occupyingFigure == null;
        }
        if (targetField.isGeneralPathway()) {
            if (occupyingFigure != null) {
                if (occupyingFigure.getOwner().equals(currentPlayer)) {
                    return false;
                } else {
                    if (targetField.isVillagePathway()) {
                        board.moveFigureToForest(occupyingFigure);
                    } else {
                        board.moveFigureToStartPosition(occupyingFigure);
                    }
                    System.out.println(HIT_FIGURE_FORMAT.formatted(currentPlayer.getId().toUpperCase(), occupyingFigure.getOwner().getId().toUpperCase()));
                    return true;
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Checks for a valid move.
     *
     * @param board the board
     * @param column the column
     * @param row the row
     * @return true if the move is valid, false otherwise
     */
    public static boolean isMoveValid(Board board, int column, int row) {
        Position position = new Position(row, column);
        Field targetField = getFieldAt(board, position);
        return isWithinBounds(board, position) && targetField != null &&
                !targetField.isEmpty() &&
                !targetField.isGeneralObstacle();
    }

    /**
     * Returns the adjacent fields of a field.
     *
     * @param board the board
     * @param field the field
     * @return the adjacent fields
     */
    public static List<Field> getAdjacentFields(Board board, Field field) {
        List<Field> adjacentFields = new ArrayList<>();
        int row = field.getPosition().getRow();
        int column = field.getPosition().getColumn();

        Position horizontalToLeft = new Position(row, column - 1);
        if (Board.isWithinBounds(board, horizontalToLeft)) {
            adjacentFields.add(getFieldAt(board,horizontalToLeft));
        }

        Position horizontalToRight = new Position(row, column + 1);
        if (Board.isWithinBounds(board, horizontalToRight)) {
            adjacentFields.add(getFieldAt(board,horizontalToRight));
        }

        Position verticalUp = new Position(row - 1, column);
        if (Board.isWithinBounds(board, verticalUp)) {
            adjacentFields.add(getFieldAt(board,verticalUp));
        }

        Position verticalDown = new Position(row + 1, column);
        if (Board.isWithinBounds(board, verticalDown)) {
            adjacentFields.add(getFieldAt(board,verticalDown));
        }

        return adjacentFields;

    }

    public void moveFigureToStartPosition(Figure occupyingFigure) {
        if (occupyingFigure.getFieldPosition() != null) {
            occupyingFigure.getFieldPosition().removeOccupyingFigure();
        }

        occupyingFigure.getOwner().removeFigureFromBoard(occupyingFigure);
    }


    public static Field getFieldAt(Board board, Position position) {
        if (isWithinBounds(board, position)) {
            return grid[position.getRow()][position.getColumn()];
        }
        return null;
    }

    /**
     * Moves the obstacle if possible.
     *
     * @param board the board
     * @param obstacleField the field with the obstacle
     * @param newPosition the end position to which the obstalce should move to
     * @return true if the obstacle was moved, false otherwise
     */
    public boolean moveObstacle(Board board, Field obstacleField, Position newPosition) {
        if (!board.isWithinBounds(board, newPosition)) {
            return false;
        }

        Field fieldToMoveTo = getFieldAt(board, newPosition);
        boolean isStartOrZoneOrInvalid = fieldToMoveTo == null || fieldToMoveTo.isStart() || fieldToMoveTo.isZone();
        boolean isForestOrSpace = fieldToMoveTo.isEmpty() || fieldToMoveTo.isForest();
        boolean isTargetOrObstacle = fieldToMoveTo.isGeneralObstacle() || fieldToMoveTo.isTarget();
        boolean isOccupied = fieldToMoveTo.isOccupied();

        if (isStartOrZoneOrInvalid ||isForestOrSpace ||isTargetOrObstacle || isOccupied) {
            return false;
        }

        obstacleField.removeObstacle();
        fieldToMoveTo.placeObstacle();
        return true;

    }


    public void resetBoard() { // TODO: resetBoard might not work
        for(Field[] fields : grid) {
            for(Field field : fields) {
                ;gridForReset[field.getPosition().getRow()][field.getPosition().getColumn()] = grid[field.getPosition().getRow()][field.getPosition().getColumn()];
            }
        }
    }

    public Field[][] getGridForReset() {
        return gridForReset;
    }


    public void moveFigureToForest(Figure figure) {
        Field forestFild = getForestField();
        if (forestFild != null) {
            figure.getFieldPosition().removeOccupyingFigure();
            forestFild.addFigureToForest(figure);
            figuresInForest.add(figure);
        }
    }

    public void removeFigureFromForest(Figure figure) {
        figuresInForest.remove(figure);
    }

    public Field getForestField() {
        for (Field[] fields : grid) {
            for (Field field : fields) {
                if (field.getType() == FieldType.FOREST) {
                    return field;
                }
            }
        }
        return null;
    }




    public Field[][] getGrid() {
        return grid;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < grid.length; i++) { // TODO: lines.size() - 1 evtl. da falsch
            String line = Arrays.toString(grid[i]);
            for(int k = 0; k < grid[i].length; k++) {
                Field field = grid[i][k];

                if(field != null) {
                    switch (field.getType()) {
                        case PATH -> builder.append(CHAR_PATHWAY);
                        case VILLAGE_PATH -> builder.append(CHAR_VILLAGE_PATHWAY);
                        case OBSTACLE -> builder.append(CHAR_OBSTACLE);
                        case VILLAGE_OBSTACLE -> builder.append(CHAR_VILLAGE_OBSTACLE);
                        case TARGET -> builder.append(CHAR_TARGET);
                        case FOREST -> builder.append(CHAR_FOREST);
                        case SAFEZONE -> builder.append(CHAR_ZONE);
                        case EMPTY -> builder.append(CHAR_EMPTY);
                        default -> builder.append(field.getFieldChar());
                    }
                } else {
                    builder.append(line.charAt(i));
                }
            }
            if(i < grid.length - 1) {
                builder.append(System.lineSeparator());
            }
        }
        return builder.toString();
    }

}
