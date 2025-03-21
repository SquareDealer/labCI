package atm.project;

import java.util.List;
import java.util.Scanner;
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
