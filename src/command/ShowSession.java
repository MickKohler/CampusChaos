package command;

import model.Player;
import model.Session;
import model.SessionManager;

import java.util.List;
import java.util.Arrays;


/**
 * Implementation of the "show session" command.
 *
 * @author Mick Kohler
 */
public class ShowSession implements Command {
    private static final String SHOW_SESSION_FORMAT = "%s -> Players: %s | Map: %s%s";
    private static final char PLAYER_SEPARATOR = ',';
    private static final String OPTIONAL_SEED_FORMAT = " | Seed: ";
    private static final String EMPTY_STRING_FORMAT = "";
    private static final String ACTIVE_SESSION_FORMAT = "*";
    private static final String ERROR_INVALID_SESION_ID = "Session ID '%s' not found: ";
    private static final String ERROR_INVALID_NUMBER_OF_ARGUMENTS = "Invalid number of arguments.";

    @Override
    public CommandResult execute(String[] commandArguments) {
        StringBuilder outputBuilder = new StringBuilder();
        List<Session> sessions = SessionManager.getAllSessions();


        if (commandArguments.length == 0) {
            // Show all sess
            for (Session session : sessions) {
                outputBuilder.append(formatSession(session));
                outputBuilder.append(System.lineSeparator());
            }
        } else if (commandArguments.length == 1) {
            // Show specific session by session_id
            String sessionId = commandArguments[0];
            Session session = null;
            for (Session toIterate : sessions) {
                if (toIterate.getSessionId().equals(sessionId)) {
                    session = toIterate;
                    break;
                }
            }

            if (session == null) {
                return new CommandResult(CommandResultType.FAILURE, ERROR_INVALID_SESION_ID.formatted(sessionId));
            }

            outputBuilder.append(formatSession(session));
        } else {
            return new CommandResult(CommandResultType.FAILURE, ERROR_INVALID_NUMBER_OF_ARGUMENTS);
        }

        return new CommandResult(CommandResultType.SUCCESS, outputBuilder.toString().trim());
    }

    @Override
    public boolean isAvailable() {
        return !SessionManager.getAllSessions().isEmpty();
    }

    @Override
    public int getMinNumberOfArguments() {
        return 0;
    }

    private String formatSession(Session session) {
        StringBuilder playersBuilder = new StringBuilder();
        List<Player> players = session.getPlayers();

        // Collect and sort players' IDs alphabetically
        char[] playerIds = new char[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playerIds[i] = players.get(i).getId().toUpperCase().charAt(0);
        }

        Arrays.sort(playerIds);

        for (int i = 0; i < playerIds.length; i++) {
            playersBuilder.append(playerIds[i]);
            if (i < playerIds.length - 1) {
                playersBuilder.append(PLAYER_SEPARATOR);
            }
        }

        String sessionId = session.getSessionId();
        String mapPath = session.getFilePath();


        String seedPart = session.getSeed() != null ? OPTIONAL_SEED_FORMAT + session.getSeed() : EMPTY_STRING_FORMAT;

        // Mark the active session with an asterisk
        if (session.equals(SessionManager.getActiveSession())) {
            sessionId += ACTIVE_SESSION_FORMAT;
        }

        return SHOW_SESSION_FORMAT.formatted(sessionId, playersBuilder.toString(), mapPath, seedPart);
    }
}
