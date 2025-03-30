package atm.project.ui;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import atm.project.service.ATMService;
import atm.project.models.Transaction;
import atm.project.exception.*;

public class App {
    private static final ATMService atm = new ATMService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void run() {
        boolean running = true;

        while (running) {
            displayMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1" -> createAccount();
                    case "2" -> checkBalance();
                    case "3" -> deposit();
                    case "4" -> withdraw();
                    case "5" -> showTransactionHistory();
                    case "6" -> {
                        running = false;
                        System.out.println("Thank you for using our ATM. Goodbye!");
                    }
                    default -> System.out.println("Invalid option. Please try again.");
                }
            } catch (AccountNotFoundException | InvalidAmountException | InsufficientFundsException e) {
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
        BigDecimal balance = atm.checkBalance();
        System.out.printf("Current balance: %s%n", balance);
    }

    private static void deposit() throws AccountNotFoundException, InvalidAmountException {
        System.out.print("Enter amount to deposit: ");
        try {
            BigDecimal amount = scanner.nextBigDecimal();
            scanner.nextLine();
            atm.deposit(amount);
            System.out.printf("Successfully deposited %s. New balance: %s%n", amount, atm.checkBalance());
        } catch (java.util.InputMismatchException e) {
            scanner.nextLine();
            throw new InvalidAmountException("Invalid amount format. Please enter a valid number.");
        }
    }

    private static void withdraw()
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        System.out.print("Enter amount to withdraw: ");
        try {
            BigDecimal amount = scanner.nextBigDecimal();
            scanner.nextLine();
            atm.withdraw(amount);
            System.out.printf("Successfully withdrew %s. New balance: %s%n", amount, atm.checkBalance());
        } catch (NumberFormatException e) {
            scanner.nextLine();
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
