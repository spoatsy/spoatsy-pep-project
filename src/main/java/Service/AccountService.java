package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    
    private AccountDAO accountDAO;

    public AccountService(){
        accountDAO = new AccountDAO();
    }

    /**
     * Persists the account
     * @param account an account object
     * @return The persisted account if successful
     */
    public Account addAccount(Account account)
    {
        return accountDAO.insertAccount(account);
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
