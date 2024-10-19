package command;

import model.Session;
import model.SessionManager;

public class DeleteSession implements Command {


    @Override
    public CommandResult execute(String[] commandArguments) {

        String sessionId;
        try {
            sessionId = commandArguments[0];
        } catch (IllegalArgumentException e) {
            return new CommandResult(CommandResultType.FAILURE,"Invalid session id");
        }

        Session sessionToDelete = SessionManager.getSessionById(sessionId);

        if(!SessionManager.getAllSessions().contains(sessionToDelete)) { // TODO: evtl. besser mit '== null' checken
            return new CommandResult(CommandResultType.FAILURE, "session does not exist");
        }

        SessionManager.deleteSession(sessionId);
        return new CommandResult(CommandResultType.SUCCESS, sessionId);
    }

    @Override
    public boolean isAvailable() {
        return !SessionManager.getAllSessions().isEmpty();
    }

    @Override
    public int getMinNumberOfArguments() {
        return 1;
    }
}
