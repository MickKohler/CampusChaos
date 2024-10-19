package command;

import model.Session;
import model.SessionManager;

public class Show implements Command{
    private static final String INVALID_NUM_OF_ARG = "Invalid number of arguments.";
    private static final String NO_ACTIVE_SESSION = "No active session.";



    @Override
    public CommandResult execute(String[] commandArguments) {
        Session activeSession = SessionManager.getActiveSession();
        if (commandArguments.length != 0) {
            return new CommandResult(CommandResultType.FAILURE, INVALID_NUM_OF_ARG);
        }

        if (activeSession == null) {
            return new CommandResult(CommandResultType.FAILURE, NO_ACTIVE_SESSION);
        }


        return new CommandResult(CommandResultType.SUCCESS, activeSession.getBoardView());


    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public int getMinNumberOfArguments() {
        return 0;
    }
}
