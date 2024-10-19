package command;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This class handles the user input and executes the commands.
 *
 * @author Programmieren-Team
 * @author untxx
 */
public final class CommandHandler {
    private static final String COMMAND_SEPARATOR_REGEX = " +";
    private static final String ERROR_PREFIX = "Error, ";
    private static final String COMMAND_NOT_FOUND_FORMAT = "command '%s' not found!";
    private static final String WRONG_ARGUMENTS_COUNT_FORMAT = "wrong number of arguments for command '%s'!";
    private static final String HELP_COMMAND_NAME = "help";
    private static final String START_SESSION_COMMAND_NAME = "start session";
    private static final String DELETE_SESSION_COMMAND_NAME = "delete session";
    private static final String SWITCH_SESSION_COMMAND_NAME = "switch session";
    private static final String SHOW_SESSION_COMMAND_NAME= "show session";
    private static final String MOVE_COMMAND_NAME = "move";
    private static final String CURRENT_PLAYER_COMMAND_NAME = "current player";
    private static final String NEW_FIGURE_COMMAND_NAME = "new figure";
    private static final String ROLL_DICE_COMMAND_NAME = "roll dice";
    private static final String SHOW_BOARD_COMMAND_NAME = "show";
    private static final String SKIP_TURN_COMMAND = "skip turn";
    private static final String MOVE_OBSTACLE_COMMAND = "move obstacle";
    private static final String REMATCH_COMMAND = "rematch";
    private static final String QUIT_COMMAND_NAME = "quit";
    private static final String INVALID_RESULT_TYPE_FORMAT = "Unexpected value: %s";
    private static final String ERROR_NON_AVAILABLE = "command '%s' is not available!";
    private final Map<String, Command> commands;
    private boolean running = false;

    /**
     * Constructs new CommandHandler.
     */
    public CommandHandler() {
        this.commands = new HashMap<>();
        this.initCommands();
    }

    /**
     * Starts the interaction with the user.
     */
    public void handleUserInput() {
        this.running = true;

        try (Scanner scanner = new Scanner(System.in)) {
            while (running && scanner.hasNextLine()) {
                executeCommand(scanner.nextLine());
            }
        }
    }

    /**
     * Quits the interaction with the user.
     */
    public void quit() {
        this.running = false;
    }

    private void executeCommand(String commandWithArguments) {
        String[] splittedCommand = commandWithArguments.trim().split(COMMAND_SEPARATOR_REGEX);

        // Default to the first word as the command name
        String commandName = splittedCommand[0];
        String[] commandArguments;

        // Check if the first two words form a valid command
        if (splittedCommand.length > 1) {
            String combinedCommandName = commandName + " " + splittedCommand[1];
            if (commands.containsKey(combinedCommandName)) {
                commandName = combinedCommandName;
                commandArguments = Arrays.copyOfRange(splittedCommand, 2, splittedCommand.length);
            } else {
                // Fall back to the first word only
                commandArguments = Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length);
            }
        } else {
            commandArguments = Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length);
        }

        // Execute the command
        executeCommand(commandName, commandArguments);
    }


    // else if (!commands.get(commandName).isAvailable()) {  // TODO: nicht sicher ob das so richtig ist
    //            System.err.println(ERROR_PREFIX + ERROR_NON_AVAILABLE.formatted(commandName));
    private void executeCommand(String commandName, String[] commandArguments) {
        if (!commands.containsKey(commandName)) {
            System.err.println(ERROR_PREFIX + COMMAND_NOT_FOUND_FORMAT.formatted(commandName));
        } else if (!(commandArguments.length >= commands.get(commandName).getMinNumberOfArguments())) {
            System.err.println(ERROR_PREFIX + WRONG_ARGUMENTS_COUNT_FORMAT.formatted(commandName));
        }else {
            CommandResult result = commands.get(commandName).execute(commandArguments);
            String output = switch (result.getType()) {
                case SUCCESS -> result.getMessage();
                case FAILURE -> ERROR_PREFIX + result.getMessage();
                case QUIT -> null; // evtl. ""
            };
            if (output != null) {
                switch (result.getType()) {
                    case SUCCESS -> System.out.println(output);
                    case FAILURE -> System.err.println(output);
                    case QUIT -> quit();
                    default -> throw new IllegalStateException(INVALID_RESULT_TYPE_FORMAT.formatted(result.getType()));
                }
            }
        }
    }

    private void initCommands() {
        this.addCommand(QUIT_COMMAND_NAME, new Quit(this));
        this.addCommand(HELP_COMMAND_NAME, new Help());
        this.addCommand(MOVE_COMMAND_NAME, new Move());
        this.addCommand(NEW_FIGURE_COMMAND_NAME, new NewFigure());
        this.addCommand(CURRENT_PLAYER_COMMAND_NAME, new CurrentPlayer());
        this.addCommand(MOVE_OBSTACLE_COMMAND, new MoveObstacle());
        this.addCommand(ROLL_DICE_COMMAND_NAME, new RollDice());
        this.addCommand(DELETE_SESSION_COMMAND_NAME, new DeleteSession());
        this.addCommand(SHOW_BOARD_COMMAND_NAME, new Show());
        this.addCommand(SHOW_SESSION_COMMAND_NAME, new ShowSession());
        this.addCommand(START_SESSION_COMMAND_NAME, new StartSession());
        this.addCommand(SWITCH_SESSION_COMMAND_NAME, new SwitchSession());
        this.addCommand(REMATCH_COMMAND, new Rematch());
        this.addCommand(SKIP_TURN_COMMAND, new SkipTurn());

    }

    private void addCommand(String commandName, Command command) {
        this.commands.put(commandName, command);
    }

    /**
     * Gets all existing commands used for help-command.
     * @return returns Map of all commands.
     */
    public Map<String, Command> getAllCommands() {
        return Collections.unmodifiableMap(commands);
    }

}
