package model;

public class Field {
    private static final String PATHWAY_SYMBOL = "P";
    private static final String VILLAGE_PATHWAY_SYMBOL = "p";
    private static final String OBSTACLE_SYMBOL = "O";
    private static final String VILLAGE_OBSTACLE_SYMBOL = "o";
    private static final String TARGET_SYMBOL = "T";
    private static final String FOREST_SYMBOL = "f";
    private static final String ZONE_SYMBOL = "Z";
    private static final String EMPTY_SYMBOL = " ";
    private static final char EMPTY_CHAR_SYMBOL = ' ';
    private static final String NOTHING = "";
    private static final String REGEX = "[^0-9]";
    private static final char[] CHAR_START_SET = {'a', 'b', 'c', 'd', 'e', 'g', 'h',
            'i', 'j', 'k', 'l', 'm', 'n', 'o', 'q', 'r', 's', 'u', 'v', 'w', 'x', 'y'};
    private static final String START_POSITION_REGEX = "[a-zA-Z0-9]+";
    private final char startChar;
    private final Position position;
    private FieldType type;
    private Figure occupyingFigure;

    Field(FieldType type, Position position, char startChar) {
        this.type = type ;
        this.position = position;
        this.startChar = (type == FieldType.START) ? startChar : EMPTY_CHAR_SYMBOL;
        this.occupyingFigure = null;

    }

    Field(char startChar, Position position) {
        this.type = type;
        this.position = position;
        this.startChar = startChar;
        this.occupyingFigure = null;
    }

    public boolean isStart() {
        return type == FieldType.START;
    }

    /**
     * Returns if the field is a target field.
     * @return true if the field is a target field, false otherwise
     */
    public boolean isTarget() {
        return type == FieldType.TARGET;
    }

    /**
     * Returns if the field is a forest field.
     * @return true if the field is a forest field, false otherwise
     */
    public boolean isForest() {
        return type == FieldType.FOREST;
    }

    /**
     * Returns if the field is a zone field.
     * @return true if the field is a zone field, false otherwise
     */
    public boolean isZone() {
        return type == FieldType.SAFEZONE;
    }

    /**
     * Returns if the field is a pathway.
     * @return true if the field is a pathway, false otherwise
     */
    public boolean isGeneralPathway() {
        return type == FieldType.PATH || type == FieldType.VILLAGE_PATH;
    }

    /**
     * Returns whether the field is an obstacle.
     * @return true if the field is an obstacle, false otherwise
     */
    public boolean isGeneralObstacle() {
        return type == FieldType.OBSTACLE || type == FieldType.VILLAGE_OBSTACLE;
    }

    public void placeObstacle() {
        if (type == FieldType.PATH) {
            type = FieldType.OBSTACLE;
        } else if (type == FieldType.VILLAGE_PATH) {
            type = FieldType.VILLAGE_OBSTACLE;
        }
    }

    /**
     * Returns whether the field is a village pathway.
     * @return true if the field is a village pathway, false otherwise
     */
    public boolean isVillagePathway() {
        return type == FieldType.VILLAGE_PATH;
    }

    /**
     * Returns whether the field is a village field.
     * @return true if the field is a village field, false otherwise
     */
    public boolean isVillage() {
        return type == FieldType.VILLAGE_PATH || type == FieldType.VILLAGE_OBSTACLE;
    }

    /**
     * Returns whether the field is an empty field.
     * @return true if the field is empty, false otherwise
     */
    public boolean isEmpty() {
        return type == FieldType.EMPTY;
    }

    public char getStartChar() {
        return startChar;
    }

    public FieldType getType() {
        return type;
    }


    public void setType(FieldType type) {
        this.type = type;
    }

    public Figure getOccupyingFigure() {
        return occupyingFigure;
    }

    public boolean isOccupied() {
        return occupyingFigure != null;
    }

    public void setOccupyingFigure(Figure occupyingFigure) {
        this.occupyingFigure = occupyingFigure;
    }

    public void removeOccupyingFigure() {
        this.occupyingFigure = null;
    }

    public void removeObstacle() {
        if(isGeneralObstacle()) {
            type = (FieldType.OBSTACLE == type) ? FieldType.PATH : FieldType.VILLAGE_PATH;
        }
    }


    public void setFieldToObstacle() {
        if(type == FieldType.PATH) {
            type = FieldType.OBSTACLE;
        } else if(type == FieldType.VILLAGE_PATH) {
            type = FieldType.VILLAGE_OBSTACLE;
        }
    }

    public void addFigureToForest(Figure figure) { // TODO: evtl. besser in anderer Klasse
        if(type == FieldType.FOREST) {
            figure.setPosition(this);
        }

    }

    // write a method that returns the type of the field by the char, so basicallly reverse method getFieldChar
    public static FieldType fromChar(char inputChar) {

        for(Character c : CHAR_START_SET) {
            if (c == inputChar) {
                return FieldType.START;
            }
        }

        return switch (inputChar) {
            case 'P' -> FieldType.PATH;
            case 'p' -> FieldType.VILLAGE_PATH;
            case 'O' -> FieldType.OBSTACLE;
            case 'o' -> FieldType.VILLAGE_OBSTACLE;
            case 'f', 'F' -> FieldType.FOREST; // nochmal überlegen wegen groß klein
            case 't', 'T' -> FieldType.TARGET;
            case 'z', 'Z' -> FieldType.SAFEZONE;
            case ' ' -> FieldType.EMPTY;
            default -> throw new IllegalArgumentException(inputChar + " is not a valid input");
        };
    }



    public char getFieldChar() {
        if(type == FieldType.START) {
            return startChar;
        }
        switch (type) {
            case PATH -> {
                return PATHWAY_SYMBOL.charAt(0);
            }
            case VILLAGE_PATH -> {
                return VILLAGE_PATHWAY_SYMBOL.charAt(0);
            }
            case OBSTACLE -> {
                return OBSTACLE_SYMBOL.charAt(0);
            }
            case VILLAGE_OBSTACLE -> {
                return VILLAGE_OBSTACLE_SYMBOL.charAt(0);
            }
            case TARGET -> {
                return TARGET_SYMBOL.charAt(0);
            }
            case FOREST -> {
                return FOREST_SYMBOL.charAt(0);
            }
            case SAFEZONE -> {
                return ZONE_SYMBOL.charAt(0);
            }
            default -> {
                return EMPTY_SYMBOL.charAt(0);
            }


        }
    }

    public Position getPosition() {
        return position;
    }



    public String toString(Player currentPlayer) {
        if (occupyingFigure != null) {
            if (occupyingFigure.getOwner().equals(currentPlayer)) {
                return String.valueOf(currentPlayer.getId().charAt(1)); // to get number out of figure ID
            } else {
                return occupyingFigure.getOwner().getId().toUpperCase();
            }
        }

        switch (type) {
            case PATH:
                return PATHWAY_SYMBOL;
            case VILLAGE_PATH:
                return VILLAGE_PATHWAY_SYMBOL;
            case OBSTACLE:
                return OBSTACLE_SYMBOL;
            case VILLAGE_OBSTACLE:
                return VILLAGE_OBSTACLE_SYMBOL;
            case START:
                return String.valueOf(startChar);
            case TARGET:
                return TARGET_SYMBOL;
            case FOREST:
                return FOREST_SYMBOL;
            case SAFEZONE:
                return ZONE_SYMBOL;
            default:
                return EMPTY_SYMBOL;
        }
    }


}
