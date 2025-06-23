package Service;

import Model.Message;
import DAO.MessageDAO;

import java.util.List;

public class MessageService {

    private MessageDAO messageDAO;
    private final AccountService accountService;

    public MessageService(AccountService accountService)
    {
        this.accountService = accountService;
        messageDAO = new MessageDAO();
    }

    /**
     * persists a message and performs format checks
     * @param message
     * @return The persisted message if successful and null otherwise
     */
    public Message addMessage(Message message)
    {
        if(!message.getMessage_text().equals("") 
            && message.getMessage_text().length() < 256 
            && accountService.getAccount(message.getPosted_by()) != null)
            return messageDAO.insertMessage(message);
        return null;

    }

    /**
     * @return Return all messages
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }
    
}
