package model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class Session {
    private String sessionId;
    private final String filePath;
    private Board board;
    private List<Player> players;
    private int currentPlayerIndex;
    private boolean hasWinner;
    private Player winner;
    private boolean active;
    private Long seed;
    private Dice dice;
    private Random random;

    private static final char PLAYER_CHAR = 'a';
    private final char[] NON_PLAYER_CHARS = {'p', 'o', 'f', 'z', 't'};

    private static final String WIN_MESSAGE = "Player %s has won!";
    private static final String HIT_FIGURE_MESSAGE = "Player %s has hit Player %s"; // TODO: keine Ahnung wie ich das machen soll
    private static final String PLAYERS_TURN_MESSAGE = "It’s player %s’s turn.";
    private static final String OCCUPIED_FOREST = "F";
    private static final String NOT_OCCUPIED_FOREST = "f";
    private static final String EMPTY = " ";
    private static final String NOTHING = "";
    private static final String NEW_LINE = "\n";
    private static final String FIRST_PART = "It's player ";
    private static final String SECOND_PART = "'s turn.";
    private static final String PLAYER = "Player ";
    private static final String HAS_WON = " has won!";


    public Session(String sessionId, String filePath, int numberOfPlayers, Long seed) throws IOException {
        this.sessionId = sessionId;
        this.filePath = filePath;
        this.board = loadBoardFromFile(filePath);
        this.players = createPlayers(numberOfPlayers, board);
        this.currentPlayerIndex = 0;
        this.hasWinner = false;
        this.hasWinner = false;
        this.winner = null;
        this.active = true; // TODO: evtl. false
        this.seed = seed;
        this.dice = (seed != null) ? new Dice(seed) : new Dice();
        this.random = (seed != null) ? new Random(seed) : new Random();
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Board getBoard() {
        return board;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public List<Player> getPlayers() {
        return players;
    }


    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public boolean hasWinner() {
        return hasWinner;
    }

    public void resetHasWinner() {
        this.hasWinner = false;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
        this.hasWinner = true;
    }

    public Long getSeed() {
        return seed;
    }

    public void setSeed(Long seed) {
        this.seed = seed;
    }

    public Dice getDice() {
        return dice;
    }

    public void setDice(Dice dice) {
        this.dice = dice;
    }

    public Random getRandom() {
        return random;
    }

    public int rollDice() {
        return dice.roll();
    }

    public void setRandom(Random random) {
        this.random = random;
    }


    public String getSessionId() {
        return sessionId;
    }

    public boolean isActiveSession() {
        return active;
    }


    public void activateSession() {
        this.active = true;
    }

    public void deactivateSession() {
        this.active = false;
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public boolean isFigureOnBoard(String figureId) {
        Figure figure = getFigureById(figureId);
        return figure != null && figure.isOnBoard();
    }

    public Figure getFigureById(String figureId) {
        for (Player player : players) {
            for (Figure figure : player.getFigures()) {
                if (figure.getId().toUpperCase().equals(figureId)) {
                    return figure;
                }
            }
        }
        return null;
    }


    public void nextPlayer() {
        this.currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
    }

    public String nextTurnOutput() {
        return PLAYERS_TURN_MESSAGE.formatted(getCurrentPlayer().getId().toUpperCase()); // toUpper evtl. unnötig, weil id sowieso groß
    }

    public String winnerOutput() {
        return WIN_MESSAGE.formatted(getCurrentPlayer().getId().toUpperCase());
    }


    private List<Player> createPlayers(int numberOfPlayers, Board board) {
        List<Player> players = new ArrayList<>();
        char playerChar = PLAYER_CHAR;

        for (int i = 0; i < numberOfPlayers; i++) {
            boolean incremented;
            while (true) {
                incremented = false;
                for (char c : NON_PLAYER_CHARS) {
                    if (playerChar == c) {
                        playerChar++;
                        incremented = true;
                        break;
                    }
                }
                if (!incremented) {
                    break;
                }
            }

            Field startField = board.getStartFieldForPlayer(board, playerChar);
            players.add(new Player(String.valueOf(playerChar), startField, board));
            playerChar++;
        }

        return players;
    }

    /**
     * Loads a board from a file.
     *
     * @param filePath the path to the file
     * @return the board to be loaded
     * @throws IOException if an I/O error occurs
     */
    public Board loadBoardFromFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        List<String> lines = Files.readAllLines(path);

        Board board = new Board();
        board.initializeGrid(lines);

        return board;
    }


    // implement the string representation of the board
    public String getBoardView() {
        StringBuilder sb = new StringBuilder();
        Player currentPlayer = getCurrentPlayer();
        Field[][] grid = board.getGrid();

        for (int row = 0; row < grid.length; row++) {
            for (int column = 0; column < grid[row].length; column++) {
                Field field = grid[row][column];

                if (field.isForest()) {
                    if (board.isForestOccupied()) {
                        sb.append(OCCUPIED_FOREST);
                    } else {
                        sb.append(NOT_OCCUPIED_FOREST);
                    }
                }
                if (field != null) {
                    if (!field.isForest()) {
                        sb.append(field.toString(currentPlayer));
                    } else {
                        sb.append(NOTHING);
                    }
                } else {
                    sb.append(EMPTY);
                }

            }
            if (row < grid.length - 1) {
                sb.append(NEW_LINE);
            }

        }
        return sb.toString();
    }




}
