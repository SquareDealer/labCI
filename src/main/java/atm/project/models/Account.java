package atm.project.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import atm.project.exception.*;

/**
 * Представляет банковский счет.
 */
public class Account {
    private BigDecimal balance;
    private final List<Transaction> transactions;
    private final String accountNumber;

    /**
     * Создает новый банковский счет.
     * @param accountNumber Номер счета.
     */
    public Account(String accountNumber) {
        this.balance = BigDecimal.ZERO;
        this.accountNumber = accountNumber;
        this.transactions = new ArrayList<>();
    }

    /**
     * Возвращает текущий баланс счета.
     * @return Текущий баланс счета.
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Возвращает номер счета.
     * @return Номер счета.
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Пополняет счет на указанную сумму.
     * @param amount Сумма для пополнения.
     * @throws InvalidAmountException Если сумма меньше или равна 0.
     */
    public void deposit(BigDecimal amount) throws InvalidAmountException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive");
        }
        balance = balance.add(amount);
        transactions.add(new Transaction(amount, "Deposit"));
    }

    /**
     * Снимает деньги со счета.
     * @param amount Сумма для снятия.
     * @throws InvalidAmountException Если сумма меньше или равна 0.
     * @throws InsufficientFundsException Если недостаточно средств на счете.
     */
    public void withdraw(BigDecimal amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }
        if (amount.compareTo(balance) > 0) {
            throw new InsufficientFundsException(
                    String.format("Insufficient funds. Current balance: %s, Requested: %s",
                            balance, amount));
        }
        balance = balance.subtract(amount);
        transactions.add(new Transaction(amount, "Withdrawal"));
    }

    /**
     * Возвращает список всех транзакций по счету.
     * @return Список транзакций.
     */
    public List<Transaction> getTransactionHistory() {
        return new ArrayList<>(transactions);
    }
}
