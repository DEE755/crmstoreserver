
import java.io.*;
import java.net.Socket;
import java.util.Map;

class ClientHandler implements Runnable {
    private final Socket socket;
    private final Map<String, Boolean> sessions;
    private String currentUser = null;

    ClientHandler(Socket socket, Map<String, Boolean> sessions) 
    {
        this.socket = socket;
        this.sessions = sessions;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(
                 new InputStreamReader(socket.getInputStream(), "UTF-8"));
             PrintWriter out = new PrintWriter(
                 new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true)) {

            out.println("OK CONNECTED");

            String line;
            while ((line = in.readLine()) != null) {
                String response = handleCommand(line.trim());
                // For multi-line responses, you can print several lines then "END"
                out.println(response);
                if ("BYE".equals(response)) break;
            }

        } catch (IOException e) {
            System.err.println("I/O error with " + socket + ": " + e.getMessage());
        } finally {
            // free session if logged in
            if (currentUser != null) {
                sessions.remove(currentUser);
            }
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private String handleCommand(String cmd) {
        if (cmd.isEmpty()) return "ERR EMPTY";
        String[] parts = cmd.split("\\s+");
        String op = parts[0].toUpperCase();

        switch (op) {
            case "PING":
                return "OK PONG";

            case "ECHO":
                return (parts.length > 1) ? "OK " + cmd.substring(5) : "ERR USAGE ECHO <text>";

            case "LOGIN":
                // LOGIN <username> <password>
                if (parts.length < 3) return "ERR USAGE LOGIN <user> <pass>";
                String user = parts[1];
                String pass = parts[2];

                // TODO: replace with real check against employees file/.ser loaded at server start
                if (!isValidUser(user, pass)) return "ERR BAD_CREDENTIALS";

                // prevent duplicate login
                if (sessions.putIfAbsent(user, Boolean.TRUE) != null) {
                    return "ERR ALREADY_LOGGED_IN";
                }
                currentUser = user;
                // TODO: include real role/branch from repository
                return "OK LOGGED_IN user=" + user + " role=SELLER branch=A";

            case "LOGOUT":
                if (currentUser != null) {
                    sessions.remove(currentUser);
                    currentUser = null;
                    return "OK LOGGED_OUT";
                }
                return "ERR NOT_LOGGED_IN";

            case "EXIT":
                return "BYE";

            default:
                return "ERR UNKNOWN_CMD";
        }
    }

    // Stub for now — swap with repository-backed validation
    private boolean isValidUser(String user, String pass) {
        return ("admin".equals(user) && "admin123".equals(pass))
            || ("alice".equals(user) && "pass".equals(pass));
    }
}

}
