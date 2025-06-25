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

    /**
     * @return Return message identified by its ID
     */
    public Message getMessageById(int messageId)
    {
        return messageDAO.getMessageById(messageId);
    }
    
        
    /**
     * @return Deleted message if message existed, otherwise null
     */
    public Message deleteMessageById(int messageId)
    {
        Message message = messageDAO.getMessageById(messageId);
        if(message != null && messageDAO.deleteMessageById(messageId) > 0)
            return message;
        return null;
    }

    /**
     * @return Full updated message if successful, otherwise null
     */
    public Message patchMessageById(int messageId, String messageText)
    {
        if(!messageText.equals("") && messageText.length() < 256)
        {
            Message message = messageDAO.getMessageById(messageId);
            if(message != null)
            {
                if(messageDAO.patchMessageById(messageId, messageText) > 0)
                    return messageDAO.getMessageById(messageId);
            }
        }
        return null;
    }

    /**
     * @return a List of messages retrieved from the database for a given account ID
     */
    public List<Message> getAllMessagesByAccountId(int accountId)
    {
        return messageDAO.getAllMessagesByAccountId(accountId);
    }
}