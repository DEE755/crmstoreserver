import java.net.Socket;
import serialization.ChatSessionSerializer;

public class ChatHandler {
    private Socket clientSocket;
    private serialization.Logger logger;
    private ChatSessionSerializer chatSessionSerializer;

    public ChatHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
        this.logger = serialization.Logger.getInstance();
        chatSessionSerializer = ChatSessionSerializer.getInstance();
    }

    }
    

