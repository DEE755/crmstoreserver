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

//UNUSED
public void startChatSession(String commandWithArgs) {
            String[] parts = commandWithArgs.split(" ", 3); // Split into 3 parts: command, branchName, message
    if (parts.length >= 3) {
                try {
                    String branchName = parts[1];
                    String message = commandWithArgs.substring(parts[0].length() + 1 + branchName.length()); // Extract message after "StartChatSession "


                    
                    chatSessionSerializer.startChatSession(branchName, message, clientSocket);

                    logger.log(branchName + " started chat session with message: " + message);
                    System.out.println("Chat session started with message: " + message);
                    
                    clientSocket.getOutputStream().write("CHAT_STARTED\n".getBytes());
                    clientSocket.getOutputStream().flush();
                } catch (Exception ex) {
                    System.err.println("Error starting chat session: " + ex.getMessage());
                }
            } else {
                try {
                    clientSocket.getOutputStream().write("INVALID ARGUMENTS\n".getBytes());
                    clientSocket.getOutputStream().flush();
                } catch (Exception e) {
                    System.err.println("Error sending invalid arguments response: " + e.getMessage());
                }
            }
           
        }

    }
    

