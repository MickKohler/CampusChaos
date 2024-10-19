package command;

import model.Session;
import model.SessionManager;
import java.io.IOException;

public class StartSession implements Command {
    private static final int MIN_NUMBER_OF_ARGUMENTS = 3;
    private static final int MAX_NUMBER_OF_ARGUMENTS = 4;

    @Override
    public CommandResult execute(String[] commandArguments) {

        String sessionId;
        String filePath;
        int numberOfPlayers;
        Long seed;
        try {
            sessionId = commandArguments[0];
            filePath = commandArguments[1];
            numberOfPlayers = Integer.parseInt(commandArguments[2]);
            seed = (commandArguments.length == 4) ? Long.parseLong(commandArguments[3]) : null;

            if (numberOfPlayers < 2) {
                return new CommandResult(CommandResultType.FAILURE, "Number of players must be at least 2");
            }
        } catch (IllegalStateException e) {
            return new CommandResult(CommandResultType.FAILURE, "Invalid input arguments");
        }


        for (Session session : SessionManager.getAllSessions()) {
            if (session.getSessionId().equals(sessionId)) {
                return new CommandResult(CommandResultType.FAILURE, "Session ID already exists");
            }
        }

        StringBuilder output = new StringBuilder();
        if (SessionManager.isValidSessionId(sessionId) && !filePath.isEmpty()) {
            try {
                SessionManager.addSession(sessionId, filePath, numberOfPlayers, seed);
            } catch (IOException e) {
                return new CommandResult(CommandResultType.FAILURE, "Session could not be created");
            }

            if(SessionManager.getActiveSession() != null) {
                output.append(SessionManager.getActiveSession().getBoard().toString()).append(System.lineSeparator());
                output.append(sessionId).append(System.lineSeparator()).append(SessionManager.getActiveSession().nextTurnOutput());
            }

//            Player currentPlayer = SessionManager.getActiveSession().getCurrentPlayer();
//            output += "It's player " + Character.toUpperCase(currentPlayer.getId()) + "'s turn.";

        }
        return new CommandResult(CommandResultType.SUCCESS, output.toString());
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
