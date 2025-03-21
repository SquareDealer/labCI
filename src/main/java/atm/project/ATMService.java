package atm.project;

import java.util.List;
import atm.project.exception.*;

/**
 * Реализует функциональность банкомата.
 */
public class ATMService {
    private Account account;

    /**
     * Создает новый банковский счет.
     * @param accountNumber Номер нового счета.
     */
    public void createAccount(String accountNumber) {
        this.account = new Account(accountNumber);
    }

    /**
     * Показывает текущий баланс счета.
     * @return Текущий баланс счета.
     * @throws AccountNotFoundException Если счет не создан.
     */
    public double checkBalance() throws AccountNotFoundException {
        if (account == null) {
            throw new AccountNotFoundException("No account found. Please create an account first.");
        }
        return account.getBalance();
    }

    /**
     * Пополняет счет.
     * @param amount Сумма пополнения.
     * @throws AccountNotFoundException Если счет не создан.
     * @throws InvalidAmountException Если сумма меньше или равна 0.
     */
    public void deposit(double amount) throws AccountNotFoundException, InvalidAmountException {
        if (account == null) {
            throw new AccountNotFoundException("No account found. Please create an account first.");
        }
        account.deposit(amount);
    }

    /**
     * Снимает деньги со счета.
     * @param amount Сумма для снятия.
     * @throws AccountNotFoundException Если счет не создан.
     * @throws InvalidAmountException Если сумма меньше или равна 0.
     * @throws InsufficientFundsException Если недостаточно средств.
     */
    public void withdraw(double amount) throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        if (account == null) {
            throw new AccountNotFoundException("No account found. Please create an account first.");
        }
        account.withdraw(amount);
    }

    /**
     * Возвращает историю всех транзакций.
     * @return Список транзакций.
     * @throws AccountNotFoundException Если счет не создан.
     */
    public List<Transaction> getTransactionHistory() throws AccountNotFoundException {
        if (account == null) {
            throw new AccountNotFoundException("No account found. Please create an account first.");
        }
        return account.getTransactionHistory();
    }
}

