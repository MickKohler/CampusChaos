package model;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * Manages all sessions on a meta level.
 *
 * @author Mick Kohler
 */
public final class SessionManager {
    private static Queue<Session> sessionQueue = new LinkedList<>();


    private SessionManager() {
        throw new UnsupportedOperationException("SessionManager as static utility class cannot be instantiated");
    }

    /**
     * Returns a list of all sessions.
     *
     * @return a list of all sessions
     */
    public static List<Session> getAllSessions() {
        return new LinkedList<>(sessionQueue);
    }

    public static boolean hasActiveSession() {
        for (Session session : sessionQueue) {
            if(session.isActiveSession()) {
                return true;
            }
        }
        return false;
    }




    /**
     * Checks if the given session id is valid.
     *
     * @param sessionId the id of the session
     * @return true if the session id is valid, false otherwise
     */
    public static boolean isValidSessionId(String sessionId) { // TODO: überlegen, ob in Session tun
        return sessionId.matches("[a-zA-Z0-9]+");
    }

    /**
     * Returns the active session.
     *
     * @return the active session
     */
    public static Session getActiveSession() {
        for (Session session : sessionQueue) {
            if(session.isActiveSession()) {
                return session;
            }
        }
        return null;
    }


    /**
     * Adds a new session with the given parameters.
     *
     * @param sessionId the id of the session
     * @param filePath the path to the board file
     * @param numberOfPlayers the number of players
     * @param seed the seed for the random number generator
     */
    public static void addSession(String sessionId, String filePath, int numberOfPlayers, Long seed) throws IOException {
        Session sessionToBeInactive = getActiveSession();

        if (sessionToBeInactive != null) {
            sessionToBeInactive.deactivateSession();
        }

//        Board board = new Board();
//        board.loa(filePath);
//        List<Player> players = new ArrayList<>();

        Session sessionToCreate = new Session(sessionId, filePath, numberOfPlayers, seed);

        sessionQueue.add(sessionToCreate);
        sessionToCreate.activateSession();
    }



    /**
     * Deletes the session with the given id.
     *
     * @param sessionId the id of the session to delete
     */
    public static void deleteSession(String sessionId) {
        Session sessionToDelete = getSessionById(sessionId);

        if(Objects.requireNonNull(sessionToDelete).isActiveSession()) {
            sessionToDelete.deactivateSession();
        }

        for(Session session : sessionQueue) {
            if (session.getSessionId().equals(sessionToDelete.getSessionId())) {
                sessionQueue.remove(sessionToDelete);
                break;
            }
        }

    }


    /**
     * switches the active session to the session with the given id
     *
     * @param targetSessionId the id of the session to switch to
     */
    public static void switchSession(String targetSessionId) {
        Session originalSession = getActiveSession();
        Objects.requireNonNull(originalSession).deactivateSession();

        Session sessionToSwitchTo = getSessionById(targetSessionId);
        Objects.requireNonNull(sessionToSwitchTo).activateSession();

    }



    /**
     * Gets the session with the given id.
     *
     * @param sessionId the id of the session to get
     * @return the session with the given id
     */
    public static Session getSessionById(String sessionId) {
        for(Session session : sessionQueue) {
            if(session.getSessionId().equals(sessionId)) {
                return session;
            }
        }
        return null;
    }
}
