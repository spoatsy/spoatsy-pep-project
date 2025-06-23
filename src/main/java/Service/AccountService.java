package Service;

import Model.Account;
import DAO.AccountDAO;

public class AccountService {
    
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /**
     * Persists the account and checks if account exists, has a non-empty username, and length greater than 3
     * @param account an account object
     * @return The persisted account if successful and null otherwise
     */
    public Account addAccount(Account account)
    {
        if(!account.getUsername().equals("") 
            && account.getPassword().length() > 3 
            && getAccount(account) == null)
            return accountDAO.insertAccount(account);
        return null;
    }

    /**
     * Retrieve user account
     * @return user account
     */
    public Account getAccount(Account account)
    {
        return accountDAO.getAccount(account.getUsername(), account.getPassword());
    }
}
