package command;

import model.Session;
import model.SessionManager;

public class SwitchSession implements Command {



    @Override
    public CommandResult execute(String[] commandArguments) {
        Session activeSession = SessionManager.getActiveSession();
        if (activeSession == null) {
            return new CommandResult(CommandResultType.FAILURE, "No active session.");
        }


        String sessionId;
        try {
            sessionId = commandArguments[0];
        } catch (IllegalArgumentException e) {
            return new CommandResult(CommandResultType.FAILURE, "Invalid id");
        }


        if(sessionId.equals(activeSession.getSessionId())) {
            return new CommandResult(CommandResultType.FAILURE, "Cannot switch to itself");
        } else if(SessionManager.getActiveSession() == null) {
            return new CommandResult(CommandResultType.FAILURE, "no active session");
        }

        SessionManager.switchSession(sessionId);

        return new CommandResult(CommandResultType.SUCCESS, sessionId);
    }

    @Override
    public boolean isAvailable() {
        Session activeSession = SessionManager.getActiveSession();

        if (activeSession == null) {
            return false;
        }

        for(Session session : SessionManager.getAllSessions()) {
            if(!session.getSessionId().equals(activeSession.getSessionId())){
                return true;
            }
        }
        return false;
    }


    @Override
    public int getMinNumberOfArguments() {
        return 1;
    }
}
