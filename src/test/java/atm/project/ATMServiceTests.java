package atm.project;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import atm.project.exception.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ATMServiceTests {
    private ATMService atm;

    @BeforeEach
    void setUp() {
        atm = new ATMService();
    }

    // Тесты для Account.deposit
    @Test
    void depositShouldThrowInvalidAmountExceptionForNegativeAmount() {
        atm.createAccount("12345");
        Exception exception = assertThrows(InvalidAmountException.class, () -> atm.deposit(-100.0));
        assertEquals("Deposit amount must be positive", exception.getMessage());
    }

    @Test
    void depositShouldThrowInvalidAmountExceptionForZeroAmount() {
        atm.createAccount("12345");
        Exception exception = assertThrows(InvalidAmountException.class, () -> atm.deposit(0.0));
        assertEquals("Deposit amount must be positive", exception.getMessage());
    }

    @Test
    void depositShouldThrowAccountNotFoundExceptionWhenNoAccount() {
        Exception exception = assertThrows(AccountNotFoundException.class, () -> atm.deposit(100.0));
        assertEquals("No account found. Please create an account first.", exception.getMessage());
    }

    @Test
    void depositShouldWorkWithValidAmount() throws AccountNotFoundException, InvalidAmountException {
        atm.createAccount("12345");
        atm.deposit(100.0);
        assertEquals(100.0, atm.checkBalance());
    }

    // Тесты для Account.withdraw
    @Test
    void withdrawShouldThrowInvalidAmountExceptionForNegativeAmount() {
        atm.createAccount("12345");
        Exception exception = assertThrows(InvalidAmountException.class, () -> atm.withdraw(-100.0));
        assertEquals("Withdrawal amount must be positive", exception.getMessage());
    }

    @Test
    void withdrawShouldThrowInvalidAmountExceptionForZeroAmount() {
        atm.createAccount("12345");
        Exception exception = assertThrows(InvalidAmountException.class, () -> atm.withdraw(0.0));
        assertEquals("Withdrawal amount must be positive", exception.getMessage());
    }

    @Test
    void withdrawShouldThrowAccountNotFoundExceptionWhenNoAccount() {
        Exception exception = assertThrows(AccountNotFoundException.class, () -> atm.withdraw(100.0));
        assertEquals("No account found. Please create an account first.", exception.getMessage());
    }

    @Test
    void withdrawShouldThrowInsufficientFundsExceptionWhenNotEnoughMoney()
            throws AccountNotFoundException, InvalidAmountException {
        atm.createAccount("12345");
        atm.deposit(50.0);
        Exception exception = assertThrows(InsufficientFundsException.class, () -> atm.withdraw(100.0));
        assertTrue(exception.getMessage().contains("Insufficient funds"));
    }

    @Test
    void withdrawShouldWorkWithValidAmount()
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        atm.createAccount("12345");
        atm.deposit(100.0);
        atm.withdraw(50.0);
        assertEquals(50.0, atm.checkBalance());
    }

    // Тесты для checkBalance
    @Test
    void checkBalanceShouldThrowAccountNotFoundExceptionWhenNoAccount() {
        Exception exception = assertThrows(AccountNotFoundException.class, () -> atm.checkBalance());
        assertEquals("No account found. Please create an account first.", exception.getMessage());
    }

    @Test
    void checkBalanceShouldReturnZeroForNewAccount() throws AccountNotFoundException {
        atm.createAccount("12345");
        assertEquals(0.0, atm.checkBalance());
    }

    // Тесты для getTransactionHistory
    @Test
    void getTransactionHistoryShouldThrowAccountNotFoundExceptionWhenNoAccount() {
        Exception exception = assertThrows(AccountNotFoundException.class, () -> atm.getTransactionHistory());
        assertEquals("No account found. Please create an account first.", exception.getMessage());
    }

    @Test
    void getTransactionHistoryShouldReturnEmptyListForNewAccount() throws AccountNotFoundException {
        atm.createAccount("12345");
        List<Transaction> history = atm.getTransactionHistory();
        assertTrue(history.isEmpty());
    }

    @Test
    void getTransactionHistoryShouldReturnTransactionsAfterOperations()
            throws AccountNotFoundException, InvalidAmountException, InsufficientFundsException {
        atm.createAccount("12345");
        atm.deposit(100.0);
        atm.withdraw(50.0);
        List<Transaction> history = atm.getTransactionHistory();
        assertEquals(2, history.size());
        assertTrue(history.get(0).toString().contains("Deposit"));
        assertTrue(history.get(1).toString().contains("Withdrawal"));
    }
}