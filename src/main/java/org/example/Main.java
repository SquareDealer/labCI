package org.example;

import java.util.Scanner;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Исключение, выбрасываемое при недостатке средств на счете.
 */
class InsufficientFundsException extends Exception {
    /**
     * Конструктор исключения.
     * @param message Сообщение об ошибке.
     */
    public InsufficientFundsException(String message) {
        super(message);
    }
}

/**
 * Исключение, выбрасываемое при попытке использовать некорректную сумму.
 */
class InvalidAmountException extends Exception {
    /**
     * Конструктор исключения.
     * @param message Сообщение об ошибке.
     */
    public InvalidAmountException(String message) {
        super(message);
    }
}

/**
 * Исключение, выбрасываемое при отсутствии счета.
 */
class AccountNotFoundException extends Exception {
    /**
     * Конструктор исключения.
     * @param message Сообщение об ошибке.
     */
    public AccountNotFoundException(String message) {
        super(message);
    }
}

/**
 * Представляет банковскую транзакцию.
 */
class Transaction {
    private double amount;
    private LocalDateTime dateTime;
    private String type;

    /**
     * Создает новую транзакцию.
     * @param amount Сумма транзакции.
     * @param type Тип транзакции (Deposit или Withdrawal).
     */
    public Transaction(double amount, String type) {
        this.amount = amount;
        this.dateTime = LocalDateTime.now();
        this.type = type;
    }

    /**
     * Возвращает строковое представление транзакции.
     * @return Строка с информацией о транзакции.
     */
    @Override
    public String toString() {
        return String.format("%s: %s %.2f at %s",
                type, type.equals("Withdrawal") ? "-" : "+", amount, dateTime);
    }
}

/**
 * Представляет банковский счет.
 */
class Account {
    private double balance;
    private List<Transaction> transactions;
    private String accountNumber;

    /**
     * Создает новый банковский счет.
     * @param accountNumber Номер счета.
     */
    public Account(String accountNumber) {
        this.balance = 0.0;
        this.accountNumber = accountNumber;
        this.transactions = new ArrayList<>();
    }

    /**
     * Возвращает текущий баланс счета.
     * @return Текущий баланс счета.
     */
    public double getBalance() {
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
    public void deposit(double amount) throws InvalidAmountException {
        if (amount <= 0) {
            throw new InvalidAmountException("Deposit amount must be positive");
        }
        balance += amount;
        transactions.add(new Transaction(amount, "Deposit"));
    }

    /**
     * Снимает деньги со счета.
     * @param amount Сумма для снятия.
     * @throws InvalidAmountException Если сумма меньше или равна 0.
     * @throws InsufficientFundsException Если недостаточно средств на счете.
     */
    public void withdraw(double amount) throws InvalidAmountException, InsufficientFundsException {
        if (amount <= 0) {
            throw new InvalidAmountException("Withdrawal amount must be positive");
        }
        if (amount > balance) {
            throw new InsufficientFundsException(
                    String.format("Insufficient funds. Current balance: %.2f, Requested: %.2f",
                            balance, amount));
        }
        balance -= amount;
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

/**
 * Реализует функциональность банкомата.
 */
class ATM {
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

public class Main {
    private static ATM atm = new ATM();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;

        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        createAccount();
                        break;
                    case "2":
                        checkBalance();
                        break;
                    case "3":
                        deposit();
                        break;
                    case "4":
                        withdraw();
                        break;
                    case "5":
                        showTransactionHistory();
                        break;
                    case "6":
                        running = false;
                        System.out.println("Thank you for using our ATM. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("=== ATM Menu ===");
        System.out.println("1. Create Account");
        System.out.println("2. Check Balance");
        System.out.println("3. Deposit");
        System.out.println("4. Withdraw");
        System.out.println("5. Show Transaction History");
        System.out.println("6. Exit");
        System.out.print("Enter your choice (1-6): ");
    }

    private static void createAccount() {
        System.out.print("Enter account number: ");
        String accountNumber = scanner.nextLine().trim();
        atm.createAccount(accountNumber);
        System.out.println("Account created successfully. Account number: " + accountNumber);
    }

    private static void checkBalance() throws AccountNotFoundException {
        double balance = atm.checkBalance();
        System.out.printf("Current balance: %.2f%n", balance);
    }

    private static void deposit() throws AccountNotFoundException, InvalidAmountException {
        System.out.print("Enter amount to deposit: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            atm.deposit(amount);
            System.out.printf("Successfully deposited %.2f. New balance: %.2f%n",
                    amount, atm.checkBalance());
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Invalid amount format");
        }
    }

    private static void withdraw()
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        System.out.print("Enter amount to withdraw: ");
        try {
            double amount = Double.parseDouble(scanner.nextLine().trim());
            atm.withdraw(amount);
            System.out.printf("Successfully withdrew %.2f. New balance: %.2f%n",
                    amount, atm.checkBalance());
        } catch (NumberFormatException e) {
            throw new InvalidAmountException("Invalid amount format");
        }
    }

    private static void showTransactionHistory() throws AccountNotFoundException {
        List<Transaction> history = atm.getTransactionHistory();
        if (history.isEmpty()) {
            System.out.println("No transactions yet.");
        } else {
            System.out.println("Transaction History:");
            for (Transaction t : history) {
                System.out.println(t);
            }
        }
    }
}