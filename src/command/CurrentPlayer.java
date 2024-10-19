package command;

import model.SessionManager;

import java.util.Objects;

public class CurrentPlayer implements Command {
    private static final String PLAYER_WITH_DICE_ROLL = "It's Player %s's tur. Dice Roll: %d";
    private static final String PLAYER_WITHOUT_DICE_ROLL = "It's Player %s's turn. Diec Roll: ?";

    private static final String INVALID_NUM_OF_ARG = "Invalid number of arguments.";
    private static final String SESSION_HAS_ENDED = "Session has ended.";
    private static final String NO_ACTIVE_SESSION = "No active session.";

    @Override
    public CommandResult execute(String[] commandArguments) {
        if (commandArguments.length != 0) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_NUM_OF_ARG);
        }
        if (!SessionManager.hasActiveSession()) {
            return new CommandResult(CommandResultType.FAILURE, NO_ACTIVE_SESSION);
        }
        if (Objects.requireNonNull(SessionManager.getActiveSession()).hasWinner()) {
            return new CommandResult(CommandResultType.FAILURE, SESSION_HAS_ENDED);
        }

        StringBuilder output = new StringBuilder();
        int diceRoll = SessionManager.getActiveSession().getCurrentPlayer().getDiceRoll();
        String currentPlayer = SessionManager.getActiveSession().getCurrentPlayer().getId().toUpperCase();

        if (diceRoll != 0) { // player has rolled the dice
            output.append(PLAYER_WITH_DICE_ROLL.formatted(currentPlayer, diceRoll));
        } else {
            output.append(PLAYER_WITHOUT_DICE_ROLL.formatted(currentPlayer));
        }
        return new CommandResult(CommandResultType.SUCCESS, output.toString());
    }

    @Override
    public boolean isAvailable() {
        if (!SessionManager.hasActiveSession()) {
            return false;
        }

        return !Objects.requireNonNull(SessionManager.getActiveSession()).hasWinner();


    }

    @Override
    public int getMinNumberOfArguments() {
        return 0;
    }
}
