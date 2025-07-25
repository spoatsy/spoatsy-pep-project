package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.nullable;

import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController()
    {
        this.accountService = new AccountService();
        this.messageService = new MessageService(accountService);
    }

    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("example-endpoint", this::exampleHandler);
        app.post("/register", this::postRegisterAccountHandler);
        app.post("/login", this::postLoginAccountHandler);
        app.post("/messages", this::postCreateMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByIdHandler);
        app.patch("/messages/{message_id}", this::patchMessageByIdHandler);
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAccountIdHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void exampleHandler(Context context) {
        context.json("sample text");
    }

    /**
     * Handler to create a new Account
     * The API returns 400 when AccountService returns null
     * @param ctx the context object
     * @throws JsonProcessingException thrown when issue converting JSON to object
     */
    private void postRegisterAccountHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account addedAccount = accountService.addAccount(account);
        if(addedAccount != null)
            ctx.json(mapper.writeValueAsString(addedAccount));
        else
            ctx.status(400);
    }

    /**
     * Handler to login to account
     * The API returns 401 when AccountService returns null
     * @param ctx the context object
     */
    private void postLoginAccountHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        Account loginAccount = accountService.getAccount(account);
        if(loginAccount != null)
            ctx.json(mapper.writeValueAsString(loginAccount));
        else
            ctx.status(401);
    }

    /**
     * Handler to create a mesage
     * The API returns 400 when MessageService returns null
     * @param ctx the context object
     */
    private void postCreateMessageHandler(Context ctx) throws JsonProcessingException
    {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message addedMessage = messageService.addMessage(message);
        if(addedMessage != null)
            ctx.json(mapper.writeValueAsString(addedMessage));
        else
            ctx.status(400);
    }

    /**
     * Handler to retrieve all messages
     * The API returns status code 200
     * @param ctx the context object
     */
    private void getAllMessagesHandler(Context ctx)
    {
        ctx.json(messageService.getAllMessages());
    }

    /**
     * Handler to retrieve a message by its ID
     * The API returns status code 200
     * @param ctx the context object
     */
    private void getMessageByIdHandler(Context ctx)
    {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = messageService.getMessageById(messageId);
        if(message != null)
            ctx.json(message);
    }

    /**
     * Hander to delete a messaage by its ID
     * The API returns status code 200
     * @param ctx the context object
     */
    private void deleteMessageByIdHandler(Context ctx)
    {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageById(messageId);
        if(deletedMessage != null)
            ctx.json(deletedMessage);
    }

    /**
     * Handler to patch a message text identified by its ID. Any other provided information is ignored.
     * The API returns status code 200 on success and 400 otherwise
     * @param ctx the context objectg
     */
    private void patchMessageByIdHandler(Context ctx) throws JsonProcessingException
    {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));

        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        Message patchedMessage = messageService.patchMessageById(messageId, message.getMessage_text());
        if(patchedMessage != null)
            ctx.json(mapper.writeValueAsString(patchedMessage));
        else
            ctx.status(400);
    }

    /**
     * Handler to retrieve all messages for an account given an account ID.
     * @param ctx the context object
     */
    private void getAllMessagesByAccountIdHandler(Context ctx)
    {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        List<Message> messages = messageService.getAllMessagesByAccountId(accountId);
        ctx.json(messages);
    }
}