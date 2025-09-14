package serialization;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import model.ChatSession;

public class ChatSessionSerializer {
    private static ChatSessionSerializer instance = null;

    public static ChatSessionSerializer getInstance() {
        if (instance == null) {
            instance = new ChatSessionSerializer();
        }
        return instance;
    }
    private ChatSessionSerializer() {
        // Private constructor to prevent instantiation
    }

    public void createBranchChatTextFileIfNeededAndWrite(ChatSession chatSession) throws IOException {
    String filePath = "chat_sessions/" + chatSession.getDestinationBranch().getId() + ".txt";
    File branchChatFile = new File(filePath);
    FileWriter writer = new FileWriter(branchChatFile);
    String message;
    

    if (!branchChatFile.exists()) {
        try {
            branchChatFile.getParentFile().mkdirs(); // Ensure directory exists
            branchChatFile.createNewFile();
            
                writer.write("Chat started for branch " + chatSession.getDestinationBranch().getName() + "\n");
                writer.flush();
            }
         catch (IOException e) {
            System.err.println("Error creating chat session text file: " + e.getMessage());
        }
    }

    while((message=chatSession.getNextMessage()) != null) {
        try {
            writer.write(message + "\n");
        } catch (IOException e) {
            System.err.println("Error writing to chat session text file: " + e.getMessage());
        }
    }
    writer.flush();
}

    public void deleteChatSessionFile(int branchId) 
    {
        String filePath = "chat_sessions/" + branchId + ".txt";
        File branchChatFile = new File(filePath);
        if (branchChatFile.exists()) {
            branchChatFile.delete();
        }
    }

}
