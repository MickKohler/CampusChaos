package command;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Help implements Command {
    private static final String HELP_DESCRIPTION = "help: Lists all commands which could be executed in the current state of the program.";
    private static final String QUIT_DESCRIPTION = "quit: Ends program.";
    private static final String SHOW_SESSION_DESCRIPTION = "show session: Lists all available sessions.";
    private static final String START_SESSION_DESCRIPTION = "start session <session_id>,<file_to_field>, <num_of_players> (<seed>): Starts a new session.";
    private static final String SWITCH_SESSION_DESCRIPTION = "switch session <session_id>: Changes the active session, the session to which all game commands are applied.";
    private static final String DELETE_SESSION_DESCRIPTION = "delete session <session_id>: Deletes a session.";
    private static final String SHOW_DESCRIPTION = "show: Displays the current board of the game.";
    private static final String CURRENT_PLAYER_DESCRIPTION = "current player: Displays the current player.";
    private static final String ROLL_DICE_DESCRIPTION = "roll dice (<roll_dice>): Rolls the dice. If the session was created without a seed the command expects the parameter dice_roll, which specifies which number was rolled.";
    private static final String NEW_FIGURE_DESCRIPTION = "new figure: Brings a figure into play for the current player and places it in the player's starting position.";
    private static final String MOVE_DESCRIPTION = "move <figure> (<distance> <direction>)...: Moves a figure on the board.";
    private static final String MOVE_OBSTACLE_DESCRIPTION = "move obstacle (<distance> <direction>): Moves an obstacle that has just been reached reached by the player.";
    private static final String SKIP_TURN_DESCRIPTION = "skip turn: skips the current player's turn.";
    private static final String REMATCH_DESCRIPTION = "rematch: Starts a new game with the same players and the same board.";
    private static final String ERROR_INVALID_COMMAND_NAME = "Unexpected value %s";
    private static final int MIN_NUMBER_OF_ARGUMENTS = 0;

    @Override
    public CommandResult execute(String[] commandArguments) {
        StringBuilder helpMessage = new StringBuilder();
        CommandHandler commandHandler = new CommandHandler();

        Map<String, Command> availabeCommands = new HashMap<>();

        for(Entry<String, Command> entry : commandHandler.getAllCommands().entrySet()) {
            if(entry.getValue().isAvailable()) {
                availabeCommands.put(entry.getKey(), entry.getValue());
            }
        }

        Map<String,Command> sortedAvailableCommands = new TreeMap<>(availabeCommands);
        for (Entry<String, Command> entry : sortedAvailableCommands.entrySet()) {
            helpMessage.append(getCommandDescription(entry.getKey())).append(System.lineSeparator());
        }

        return new CommandResult(CommandResultType.SUCCESS, helpMessage.toString().trim());

    }

    private String getCommandDescription(String commandName) {
        return switch (commandName) {
            case "help" -> HELP_DESCRIPTION;
            case "quit" -> QUIT_DESCRIPTION;
            case "show session" -> SHOW_SESSION_DESCRIPTION;
            case "start session" -> START_SESSION_DESCRIPTION;
            case "switch session" -> SWITCH_SESSION_DESCRIPTION;
            case "delete session" -> DELETE_SESSION_DESCRIPTION;
            case "show" -> SHOW_DESCRIPTION;
            case "current player" -> CURRENT_PLAYER_DESCRIPTION;
            case "roll dice" -> ROLL_DICE_DESCRIPTION;
            case "new figure" -> NEW_FIGURE_DESCRIPTION;
            case "move" -> MOVE_DESCRIPTION;
            case "move obstacle" -> MOVE_OBSTACLE_DESCRIPTION;
            case "skip turn" -> SKIP_TURN_DESCRIPTION;
            case "rematch" -> REMATCH_DESCRIPTION;
            default -> throw new IllegalStateException(ERROR_INVALID_COMMAND_NAME.formatted(commandName));
        };
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public int getMinNumberOfArguments() {
        return MIN_NUMBER_OF_ARGUMENTS;
    }
}
